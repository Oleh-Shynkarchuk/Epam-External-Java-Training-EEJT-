package com.epam.esm.tag.repo;

import com.epam.esm.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(value = """
            select tags.id from certificates
            LEFT JOIN certificates_has_tags ON certificates.id=certificates_id
            LEFT JOIN tags ON tags_id=tags.id join order_has_certificate on order_has_certificate.certificate_id=certificates.id
            join customer_order on customer_order.id=order_has_certificate.order_id
            where user_id = (select user_id FROM gifts.customer_order group by user_id
            order by sum(price) desc limit 1) group by tags.id order by count(tags.name) desc limit 1
             """, nativeQuery = true)
    Long findId();
}