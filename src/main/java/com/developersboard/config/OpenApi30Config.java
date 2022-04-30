package com.developersboard.config;

import com.developersboard.config.properties.OpenApiProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * This class holds OpenApi 3 configurations for this application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class OpenApi30Config {

  private final OpenApiProperties properties;

  /**
   * Configures the OpenApi 3.0 bean.
   *
   * @return the OpenApi 3.0 bean
   */
  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    final String apiTitle = String.format("%s API", StringUtils.capitalize(properties.getName()));

    var info =
        new Info()
            .title(apiTitle)
            .version(properties.getVersion())
            .description(properties.getDescription());

    var securityScheme =
        new SecurityScheme()
            .name(securitySchemeName)
            .type(Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

    var components = new Components().addSecuritySchemes(securitySchemeName, securityScheme);

    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(components)
        .info(info);
  }
}
