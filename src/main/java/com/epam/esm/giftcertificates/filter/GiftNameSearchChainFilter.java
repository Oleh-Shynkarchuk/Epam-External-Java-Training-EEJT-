package com.epam.esm.giftcertificates.filter;

import com.epam.esm.integration.sqlrepo.SQLQuery;

import java.util.List;

public class GiftNameSearchChainFilter extends SearchChainFilter {
    private final SearchChainFilter nextChain;
    private final String giftName;

    public GiftNameSearchChainFilter(SearchChainFilter nextChain, String giftName) {
        this.nextChain = nextChain;
        this.giftName = giftName;
    }

    String buildQuery(boolean isFirst) {
        if (isFirst) {
            return SQLQuery.BuildQuery.CERTIFICATES_NAME_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return SQLQuery.BuildQuery.AND_CERTIFICATES_NAME_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
        paramList.add("%"+giftName+"%");
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
