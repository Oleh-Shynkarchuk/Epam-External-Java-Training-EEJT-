package com.epam.esm.certificate.repo;

import com.epam.esm.certificate.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    boolean existsByName(String name);

    @Query("select c from Certificate c inner join c.tags tags where tags.name in (:tagName) group by c.id having count(c.id) = :tagCount")
    Page<Certificate> findByTagsNameAndPagination(List<String> tagName, Long tagCount, Pageable pageRequest);

    Certificate findByName(String name);
}
