package c.jbd.webflux.data.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

//@Repository //optional annotation
public interface ContactRepository extends ReactiveMongoRepository<Contact, String> {
  Mono<Contact> findFirstByEmail(String email);

  Mono<Contact> findAllByPhoneOrName(String phoneOrName);
}
