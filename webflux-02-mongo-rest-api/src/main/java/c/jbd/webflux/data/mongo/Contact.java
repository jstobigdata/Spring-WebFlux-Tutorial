package c.jbd.webflux.data.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.StringJoiner;

@Document(collection = "contact")
public class Contact {
  @Id
  private String id;
  private String name;
  private String email;
  private String phone;

  public Contact() {
  }

  public Contact(String name, String email, String phone) {
    this.name = name;
    this.email = email;
    this.phone = phone;
  }

  public String getId() {
    return id;
  }

  public Contact setId(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Contact setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Contact setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public Contact setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Contact contact = (Contact) o;
    return Objects.equals(id, contact.id) &&
      Objects.equals(name, contact.name) &&
      Objects.equals(email, contact.email) &&
      Objects.equals(phone, contact.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Contact.class.getSimpleName() + "[", "]")
      .add("id='" + id + "'")
      .add("name='" + name + "'")
      .add("email='" + email + "'")
      .add("phone=" + phone)
      .toString();
  }
}
