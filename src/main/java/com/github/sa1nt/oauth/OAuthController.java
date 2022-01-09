package com.github.sa1nt.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
public class OAuthController {

    private final WebClient webClient;

    @Autowired
    public OAuthController(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * In this sample we just pretend that the call to Mocky requires a valid OAuth token from a
     * given ClientRegistration.
     *
     * Next stop: try to use https://docs.spring.io/spring-security/site/docs/5.5.x/reference/html5/#oauth2Client-authorized-repo-service
     * manual to employ Spring machinery to get from Cache or from Provider, so that we could still use
     * RestTemplate easily after a call to getAccessToken() method somewhere.
     */
    @GetMapping("/")
    public String oauth(@RequestParam(defaultValue = "az-one") String registrationId) {
        var authFilterFunction = ServerOAuth2AuthorizedClientExchangeFilterFunction
                .clientRegistrationId(registrationId);

        var mockyResponse = webClient.get()
                .uri("https://run.mocky.io/v3/3ff7e275-00e6-4a1b-8199-9293ec0f2bd5")
                // instructs the WebClient to authenticate with a specific Client Registration
                .attributes(authFilterFunction)
                .retrieve()
                .bodyToMono(String.class)
                .take(Duration.of(5L, ChronoUnit.SECONDS));

        String mockyResponseStr = mockyResponse.block(Duration.of(10L, ChronoUnit.SECONDS));
        return mockyResponseStr;
    }
}
