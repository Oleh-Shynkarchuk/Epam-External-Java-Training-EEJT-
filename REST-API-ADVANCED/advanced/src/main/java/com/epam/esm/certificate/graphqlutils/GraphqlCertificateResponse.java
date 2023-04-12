package com.epam.esm.certificate.graphqlutils;

import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.certificate.entity.Variant;
import com.epam.esm.tag.entity.Tag;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


public record GraphqlCertificateResponse(Products products) {

    public record Products(List<CustomProduct> results) {

        public record CustomProduct(String id,
                                    ZonedDateTime lastModifiedAt,
                                    ZonedDateTime createdAt,
                                    MasterData masterData,
                                    Long version) {

            private record MasterData(Current current) {

                private record Current(String name,
                                       String description,
                                       List<Categoriess> categories,
                                       List<AllVariants> allVariants) {

                    private record AllVariants(Long id,
                                               String sku,
                                               String key,
                                               List<Prices> prices,
                                               List<AttributesRaw> attributesRaw) {

                        private record Prices(Value value,
                                              String country,
                                              String id) {

                            private record Value(Long centAmount,
                                                 String currencyCode) {
                            }
                        }

                        private record AttributesRaw(String name,
                                                     Long value) {
                        }
                    }

                    private record Categoriess(String id) {
                    }
                }
            }

            public GCertificate getCertificate(List<Tag> tagList) {
                return GCertificate.builder()
                        .id(this.id)
                        .tags(tagList.stream()
                                .filter(tag -> this.masterData.current.categories.stream()
                                        .map(categoriess -> categoriess.id).collect(Collectors.toSet())
                                        .contains(tag.getId()))
                                .toList())
                        .description(this.masterData.current.description)
                        .name(this.masterData.current.name)
                        .variants(this.masterData.current.allVariants
                                .stream()
                                .map(variant ->
                                        new Variant(
                                                variant.id,
                                                variant.sku,
                                                variant.key,
                                                variant.attributesRaw.stream()
                                                        .filter(attributesRaw ->
                                                                attributesRaw.name.equals("duration-id-1"))
                                                        .findFirst().orElse(
                                                                new MasterData.
                                                                        Current.
                                                                        AllVariants.
                                                                        AttributesRaw("", null))
                                                        .value,
                                                variant.prices.stream()
                                                        .map(prices -> new Variant.
                                                                Prices(prices.id, prices.value.currencyCode,
                                                                BigDecimal.valueOf(prices.value().centAmount)
                                                                        .divide(BigDecimal.valueOf(100), 2,
                                                                                RoundingMode.UP)))
                                                        .toList()
                                        ))
                                .toList())
                        .createDate(this.createdAt)
                        .lastUpdateDate(this.lastModifiedAt)
                        .version(this.version)
                        .build();
            }
        }
    }

}
