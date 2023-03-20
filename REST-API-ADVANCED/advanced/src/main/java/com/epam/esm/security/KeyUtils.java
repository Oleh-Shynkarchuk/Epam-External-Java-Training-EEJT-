package com.epam.esm.security;

import com.epam.esm.security.exception.KeyPairException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
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
                return getKeyPairFromFiles(publicKeyFile, privateKeyFile);
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new KeyPairException(e.getMessage());
            }
        } else {
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                throw new KeyPairException("public and private key don`t exist");
            }
        }
        createDirectoryForKeyPair();
        try {
            keyPair = generateNewKeypair(privateKeyPath, publicKeyPath);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new KeyPairException(e.getMessage());
        }
        return keyPair;
    }

    private void createDirectoryForKeyPair() {
        File directory = new File("access-refresh-token-keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
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

    private PublicKey getPublicKey(File publicKeyFile, KeyFactory keyFactory) throws IOException, InvalidKeySpecException {
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        EncodedKeySpec encodedPublicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(encodedPublicKeySpec);
    }

    private PrivateKey getPrivateKey(File privateKeyFile, KeyFactory keyFactory) throws IOException, InvalidKeySpecException {
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        PKCS8EncodedKeySpec encodedPrivateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(encodedPrivateKeySpec);
    }

    @NotNull
    private KeyPair getKeyPairFromFiles(File publicKeyFile, File privateKeyFile) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyPair keyPair;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        keyPair = new KeyPair(getPublicKey(publicKeyFile, keyFactory), getPrivateKey(privateKeyFile, keyFactory));
        return keyPair;
    }

    @NotNull
    private KeyPair generateNewKeypair(String privateKeyPath, String publicKeyPath) throws NoSuchAlgorithmException, IOException {
        KeyPair keyPair;
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
        return keyPair;
    }
}
