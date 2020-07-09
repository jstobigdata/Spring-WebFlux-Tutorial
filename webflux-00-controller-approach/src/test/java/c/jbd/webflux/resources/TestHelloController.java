package c.jbd.webflux.resources;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TestHelloController {

  private static WebClient webClient;

  @BeforeAll
  private static void setWebClient() {
    webClient = WebClient.create("http://localhost:8080");
  }

  @Test
  public void testMonoEndpoint() {
    Mono<String> msg$ = webClient.get()
      .uri("/hello/mono")
      .retrieve()
      .bodyToMono(String.class).log();

    StepVerifier.create(msg$)
      .expectNext("Welcome to JstoBigdata.com")
      .expectComplete();
  }

  @Test
  public void testFluxEndpoint() {
    Flux<String> msg$ = webClient.get()
      .uri("/hello/flux")
      .accept(MediaType.APPLICATION_STREAM_JSON)
      .retrieve()
      .bodyToFlux(String.class);

    StepVerifier.create(msg$)
      .expectNext("Welcome to JstoBigdata.com")
      .verifyComplete();
  }
}
