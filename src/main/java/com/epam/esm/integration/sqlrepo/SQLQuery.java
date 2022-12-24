package com.epam.esm.integration.sqlrepo;

public class SQLQuery {
    public static final String SELECT_FROM_TAGS_WHERE_ID = "SELECT * FROM tags WHERE id=?";
    public static final String SELECT_FROM_TAGS = "SELECT * FROM tags";
    public static final String SELECT_FROM_GIFT_CERTIFICATE_WITH_TAGS_BY_GIFT_ID = "SELECT * FROM certificates gc inner join certificates_has_tags ght on gc.id=ght.certificates_id inner join tags tg on ght.tags_id=tg.id where gc.id=?";
    public static final String INSERT_INTO_TAGS_NAME_VALUES = "INSERT INTO tags (name) VALUES (?)";
    public static final String DELETE_FROM_TAGS_WHERE_ID = "DELETE FROM tags WHERE id = ?";
    public static final String SELECT_FROM_GIFT_CERTIFICATE = "SELECT * FROM certificates";
    public static final String INSERT_INTO_GIFTCERTIFICATE_ALL_FIELDS = "INSERT INTO certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_INTO_GIFTCERTIFICATE_HAS_TAGS_BY_THEIR_ID = "INSERT INTO certificates_has_tags (certificates_id,tags_id) VALUES (?,?)";
    public static final String DELETE_FROM_GIFTCERTIFICATE_WHERE_ID = "DELETE FROM certificates WHERE id = ?";
    public static final String UPDATE_GIFTCERTIFICATE_ALL_FIELDS = "UPDATE certificates SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ?";

    public static final String DELETE_ALL_RELATIONSHIPS_BETWEN_TAG_AND_CERTIFICATE = "DELETE FROM certificates_has_tags WHERE certificates_id = ?";
}
