package com.epam.esm.certificate.entity;

import com.epam.esm.Generated;
import com.epam.esm.tag.entity.Tag;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@Generated
public class GCertificate extends RepresentationModel<GCertificate> {

    private String id;
    @Column(unique = true)
    private String name;
    private String description;
    private List<Variant> variants;
    private List<Tag> tags;
    private Long version;
    @CreatedDate
    private ZonedDateTime createDate;
    @LastModifiedDate
    private ZonedDateTime lastUpdateDate;

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, variants, createDate, lastUpdateDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GCertificate that = (GCertificate) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(description, that.description) && Objects.equals(variants, that.variants)
                && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }
}