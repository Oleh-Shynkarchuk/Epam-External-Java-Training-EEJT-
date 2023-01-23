package com.epam.esm.certificate.repo;

import com.epam.esm.certificate.entity.Certificate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @Query("select c from Certificate c inner join c.tags tags where tags.name in (:tagName) group by c.id having count(c.id) = :tagCount")
    List<Certificate> findByTagsNameAndPagination(List<String> tagName, Long tagCount, PageRequest pageRequest);

    @Modifying
    @Query(value = """
            INSERT INTO certificates_has_tags (certificates_id,tags_id)  VALUES (:certificates_id,:tags_id)
            """, nativeQuery = true)
    void addRelationship(@Param("certificates_id") long certificateID, @Param("tags_id") long tagId);
}
