package com.epam.esm.tag.entity;

import com.epam.esm.certificate.entity.Certificate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tags")
@Builder
public class Tag extends RepresentationModel<Tag> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @ToString.Exclude
    private List<Certificate> certificates;

}
