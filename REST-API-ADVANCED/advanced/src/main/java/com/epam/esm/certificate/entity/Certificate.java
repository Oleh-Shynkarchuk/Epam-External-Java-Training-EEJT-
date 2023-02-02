package com.epam.esm.certificate.entity;

import com.epam.esm.order.entity.Order;
import com.epam.esm.tag.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certificates")
@Builder
public class Certificate extends RepresentationModel<Certificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @Min(value = 0)
    private BigDecimal price;
    @Column(name = "duration")
    @Min(value = 0)
    private String durationOfDays;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "last_update_date")
    private String lastUpdateDate;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
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
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(description, that.description) && Objects.equals(price, that.price)
                && Objects.equals(durationOfDays, that.durationOfDays) && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate) && Objects.equals(tags, that.tags);
    }
}