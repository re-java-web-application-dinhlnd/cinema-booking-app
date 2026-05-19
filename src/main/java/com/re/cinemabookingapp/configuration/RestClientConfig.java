package com.re.cinemabookingapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Cấu hình RestClient cho TMDB API.
 * Sử dụng v3 API Key gắn vào query parameter mặc định cho mọi request.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient tmdbRestClient(TmdbProperties tmdbProperties) {
        // Tạo UriBuilderFactory gắn api_key vào mọi request tự động
        String baseWithKey = tmdbProperties.getBaseUrl();

        return RestClient.builder()
                .baseUrl(baseWithKey)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
