package com.developersboard.config.security;

import com.developersboard.constant.HomeConstants;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.constant.SecurityConstants;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
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
   * Override this method to configure the {@link HttpSecurity}. Typically, subclasses should not
   * call super as it may override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @throws Exception thrown when error happens during authentication.
   */
  @Bean
  public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {

    // if we are running with dev profile, disable csrf and frame options to enable h2 to work.
    if (Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.DEV)) {
      http.cors().and().csrf().disable().headers().frameOptions().sameOrigin();
    }

    http.authorizeRequests()
        .antMatchers(SecurityConstants.getPublicMatchers().toArray(new String[0]))
        .permitAll()
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated();
    http.formLogin()
        .loginPage(SecurityConstants.LOGIN)
        .failureUrl(SecurityConstants.LOGIN_FAILURE_URL)
        .defaultSuccessUrl(HomeConstants.INDEX_URL_MAPPING)
        .permitAll();
    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher(SecurityConstants.LOGOUT))
        .logoutSuccessUrl(SecurityConstants.LOGIN_LOGOUT)
        .deleteCookies(SecurityConstants.JSESSIONID)
        .permitAll()
        .deleteCookies(SecurityConstants.REMEMBER_ME)
        .permitAll();
    http.rememberMe().tokenRepository(persistentRepository);

    return http.build();
  }
}
