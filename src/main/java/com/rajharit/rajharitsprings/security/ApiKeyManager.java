package com.rajharit.rajharitsprings.security;

import org.springframework.stereotype.Component;

@Component
public class ApiKeyManager {
    private static final String ANALAMAHITSY_API_KEY = "ANALAMAHITSY-API-KEY-12345";
    private static final String ANTANIMENA_API_KEY = "ANTANIMENA-API-KEY-67890";

    public static boolean isValidApiKey(String apiKey) {
        return ANALAMAHITSY_API_KEY.equals(apiKey) || ANTANIMENA_API_KEY.equals(apiKey);
    }
}
