package com.epam.esm.certificate.repo;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.category.Category;
import com.commercetools.api.models.category.CategoryDraft;
import com.commercetools.api.models.graph_ql.GraphQLResponse;
import com.commercetools.api.models.product.Product;
import com.epam.esm.cache.service.CacheService;
import com.epam.esm.cache.utils.CacheUtil;
import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.certificate.exception.CertificateNotFoundException;
import com.epam.esm.certificate.graphqlutils.GraphqlHandler;
import com.epam.esm.certificate.utils.CertificateUtils;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.EcommerceTagService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CertificateEcommerceRepo {

    private final ProjectApiRoot projectApiRoot;
    private final CertificateUtils certificateUtils;
    private final GraphqlHandler graphqlHandler;
    private final CacheService cacheService;
    private final EcommerceTagService ecommerceTagService;

    public GCertificate getCertificateById(String id) {
        return certificateUtils.transformProductToGCertificate(getCategoryFromCache(),
                getProductsFromCache(Pageable.ofSize(100))
                        .stream()
                        .filter(product -> product.getId().equals(id))
                        .findFirst()
                        .orElseThrow(CertificateNotFoundException::new));
    }

    public List<GCertificate> getPageableCertificates(Pageable paginationCriteria) {
        return getProductsFromCache(paginationCriteria).stream().map(product -> certificateUtils.
                transformProductToGCertificate(getCategoryFromCache(), product)).toList();
    }

    public GCertificate createCertificate(GCertificate certificate) {
        List<Category> categoryList = getCategoryFromCache();
        certificateUtils.transformTagsToCategory(categoryList, certificate.getTags())
                .forEach(this::createCategory);
        Product newProduct = projectApiRoot.products().post(certificateUtils.transformCertificateToProduct(categoryList, certificate)).executeBlocking().getBody();
        return certificateUtils.transformProductToGCertificate(categoryList, newProduct);
    }


    public GCertificate updateCertificate(String id, GCertificate certificate) {
        List<Category> categoryList = getCategoryFromCache();
        List<Product> productList = getProductsFromCache(Pageable.ofSize(100));
        setVersionBeforeUpdate(certificate, getCertificateById(id));
        if (certificate.getTags() != null) {
            certificateUtils.transformTagsToCategory(categoryList, certificate.getTags())
                    .forEach(this::createCategory);
        }
        Product updatedProduct = projectApiRoot
                .products()
                .withId(id)
                .post(certificateUtils.transformCertificateToProductUpdate(categoryList, certificate))
                .executeBlocking().getBody();
        productList.set(productList.indexOf(productList.stream().filter(product -> product.getId().equals(id)).findFirst().orElseThrow()), updatedProduct);
        return certificateUtils.transformProductToGCertificate(categoryList, updatedProduct);
    }

    public void deleteCertificateById(String id) {
        List<Product> products = getProductsFromCache(Pageable.ofSize(100));
        projectApiRoot.products()
                .withId(id)
                .post(certificateUtils.makeProductUnpublished(products
                        .stream()
                        .filter(product -> product.getId().equals(id))
                        .findFirst().orElseThrow(CertificateNotFoundException::new)))
                .executeBlocking()
                .getBody();
        projectApiRoot.products().withId(id)
                .delete(projectApiRoot.products().withId(id)
                        .get()
                        .executeBlocking()
                        .getBody()
                        .getVersion())
                .executeBlocking()
                .getBody();
        products.removeIf(product -> product.getId().equals(id));
    }

    public List<GCertificate> getCertificateByTagsName(Pageable pageRequest, List<String> tagName) {
        GraphQLResponse tagResponse = projectApiRoot.graphql()
                .post(graphqlHandler.tagRequest(tagName))
                .executeBlocking()
                .getBody();
        List<Tag> results = graphqlHandler.convertToTagResponse(tagResponse).categories().results();
        GraphQLResponse certificateResponse = projectApiRoot.graphql()
                .post(graphqlHandler.certificateRequest(pageRequest, results))
                .executeBlocking()
                .getBody();
        return graphqlHandler.convertToCertificateResponse(certificateResponse).
                products().
                results()
                .stream()
                .map(customProduct -> customProduct.getCertificate(results))
                .toList();
    }

    private List<Category> getCategoryFromCache() {
        Object cache = cacheService.getCache(CacheUtil.CATEGORY_CACHE);
        if (ObjectUtils.isEmpty(cache)) {
            ecommerceTagService.getAllTags(Pageable.ofSize(100));
        }
        return cacheService.getCache(CacheUtil.CATEGORY_CACHE);
    }

    private List<Product> getProductsFromCache(Pageable paginationCriteria) {
        Object cache = cacheService.getCache(CacheUtil.PRODUCT_CACHE);
        if (ObjectUtils.isEmpty(cache)) {
            cacheService.put(CacheUtil.PRODUCT_CACHE,
                    projectApiRoot.products().get()
                            .withOffset(paginationCriteria.getOffset())
                            .withLimit(paginationCriteria.getPageSize())
                            .executeBlocking().getBody().getResults());
        }
        return cacheService.getCache(CacheUtil.PRODUCT_CACHE);
    }

    private void createCategory(CategoryDraft categoryDraft) {
        Category category = projectApiRoot.categories().post(categoryDraft).executeBlocking().getBody();
        List<Category> categories = cacheService.getCache(CacheUtil.CATEGORY_CACHE);
        categories.add(category);
    }

    private void setVersionBeforeUpdate(GCertificate certificate, GCertificate certificateById) {
        certificate.setVersion(certificateById.getVersion());
    }
}