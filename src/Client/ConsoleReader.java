package Client;

import java.util.Scanner;

/** Простая оболочка над Scanner, чтобы при необходимости можно было расширить. */
public final class ConsoleReader {

    private final Scanner sc;

    public ConsoleReader(Scanner sc) { this.sc = sc; }

    public String readLine() {
        System.out.print("> ");
        return sc.hasNextLine() ? sc.nextLine() : null;
    }
}

