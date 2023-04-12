package com.epam.esm.certificate.repo;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.category.Category;
import com.commercetools.api.models.graph_ql.GraphQLResponse;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductPagedQueryResponse;
import com.epam.esm.cache.config.SpringCachingConfig;
import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.certificate.graphqlutils.GraphqlHandler;
import com.epam.esm.certificate.utils.CertificateUtils;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.repo.EcommerceTagRepo;
import lombok.RequiredArgsConstructor;
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
    private final SpringCachingConfig springCachingConfig;
    private final EcommerceTagRepo ecommerceTagRepo;

    public GCertificate getCertificateById(String id) {
        return certificateUtils.transformProductToGCertificate(
                ecommerceTagRepo.getAllTags(), projectApiRoot.products()
                        .withId(id)
                        .get()
                        .executeBlocking()
                        .getBody()
        );
    }

    public List<GCertificate> getPageableCertificates(Pageable paginationCriteria) {
        List<Category> results = projectApiRoot.categories().get().executeBlocking().getBody().getResults();
        ProductPagedQueryResponse body = projectApiRoot.products().get()
                .withOffset(paginationCriteria.getOffset())
                .withLimit(paginationCriteria.getPageSize())
                .executeBlocking().getBody();
        return body.getResults().stream().map(product -> certificateUtils.transformProductToGCertificate(results, product)).toList();
    }

    public GCertificate createCertificate(GCertificate certificate) {
        certificateUtils.transformTagsToCategory(ecommerceTagRepo.getAllTags(), certificate.getTags())
                .forEach(ecommerceTagRepo::createCategory);
        Product newProduct = projectApiRoot.products().post(certificateUtils.transformCertificateToProduct(ecommerceTagRepo.getAllTags(), certificate)).executeBlocking().getBody();
        return certificateUtils.transformProductToGCertificate(ecommerceTagRepo.getAllTags(), newProduct);
    }

    public GCertificate updateCertificate(String id, GCertificate certificate) {
        //отримати версію по айдішніку
        if (certificate.getTags() != null) {
            certificateUtils.transformTagsToCategory(ecommerceTagRepo.getAllTags(), certificate.getTags())
                    .forEach(ecommerceTagRepo::createCategory);
        }
        Product updatedProduct = projectApiRoot
                .products()
                .withId(id)
                .post(certificateUtils.transformCertificateToProductUpdate(ecommerceTagRepo.getAllTags(), certificate))
                .executeBlocking().getBody();
        return certificateUtils.transformProductToGCertificate(ecommerceTagRepo.getAllTags(), updatedProduct);
    }

    public void deleteCertificateById(String id) {
        projectApiRoot.products().withId(id)
                .post(certificateUtils.makeProductUnpublished(
                        projectApiRoot.products().withId(id)
                                .get()
                                .executeBlocking()
                                .getBody()));
        projectApiRoot.products().withId(id)
                .delete(projectApiRoot.products().withId(id)
                        .get()
                        .executeBlocking()
                        .getBody()
                        .getVersion())
                .executeBlocking()
                .getBody();
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
}
//Kеш сервіс в ньому 2 методи гет сет в кеш ( в реалізація мапа) якщо він пустий то зробити запит на отримання кеша.
//TIME TO LIVE(ОПЦІОНАЛЬНО)
//Зробити кеш не тільки на категорії