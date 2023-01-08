package com.epam.esm.giftcertificates.filter;


import com.epam.esm.integration.sqlrepo.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class NameSortSearchChainFilter extends SearchChainFilter {

    private final String sortName;
    private final SearchChainFilter nextChain;

    private NameSortSearchChainFilter(SearchChainFilter nextChain, String sortName) {
        this.nextChain = nextChain;
        this.sortName = sortName;
    }

    protected static SearchChainFilter createChain(SearchChainFilter chainFilter, String parameters) {
        return StringUtils.isNotEmpty(parameters) ? new NameSortSearchChainFilter(chainFilter, parameters) : null;
    }

    protected String buildQuery(boolean isFirst) {
        if (isFirst) {
            return Constants.BuildQuery.ORDER_BY_CERTIFICATES_NAME + sortName + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return Constants.BuildQuery.CERTIFICATES_NAME + sortName + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    protected void buildListParam(List<String> paramList) {
    }
}
