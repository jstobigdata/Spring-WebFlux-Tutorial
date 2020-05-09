package c.jbd.projectreactor;

import reactor.core.publisher.Mono;

public class SimpleMono {

  public static void main(String[] args) {
    //Creates a new String Mono which emits "Hi there!"
    Mono<String> message = Mono.just("Hi there!");
    message.subscribe(System.out::println);
  }

}
