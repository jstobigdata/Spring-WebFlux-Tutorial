package c.jbd.webflux.resources;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/hello")
public class HelloController {

  @GetMapping(path = "/mono")
  public Mono<String> getMono(){
    return Mono.just("Welcome to JstoBigdata.com");
  }

  @GetMapping(path = "/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<String> getFlux(){
    Flux<String> msg$ = Flux.just("Welcome ", "to ", "JstoBigdata.com")
      .delayElements(Duration.ofSeconds(1)).log();
    return msg$;
  }

  //@GetMapping
}
