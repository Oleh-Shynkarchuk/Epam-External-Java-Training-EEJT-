package com.epam.esm.certificate.graphqlutils;

import com.commercetools.api.models.graph_ql.GraphQLRequest;
import com.commercetools.api.models.graph_ql.GraphQLResponse;
import com.epam.esm.tag.entity.Tag;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.certificate.graphqlutils.GraphqlUtils.CATEGORIES_WHERE_TAGS_NAME;
import static com.epam.esm.certificate.graphqlutils.GraphqlUtils.CERTIFICATE_BY_TAG_ID_QUERY;

@Component
public class GraphqlHandler {
    public GraphQLRequest tagRequest(List<String> tagName) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < tagName.size(); i++) {
            if (i == 0) {
                str.append("name(en-US = \"").append(tagName.get(i)).append("\")");
            } else {
                str.append("or name(en-US = \"").append(tagName.get(i)).append("\")");
            }
        }
        return GraphQLRequest.builder()
                .query(CATEGORIES_WHERE_TAGS_NAME)
                .variables(builder -> builder.addValue("filter", str.toString()))
                .build();
    }

    public GraphqlTagResponse convertToTagResponse(GraphQLResponse graphQLResponse) {
        return JsonMapper.builder().build().convertValue(graphQLResponse.getData(), GraphqlTagResponse.class);
    }

    public GraphQLRequest certificateRequest(Pageable pageRequest, List<Tag> results) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            if (i == 0) {
                str.append("masterData(current(categories(id = \"").append(results.get(i).getId()).append("\")))");
            } else {
                str.append("and masterData(current(categories(id = \"").append(results.get(i).getId()).append("\")))");
            }
        }
        return GraphQLRequest.builder()
                .query(CERTIFICATE_BY_TAG_ID_QUERY)
                .variables(builder -> builder.addValue("filter", str.toString()))
                .build();
    }

    public GraphqlCertificateResponse convertToCertificateResponse(GraphQLResponse certificateResponse) {
        return JsonMapper.builder().findAndAddModules().build()
                .convertValue(certificateResponse.getData(), GraphqlCertificateResponse.class);
    }
}
