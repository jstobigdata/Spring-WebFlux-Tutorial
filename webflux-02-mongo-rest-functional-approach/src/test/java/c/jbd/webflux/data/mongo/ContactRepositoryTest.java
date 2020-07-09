package c.jbd.webflux.data.mongo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.test.StepVerifier;

//@DataMongoTest
//@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest // - not needed as it become heavy
public class ContactRepositoryTest {
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private ReactiveMongoOperations mongoOperations;

  @BeforeAll
  //@Order(1)
  public void insertData() {
    Contact contact1 = new Contact();
    contact1.setName("John")
      .setEmail("John@world.com")
      .setPhone("1111111111");

    Contact contact2 = new Contact();
    contact2.setName("Jonny")
      .setEmail("Jonny@world.com")
      .setPhone("22222222222");

    //
    StepVerifier.create(contactRepository.insert(contact1).log())
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete();

    //
    StepVerifier.create(contactRepository.save(contact2).log())
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete();
  }

  @Test
  @Order(2)
  public void findAll() {
    StepVerifier.create(contactRepository.findAll().log())
      .expectSubscription()
      .expectNextCount(2)
      .verifyComplete();
  }

  @Test
  public void findByEmail(){
    StepVerifier.create(contactRepository.findFirstByEmail("John@world.com").log())
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete();
  }

  @Test
  @Order(3)
  public void updateContact() {
    contactRepository.findFirstByEmail("John@world.com")
      .map(contact -> {
        contact.setPhone("1212121212");
        return contact;
      }).flatMap(contact -> {
      return contactRepository.save(contact);
    }).log().subscribe();
  }

  @Test
  @Order(4)
  public void deleteContact(){
    Contact contact = new Contact();
    contact.setName("Hello")
      .setEmail("hello@world.com")
      .setPhone("3333333333");

    contactRepository.save(contact)
      /*.flatMap(contact1 -> {
        return contactRepository.delete(contact1);
      })*/.log().subscribe();
  }
}
