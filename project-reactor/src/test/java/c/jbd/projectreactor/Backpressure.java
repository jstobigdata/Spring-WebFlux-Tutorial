package c.jbd.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Backpressure {

  @Test
  public void subscriptionRequest() {
    Flux<Integer> range$ = Flux.range(1, 100);

    //gets only 10 elements
    range$.subscribe(
      value -> System.out.println(value),
      err -> err.printStackTrace(),
      () -> System.out.println("==== Completed ===="),
      subscription -> subscription.request(10)
    );
  }

  @Test
  public void subscriptionCancel() {
    Flux<Integer> range$ = Flux.range(1, 100);

    //Nothing will be printed as we don't have a cancel callback
    range$.subscribe(
      value -> System.out.println(value),
      err -> err.printStackTrace(),
      () -> System.out.println("==== Completed ===="),
      subscription -> subscription.cancel()
    );
  }

  @Test
  public void cancelCallback() {
    Flux<Integer> range$ = Flux.range(1, 100).log();

    range$.doOnCancel(() -> {
      System.out.println("===== Cancel method invoked =======");
    }).doOnComplete(() -> {
      System.out.println("==== Completed ====");
    }).subscribe(new BaseSubscriber<Integer>() {
      @Override
      protected void hookOnNext(Integer value) {
        try {
          Thread.sleep(500);
          request(1);
          System.out.println(value);
          if (value == 5) {
            cancel();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    //Test the code
    StepVerifier.create(range$)
      .expectNext(1, 2, 3, 4, 5)
      .thenCancel()
      .verify();
  }

  @Test
  public void backpressureVerifier() {
    Flux data$ = Flux.just(101, 201, 301).log();

    StepVerifier.create(data$)
      .expectSubscription()
      .thenRequest(1)
      .expectNext(101)
      .thenRequest(2)
      .expectNext(201, 301)
      .verifyComplete();
  }
}
