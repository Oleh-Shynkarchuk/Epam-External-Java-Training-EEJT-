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

import java.math.BigDecimal;
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
                Optional.of(new Tag(getId(rs), getName(rs))) : Optional.empty(), id);
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
        return Optional.of(jdbcTemplate.query(Constants.SqlQuery.SELECT_FROM_TAGS, (ResultSet rs, int i) ->
                new Tag(getId(rs), getName(rs))));
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(Long id) {
        return jdbcTemplate.query(Constants.SqlQuery.SELECT_FROM_CERTIFICATES_WITH_TAGS_BY_ID, rs -> {
            GiftCertificate giftCertificate = null;
            while (rs.next()) {
                if (giftCertificate == null) {
                    giftCertificate = new GiftCertificate(
                            getId(rs), getName(rs), getDescription(rs), getPrice(rs), getDuration(rs),
                            getCreateDate(rs), getLastUpdateDate(rs), new ArrayList<>());
                }
                addTagToCertificateListIfExist(rs, giftCertificate);
            }
            return giftCertificate == null ? Optional.empty() : Optional.of(giftCertificate);
        }, id);
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
                if (TagListIsNotEmpty(giftCertificate)) {
                    addEachNewTagToDbAndCreateRelationships(giftCertificate_id, giftCertificate, keyHolder);
                }
                return getGiftCertificateById(giftCertificate_id);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new CertificateTransactionException("Exception during create certificate." +
                        " Certificate has not been created.");
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
                throw new CertificateTransactionException("Exception during update certificate." +
                        " Certificate has not been updated.");
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
            long idForCheck = getId(rs);
            if (isaNextCertificate(list, idForCheck)) {
                list.add(new GiftCertificate(idForCheck, getName(rs), getDescription(rs), getPrice(rs),
                        getDuration(rs), getCreateDate(rs), getLastUpdateDate(rs), new ArrayList<>()));
            }
            Optional<GiftCertificate> certificate = list.stream().filter(giftCertificate -> giftCertificate.getId().equals(idForCheck)).findFirst();
            if (certificate.isPresent()) {
                certificate.get().getTagsList().add(new Tag(getTagsId(rs), getTagName(rs)));
            }
        }
        return list.isEmpty() ? Optional.empty() : Optional.of(list);
    }

    private static long getTagsId(ResultSet rs) throws SQLException {
        return rs.getLong(Constants.TableColumnsName.TAGS_ID);
    }

    private static String getTagName(ResultSet rs) throws SQLException {
        return rs.getString(Constants.TableColumnsName.TAGS_NAME);
    }

    private static long getId(ResultSet rs) throws SQLException {
        return rs.getLong(Constants.TableColumnsName.ID);
    }

    private static String getName(ResultSet rs) throws SQLException {
        return rs.getString(Constants.TableColumnsName.NAME);
    }

    private static String getDescription(ResultSet rs) throws SQLException {
        return rs.getString(Constants.TableColumnsName.DESCRIPTION);
    }

    private static BigDecimal getPrice(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(Constants.TableColumnsName.PRICE);
    }

    private static String getDuration(ResultSet rs) throws SQLException {
        return String.valueOf(rs.getLong(Constants.TableColumnsName.DURATION));
    }

    private static String getCreateDate(ResultSet rs) throws SQLException {
        return rs.getTimestamp(Constants.TableColumnsName.CREATE_DATE).toLocalDateTime().toString();
    }

    private static String getLastUpdateDate(ResultSet rs) throws SQLException {
        return rs.getTimestamp(Constants.TableColumnsName.LAST_UPDATE_DATE).toLocalDateTime().toString();
    }

    private static boolean isaNextCertificate(ArrayList<GiftCertificate> list, long idForCheck) {
        return list.stream().noneMatch(giftCertificate -> giftCertificate.getId().equals(idForCheck));
    }

    private static boolean TagListIsNotEmpty(GiftCertificate giftCertificate) {
        return giftCertificate.getTagsList() != null && !giftCertificate.getTagsList().isEmpty();
    }

    private static void addTagToCertificateListIfExist(ResultSet rs, GiftCertificate giftCertificate) throws SQLException {
        String tagName = getTagName(rs);
        if (StringUtils.isNotEmpty(tagName)) {
            giftCertificate.getTagsList().add(new Tag(getTagsId(rs), tagName));
        }
    }
}
