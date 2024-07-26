package it.francescofiora.product.company.api.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import it.francescofiora.product.company.api.web.filter.EndPointFilter;
import java.util.List;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Open Api Configuration.
 */
@Configuration
public class OpenApiConfig {

  /**
   * Custom OpenAPI component.
   *
   * @param serverUrl the url of the server
   * @return OpenAPI Bean
   */
  @Bean
  public OpenAPI customOpenApi(
      @Value("${openapi.server.url:http://localhost:8082}") String serverUrl) {
    // @formatter:off
    return new OpenAPI()
        .servers(List.of(new Server().url(serverUrl)))
        .components(new Components())
        .info(new Info()
            .title("Company Demo App")
            .description("This is a sample Spring Boot RESTful service"));
    // @formatter:on
  }

  /**
   * Custom GlobalHeaders component.
   *
   * @return OperationCustomizer Bean
   */
  @Bean
  public OperationCustomizer customGlobalHeaders() {

    return (operation, handlerMethod) -> {

      var requestIdParam =
          new Parameter().in(ParameterIn.HEADER.toString()).schema(new StringSchema())
              .name(EndPointFilter.X_REQUEST_ID).description("Request ID").required(false);

      operation.addParametersItem(requestIdParam);

      return operation;
    };
  }
}
