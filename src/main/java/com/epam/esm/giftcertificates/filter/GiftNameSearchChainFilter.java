package com.epam.esm.giftcertificates.filter;

import com.epam.esm.integration.sqlrepo.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GiftNameSearchChainFilter extends SearchChainFilter {
    private final SearchChainFilter nextChain;
    private final String giftName;

    private GiftNameSearchChainFilter(SearchChainFilter nextChain, String giftName) {
        this.nextChain = nextChain;
        this.giftName = giftName;
    }

    protected static SearchChainFilter createChain(SearchChainFilter chainFilter, String parameters) {
        return StringUtils.isNotEmpty(parameters) ? new GiftNameSearchChainFilter(chainFilter, parameters) : null;
    }

    protected String buildQuery(boolean isFirst) {
        if (isFirst) {
            return Constants.BuildQuery.CERTIFICATES_NAME_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return Constants.BuildQuery.AND_CERTIFICATES_NAME_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    protected void buildListParam(List<String> paramList) {
        paramList.add("%" + giftName + "%");
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
