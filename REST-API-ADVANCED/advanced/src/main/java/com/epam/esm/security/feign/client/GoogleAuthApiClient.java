package com.epam.esm.security.feign.client;

import com.epam.esm.security.feign.config.GoogleApiConfig;
import com.epam.esm.security.feign.model.VerifiedTokenResponce;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "token", url = "https://www.googleapis.com/oauth2/v3/tokeninfo", configuration = GoogleApiConfig.class)
public interface GoogleAuthApiClient {
    @RequestMapping(method = RequestMethod.GET, params = {"id_token"}, produces = "application/json")
    VerifiedTokenResponce getVerifiedToken(@RequestParam("id_token") String id_token);
}