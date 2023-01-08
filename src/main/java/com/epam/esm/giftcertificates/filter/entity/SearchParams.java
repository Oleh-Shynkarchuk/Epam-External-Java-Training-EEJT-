package com.epam.esm.giftcertificates.filter.entity;

import java.util.ArrayList;
import java.util.List;

public class SearchParams {
    private String tagName;
    private String giftName;
    private String description;
    private String sortDate;
    private String sortName;

    public SearchParams() {
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSortDate() {
        return sortDate;
    }

    public void setSortDate(String sortDate) {
        this.sortDate = sortDate;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public List<String> getAllParams() {
        return new ArrayList<>() {
            {
                add(tagName);
                add(giftName);
                add(description);
                add(sortDate);
                add(sortName);
            }
        };
    }
}
