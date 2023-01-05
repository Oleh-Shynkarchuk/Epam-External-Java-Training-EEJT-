package com.epam.esm.integration.sqlrepo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.tags.entity.Tag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MySQLRepositoryTest {
    private EmbeddedDatabase db;
    private MySQLRepository mySQLRepository;


    @BeforeEach
    void setUp() {
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("dbScript/create-db.sql")
//                .addScript("dbScript/insert-data.sql")
                .build();
        mySQLRepository = new MySQLRepository(new JdbcTemplate(db), new TransactionTemplate(new DataSourceTransactionManager(db)), new GeneratedKeyHolder());
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void shouldReturnOptionalTagByID() {
//        assertEquals(Optional.empty(), mySQLRepository.getTagById(1L));

        Tag newTag = new Tag(null, "newTag1");

        mySQLRepository.createNewTag(newTag);

        assertEquals(Optional.of(new Tag(1L, "newTag1")), mySQLRepository.getTagById(1L));
    }

    @Test
    void createNewTag() {
        Tag newTag = new Tag(null, "newTag");
        Optional<Tag> tag = mySQLRepository.createNewTag(newTag);
        System.out.println(tag);
        Optional<Tag> tag1 = mySQLRepository.createNewTag(new Tag(null, "newtag2"));
        System.out.println(tag1);
        assertEquals(Optional.of(newTag), tag);
    }

    @Test
    void deleteTagById() {
    }

    @Test
    void getAllTags() {
    }

    @Test
    void getGiftCertificateById() {
    }

    @Test
    void getAllGiftCertificates() {
    }

    @Test
    void createNewGiftCertificate() {
    }

    @Test
    void deleteGiftCertificateById() {
    }

    @Test
    void updateGiftCertificateById() {
    }

    @Test
    void isGiftCertificateByNameExist() {
    }

    @Test
    void getGiftCertificateByParam() {
    }
}