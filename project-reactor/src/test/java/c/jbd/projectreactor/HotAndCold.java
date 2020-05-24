package c.jbd.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class HotAndCold {

  @Test
  public void coldSubscriber() {
    Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
      .map(String::toUpperCase)
      .delayElements(Duration.ofSeconds(1));

    source.subscribe(d -> System.out.println("Subscriber 1: " + d));
    source.subscribe(d -> System.out.println("Subscriber 2: " + d));
  }

  @Test
  public void hotSubscriber() {
    DirectProcessor<String> hotSource = DirectProcessor.create();
    Flux<String> hotFlux = hotSource.map(String::toUpperCase);

    hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: " + d));

    hotSource.onNext("blue");
    hotSource.onNext("green");

    hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: " + d));

    hotSource.onNext("orange");
    hotSource.onNext("purple");
    hotSource.onComplete();
  }
}
