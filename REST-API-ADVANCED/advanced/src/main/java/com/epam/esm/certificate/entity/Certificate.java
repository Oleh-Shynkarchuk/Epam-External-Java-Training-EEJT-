package com.epam.esm.certificate.entity;

import com.epam.esm.order.entity.Order;
import com.epam.esm.tag.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certificates")
@Builder
public class Certificate extends RepresentationModel<Certificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    @Column(name = "duration")
    private String durationOfDays;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "last_update_date")
    private String lastUpdateDate;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @ToString.Exclude
    @JoinTable(name = "certificates_has_tags",
            joinColumns = @JoinColumn(name = "certificates_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private List<Tag> tags;
    @ManyToMany(mappedBy = "certificates")
    @ToString.Exclude
    @JsonIgnore
    private List<Order> orders;

    public Certificate merge(Certificate sourceCertificate) {
        if (StringUtils.isNotEmpty(sourceCertificate.getName())) {
            this.setName(sourceCertificate.getName());
        }
        if (StringUtils.isNotEmpty(sourceCertificate.getDescription())) {
            this.setDescription(sourceCertificate.getDescription());
        }
        if (StringUtils.isNotEmpty(sourceCertificate.getDurationOfDays())) {
            this.setDurationOfDays(sourceCertificate.getDurationOfDays());
        }
        if (sourceCertificate.getPrice() != null) {
            this.setPrice(sourceCertificate.getPrice());
        }
        if (sourceCertificate.getTags() != null) {
            this.setTags(sourceCertificate.getTags());
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Certificate that = (Certificate) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getCertificates().add(this);
    }

    public void removeTag(long tagId) {
        Tag tag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
        if (tag != null) {
            this.tags.remove(tag);
            tag.getCertificates().remove(this);
        }
    }
}