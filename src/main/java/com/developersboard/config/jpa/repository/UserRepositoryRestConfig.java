package com.developersboard.config.jpa.repository;

import com.developersboard.backend.persistent.domain.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Customize the REST configuration for the UserRepository. We want to expose just the ability to
 * query the UserRepository.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class UserRepositoryRestConfig implements RepositoryRestConfigurer {

  /**
   * Override this method to add additional configuration.
   *
   * @param restConfig Main configuration bean.
   * @param cors CORS configuration.
   * @since 3.4
   */
  @Override
  public void configureRepositoryRestConfiguration(
      RepositoryRestConfiguration restConfig, CorsRegistry cors) {
    var config = restConfig.getExposureConfiguration();
    config
        .forDomainType(User.class)
        .withItemExposure(
            (metadata, httpMethods) ->
                httpMethods.disable(
                    HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH));
  }
}
