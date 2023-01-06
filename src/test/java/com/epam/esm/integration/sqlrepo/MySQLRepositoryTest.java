package com.epam.esm.integration.sqlrepo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateTransactionException;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                .addScript("dbScript/insert-data.sql")
                .build();
        mySQLRepository = new MySQLRepository(new JdbcTemplate(db), new TransactionTemplate(new DataSourceTransactionManager(db)), new GeneratedKeyHolder());
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void shouldReturnOptionalTagByID() {
        Tag firstExpectedTag = new Tag(1L, "testTag1");
        Tag scndExpectedTag = new Tag(2L, "testTag2");
        Tag thrdExpectedTag = new Tag(3L, "testTag3");

        assertEquals(firstExpectedTag, mySQLRepository.getTagById(1L).orElseThrow());
        assertEquals(scndExpectedTag, mySQLRepository.getTagById(2L).orElseThrow());
        assertEquals(thrdExpectedTag, mySQLRepository.getTagById(3L).orElseThrow());
    }

    @Test
    void createNewTag() {
        Tag newTag = new Tag(null, "newTag");
        assertEquals(newTag.getName(), mySQLRepository.createNewTag(newTag).orElseThrow().getName());
    }

    @Test
    void deleteTagById() {
        assertTrue(mySQLRepository.deleteTagById(1L));
        assertTrue(mySQLRepository.deleteTagById(2L));
        assertFalse(mySQLRepository.deleteTagById(100L));
    }

    @Test
    void getAllTags() {
        List<Tag> expectedList = List.of(
                new Tag(1L, "testTag1"),
                new Tag(2L, "testTag2"),
                new Tag(3L, "testTag3"));
        assertEquals(Optional.of(expectedList), mySQLRepository.getAllTags());
    }

    @Test
    void getGiftCertificateById() {
        GiftCertificate firstExpectedCertificate = new GiftCertificate(1L, "testCertificate1",
                "first description", BigDecimal.valueOf(210.24), "10",
                "2022-12-24 12:51:55", "2022-12-24 12:51:55", List.of(new Tag(1L, "testTag1"),
                new Tag(2L, "testTag2")));
        GiftCertificate secondExpectedCertificate = new GiftCertificate(2L, "testCertificate2",
                "second description", BigDecimal.valueOf(145.33), "5",
                "2022-12-24 23:51:55", "2022-12-24 23:51:55", List.of(new Tag(2L, "testTag2"),
                new Tag(3L, "testTag3")));
        assertEquals(firstExpectedCertificate, mySQLRepository.getGiftCertificateById(firstExpectedCertificate.getId()).orElseThrow());
        assertEquals(secondExpectedCertificate, mySQLRepository.getGiftCertificateById(secondExpectedCertificate.getId()).orElseThrow());
    }

    @Test
    void getAllGiftCertificates() {
        List<GiftCertificate> expectedList = List.of(new GiftCertificate(1L, "testCertificate1",
                "first description", BigDecimal.valueOf(210.24), "10",
                "2022-12-24 12:51:55", "2022-12-24 12:51:55", List.of(new Tag(1L, "testTag1"),
                new Tag(2L, "testTag2"))), new GiftCertificate(2L, "testCertificate2",
                "second description", BigDecimal.valueOf(145.33), "5",
                "2022-12-24 23:51:55", "2022-12-24 23:51:55", List.of(new Tag(2L, "testTag2"),
                new Tag(3L, "testTag3"))));
        assertEquals(expectedList, mySQLRepository.getAllGiftCertificates().orElseThrow());
    }
    @Test
    void createGiftCertificateShouldThrowCertificateTransactionException() {
        GiftCertificate wrongDuration = new GiftCertificate(null, "updateCertificateName",
                "new description", BigDecimal.valueOf(12210.24), "gerferfer",
                null, null, null);
        assertThrows(CertificateTransactionException.class,()->mySQLRepository.createNewGiftCertificate(wrongDuration));
    }

    @Test
    void createNewGiftCertificateWithoutTags() {

        GiftCertificate newCertificate = new GiftCertificate(null, "newCertificate",
                "first description", BigDecimal.valueOf(12210.24), "34",
                LocalDateTime.now().toString(), LocalDateTime.now().toString(), null);

        GiftCertificate actualCertificate = mySQLRepository.createNewGiftCertificate(newCertificate).orElseThrow();

        assertEquals(newCertificate.getName(), actualCertificate.getName());
        assertNull(actualCertificate.getTagsList());
    }

    @Test
    void createNewGiftCertificateWithTags() {

        List<Tag> tagList = new ArrayList<>();
        Tag expectedTag1 = new Tag(null, "testTag1");
        Tag expectedTag2 = new Tag(null, "newTag");
        tagList.add(expectedTag1);
        tagList.add(expectedTag2);
        GiftCertificate newCertificateWithTags = new GiftCertificate(null, "certificateWithTags",
                "first description", BigDecimal.valueOf(12210.24), "34",
                null, null, tagList);

        GiftCertificate actualCertificate = mySQLRepository.createNewGiftCertificate(newCertificateWithTags).orElseThrow();

        assertEquals(newCertificateWithTags.getName(), actualCertificate.getName());
        assertTrue(actualCertificate.getTagsList().containsAll(List.of(expectedTag1, expectedTag2)));
    }

    @Test
    void deleteGiftCertificateById() {
        assertTrue(mySQLRepository.deleteGiftCertificateById(1L));
        assertFalse(mySQLRepository.deleteGiftCertificateById(10L));
    }
    @Test
    void updateGiftCertificateShouldThrowCertificateTransactionException() {
        Long certificateID = 1L;

        GiftCertificate wrongDuration = new GiftCertificate(null, "updateCertificateName",
                "new description", BigDecimal.valueOf(12210.24), "gerferfer",
                null, null, null);
        assertThrows(CertificateTransactionException.class,()->mySQLRepository.updateGiftCertificateById(certificateID,wrongDuration));
    }

    @Test
    void updateGiftCertificateByIdWithoutTagsDelete() {
        Long certificateID = 1L;

        GiftCertificate expected = new GiftCertificate(null, "updateCertificateName",
                "new description", BigDecimal.valueOf(12210.24), "34",
                null, null, null);
        GiftCertificate actual = mySQLRepository.updateGiftCertificateById(certificateID, expected).orElseThrow();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(Duration.ofDays(Long.parseLong(expected.getDuration())).toString(), actual.getDuration());
        assertNotNull(actual.getTagsList());
    }

    @Test
    void updateGiftCertificateByIdAllTagsDelete() {
        Long certificateID = 1L;

        GiftCertificate expected = new GiftCertificate(null, "updateCertificateName",
                "new description", BigDecimal.valueOf(12210.24), "34",
                null, null, List.of());
        GiftCertificate actual = mySQLRepository.updateGiftCertificateById(certificateID, expected).orElseThrow();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(Duration.ofDays(Long.parseLong(expected.getDuration())).toString(), actual.getDuration());
        assertNull(actual.getTagsList());
    }
    @Test
    void updateGiftCertificateByIdAndChangeTagsRelationships() {
        Long certificateID = 1L;
        List<Tag>newTaglist=new ArrayList<>();
        Tag expectedTag1=new Tag(null,"updateTag1");
        Tag expectedTag2=new Tag(null,"updateTag2");
        Tag expectedTag3=new Tag(null,"updateTag3");
        newTaglist.add(expectedTag1);
        newTaglist.add(expectedTag2);
        newTaglist.add(expectedTag3);
        GiftCertificate expected = new GiftCertificate(null, "updateCertificateName",
                "new description", BigDecimal.valueOf(12210.24), "34",
                null, null,newTaglist);

        GiftCertificate actual = mySQLRepository.updateGiftCertificateById(certificateID, expected).orElseThrow();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(Duration.ofDays(Long.parseLong(expected.getDuration())).toString(), actual.getDuration());
        assertTrue(actual.getTagsList().containsAll(List.of(expectedTag1,expectedTag2,expectedTag3)));
    }

    @Test
    void isGiftCertificateByNameExist() {
        assertTrue(mySQLRepository.isGiftCertificateByNameExist("testCertificate1"));
        assertFalse(mySQLRepository.isGiftCertificateByNameExist("Non-existentCertificate"));
    }
    @Test
    void isTagByNameExist() {
        assertTrue(mySQLRepository.tagByNameExist("testTag1"));
        assertFalse(mySQLRepository.tagByNameExist("Non-existentTag"));
    }
    @Test
    void getGiftCertificateByParam() {
        String sqlStatement=SQLQuery.BuildQuery.SEARCH_BASE_QUERY+SQLQuery.BuildQuery.SEARCH_TAG_NAME_QUERY_PART
                +SQLQuery.BuildQuery.AND_CERTIFICATES_NAME_LIKE+SQLQuery.BuildQuery.AND_DESCRIPTION_LIKE;
        List<String>requestParameters = List.of("testTag1","%Certificate%","%description%");
        List<String>requestParametersDoesNotExist = List.of("fwefewfewf","fewfwefwefwef","fewwfdfwefd");

        assertFalse(mySQLRepository.getGiftCertificateByParam(sqlStatement,requestParameters).isEmpty());
        assertTrue(mySQLRepository.getGiftCertificateByParam(sqlStatement,requestParametersDoesNotExist).isEmpty());
    }
}