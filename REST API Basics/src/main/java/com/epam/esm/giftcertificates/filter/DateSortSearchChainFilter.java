package com.epam.esm.giftcertificates.filter;


import com.epam.esm.integration.sqlrepo.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class DateSortSearchChainFilter extends SearchChainFilter {
    private final String sortDate;
    private final SearchChainFilter nextChain;

    private DateSortSearchChainFilter(SearchChainFilter nextChain, String sortDate) {
        this.nextChain = nextChain;
        this.sortDate = sortDate;
    }

    protected static SearchChainFilter createChain(SearchChainFilter chainFilter, String parameters) {
        return StringUtils.isNotEmpty(parameters) ? new DateSortSearchChainFilter(chainFilter, parameters) : chainFilter;
    }

    protected String buildQuery(boolean isFirst) {
        return Constants.BuildQuery.ORDER_BY_CREATE_DATE + sortDate + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    protected void buildListParam(List<String> paramList) {
    }
}
