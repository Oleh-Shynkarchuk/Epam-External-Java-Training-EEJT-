package com.epam.esm.order.entity;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customer_order")
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "price")
    private BigDecimal totalPrice;
    @Column(name = "purchase")
    private String purchaseDate;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @ToString.Exclude
    @JoinTable(name = "order_has_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private List<Certificate> certificates;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "totalPrice = " + totalPrice + ", " +
                "purchaseDate = " + purchaseDate + ")";
    }
}