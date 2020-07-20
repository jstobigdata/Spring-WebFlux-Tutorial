package c.jbd.webflux.data.mongo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@DataMongoTest
//@SpringJUnitConfig
@SpringBootTest // - not needed as it become heavy
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactRepositoryTest {
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private ReactiveMongoOperations mongoOperations;

  @BeforeAll
  public void insertData() {
    Contact contact1 = new Contact()
      .setName("John")
      .setEmail("John@world.com")
      .setPhone("1121121121");

    Contact contact2 = new Contact()
      .setName("Jonny")
      .setEmail("Jonny@world.com")
      .setPhone("22222222222");

    Contact contact3 = new Contact()
      .setName("Hello")
      .setEmail("hello@world.com")
      .setPhone("3333333333");

    //Save and verify contact1
    StepVerifier.create(contactRepository.insert(contact1).log())
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete();

    //save and verify contact2
    StepVerifier.create(contactRepository.save(contact2).log())
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete();

    //Save and verify id
    StepVerifier.create(contactRepository.save(contact3).log())
      .expectSubscription()
      .expectNextMatches(contact -> (contact.getId() != null))
      .verifyComplete();
  }

  @Test
  @Order(1)
  public void findAll() {
    StepVerifier.create(contactRepository.findAll().log())
      .expectSubscription()
      .expectNextCount(3)
      .verifyComplete();
  }

  @Test
  @Order(2)
  public void findByEmail(){
    StepVerifier.create(contactRepository.findFirstByEmail("John@world.com").log())
      .expectSubscription()
      .expectNextMatches(contact -> contact.getEmail().equals("John@world.com"))
      //.expectNextCount(1)
      .verifyComplete();
  }

  @Test
  @Order(3)
  public void updateContact() {
    Mono<Contact> updatedContact$ = contactRepository.findFirstByEmail("John@world.com")
      .map(contact -> {
        //Update the new values
        contact.setPhone("1111111111");
        return contact;
      }).flatMap(contact -> {
      return contactRepository.save(contact);
    });

    StepVerifier.create(updatedContact$.log())
      .expectSubscription()
      .expectNextMatches(contact -> (contact.getPhone().equals("1111111111")))
      .verifyComplete();
  }

  @Test
  @Order(4)
  public void deleteContactById(){
    Mono<Void> deletedContact$ = contactRepository.findFirstByEmail("hello@world.com")
      //.map(Contact::getId)
      .flatMap(contact -> {
        return contactRepository.deleteById(contact.getId());
      }).log();

    StepVerifier.create(deletedContact$)
      .expectSubscription()
      .verifyComplete();
  }

  @Test
  public void deleteContact(){
    Mono<Void> deletedContact$ = contactRepository.findFirstByEmail("Jonny@world.com")
      .flatMap(contact -> contactRepository.delete(contact));

    StepVerifier.create(deletedContact$)
      .expectSubscription()
      .verifyComplete();
  }

  @AfterAll
  public void clearData(){
    Mono<Void> deletedItems$ = contactRepository.deleteAll();
    StepVerifier.create(deletedItems$.log())
      .expectSubscription()
      .verifyComplete();
  }
}