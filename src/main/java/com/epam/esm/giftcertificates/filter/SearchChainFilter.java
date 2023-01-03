package com.epam.esm.giftcertificates.filter;

import java.util.List;

public abstract class SearchChainFilter {


    abstract String buildQuery(boolean isFirst);

    abstract void buildListParam(List<String> paramList);
}
