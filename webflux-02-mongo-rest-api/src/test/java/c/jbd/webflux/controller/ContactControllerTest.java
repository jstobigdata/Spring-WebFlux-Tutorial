package c.jbd.webflux.controller;

import c.jbd.webflux.data.mongo.Contact;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("unit_test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  private Contact savedContact;

  @Test
  @Order(0)
  public void createContact() {
    Flux<Contact> contactFlux = webTestClient.post()
      .uri("/controller/contacts")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(
        new Contact("webtest", "wt@webtest.com", "0000000000")))
      .exchange()
      .expectStatus().isAccepted()
      .returnResult(Contact.class).getResponseBody()
      .log();

    contactFlux.next().subscribe(contact -> {
      this.savedContact = contact;
    });

    Assertions.assertNotNull(savedContact);
  }

  @Test
  @Order(1)
  public void getByEmail() {
    Flux<Contact> contactFlux = webTestClient.get()
      .uri("/controller/contacts/byEmail/{email}", "wt@webtest.com")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .returnResult(Contact.class).getResponseBody()
      .log();

    StepVerifier.create(contactFlux)
      .expectSubscription()
      .expectNextMatches(contact -> contact.getEmail().equals("wt@webtest.com"))
      .verifyComplete();
  }

  @Test
  @Order(2)
  public void updateContact() {

    Flux<Contact> contactFlux = webTestClient.put()
      .uri("/controller/contacts/{id}", savedContact.getId())
      .accept(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(new Contact("WebTestClient", "wtc@email.com", "11111111").setId(savedContact.getId())))
      .exchange()
      .returnResult(Contact.class).getResponseBody()
      .log();

    StepVerifier.create(contactFlux)
      .expectSubscription()
      .expectNextMatches(contact -> contact.getEmail().equals("wtc@email.com"))
      .verifyComplete();
  }

  @Test
  @Order(2)
  public void getAllContacts() {
    Flux<Contact> contactsFlux = webTestClient.get()
      .uri("/controller/contacts")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .returnResult(Contact.class).getResponseBody()
      .log();

    StepVerifier.create(contactsFlux)
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete();
  }

  @Test
  @Order(3)
  public void deleteContact() {
    Flux<Void> vFlux = webTestClient.delete()
      .uri("/controller/contacts/{id}", savedContact.getId())
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .returnResult(Void.class).getResponseBody()
      .log();

    StepVerifier.create(vFlux)
      .expectSubscription()
      .verifyComplete();
  }
}
