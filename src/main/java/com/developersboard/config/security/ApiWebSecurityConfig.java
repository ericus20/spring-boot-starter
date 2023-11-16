package com.developersboard.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.developersboard.config.security.jwt.JwtAuthTokenFilter;
import com.developersboard.config.security.jwt.JwtAuthenticationEntryPoint;
import com.developersboard.constant.AdminConstants;
import com.developersboard.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This configuration handles api web requests with stateless session.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ApiWebSecurityConfig {

  private final JwtAuthTokenFilter jwtAuthTokenFilter;
  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final AuthenticationManager authenticationManager;

  /**
   * Configure the {@link HttpSecurity}. Typically, subclasses should not call super as it may
   * override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @throws Exception thrown when error happens during authentication.
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

    // Match any incoming request targeting the /api/** to use this security filter chain
    http.securityMatcher(SecurityConstants.API_ROOT_URL_MAPPING);

    // Use a custom exception handler when authentication fails
    http.exceptionHandling(
            (exceptionHandling) -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler))

        // guarantee that the application won't create any session at all by using stateless policy
        .sessionManagement(
            (sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            requests -> {
              // Allow public access to POST /api/v1/users: Used to register new users
              requests
                  .requestMatchers(
                      new AntPathRequestMatcher(
                          AdminConstants.API_V1_USERS_ROOT_URL, HttpMethod.POST.name()))
                  .permitAll();

              // Allow access for users to authenticate after registration and refresh tokens
              requests
                  .requestMatchers(
                      new AntPathRequestMatcher(SecurityConstants.API_V1_AUTH_URL_MAPPING))
                  .permitAll();
              requests.anyRequest().authenticated();
            })
        .cors(withDefaults())

        // If your application is attaching the credentials via an Authorization header,
        // then the browser can't automatically authenticate the requests,
        // and CSRF isn't possible.
        .csrf(AbstractHttpConfigurer::disable)
        .authenticationManager(authenticationManager)
        .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
