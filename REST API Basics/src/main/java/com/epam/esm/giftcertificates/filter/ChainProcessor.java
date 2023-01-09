package com.epam.esm.giftcertificates.filter;


import com.epam.esm.giftcertificates.filter.entity.SearchParams;
import com.epam.esm.integration.sqlrepo.Constants;

import java.util.ArrayList;
import java.util.List;

public class ChainProcessor {
    private SearchChainFilter chainFilter;
    private final SearchParams params;

    public ChainProcessor(SearchParams params) {
        this.params = params;
        buildChain();
    }

    private void buildChain() {
        chainFilter = NameSortSearchChainFilter.createChain(chainFilter, params.getSortName());
        chainFilter = DateSortSearchChainFilter.createChain(chainFilter, params.getSortDate());
        chainFilter = DescriptionSearchChainFilter.createChain(chainFilter, params.getDescription());
        chainFilter = GiftNameSearchChainFilter.createChain(chainFilter, params.getGiftName());
        chainFilter = TagNameSearchChainFilter.createChain(chainFilter, params.getTagName());
    }

    public String buildQuery() {
        String baseQuery = Constants.BuildQuery.SEARCH_BASE_QUERY;
        baseQuery += chainFilter.buildQuery(true);
        return baseQuery;
    }

    public List<String> buildParamList() {
        List<String> paramList = new ArrayList<>();
        chainFilter.buildListParam(paramList);
        return paramList;
    }
}
