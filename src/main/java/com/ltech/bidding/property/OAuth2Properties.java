package com.ltech.bidding.property;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration")
public class OAuth2Properties {
    private Google google;

    @Data
    public static class Google {
        private String accessTokenUri;
        private String userAuthorizationUri;
        private String userInfoUri;
        private String clientId;
        private String redirectUri;
    }
}
