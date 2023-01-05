package com.epam.esm.giftcertificates.filter;

import com.epam.esm.integration.sqlrepo.SQLQuery;

import java.util.List;

public class DescriptionSearchChainFilter extends SearchChainFilter {
    private final String description;
    private final SearchChainFilter nextChain;

    public DescriptionSearchChainFilter(SearchChainFilter nextChain, String description) {
        this.nextChain = nextChain;
        this.description = description;
    }


    String buildQuery(boolean isFirst) {
        if (isFirst) {
            return SQLQuery.BuildQuery.DESCRIPTION_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return SQLQuery.BuildQuery.AND_DESCRIPTION_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
        paramList.add("%"+description+"%");
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
