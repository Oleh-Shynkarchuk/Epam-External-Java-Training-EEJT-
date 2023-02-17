package com.epam.esm.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

@Component
@Slf4j

public class KeyUtils {

    private final Environment environment;

    @Value("${access-token.private}")
    private String accessTokenPrivateKeyPath;

    @Value("${access-token.public}")
    private String accessTokenPublicKeyPath;

    @Value("${refresh-token.private}")
    private String refreshTokenPrivateKeyPath;

    @Value("${refresh-token.public}")
    private String refreshTokenPublicKeyPath;

    private KeyPair accessTokenKeyPair;
    private KeyPair refreshTokenKeyPair;

    @Autowired
    public KeyUtils(Environment environment) {
        this.environment = environment;
    }

    private KeyPair getAccessTokenKeyPair() {
        if (ObjectUtils.isEmpty(accessTokenKeyPair)) {
            accessTokenKeyPair = getTokenKeyPair(accessTokenPrivateKeyPath, accessTokenPublicKeyPath);
        }
        return accessTokenKeyPair;
    }

    private KeyPair getRefreshTokenKeyPair() {
        if (ObjectUtils.isEmpty(refreshTokenKeyPair)) {
            refreshTokenKeyPair = getTokenKeyPair(refreshTokenPrivateKeyPath, refreshTokenPublicKeyPath);
        }
        return refreshTokenKeyPair;
    }

    private KeyPair getTokenKeyPair(String privateKeyPath, String publicKeyPath) {
        KeyPair keyPair;
        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);
        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec encodedPublicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(encodedPublicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec encodedPrivateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(encodedPrivateKeySpec);

                keyPair = new KeyPair(publicKey, privateKey);
                return keyPair;
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                throw new RuntimeException("public and private key don`t exist");
            }
        }
        File directory = new File("access-refresh-token-keys");
        if (!directory.exists()) {
            boolean mkdirs = directory.mkdirs();
        }
        try {
            log.info("Generating new keypair: {},{}", privateKeyPath, privateKeyPath);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            try (FileOutputStream fileOutputStream = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fileOutputStream.write(keySpec.getEncoded());
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fileOutputStream.write(keySpec.getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }
}
