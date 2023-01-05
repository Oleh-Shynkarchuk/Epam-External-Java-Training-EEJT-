package com.epam.esm.giftcertificates.filter;

import com.epam.esm.integration.sqlrepo.SQLQuery;

import java.util.List;

public class TagNameSearchChainFilter extends SearchChainFilter {
    private final SearchChainFilter nextChain;
    private final String tag_name;

    public TagNameSearchChainFilter(SearchChainFilter nextChain, String tag_name) {
        this.nextChain = nextChain;
        this.tag_name = tag_name;
    }

    String buildQuery(boolean isFirst) {
        return SQLQuery.BuildQuery.SEARCH_TAG_NAME_QUERY_PART + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    void buildListParam(List<String> paramList) {
        paramList.add(tag_name);
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
