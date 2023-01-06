package com.epam.esm.integration.sqlrepo;

public class SQLQuery {
    public static final String SELECT_FROM_TAGS_WHERE_ID = "SELECT * FROM tags WHERE id=?";
    public static final String SELECT_FROM_TAGS = "SELECT * FROM tags";
    public static final String SELECT_FROM_TAGS_BY_NAME = "SELECT count(*) FROM tags WHERE name=?";
    public static final String SELECT_FROM_CERTIFICATES_WITH_TAGS_BY_ID = "SELECT * FROM certificates left join certificates_has_tags on certificates.id=certificates_has_tags.certificates_id left join tags on certificates_has_tags.tags_id=tags.id where certificates.id=?";
    public static final String INSERT_INTO_TAGS_NAME_VALUES = "INSERT INTO tags (name) VALUES (?)";
    public static final String DELETE_FROM_TAGS_WHERE_ID = "DELETE FROM tags WHERE id = ?";
    public static final String SELECT_FROM_CERTIFICATES = "SELECT * FROM certificates";
    public static final String SELECT_FROM_CERTIFICATES_BY_NAME = "SELECT count(*) FROM certificates WHERE name=?";
    public static final String INSERT_INTO_CERTIFICATES_ALL_FIELDS = "INSERT INTO certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_INTO_CERTIFICATES_HAS_TAGS_BY_THEIR_ID = "INSERT INTO certificates_has_tags (certificates_id,tags_id) VALUES (?,?)";
    public static final String DELETE_FROM_CERTIFICATES_WHERE_ID = "DELETE FROM certificates WHERE id = ?";
    public static final String UPDATE_CERTIFICATES_ALL_FIELDS = "UPDATE certificates SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ?";

    public static final String DELETE_ALL_RELATIONSHIPS_BETWEEN_TAGS_AND_CERTIFICATES = "DELETE FROM certificates_has_tags WHERE certificates_id = ?";

    public static class BuildQuery {
        public static final String SEARCH_BASE_QUERY = "SELECT * FROM certificates JOIN certificates_has_tags ON certificates.id=certificates_id JOIN tags ON tags_id=tags.id WHERE";
        public static final String SEARCH_TAG_NAME_QUERY_PART = " certificates_id IN (select certificates_id FROM certificates_has_tags JOIN tags ON tags_id=id WHERE name = ?)";
        public static final String AND_CERTIFICATES_NAME_LIKE = " AND certificates.name LIKE ? ";
        public static final String CERTIFICATES_NAME_LIKE = " certificates.name LIKE ? ";
        public static final String AND_DESCRIPTION_LIKE = " AND description LIKE ? ";
        public static final String DESCRIPTION_LIKE = " description LIKE ? ";
        public static final String ORDER_BY_CERTIFICATES_NAME = " ORDER BY certificates.name ";
        public static final String CERTIFICATES_NAME = " , certificates.name ";
        public static final String ORDER_BY_CREATE_DATE = " ORDER BY create_date ";
    }
}
