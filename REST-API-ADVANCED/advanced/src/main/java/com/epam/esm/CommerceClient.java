package com.epam.esm;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommerceClient {

    @Value("${ctp.clientId}")
    private String clientId;
    @Value("${ctp.clientSecret}")
    private String clientSecret;
    @Value("${ctp.projectKey}")
    private String projectKey;

    @Bean
    public ProjectApiRoot projectApiRoot() {
        return ApiRootBuilder.of()
                .defaultClient(ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1)
                .build(projectKey);
    }
}