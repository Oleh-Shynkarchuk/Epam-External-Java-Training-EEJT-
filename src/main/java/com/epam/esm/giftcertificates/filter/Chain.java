package com.epam.esm.giftcertificates.filter;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Chain {
    private final Map<String, String> parameters;
    private SearchChainFilter chainFilter;
    private Map<Predicate<Map<String, String>>, Function<SearchChainFilter, SearchChainFilter>> chainProcessorMap;

    public Chain(Map<String, String> parameters) {
        this.parameters = parameters;
        buildChain();
    }

    private void buildChain() {
        chainProcessorMap = new LinkedHashMap<>();
        chainProcessorMap.put(parameters -> parameters.get("sort_name") != null, chainFilter -> new NameSortSearchChainFilter(chainFilter, parameters.get("sort_name")));
        chainProcessorMap.put(parameters -> parameters.get("sort_date") != null, chainFilter -> new DateSortSearchChainFilter(chainFilter, parameters.get("sort_date")));
        chainProcessorMap.put(parameters -> parameters.get("description") != null, chainFilter -> new DescriptionSearchChainFilter(chainFilter, parameters.get("description")));
        chainProcessorMap.put(parameters -> parameters.get("gift_name") != null, chainFilter -> new GiftNameSearchChainFilter(chainFilter, parameters.get("gift_name")));
        chainProcessorMap.put(parameters -> parameters.get("tag_name") != null, chainFilter -> new TagNameSearchChainFilter(chainFilter, parameters.get("tag_name")));
    }

    public String buildQuery() {
        String baseQuery = "SELECT * FROM gifts.certificates" +
                " JOIN gifts.certificates_has_tags" +
                " ON id=certificates_id JOIN gifts.tags" +
                " ON tags_id=tags.id WHERE";
        if (parameters != null) {
            for (Map.Entry<Predicate<Map<String, String>>, Function<SearchChainFilter, SearchChainFilter>> entry : chainProcessorMap.entrySet()) {
                if (entry.getKey().test(parameters)) {
                    chainFilter = entry.getValue().apply(chainFilter);
                }
            }
            if (chainFilter != null) {
                baseQuery += chainFilter.buildQuery(true);
            }
        }
        return baseQuery;
    }
    public List<String> buildParamList() {
        List<String> paramList=null;
        if (parameters != null) {
            if (chainFilter != null) {
                paramList=new ArrayList<>();
                chainFilter.buildListParam(paramList);
            }
        }
        return paramList;
    }
}
