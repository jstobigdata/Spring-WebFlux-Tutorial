package c.jbd.projectreactor;

import reactor.core.publisher.Flux;

public class SimpleFlux {
  public static void main(String[] args) {

Flux<Integer> $num = Flux.range(1, 10).log()
    .map(val -> val * 2).log()
    .map(val -> val + 1).log();

//Subscriber - prints 3,5,7..21
$num.subscribe(
    value -> {
      System.out.println(value);
    }, error -> {
      System.out.println(error);
    }, () -> {
      System.out.println("===== Completed =====");
    });

  }

}
