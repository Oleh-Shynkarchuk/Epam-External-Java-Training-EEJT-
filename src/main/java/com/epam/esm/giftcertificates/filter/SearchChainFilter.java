package com.epam.esm.giftcertificates.filter;

import java.util.List;

public abstract class SearchChainFilter {
    abstract protected String buildQuery(boolean isFirst);

    abstract protected void buildListParam(List<String> paramList);
}
