package c.jbd.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestFlux {

  /**
   * Create a simple flux.
   */
  @Test
  public void justFlux() {
    Flux<String> simpleFlux$ = Flux.just("hello", "there").log();

    simpleFlux$.subscribe(val -> System.out.println(val));

    StepVerifier.create(simpleFlux$)
        .expectNext("hello")
        .expectNext("there")
        .verifyComplete();
  }

  /**
   * Create an Empty Flux
   */
  @Test
  public void emptyFlux() {
    Flux emptyFlux$ = Flux.empty().log();
    emptyFlux$.subscribe(
        val -> {
          System.out.println("=== Never called ===");
        },
        error -> {
          //Error
        },
        () -> {
          System.out.println("==== Completed ===");
        });

    StepVerifier.create(emptyFlux$)
        .expectNextCount(0)
        .verifyComplete();
  }

  /**
   * Never emitting flux
   */
  @Test
  public void neverFlux() {
    Flux neverFlux = Flux.never().log();

    StepVerifier
        .create(neverFlux)
        .expectTimeout(Duration.ofSeconds(2))
        .verify();
  }

  @Test
  public void handleError() {
    Flux<String> names$ = Flux.just("Tom", "Jerry")
        .concatWith(Flux.error(new RuntimeException("Exception occurred..!")))
        .concatWith(Flux.just("John"));

    names$.subscribe(
        name -> {
          System.out.println(name);
        }, err -> {
          err.printStackTrace();
        }
    );

    StepVerifier.create(names$)
        .expectNext("Tom")
        .expectNext("Jerry")
        .expectError(RuntimeException.class)
        .verify();
  }

  /**
   * Filter the values
   */
  @Test
  public void rangeFlux() {
    Flux<Integer> range$ = Flux.range(10, 10)
        .filter(num -> Math.floorMod(num, 2) == 1)
        .log();

    range$.subscribe(System.out::println);

    StepVerifier.create(range$)
        //.expectNextCount(5)
        .expectNext(11, 13, 15, 17, 19)
        .verifyComplete();
  }

  /**
   * Transform using the map
   */
@Test
public void transformUsingMap() {
  List<Person> list = new ArrayList<>();
  list.add(new Person("John", "john@gmail.com", "12345678"));
  list.add(new Person("Jack", "jack@gmail.com", "12345678"));
  Flux<Person> people$ = Flux.fromIterable(list)
      .map(p -> {
        return new Person(p.getName().toUpperCase(),
            p.getEmail().toUpperCase(), p.getPhone().toUpperCase());
      })
      .log();

  people$.subscribe(System.out::println);

  StepVerifier.create(people$)
      .expectNext(new Person("JOHN", "JOHN@GMAIL.COM", "12345678"))
      .expectNextCount(1)
      .verifyComplete();
}

  /**
   * Transform using the FlatMap
   */
@Test
public void transformUsingFlatMap() {
  List<Person> list = new ArrayList<>();
  list.add(new Person("John", "john@gmail.com", "12345678"));
  list.add(new Person("Jack", "jack@gmail.com", "12345678"));
  Flux<Person> people$ = Flux.fromIterable(list)
      .flatMap(person -> {
        return asyncCapitalize(person);
      })
      .log();

  people$.subscribe(System.out::println);

  StepVerifier.create(people$)
      .expectNext(new Person("JOHN", "JOHN@GMAIL.COM", "12345678"))
      .expectNext(new Person("JACK", "JACK@GMAIL.COM", "12345678"))
      .verifyComplete();
}

//Used in asynchronously process the Person
Mono<Person> asyncCapitalize(Person person) {
  Person p = new Person(person.getName().toUpperCase(),
      person.getEmail().toUpperCase(), person.getPhone().toUpperCase());
  Mono<Person> person$ = Mono.just(p);
  return person$;
}


}
