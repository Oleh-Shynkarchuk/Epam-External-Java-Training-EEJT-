package com.epam.esm.giftcertificates.filter;


import com.epam.esm.integration.sqlrepo.SQLQuery;

import java.util.List;

public class DateSortSearchChainFilter extends SearchChainFilter {
    private final String sortDate;
    private final SearchChainFilter nextChain;

    public DateSortSearchChainFilter(SearchChainFilter nextChain, String sortDate) {
        this.nextChain = nextChain;
        this.sortDate = sortDate;
    }

    String buildQuery(boolean isFirst) {
        return SQLQuery.BuildQuery.ORDER_BY_CREATE_DATE + sortDate + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
    }
}
