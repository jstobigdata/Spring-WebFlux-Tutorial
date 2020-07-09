package c.jbd.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient //Important
public class FunctionalAppTest {
  @Autowired
  private WebTestClient webTestClient;

  private final String TEST_MESSAGE = "Welcome to JstoBigdata.com";


  @Test
  public void testMonoEndpoint(){
    Flux<String> msg$ = webTestClient.get()
      .uri("/functional/mono")
      .accept(MediaType.TEXT_PLAIN)
      .exchange()
      .expectStatus().isOk()
      .returnResult(String.class).getResponseBody()
      .log();

    StepVerifier.create(msg$)
      .expectNext(TEST_MESSAGE)
      .verifyComplete();
  }

  @Test
  public void testFluxEndpoint(){
    Flux<String> msg$ = webTestClient.get()
      .uri("/functional/flux")
      .accept(MediaType.APPLICATION_STREAM_JSON)
      .exchange()
      .expectStatus().isOk()
      .returnResult(String.class).getResponseBody()
      .log();

    StepVerifier.create(msg$)
      .expectNext(TEST_MESSAGE)
      .verifyComplete();
  }
}
