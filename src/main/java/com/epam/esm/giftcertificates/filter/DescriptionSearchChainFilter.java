package com.epam.esm.giftcertificates.filter;

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
            return " certificates.name LIKE ? " + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return "AND description LIKE ? " + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
        System.out.println("before adding|"+paramList);
        paramList.add("%"+description+"%");
        System.out.println("after adding|"+paramList);
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
