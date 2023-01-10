package com.epam.esm.giftcertificates.filter.entity;

public class SearchParamsBuilder {
    protected SearchParams searchParams = new SearchParams();

    public SearchParamsBuilder() {
    }

    public SearchParamsBuilder setTagName(String tagName) {
        searchParams.setTagName(tagName);
        return this;
    }

    public SearchParamsBuilder setGiftName(String giftName) {
        searchParams.setGiftName(giftName);
        return this;
    }

    public SearchParamsBuilder setDescription(String description) {
        searchParams.setDescription(description);
        return this;
    }

    public SearchParamsBuilder setSortDate(String sortDate) {
        searchParams.setSortDate(sortDate);
        return this;
    }

    public SearchParamsBuilder setSortName(String sortName) {
        searchParams.setSortName(sortName);
        return this;
    }

    public SearchParams create() {
        return searchParams;
    }
}
