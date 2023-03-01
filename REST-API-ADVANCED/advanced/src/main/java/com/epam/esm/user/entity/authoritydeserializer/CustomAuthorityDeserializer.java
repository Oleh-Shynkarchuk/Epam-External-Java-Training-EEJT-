package com.epam.esm.user.entity.authoritydeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthorityDeserializer extends JsonDeserializer<List<GrantedAuthority>> {
    @Override
    public List<GrantedAuthority> deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        jsonNode.elements().forEachRemaining(node -> {
            JsonNode authority = node.get("authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        });
        return grantedAuthorities;
    }
}
