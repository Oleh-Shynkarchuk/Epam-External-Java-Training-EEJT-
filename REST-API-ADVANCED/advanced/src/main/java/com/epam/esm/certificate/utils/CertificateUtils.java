package com.epam.esm.certificate.utils;

import com.commercetools.api.models.cart.ProductPublishScope;
import com.commercetools.api.models.category.*;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.common.PriceDraft;
import com.commercetools.api.models.product.*;
import com.commercetools.api.models.product_type.ProductTypeResourceIdentifier;
import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.certificate.entity.Variant;
import com.epam.esm.tag.entity.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Component
public class CertificateUtils {
    public ProductDraft transformCertificateToProduct(List<Category> categories, GCertificate certificate) {
        ProductDraft newProduct = new ProductDraftImpl();
        Variant variant = certificate.getVariants().get(0);
        newProduct.setSlug(LocalizedString.of(Locale.US, StringUtils.deleteWhitespace(certificate.getName()) + "-1"));
        newProduct.setName(LocalizedString.of(Locale.US, certificate.getName()));
        newProduct.setProductType(ProductTypeResourceIdentifier.builder().key("certificate2023key").build());
        newProduct.setDescription(LocalizedString.of(Locale.US, certificate.getDescription()));
        newProduct.setCategories(getCategoryList(categories, certificate));
        newProduct.setMasterVariant(getMasterVariant(variant));
        newProduct.setPublish(true);
        return newProduct;
    }

    public GCertificate transformProductToGCertificate(List<Category> results, Product product) {
        ProductData current = product.getMasterData().getCurrent();
        return GCertificate.builder()
                .id(product.getId())
                .name(current.getName().get(Locale.US))
                .description(current.getDescription().get(Locale.US))
                .variants(getVariants(current.getAllVariants()))
                .tags(getTags(results, current))
                .createDate(product.getCreatedAt())
                .lastUpdateDate(product.getLastModifiedAt())
                .version(product.getVersion())
                .build();

    }

    private List<Tag> getTags(List<Category> results, ProductData current) {
        return results.stream()
                .filter(category -> current.getCategories()
                        .stream()
                        .map(CategoryReference::getId)
                        .collect(Collectors.toSet())
                        .contains(category.getId()))
                .map(category ->
                        new Tag(category.getId(),
                                category.getName().get(Locale.US),
                                category.getVersion(),
                                List.of()))
                .toList();
    }

    private List<Variant> getVariants(List<ProductVariant> variants) {
        return variants
                .stream()
                .map(productVariant ->
                        new Variant(productVariant.getId(),
                                productVariant.getSku(),
                                productVariant.getKey(),
                                (Long) productVariant
                                        .getAttributes()
                                        .stream()
                                        .filter(attribute -> attribute.getName().equals("duration-id-1"))
                                        .findFirst()
                                        .orElse(Attribute.of())
                                        .getValue(),
                                productVariant.getPrices()
                                        .stream()
                                        .map(price -> new Variant.Prices(price.getId(),
                                                price.getValue().getCurrencyCode(),
                                                BigDecimal.valueOf(price.getValue()
                                                                .getCentAmount())
                                                        .divide(BigDecimal.valueOf(100), 2,
                                                                RoundingMode.UP)))
                                        .toList()))
                .toList();
    }

    private @Valid List<CategoryResourceIdentifier> getCategoryList(List<Category> categories, GCertificate certificate) {
        return categories.stream()
                .filter(category -> certificate.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toSet())
                        .contains(category.getName()
                                .get(Locale.US)))
                .map(CategoryMixin::toResourceIdentifier).toList();
    }

    private ProductVariantDraft getMasterVariant(Variant variant) {
        return ProductVariantDraft.builder()
                .prices(PriceDraft.builder()
                        .value(Money.builder()
                                .currencyCode(variant
                                        .prices().get(0).currencyCode())
                                .centAmount(variant
                                        .prices().get(0).centAmount()
                                        .multiply(BigDecimal.valueOf(100))
                                        .longValue())
                                .build())
                        .country("US")
                        .build())
                .attributes(Attribute.builder()
                        .name("duration-id-1")
                        .value(variant.duration())
                        .build())
                .build();
    }

    public List<CategoryDraft> transformTagsToCategory(List<Category> results, List<Tag> tags) {
        return tags.stream()
                .filter(tag -> !results.stream()
                        .map(category -> category.getName().get(Locale.US))
                        .collect(Collectors.toSet())
                        .contains(tag.getName()))
                .map(tag -> CategoryDraft.builder()
                        .name(LocalizedString.of(Locale.US, tag.getName()))
                        .slug(LocalizedString.of(Locale.US, StringUtils.deleteWhitespace(tag.getName() + "1")))
                        .orderHint("0.5")
                        .build()).toList();
    }

    public ProductUpdate transformCertificateToProductUpdate(List<Category> results, GCertificate certificate) {
        return ProductUpdate
                .builder()
                .version(certificate.getVersion())
                .actions(getActions(results, certificate))
                .build();
    }

    private List<ProductUpdateAction> getActions(List<Category> results, GCertificate certificate) {
        List<ProductUpdateAction> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(certificate.getName())) {
            list.add(ProductUpdateAction.changeNameBuilder().name(LocalizedString.of(Locale.US, certificate.getName())).build());
        }
        if (StringUtils.isNotEmpty(certificate.getDescription())) {
            list.add(ProductUpdateAction
                    .setDescriptionBuilder()
                    .description(LocalizedString.of(Locale.US, certificate.getDescription()))
                    .build());
        }
        if (!CollectionUtils.isEmpty(certificate.getVariants())) {
            for (Variant variant : certificate.getVariants()) {
                if (!CollectionUtils.isEmpty(variant.prices())) {
                    list.addAll(variant.prices()
                            .stream()
                            .map(prices -> ProductUpdateAction.changePriceBuilder()
                                    .priceId(prices.id())
                                    .staged(false)
                                    .price(priceDraftBuilder -> priceDraftBuilder.value(Money.builder()
                                            .currencyCode(prices.currencyCode())
                                            .centAmount(prices.centAmount()
                                                    .multiply(BigDecimal.valueOf(100))
                                                    .longValue())
                                            .build()))
                                    .build())
                            .toList());
                }
                if (ObjectUtils.isNotEmpty(variant.duration())) {
                    list.add(ProductUpdateAction.setAttributeBuilder().variantId(variant.id())
                            .name("duration-id-1")
                            .value(variant.duration())
                            .build());
                }
            }
            if ((certificate.getTags() != null)) {
                if (!certificate.getTags().isEmpty()) {
                    getCategoryList(results, certificate)
                            .forEach(categoryResourceIdentifier ->
                                    list.add(ProductUpdateAction.addToCategoryBuilder()
                                            .category(categoryResourceIdentifier)
                                            .build()));
                } else list.add(ProductUpdateAction.removeFromCategoryBuilder().build());
            }
        }
        list.add(ProductUpdateAction.publishBuilder().scope(ProductPublishScope.ALL).build());
        return list;
    }

    public ProductUpdate makeProductUnpublished(Product product) {
        return ProductUpdate
                .builder()
                .version(product.getVersion())
                .actions(ProductUpdateAction.unpublishBuilder().build())
                .build();
    }
}
