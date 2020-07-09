package c.jbd.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HelloRouter {
  @Bean
  public RouterFunction<ServerResponse> functionalRoutes(HelloHandler helloHandler) {
    return RouterFunctions
      .route(RequestPredicates.GET("/functional/mono")
        , helloHandler::monoMessage)
      .andRoute(RequestPredicates.GET("functional/flux")
        , helloHandler::fluxMessage);
  }

 /* @Bean
  public RouterFunction<ServerResponse> routeFunctionalFlux(HelloHandler helloHandler) {
    return RouterFunctions
      .route(RequestPredicates.GET("functional/flux")
        , helloHandler::fluxMessage);
  }*/
}
