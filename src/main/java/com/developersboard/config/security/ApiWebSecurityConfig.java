package com.developersboard.config.security;

import com.developersboard.config.security.jwt.JwtAuthTokenFilter;
import com.developersboard.config.security.jwt.JwtAuthenticationEntryPoint;
import com.developersboard.constant.SecurityConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.util.core.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

  private final Environment environment;
  private final JwtAuthTokenFilter jwtAuthTokenFilter;
  private final JwtAuthenticationEntryPoint unauthorizedHandler;

  private final ApplicationAuthenticationManager authenticationManager;

  /**
   * Override this method to configure the {@link HttpSecurity}. Typically, subclasses should not
   * call super as it may override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @throws Exception thrown when error happens during authentication.
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

    // if we are running with dev profile, disable csrf and frame options to enable h2 to work.
    SecurityUtils.configureDevEnvironmentAccess(http, environment);

    http.exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.antMatcher(SecurityConstants.API_ROOT_URL_MAPPING)
        .authorizeRequests()
        .antMatchers(SecurityConstants.API_V1_AUTH_URL_MAPPING)
        .permitAll()
        .anyRequest()
        .hasAuthority(RoleType.ROLE_ADMIN.getName());

    http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

    http.authenticationManager(authenticationManager);

    return http.build();
  }
}
