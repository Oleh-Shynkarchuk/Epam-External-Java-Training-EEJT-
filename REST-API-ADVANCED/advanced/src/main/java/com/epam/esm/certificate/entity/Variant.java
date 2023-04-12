package com.epam.esm.certificate.entity;

import java.math.BigDecimal;
import java.util.List;

public record Variant(Long id, String sku, String key, Long duration, List<Prices> prices) {
    public record Prices(String id, String currencyCode, BigDecimal centAmount) {
    }
}
