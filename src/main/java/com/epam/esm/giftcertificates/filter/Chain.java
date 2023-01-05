package com.epam.esm.giftcertificates.filter;


import com.epam.esm.integration.sqlrepo.SQLQuery;

import java.util.*;
import java.util.function.Function;

public class Chain {
    private final Map<String, String> parameters;
    private SearchChainFilter chainFilter;
    private List<Function<SearchChainFilter, SearchChainFilter>> chainProcessorMap;//todo predicate remove

    public Chain(Map<String, String> parameters) {
        this.parameters = parameters;
        buildChain(parameters);
    }

    private void buildChain(Map<String, String> parameters) {
        chainProcessorMap = new ArrayList<>();
        chainProcessorMap.add(parameters.get("sort_name") != null ? chainFilter -> new NameSortSearchChainFilter(chainFilter, parameters.get("sort_name")) : null);
        chainProcessorMap.add(parameters.get("sort_date") != null ? chainFilter -> new DateSortSearchChainFilter(chainFilter, parameters.get("sort_date")) : null);
        chainProcessorMap.add(parameters.get("description") != null ? chainFilter -> new DescriptionSearchChainFilter(chainFilter, parameters.get("description")) : null);
        chainProcessorMap.add(parameters.get("gift_name") != null ? chainFilter -> new GiftNameSearchChainFilter(chainFilter, parameters.get("gift_name")) : null);
        chainProcessorMap.add(parameters.get("tag_name") != null ? chainFilter -> new TagNameSearchChainFilter(chainFilter, parameters.get("tag_name")) : null);
    }

    public String buildQuery() {
        String baseQuery = SQLQuery.BuildQuery.SEARCH_BASE_QUERY;
        if (parameters != null) {
            for (Function<SearchChainFilter, SearchChainFilter> chainFilterFunction : chainProcessorMap) {
                if (chainFilterFunction != null) {
                    chainFilter = chainFilterFunction.apply(chainFilter);
                }
            }
            if (chainFilter != null) {
                baseQuery += chainFilter.buildQuery(true);
            }
        }
        return baseQuery;
    }

    public List<String> buildParamList() {
        List<String> paramList = new ArrayList<>();
        chainFilter.buildListParam(paramList);
        return paramList;
    }
}
