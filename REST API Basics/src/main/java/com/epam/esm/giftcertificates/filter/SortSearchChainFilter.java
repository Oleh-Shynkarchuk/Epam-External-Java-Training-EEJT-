package com.epam.esm.giftcertificates.filter;


import com.epam.esm.integration.sqlrepo.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SortSearchChainFilter extends SearchChainFilter {

    private final String sortName;
    private final String sortDate;
    private final SearchChainFilter nextChain;

    private SortSearchChainFilter(SearchChainFilter nextChain, String sortDate, String sortName) {
        this.nextChain = nextChain;
        this.sortDate = sortDate;
        this.sortName = sortName;
    }

    protected static SearchChainFilter createChain(SearchChainFilter chainFilter, String sortDate, String sortName) {
        return (StringUtils.isNotEmpty(sortDate) || StringUtils.isNotEmpty(sortName)) ?
                new SortSearchChainFilter(chainFilter, sortDate, sortName) : chainFilter;
    }

    protected String buildQuery(boolean isFirst) {
        String sortBuild = StringUtils.isNotEmpty(sortDate) ? Constants.BuildQuery.ORDER_BY_CREATE_DATE + sortDate
                + (StringUtils.isNotEmpty(sortName) ? Constants.BuildQuery.CERTIFICATES_NAME + sortName : "") : "";
        sortBuild = StringUtils.isEmpty(sortBuild) ? Constants.BuildQuery.ORDER_BY_CERTIFICATES_NAME +
                sortName : sortBuild + "";
        return sortBuild + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    protected void buildListParam(List<String> paramList) {
    }
}
