package com.jreyes.accountsapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EnvConfig {

    @Value("${app.secret}")
    private String secret;
}
