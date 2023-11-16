package com.developersboard.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.developersboard.constant.EnvConstants;
import com.developersboard.constant.HomeConstants;
import com.developersboard.constant.SecurityConstants;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This configuration handles form login requests with session. This configuration is considered
 * before ApiWebSecurityConfigurationAdapter since it has an @Order value after 1 (no @Order
 * defaults to last).
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class FormLoginSecurityConfig {

  private final Environment environment;
  private final PersistentTokenRepository persistentRepository;

  /**
   * Configure the {@link HttpSecurity}. Typically, subclasses should not call super as it may
   * override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @param mvc the {@link MvcRequestMatcher.Builder}
   * @throws Exception thrown when error happens during authentication.
   */
  @Bean
  public SecurityFilterChain formLoginFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc)
      throws Exception {

    // if we are running with dev profile, disable csrf and frame options to enable h2 to work.
    if (Arrays.asList(environment.getActiveProfiles()).contains(EnvConstants.DEVELOPMENT)) {
      http.headers(
          (headers) ->
              headers
                  .contentTypeOptions(withDefaults())
                  .xssProtection(withDefaults())
                  .cacheControl(withDefaults())
                  .httpStrictTransportSecurity(withDefaults())
                  .frameOptions(FrameOptionsConfig::sameOrigin));
      http.authorizeHttpRequests(req -> req.requestMatchers(PathRequest.toH2Console()).permitAll())
          .csrf(AbstractHttpConfigurer::disable)
          .cors(AbstractHttpConfigurer::disable);
    }

    http.authorizeHttpRequests(
            (requests) ->
                requests
                    //
                    // .requestMatchers(SecurityConstants.getPublicMatchers().toArray(new
                    // String[0]))
                    .requestMatchers(SecurityConstants.getPublicMatchers(mvc))
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            (form) ->
                form.loginPage(SecurityConstants.LOGIN)
                    .failureUrl(SecurityConstants.LOGIN_FAILURE_URL)
                    .defaultSuccessUrl(HomeConstants.INDEX_URL_MAPPING))
        .logout(
            (logout) ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher(SecurityConstants.LOGOUT))
                    .logoutSuccessUrl(SecurityConstants.LOGIN_LOGOUT)
                    .invalidateHttpSession(true)
                    .deleteCookies(SecurityConstants.JSESSIONID, SecurityConstants.REMEMBER_ME)
                    .permitAll())
        .rememberMe((rememberMe) -> rememberMe.tokenRepository(persistentRepository));

    return http.build();
  }
}
