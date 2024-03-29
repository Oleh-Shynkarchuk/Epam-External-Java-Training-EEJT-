package com.epam.esm.tag.repo;

import com.epam.esm.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    String MOST_WIDELY_USED_TAG_ID = """
            SELECT tags.id FROM certificates
            LEFT JOIN certificates_has_tags ON certificates.id = certificates_id
            LEFT JOIN tags ON tags_id = tags.id JOIN order_has_certificate
            ON order_has_certificate.certificate_id = certificates.id
            JOIN customer_order ON customer_order.id = order_has_certificate.order_id
            WHERE user_id =
            ( SELECT user_id FROM gifts.customer_order GROUP BY user_id
            ORDER BY SUM(price) DESC LIMIT 1 )
            GROUP BY tags.id ORDER BY count(tags.name) DESC LIMIT 1
            """;

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    @Query(value = MOST_WIDELY_USED_TAG_ID, nativeQuery = true)
    Long findId();
}