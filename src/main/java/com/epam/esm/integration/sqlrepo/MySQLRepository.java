package com.epam.esm.integration.sqlrepo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateTransactionException;
import com.epam.esm.giftcertificates.repo.GiftCertificatesRepository;
import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.repository.TagsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MySQLRepository implements TagsRepository, GiftCertificatesRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final KeyHolder keyHolder;
    @Autowired
    public MySQLRepository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate, KeyHolder keyHolder) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.keyHolder = keyHolder;
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return jdbcTemplate.query(Constants.SqlQuery.SELECT_FROM_TAGS_WHERE_ID, rs -> rs.next() ?
                Optional.of(new Tag(rs.getLong(Constants.TableColumnsName.ID),
                        rs.getString(Constants.TableColumnsName.NAME))) : Optional.empty(), id);
    }

    @Override
    public Optional<Tag> createNewTag(Tag newTag) {
        return transactionTemplate.execute(status -> {
                    createTagsInTransaction(keyHolder, newTag);
                    return getTagById(Objects.requireNonNull(keyHolder.getKey()).longValue());
                }
        );
    }

    @Override
    public boolean deleteTagById(Long id) {
        return jdbcTemplate.update(Constants.SqlQuery.DELETE_FROM_TAGS_WHERE_ID, id) == 1;
    }

    @Override
    public boolean tagByNameExist(String name) {
        Integer exist = jdbcTemplate.queryForObject(Constants.SqlQuery.SELECT_FROM_TAGS_BY_NAME, Integer.class, name);
        if (exist == null) return false;
        else return exist > 0;
    }

    @Override
    public Optional<List<Tag>> getAllTags() {
        return Optional.of(jdbcTemplate.query(Constants.SqlQuery.SELECT_FROM_TAGS, (ResultSet resultset, int i) ->
                new Tag(resultset.getLong(Constants.TableColumnsName.ID),
                        resultset.getString(Constants.TableColumnsName.NAME))));
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(Long id) {
        return jdbcTemplate.query(Constants.SqlQuery.SELECT_FROM_CERTIFICATES_WITH_TAGS_BY_ID, rs -> rs.next() ? Optional.of(new GiftCertificate(
                rs.getLong(Constants.TableColumnsName.ID),
                rs.getString(Constants.TableColumnsName.NAME),
                rs.getString(Constants.TableColumnsName.DESCRIPTION),
                rs.getBigDecimal(Constants.TableColumnsName.PRICE),
                String.valueOf(rs.getLong(Constants.TableColumnsName.DURATION)),
                rs.getTimestamp(Constants.TableColumnsName.CREATE_DATE).toLocalDateTime().toString(),
                rs.getTimestamp(Constants.TableColumnsName.LAST_UPDATE_DATE).toLocalDateTime().toString(),
                new ArrayList<>() {{
                    String tagExist = rs.getString(Constants.TableColumnsName.TAGS_NAME);
                    if (StringUtils.isNotEmpty(tagExist)) {
                        add(new Tag(rs.getLong(Constants.TableColumnsName.TAGS_ID), tagExist));
                    }
                    while (rs.next()) {
                        tagExist = rs.getString(Constants.TableColumnsName.TAGS_NAME);
                        if (StringUtils.isNotEmpty(tagExist)) {
                            add(new Tag(rs.getLong(Constants.TableColumnsName.TAGS_ID), tagExist));
                        }
                    }
                }})) : Optional.empty(), id);
    }

    @Override
    public Optional<List<GiftCertificate>> getAllGiftCertificates() {
        String SELECT_FROM_GiftCertificates = Constants.SqlQuery.SELECT_FROM_CERTIFICATES;
        return jdbcTemplate.query(SELECT_FROM_GiftCertificates, MySQLRepository::getGiftCertificateList);
    }

    @Override
    public Optional<GiftCertificate> createNewGiftCertificate(GiftCertificate giftCertificate) {
        return transactionTemplate.execute(status -> {
            try {
                createCertificateInTransaction(giftCertificate, keyHolder);
                long giftCertificate_id = (Objects.requireNonNull(keyHolder.getKey())).longValue();
                if (giftCertificate.getTagsList() != null && !giftCertificate.getTagsList().isEmpty()) {
                    addEachNewTagToDbAndCreateRelationships(giftCertificate_id, giftCertificate, keyHolder);
                }
                return getGiftCertificateById(giftCertificate_id);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new CertificateTransactionException("Exception during create certificate. Certificate has not been created.");
            }
        });
    }


    @Override
    public boolean deleteGiftCertificateById(Long id) {
        return jdbcTemplate.update(Constants.SqlQuery.DELETE_FROM_CERTIFICATES_WHERE_ID, id) == 1;
    }

    @Override
    public Optional<GiftCertificate> updateGiftCertificateById(Long id, GiftCertificate giftCertificate) {
        return transactionTemplate.execute(status -> {
            try {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(Constants.SqlQuery.UPDATE_CERTIFICATES_ALL_FIELDS);
                    int parameterIndex = 0;
                    ps.setString(++parameterIndex, giftCertificate.getName());
                    ps.setString(++parameterIndex, giftCertificate.getDescription());
                    ps.setBigDecimal(++parameterIndex, giftCertificate.getPrice());
                    ps.setInt(++parameterIndex, Integer.parseInt(giftCertificate.getDuration()));
                    ps.setTimestamp(++parameterIndex, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setLong(++parameterIndex, id);
                    return ps;
                });
                if (giftCertificate.getTagsList() != null) {
                    jdbcTemplate.update(Constants.SqlQuery.DELETE_ALL_RELATIONSHIPS_BETWEEN_TAGS_AND_CERTIFICATES, id);
                    if (!giftCertificate.getTagsList().isEmpty()) {
                        addEachNewTagToDbAndCreateRelationships(id, giftCertificate, keyHolder);
                    }
                }
                return getGiftCertificateById(id);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new CertificateTransactionException("Exception during update certificate. Certificate has not been updated.");
            }
        });
    }

    @Override
    public boolean isGiftCertificateByNameExist(String name) {
        Integer exist = jdbcTemplate.queryForObject(Constants.SqlQuery.SELECT_FROM_CERTIFICATES_BY_NAME, Integer.class, name);
        if (exist == null) return false;
        else return exist > 0;
    }

    @Override
    public Optional<List<GiftCertificate>> getGiftCertificateByParam(String statementQuery, List<String> paramList) {
        return jdbcTemplate.query(statementQuery, MySQLRepository::getGiftCertificateList, paramList.toArray());
    }

    private void addEachNewTagToDbAndCreateRelationships(Long id, GiftCertificate giftCertificate, KeyHolder keyHolder) {
        List<Tag> allTags = getAllTags().orElse(List.of());
        allTags.forEach(tag -> giftCertificate.getTagsList().forEach(tag1 -> {
            if (tag1.getName().equals(tag.getName())) {
                createManyToManyRelationships(id, tag.getId());
            }
        }));
        giftCertificate.getTagsList().removeIf(tag -> allTags.stream().anyMatch(tag1 -> tag1.getName().equals(tag.getName())));
        giftCertificate.getTagsList().forEach(tag -> {
            createTagsInTransaction(keyHolder, tag);
            createManyToManyRelationships(id, (Objects.requireNonNull(keyHolder.getKey())).longValue());
        });
    }

    private void createManyToManyRelationships(long giftCertificate_id, long tag_id) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(Constants.SqlQuery.INSERT_INTO_CERTIFICATES_HAS_TAGS_BY_THEIR_ID);
            int parameterIndex = 0;
            ps.setLong(++parameterIndex, giftCertificate_id);
            ps.setLong(++parameterIndex, tag_id);
            return ps;
        });
    }

    private void createTagsInTransaction(KeyHolder keyHolder, Tag tag) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(Constants.SqlQuery.INSERT_INTO_TAGS_NAME_VALUES, new String[]{Constants.TableColumnsName.ID});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
    }

    private void createCertificateInTransaction(GiftCertificate giftCertificate, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(Constants.SqlQuery.INSERT_INTO_CERTIFICATES_ALL_FIELDS, new String[]{Constants.TableColumnsName.ID});
            int parameterIndex = 0;
            ps.setString(++parameterIndex, giftCertificate.getName());
            ps.setString(++parameterIndex, giftCertificate.getDescription());
            ps.setBigDecimal(++parameterIndex, giftCertificate.getPrice());
            ps.setInt(++parameterIndex, Integer.parseInt(giftCertificate.getDuration()));
            ps.setTimestamp(++parameterIndex, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(++parameterIndex, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
    }

    private static Optional<List<GiftCertificate>> getGiftCertificateList(ResultSet rs) throws SQLException {
        ArrayList<GiftCertificate> list = new ArrayList<>();
        while (rs.next()) {
            long idForCheck = rs.getLong(Constants.TableColumnsName.ID);
            if (list.stream().noneMatch(giftCertificate -> giftCertificate.getId().equals(idForCheck))) {
                list.add(new GiftCertificate(
                        idForCheck,
                        rs.getString(Constants.TableColumnsName.NAME),
                        rs.getString(Constants.TableColumnsName.DESCRIPTION),
                        rs.getBigDecimal(Constants.TableColumnsName.PRICE),
                        String.valueOf(rs.getLong(Constants.TableColumnsName.DURATION)),
                        rs.getTimestamp(Constants.TableColumnsName.CREATE_DATE).toLocalDateTime().toString(),
                        rs.getTimestamp(Constants.TableColumnsName.LAST_UPDATE_DATE).toLocalDateTime().toString(),
                        new ArrayList<>()));
            }
            Optional<GiftCertificate> certificate = list.stream().filter(giftCertificate -> giftCertificate.getId().equals(idForCheck)).findFirst();
            if (certificate.isPresent()) {
                certificate.get().getTagsList().add(new Tag(rs.getLong(Constants.TableColumnsName.TAGS_ID), rs.getString(Constants.TableColumnsName.TAGS_NAME)));
            }
        }
        return list.isEmpty() ? Optional.empty() : Optional.of(list);
    }
}
