package com.developersboard.config.security;

import com.developersboard.constant.SecurityConstants;
import java.time.Duration;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * This class defines the beans needed for the security operation of the application.
 *
 * @author Matthew Puentes on 6/26/2021
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class SecurityBean {

  /**
   * PasswordEncoder bean used in security operations.
   *
   * @return BcryptPasswordEncoder with security strength 12
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(SecurityConstants.SECURITY_STRENGTH);
  }

  /**
   * Making use of persistent option instead of in-memory for maximum security.
   *
   * @param dataSource DataSource
   * @return persistentTokenRepository
   */
  @Bean
  public PersistentTokenRepository persistentRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    jdbcTokenRepository.setDataSource(dataSource);

    return jdbcTokenRepository;
  }

  /**
   * Configures cors for all requests towards the API.
   *
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setMaxAge(Duration.ofHours(1));
    corsConfiguration.setAllowedHeaders(SecurityConstants.ALLOWED_HTTP_HEADERS);
    corsConfiguration.setAllowedMethods(SecurityConstants.ALLOWED_HTTP_METHODS);
    corsConfiguration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE));

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(SecurityConstants.API_ROOT_URL_MAPPING, corsConfiguration);

    return source;
  }

  /**
   * Enables support for legacy cookie processing.
   *
   * @return WebServerFactoryCustomizer
   */
  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {

    return tomcatServletWebServerFactory ->
        tomcatServletWebServerFactory.addContextCustomizers(
            context -> context.setCookieProcessor(new LegacyCookieProcessor()));
  }
}
