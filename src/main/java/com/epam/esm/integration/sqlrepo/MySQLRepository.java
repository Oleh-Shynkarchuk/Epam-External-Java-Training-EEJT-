package com.epam.esm.integration.sqlrepo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificateRepository;
import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MySQLRepository implements TagsRepository, GiftCertificateRepository {
    public static final String SELECT_FROM_TAGS_WHERE_ID = "SELECT * FROM tags WHERE id=?";
    public static final String SELECT_FROM_TAGS = "SELECT * FROM tags";
    private static final RowMapper<Tag> MAPPER_ACCOUNT_TAG =
            (rs, i) -> new Tag(rs.getLong("id"),
                    rs.getString("name"));
    private static final ResultSetExtractor<Optional<Tag>> EXTRACTOR_ACCOUNT_TAG =
            singletonOptionalExtractor(MAPPER_ACCOUNT_TAG);
    private static final String SELECT_FROM_TAGS_WHERE_NAME = "SELECT * FROM tags WHERE name=?";
    private static final RowMapper<GiftCertificate> MAPPER_ACCOUNT_GIFT = (rs, i) -> {
        GiftCertificate giftCertificate = null;
        while (rs.next())
            if (giftCertificate == null) {
                giftCertificate = new GiftCertificate(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        Duration.ofDays(rs.getLong("duration")).toString(),
                        rs.getTimestamp("create_date").toLocalDateTime().toString(),
                        rs.getTimestamp("last_update_date").toLocalDateTime().toString(),
                        Collections.singletonList(new Tag(rs.getLong("tg.id"), rs.getString("tg.name"))
                        ));
            } else giftCertificate.getTagsList().add(new Tag(rs.getLong("tg.id"), rs.getString("tg.name")));
        return giftCertificate;
    };
    private static final ResultSetExtractor<Optional<GiftCertificate>> EXTRACTOR_ACCOUNT_GIFT = singletonOptionalExtractor(MAPPER_ACCOUNT_GIFT);
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    TransactionTemplate transactionTemplate;

    public static <T> ResultSetExtractor<Optional<T>> singletonOptionalExtractor(
            RowMapper<? extends T> mapper) {
        return rs -> rs.next() ? Optional.ofNullable(mapper.mapRow(rs, 1)) : Optional.empty();
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return jdbcTemplate.query(SELECT_FROM_TAGS_WHERE_ID, EXTRACTOR_ACCOUNT_TAG, id);
    }

    @Override
    public boolean createNewTag(Tag newTag) {
        return jdbcTemplate.update("INSERT INTO tags (name) VALUES (?)", newTag.getName()) == 1;
    }

    @Override
    public boolean deleteTagById(Long id) {
        return jdbcTemplate.update("DELETE FROM tags WHERE id = ?", id) == 1;
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        return jdbcTemplate.query(SELECT_FROM_TAGS_WHERE_NAME, EXTRACTOR_ACCOUNT_TAG, name);
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SELECT_FROM_TAGS, (ResultSet resultset, int i) ->
                new Tag(resultset.getLong("id"),
                        resultset.getString("name")));
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(Long id) {
        return jdbcTemplate.query("SELECT * FROM giftcertificate gc inner join giftcertificate_has_tags ght on gc.id=ght.giftcertificate_id inner join tags tg on ght.tags_id=tg.id where gc.id=?", new ResultSetExtractor<Optional<GiftCertificate>>() {
            @Override
            public Optional<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
                GiftCertificate giftCertificate = null;
                while (rs.next()){
                    if (giftCertificate == null) {
                        giftCertificate = new GiftCertificate(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getBigDecimal("price"),
                                Duration.ofDays(rs.getLong("duration")).toString(),
                                rs.getTimestamp("create_date").toLocalDateTime().toString(),
                                rs.getTimestamp("last_update_date").toLocalDateTime().toString(),
                                new ArrayList<>());
                        giftCertificate.getTagsList().add(new Tag(rs.getLong("tg.id"), rs.getString("tg.name")));
                    } else giftCertificate.getTagsList().add(new Tag(rs.getLong("tg.id"), rs.getString("tg.name")));
                }
                return giftCertificate==null?Optional.empty():Optional.of(giftCertificate);
            }
        }, id);
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        String SELECT_FROM_GiftCertificates = "SELECT * FROM giftcertificate";
        return jdbcTemplate.query(SELECT_FROM_GiftCertificates, (ResultSet resultset, int i) ->
                new GiftCertificate(resultset.getLong("id"),
                        resultset.getString("name"),
                        resultset.getString("description"),
                        resultset.getBigDecimal("price"),
                        Duration.ofDays(resultset.getLong("duration")).toString(),
                        resultset.getTimestamp("create_date").toLocalDateTime().toString(),
                        resultset.getTimestamp("last_update_date").toLocalDateTime().toString(),
                        Collections.singletonList(null)));
    }

    @Override
    public Optional<GiftCertificate> createNewGiftCertificate(GiftCertificate giftCertificate) {
        return transactionTemplate.execute(status -> {
            try {
                String sql = "INSERT INTO giftcertificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, giftCertificate.getName());
                    ps.setString(2, giftCertificate.getDescription());
                    ps.setBigDecimal(3, giftCertificate.getPrice());
                    ps.setInt(4, Integer.parseInt(giftCertificate.getDuration()));
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    System.out.println(ps);
                    return ps;
                }, keyHolder);

                long giftCertificate_id = (Objects.requireNonNull(keyHolder.getKey())).longValue();
                giftCertificate.getTagsList().removeAll(getAllTags());
                for (Tag tag : giftCertificate.getTagsList()) {
                    String sql1 = "INSERT INTO tags (name) VALUES (?)";
                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(sql1, new String[]{"id"});
                        ps.setString(1, tag.getName());
                        return ps;
                    }, keyHolder);

                    long tag_id = (Objects.requireNonNull(keyHolder.getKey())).longValue();
                    String sqlAddRelationship = "INSERT INTO giftcertificate_has_tags (giftcertificate_id,tags_id) VALUES (?,?)";
                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(sqlAddRelationship);
                        ps.setLong(1, giftCertificate_id);
                        ps.setLong(2, tag_id);
                        return ps;
                    });
                }
                return getGiftCertificateById(giftCertificate_id);
            } catch (Exception e) {
                status.setRollbackOnly();
            }
            return Optional.empty();
        });
    }

    @Override
    public boolean deleteGiftCertificateById(Long id) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    jdbcTemplate.update("DELETE FROM giftcertificate WHERE id = ?", id);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return false;
    }

    @Override
    public boolean updateGiftCertificateById(Long id, GiftCertificate giftCertificate) {
        return false;
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateByTagName(String name) {
        return Optional.empty();
    }
}
