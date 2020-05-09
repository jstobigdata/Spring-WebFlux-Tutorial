package c.jbd.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Supplier;

public class TestSimpleMono {

  /**
   * Create using Mono.just() and use map to tra
   */
  @Test
  public void simpleMono() {
    //Create a Mono with a msg.
    //.map is an Operator to manipulate the string
    Mono<String> message$ = Mono.just("Welcome to Jstobigdata")
        .map(msg -> msg.concat(".com")).log();

    message$.subscribe(System.out::println);
    StepVerifier.create(message$)
        .expectNext("Welcome to Jstobigdata.com")
        .verifyComplete();
  }

  @Test
  public void emptyMono() {
    Mono empty$ = Mono.empty().log();

    empty$.subscribe(val -> {
      System.out.println("==== Value ====" + val);
    }, err -> {

    }, () -> {
      System.out.println("====== On Complete Invoked ======");
    });

    //To Test
    StepVerifier.create(empty$)
        .expectNextCount(0) //optional
        .verifyComplete();
  }

  @Test
  public void noSignalMono() {
    //Mono that never returns a value
    Mono<String> noSignal$ = Mono.never();
    //Can not use - Mono.never().log()

    noSignal$.subscribe(val -> {
      System.out.println("==== Value ====" + val);
    });

    StepVerifier.create(noSignal$)
        .expectTimeout(Duration.ofSeconds(5))
        .verify();
  }

  @Test
  public void fromSupplier() {
    Supplier<String> stringSupplier = () -> "Sample Message";
    Mono<String> sMono$ = Mono.fromSupplier(stringSupplier).log();
    sMono$.subscribe(System.out::println);

    StepVerifier.create(sMono$)
        .expectNext("Sample Message")
        .verifyComplete();
  }

  @Test
  public void filterMono() {
    Supplier<String> stringSupplier = () -> "Hello World";

    Mono<String> filteredMono$ = Mono.fromSupplier(stringSupplier)
        .filter(str -> str.length() > 5)
        .log();
    filteredMono$.subscribe(System.out::println);

    StepVerifier.create(filteredMono$)
        .expectComplete();
  }
}
