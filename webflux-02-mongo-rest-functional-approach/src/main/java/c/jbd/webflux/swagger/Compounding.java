package c.jbd.webflux.swagger;

import java.math.BigDecimal;
import java.util.Scanner;

public class Compounding {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int interestRate = scanner.nextInt();
    System.out.println("Base Money");
    BigDecimal money = scanner.nextBigDecimal();
    System.out.println("Year to die: ");
    int age = scanner.nextInt() - 30;

    for (int i = 0; i < age; i++) {
      System.out.println("After " + (30 + i) + ": ");
      money = money
        .add(money.multiply(BigDecimal.valueOf(100))
          .divide(BigDecimal.valueOf(interestRate)));
    }
  }
}
