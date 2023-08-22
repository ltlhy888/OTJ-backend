package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.enumeration.AuthProvider;
import com.ltech.bidding.property.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RequiredArgsConstructor
@Component
public class OAuth2AuthUrIFactory {
    private final OAuth2Properties oAuth2Properties;


    public String getUri(String authProvider) {
        UriComponentsBuilder builder = null;

        switch (authProvider) {
            case "google" -> {
                builder = UriComponentsBuilder
                        .fromUriString(oAuth2Properties.getGoogle().getUserAuthorizationUri())
                        .queryParam("client_id", oAuth2Properties.getGoogle().getClientId())
                        .queryParam("response_type","token")
                        .queryParam("redirect_uri",oAuth2Properties.getGoogle().getRedirectUri())
                        .queryParam("scope","openid https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");

                break;
            }
            default -> {
                return oAuth2Properties.getGoogle().getClientId();
            }
        }

        return builder.build().toUriString();
    }
}
