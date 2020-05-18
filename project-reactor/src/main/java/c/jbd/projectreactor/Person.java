package c.jbd.projectreactor;

import java.util.Objects;
import java.util.StringJoiner;

public class Person {
  private String name;
  private String email;
  private String phone;

  public Person(String name, String email, String phone) {
    this.name = name;
    this.email = email;
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public Person setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Person setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public Person setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Important to Override the equals.
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name) &&
        Objects.equals(email, person.email) &&
        Objects.equals(phone, person.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, email, phone);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .add("email='" + email + "'")
        .add("phone='" + phone + "'")
        .toString();
  }
}
