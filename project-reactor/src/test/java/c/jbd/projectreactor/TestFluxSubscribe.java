package c.jbd.projectreactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TestFluxSubscribe {

  @Test
  public void subscribeFlux() {
    Flux<String> messages$ = Flux.just("Welcome", "to", "Jstobigdata").log();

    messages$.subscribe(new Subscriber<String>() {
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

    StepVerifier.create(messages$)
        .expectNext("Welcome")
        .expectNext("to")
        .expectNext("Jstobigdata")
        .verifyComplete();
  }

  @Test
  public void subscribeFlux2() {
    Flux<String> messages$ = Flux.just("Welcome", "to", "Jstobigdata").log();

    messages$.subscribe(
        msg -> {
          System.out.println(msg);
        },
        err -> {
          err.printStackTrace();
        },
        () -> {
          System.out.println("===== Execution completed =====");
        });

    StepVerifier.create(messages$)
        .expectNext("Welcome")
        .expectNext("to")
        .expectNext("Jstobigdata")
        .verifyComplete();
  }


  @Test
  public void subscribeFlux3() {
    Flux<String> messages$ = Flux.error(new RuntimeException());

    messages$.subscribe(
        msg -> {
          System.out.println(msg);
        },
        err -> {
          err.printStackTrace();
        },
        () -> {
          System.out.println("===== Execution completed =====");
        });

    StepVerifier.create(messages$)
        .expectError(RuntimeException.class)
        .verify();
  }
}
