package com.epam.esm.springapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EnableJpaRepositories({
        "com.epam.esm.certificate.repo",
        "com.epam.esm.tag.repo",
        "com.epam.esm.user.repository",
        "com.epam.esm.order.repository"
})
@EntityScan({
        "com.epam.esm.certificate.entity",
        "com.epam.esm.tag.entity",
        "com.epam.esm.user.entity",
        "com.epam.esm.order.entity"
})
public class AdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedApplication.class, args);
    }

}
