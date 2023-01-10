package com.epam.esm.giftcertificates.filter;

import com.epam.esm.integration.sqlrepo.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TagNameSearchChainFilter extends SearchChainFilter {
    private final SearchChainFilter nextChain;
    private final String tagName;

    private TagNameSearchChainFilter(SearchChainFilter nextChain, String tagName) {
        this.nextChain = nextChain;
        this.tagName = tagName;
    }

    protected static SearchChainFilter createChain(SearchChainFilter chainFilter, String parameters) {
        return StringUtils.isNotEmpty(parameters) ? new TagNameSearchChainFilter(chainFilter, parameters) : chainFilter;
    }

    protected String buildQuery(boolean isFirst) {
        return Constants.BuildQuery.SEARCH_TAG_NAME_QUERY_PART + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    protected void buildListParam(List<String> paramList) {
        paramList.add(tagName);
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
