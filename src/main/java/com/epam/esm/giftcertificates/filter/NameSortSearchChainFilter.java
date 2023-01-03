package com.epam.esm.giftcertificates.filter;


import java.util.List;

public class NameSortSearchChainFilter extends SearchChainFilter {

    private final String sortName;
    private final SearchChainFilter nextChain;

    public NameSortSearchChainFilter(SearchChainFilter nextChain, String sortName) {
        this.nextChain = nextChain;
        System.out.println("name const " + nextChain);
        this.sortName = sortName;
    }


    String buildQuery(boolean isFirst) {
        if (isFirst) {
            return " order by certificates.name " + sortName + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return " ,certificates.name " + sortName + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
    }
}
