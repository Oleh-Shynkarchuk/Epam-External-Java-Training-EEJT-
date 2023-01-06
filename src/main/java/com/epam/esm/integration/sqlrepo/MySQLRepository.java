package com.epam.esm.integration.sqlrepo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificatesRepository;
import com.epam.esm.giftcertificates.exception.CertificateTransactionException;
import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
        return jdbcTemplate.query(SQLQuery.SELECT_FROM_TAGS_WHERE_ID, rs -> {
            Tag tag = null;
            if (rs.next()) {
                tag = new Tag(rs.getLong("id"),
                        rs.getString("name"));
            }
            return tag == null ? Optional.empty() : Optional.of(tag);
        }, id);
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
        return jdbcTemplate.update(SQLQuery.DELETE_FROM_TAGS_WHERE_ID, id) == 1;
    }

    @Override
    public boolean tagByNameExist(String name) {
        Integer exist = jdbcTemplate.queryForObject(SQLQuery.SELECT_FROM_TAGS_BY_NAME,Integer.class,name);
        if (exist == null) return false;
        else return exist>0;
    }

    @Override
    public Optional<List<Tag>> getAllTags() {
        return Optional.of(jdbcTemplate.query(SQLQuery.SELECT_FROM_TAGS, (ResultSet resultset, int i) ->
                new Tag(resultset.getLong("id"),
                        resultset.getString("name"))));
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(Long id) {
        return jdbcTemplate.query(SQLQuery.SELECT_FROM_CERTIFICATES_WITH_TAGS_BY_ID, rs -> {
            GiftCertificate giftCertificate = null;
            while (rs.next()) {
                if (giftCertificate == null) {
                    giftCertificate = new GiftCertificate(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBigDecimal("price"),
                            Duration.ofDays(rs.getLong("duration")).toString(),
                            rs.getTimestamp("create_date").toLocalDateTime().toString(),
                            rs.getTimestamp("last_update_date").toLocalDateTime().toString(),
                            null);
                    String tagExist = rs.getString("tags.name");
                    if (tagExist != null) {
                        giftCertificate.setTagsList(new ArrayList<>());
                        giftCertificate.getTagsList().add(new Tag( rs.getLong("tags.id"), tagExist));
                    }
                } else giftCertificate.getTagsList().add(new Tag(rs.getLong("tags.id"), rs.getString("tags.name")));
            }
            return giftCertificate == null ? Optional.empty() : Optional.of(giftCertificate);
        }, id);
    }

    @Override
    public Optional<List<GiftCertificate>> getAllGiftCertificates() {
        String SELECT_FROM_GiftCertificates = SQLQuery.SELECT_FROM_CERTIFICATES;
        return Optional.of(jdbcTemplate.query(SELECT_FROM_GiftCertificates, (ResultSet resultset, int i) ->
                new GiftCertificate(resultset.getLong("id"),
                        resultset.getString("name"),
                        resultset.getString("description"),
                        resultset.getBigDecimal("price"),
                        Duration.ofDays(resultset.getLong("duration")).toString(),
                        resultset.getTimestamp("create_date").toLocalDateTime().toString(),
                        resultset.getTimestamp("last_update_date").toLocalDateTime().toString(),
                        Collections.singletonList(null))));
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
        return jdbcTemplate.update(SQLQuery.DELETE_FROM_CERTIFICATES_WHERE_ID, id) == 1;
    }

    @Override
    public Optional<GiftCertificate> updateGiftCertificateById(Long id, GiftCertificate giftCertificate) {
        return transactionTemplate.execute(status -> {
            try {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(SQLQuery.UPDATE_CERTIFICATES_ALL_FIELDS);
                    ps.setString(1, giftCertificate.getName());
                    ps.setString(2, giftCertificate.getDescription());
                    ps.setBigDecimal(3, giftCertificate.getPrice());
                    ps.setInt(4, Integer.parseInt(giftCertificate.getDuration()));
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setLong(6, id);
                    return ps;
                });
                if (giftCertificate.getTagsList() != null) {
                    jdbcTemplate.update(SQLQuery.DELETE_ALL_RELATIONSHIPS_BETWEEN_TAGS_AND_CERTIFICATES, id);
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
        Integer exist = jdbcTemplate.queryForObject(SQLQuery.SELECT_FROM_CERTIFICATES_BY_NAME,Integer.class,name);
        if (exist == null) return false;
        else return exist>0;
    }

    @Override
    public Optional<List<GiftCertificate>> getGiftCertificateByParam(String statementQuery, List<String> paramList) {
        return jdbcTemplate.query(statementQuery, rs -> {
            ArrayList<GiftCertificate> list = new ArrayList<>();
            while (rs.next()) {
                long idForCheck = rs.getLong("id");
                if (list.stream().noneMatch(giftCertificate -> giftCertificate.getId().equals(idForCheck))) {
                    list.add(new GiftCertificate(
                            idForCheck,
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBigDecimal("price"),
                            Duration.ofDays(rs.getLong("duration")).toString(),
                            rs.getTimestamp("create_date").toLocalDateTime().toString(),
                            rs.getTimestamp("last_update_date").toLocalDateTime().toString(),
                            new ArrayList<>()));
                }
                Optional<GiftCertificate> certificate = list.stream().filter(giftCertificate -> giftCertificate.getId().equals(idForCheck)).findFirst();
                if (certificate.isPresent()) {
                    certificate.get().getTagsList().add(new Tag(rs.getLong("tags.id"), rs.getString("tags.name")));
                }
            }
            return list.isEmpty() ? Optional.empty() : Optional.of(list);
        }, paramList.toArray());
    }

    private void addEachNewTagToDbAndCreateRelationships(Long id, GiftCertificate giftCertificate, KeyHolder keyHolder) {
        List<Tag> allTags = getAllTags().orElse(List.of());
        allTags.forEach(tag -> giftCertificate.getTagsList().forEach(tag1 -> {
            if (tag1.getName().equals(tag.getName())){
                createManyToManyRelationships(id, tag.getId());
            }
        }));
        giftCertificate.getTagsList().removeAll(allTags);
        giftCertificate.getTagsList().forEach(tag -> {
            createTagsInTransaction(keyHolder, tag);
            createManyToManyRelationships(id, (Objects.requireNonNull(keyHolder.getKey())).longValue());
        });
    }

    private void createManyToManyRelationships(long giftCertificate_id, long tag_id) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQLQuery.INSERT_INTO_CERTIFICATES_HAS_TAGS_BY_THEIR_ID);
            ps.setLong(1, giftCertificate_id);
            ps.setLong(2, tag_id);
            return ps;
        });
    }

    private void createTagsInTransaction(KeyHolder keyHolder, Tag tag) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQLQuery.INSERT_INTO_TAGS_NAME_VALUES, new String[]{"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
    }

    private void createCertificateInTransaction(GiftCertificate giftCertificate, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQLQuery.INSERT_INTO_CERTIFICATES_ALL_FIELDS,new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, Integer.parseInt(giftCertificate.getDuration()));
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
    }
}
