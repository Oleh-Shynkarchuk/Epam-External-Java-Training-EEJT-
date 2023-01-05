package com.epam.esm.giftcertificates.filter;


import com.epam.esm.integration.sqlrepo.SQLQuery;

import java.util.List;

public class NameSortSearchChainFilter extends SearchChainFilter {

    private final String sortName;
    private final SearchChainFilter nextChain;

    public NameSortSearchChainFilter(SearchChainFilter nextChain, String sortName) {
        this.nextChain = nextChain;
        this.sortName = sortName;
    }


    String buildQuery(boolean isFirst) {
        if (isFirst) {
            return SQLQuery.BuildQuery.ORDER_BY_CERTIFICATES_NAME + sortName + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return SQLQuery.BuildQuery.CERTIFICATES_NAME + sortName + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
    }
}
