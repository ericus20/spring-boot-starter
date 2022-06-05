# CORS Configuration

Background on CORS Support in Spring Boot can be
found [here](https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/cors.html)
.

# Advanced CORS Configuration

CORS configuration in the application uses the advanced configuration approach.

[CorsConfiguration](https://docs.spring.io/spring-framework/docs/4.2.9.RELEASE/javadoc-api/org/springframework/web/cors/CorsConfiguration.html)
allows you to specify how the CORS requests should be processed: allowed origins, headers, methods,
etc.

These environment variables can be customized as needed:

```
cors.max-age=${CORS_MAX_AGE:3600} # default is 3600 seconds
cors.allowed-methods=${CORS_ALLOWED_METHODS:}
cors.allowed-headers=${CORS_ALLOWED_HEADERS:}
cors.exposed-headers=${CORS_EXPOSED_HEADERS:}
cors.allow-credentials=${CORS_ALLOW_CREDENTIALS:true}
cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:3000/, http://localhost:4200/}
```

* `CORS_ALLOWED_ORIGINS` - Comma-separated list of allowed origins.
* `CORS_ALLOWED_HEADERS` - Comma-separated list of allowed headers.
* `CORS_ALLOWED_METHODS` - Comma-separated list of allowed methods.
* `CORS_ALLOWED_EXPOSE_HEADERS` - Comma-separated list of exposed headers.
* `CORS_ALLOWED_CREDENTIALS` - Boolean value indicating whether credentials are allowed.
* `CORS_PREFLIGHT_MAX_AGE` - Integer value indicating the max age of the preflight request.

The bean responsible for the CORS configuration is:

```
com.developersboard.config.security.SecurityBean;

/**
   * Configures cors for all requests towards the API.
   *
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource(final CorsConfigProperties props)
```
