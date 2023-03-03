package com.epam.esm.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KeyUtilsTest {

    private final KeyUtils keyUtils;

    @Autowired
    KeyUtilsTest(KeyUtils keyUtils) {
        this.keyUtils = keyUtils;
    }

    @Test
    void getAccessTokenPublicKey() {
        assertNotNull(keyUtils.getAccessTokenPublicKey());
    }

    @Test
    void getAccessTokenPrivateKey() {
        assertNotNull(keyUtils.getAccessTokenPrivateKey());
    }

    @Test
    void getRefreshTokenPublicKey() {
        assertNotNull(keyUtils.getRefreshTokenPublicKey());
    }

    @Test
    void getRefreshTokenPrivateKey() {
        assertNotNull(keyUtils.getRefreshTokenPrivateKey());
    }
}