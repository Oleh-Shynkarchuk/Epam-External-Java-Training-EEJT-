package com.epam.esm.giftcertificates.entity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.epam.esm.tags.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    private String duration;

    private String create_date;

    private String last_update_date;
    private List<Tag> tagsList;

    public GiftCertificate() {
    }

    public GiftCertificate(Long id, String name, String description, BigDecimal price, String duration, String create_date, String last_update_date,List<Tag> tagsList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.create_date = create_date;
        this.last_update_date = last_update_date;
        this.tagsList=tagsList;
    }

    public GiftCertificate(Long id, String name, String description, BigDecimal price,
                           String duration,
                           List<Tag> tagsList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tagsList = tagsList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getLast_update_date() {
        return last_update_date;
    }

    public List<Tag> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<Tag> tagsList) {
        this.tagsList = tagsList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public void setLast_update_date(String last_update_date) {
        this.last_update_date = last_update_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", create_date=" + create_date +
                ", last_update_date=" + last_update_date +
                ", tagsList=" + tagsList +
                '}';
    }
}