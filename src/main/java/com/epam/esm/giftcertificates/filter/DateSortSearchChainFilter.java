package com.epam.esm.giftcertificates.filter;


import java.util.List;

public class DateSortSearchChainFilter extends SearchChainFilter {
    private final String sortDate;
    private final SearchChainFilter nextChain;

    public DateSortSearchChainFilter(SearchChainFilter nextChain, String sortDate) {
        this.nextChain = nextChain;
        this.sortDate = sortDate;
    }

    String buildQuery(boolean isFirst) {
        return "ORDER BY create_date "+sortDate + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
    }
}
