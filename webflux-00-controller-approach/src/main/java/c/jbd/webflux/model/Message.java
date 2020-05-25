package c.jbd.webflux.model;

import java.time.LocalDate;
import java.util.StringJoiner;

public class Message {
  private LocalDate date;
  private String greeting;
  private String ipAddress;

  public LocalDate getDate() {
    return date;
  }

  public Message setDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public String getGreeting() {
    return greeting;
  }

  public Message setGreeting(String greeting) {
    this.greeting = greeting;
    return this;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public Message setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
      .add("date=" + date)
      .add("greeting='" + greeting + "'")
      .add("ipAddress='" + ipAddress + "'")
      .toString();
  }
}
