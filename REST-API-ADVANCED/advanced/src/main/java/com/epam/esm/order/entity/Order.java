package com.epam.esm.order.entity;

import com.epam.esm.Generated;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "customer_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Generated
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "price")
    private BigDecimal totalPrice;
    @Column(name = "purchase")
    @CreatedDate
    private LocalDateTime purchaseDate;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinTable(name = "order_has_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private List<Certificate> certificates;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(totalPrice, that.totalPrice)) return false;
        if (!Objects.equals(purchaseDate, that.purchaseDate)) return false;
        return Objects.equals(certificates, that.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, totalPrice, purchaseDate, certificates, user);
    }
}