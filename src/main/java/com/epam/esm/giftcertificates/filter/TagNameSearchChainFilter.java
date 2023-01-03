package com.epam.esm.giftcertificates.filter;

import java.util.List;

public class TagNameSearchChainFilter extends SearchChainFilter {
    private final SearchChainFilter nextChain;
    private final String tag_name;

    public TagNameSearchChainFilter(SearchChainFilter nextChain, String tag_name) {
        this.nextChain = nextChain;
        this.tag_name = tag_name;
    }

    String buildQuery(boolean isFirst) {
        return " certificates_id IN (select certificates_id" +
                " FROM gifts.certificates_has_tags" +
                " JOIN gifts.tags ON tags_id=id WHERE name = ?) " + (nextChain != null ? nextChain.buildQuery(false) : "");
    }
    @Override
    void buildListParam(List<String> paramList) {
        paramList.add(tag_name);
        if (nextChain != null) nextChain.buildListParam(paramList);
    }
}
