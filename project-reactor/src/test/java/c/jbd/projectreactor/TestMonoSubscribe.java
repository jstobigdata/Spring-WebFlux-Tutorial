package c.jbd.projectreactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TestMonoSubscribe {

  /**
   * Subscribe a Mono using {@link Subscriber}.
   */
  @Test
  public void subscribeMono() {
    Mono<String> message$ = Mono.just("Welcome to Jstobigdata")
        .map(msg -> msg.concat(".com")).log();

    message$.subscribe(new Subscriber<String>() {
      @Override
      public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(String s) {
        System.out.println("onNext: " + s);
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("====== Execution Completed ======");
      }
    });

    StepVerifier.create(message$)
        .expectNext("Welcome to Jstobigdata.com")
        .verifyComplete();
  }

  /**
   * Subscribe a Mono using Lambda subscriber.
   */
  @Test
  public void subscribeMono2() {
    Mono<String> message$ = Mono.just("Welcome to Jstobigdata")
        .map(msg -> msg.concat(".com")).log();

    message$.subscribe(
        value -> {
          System.out.println(value);
        },
        err -> {
          err.printStackTrace();
        },
        () -> {
          System.out.println("===== Execution completed =====");
        });

    StepVerifier.create(message$)
        .expectNext("Welcome to Jstobigdata.com")
        .expectNextCount(0) //no next element
        .verifyComplete();
  }

  /**
   * Throw a {@link RuntimeException} inside {@link Mono}.
   */
  @Test
  public void subscribeMono3() {
    Mono<String> message$ = Mono.error(new RuntimeException("Check error mono"));

    message$.subscribe(
        value -> {
          System.out.println(value);
        },
        err -> {
          err.printStackTrace();
        },
        () -> {
          System.out.println("===== Execution completed =====");
        });

    StepVerifier.create(message$)
        .expectError(RuntimeException.class)
        .verify();
  }
}
