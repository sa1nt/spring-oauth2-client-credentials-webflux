package com.github.sa1nt.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuthConfiguration {

    /**
     * Allows all requests to pass.
     */
    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .anyExchange().permitAll()
                .and().build();
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
                        ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository,
                authorizedClientRepository
        );

        // matches the spring.security.oauth2.client.registration.az configuration key
        // this auth method will be used by default (see OAuthController.oauth() method on how to use
        //  a non-default method
        oauth.setDefaultClientRegistrationId("az");

        return WebClient.builder()
                .filter(oauth)
                .build();
    }
}
