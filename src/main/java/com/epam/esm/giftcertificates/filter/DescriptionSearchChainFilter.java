package com.epam.esm.giftcertificates.filter;

import com.epam.esm.integration.sqlrepo.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class DescriptionSearchChainFilter extends SearchChainFilter {
    private final String description;
    private final SearchChainFilter nextChain;

    private DescriptionSearchChainFilter(SearchChainFilter nextChain, String description) {
        this.nextChain = nextChain;
        this.description = description;
    }

    protected static SearchChainFilter createChain(SearchChainFilter chainFilter, String parameters) {
        return StringUtils.isNotEmpty(parameters) ? new DescriptionSearchChainFilter(chainFilter, parameters) : null;
    }


    protected String buildQuery(boolean isFirst) {
        if (isFirst) {
            return Constants.BuildQuery.DESCRIPTION_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
        }
        return Constants.BuildQuery.AND_DESCRIPTION_LIKE + (nextChain != null ? nextChain.buildQuery(false) : "");
    }

    @Override
    protected void buildListParam(List<String> paramList) {
        paramList.add("%" + description + "%");
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
