package com.re.cinemabookingapp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình kết nối TMDB API.
 * Giá trị được đọc từ application.properties (prefix = "tmdb").
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tmdb")
public class TmdbProperties {

    private String apiKey;
    private String baseUrl;
    private String imageBaseUrl;
}
