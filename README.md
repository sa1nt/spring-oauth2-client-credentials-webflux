Investigating Spring Security autoconfiguration abilities for OAuth2. 

Note: 
* Client Credentials and Provider info in `application.yml` 
* Results in `ClientRegistration` instance being autoconfigured
* Within a `ReactiveClientRegistrationRepository` autoconfigured instance

That and a `ServerOAuth2AuthorizedClientExchangeFilterFunction` allow you to constuct a 
`WebClient` that upon a request to a target URI will: 
1. check if token for the URI exists and is not expired
2. fetch the token if necessary
3. call the target URI with the fetched or cached token

See `com.github.sa1nt.oauth.OAuthController#oauth()` method for that.

See: 
* `ReactiveOAuth2ClientAutoConfiguration`
* `ReactiveOAuth2ClientConfigurations`
