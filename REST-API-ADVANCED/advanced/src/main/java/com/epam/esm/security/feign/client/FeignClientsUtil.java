package com.epam.esm.security.feign.client;

import com.epam.esm.security.feign.config.FeignSupportConfig;
import com.epam.esm.security.feign.dto.TokenVerifiedDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "token", url = "https://www.googleapis.com/oauth2/v3/tokeninfo", configuration = FeignSupportConfig.class)
public interface FeignClientsUtil {
    @RequestMapping(method = RequestMethod.GET, params = {"id_token"}, produces = "application/json")
    TokenVerifiedDTO getVerifiedToken(@RequestParam("id_token") String id_token);
}