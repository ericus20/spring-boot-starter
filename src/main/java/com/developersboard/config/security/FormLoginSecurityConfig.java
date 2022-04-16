package com.developersboard.config.security;

import com.developersboard.constant.SecurityConstants;
import com.developersboard.shared.util.core.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
public class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {

  private final Environment environment;
  private final PersistentTokenRepository persistentRepository;

  /**
   * Override this method to configure the {@link HttpSecurity}. Typically, subclasses should not
   * call super as it may override their configuration.
   *
   * @param http the {@link HttpSecurity} to modify.
   * @throws Exception thrown when error happens during authentication.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // if we are running with dev profile, disable csrf and frame options to enable h2 to work.
    SecurityUtils.configureDevEnvironmentAccess(http, environment);

    http.authorizeRequests()
        .antMatchers(SecurityConstants.getPublicMatchers().toArray(new String[0]))
        .permitAll()
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated();
    http.formLogin();
    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher(SecurityConstants.LOGOUT))
        .logoutSuccessUrl(SecurityConstants.LOGIN_LOGOUT)
        .deleteCookies(SecurityConstants.JSESSIONID)
        .permitAll()
        .deleteCookies(SecurityConstants.REMEMBER_ME)
        .permitAll();
    http.rememberMe().tokenRepository(persistentRepository);
  }
}
