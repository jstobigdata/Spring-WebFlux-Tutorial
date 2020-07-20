package c.jbd.webflux.controller;

import c.jbd.webflux.data.mongo.Contact;
import c.jbd.webflux.data.mongo.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
@RequestMapping("/controller")
public class ContactController {

  private final ContactRepository contactRepository;

  @Autowired
  public ContactController(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  @GetMapping("/contacts")
  public Flux<Contact> getAllContacts() {
    return contactRepository.findAll();
  }

  @GetMapping(value = "/contacts/{id}")
  public Mono<ResponseEntity<Contact>> getContact(@PathVariable String id) {
    return contactRepository.findById(id)
      .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
      .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping(value = "/contacts/byEmail/{email}")
  public Mono<ResponseEntity<Contact>> getByEmail(@PathVariable String email) {
    return contactRepository.findFirstByEmail(email)
      .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
      .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/contacts")
  public Mono<ResponseEntity<Contact>> insertContact(@RequestBody Contact contact){
    return contactRepository.insert(contact)
      .map(contact1 -> new ResponseEntity<>(contact1, HttpStatus.ACCEPTED))
      .defaultIfEmpty(new ResponseEntity<>(contact, HttpStatus.NOT_ACCEPTABLE));
  }

  @PutMapping("/contacts/{id}")
  public Mono<ResponseEntity<Contact>> updateContact(@RequestBody Contact contact, @PathVariable String id) {
    return contactRepository.findById(id)
      .flatMap(contact1 -> {
        contact.setId(id);
        return contactRepository.save(contact)
          .map(contact2 -> new ResponseEntity<>(contact2, HttpStatus.ACCEPTED));
      }).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping(value = "/contacts/{id}")
  public Mono<Void> deleteContact(@PathVariable String id) {
    return contactRepository.deleteById(id);
  }
}
