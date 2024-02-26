package pt.isel;

import static java.lang.System.currentTimeMillis;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class App {
    public static void main(String[] args) {
        reverse(LocalDate.now());
        reverse(Instant.now());

        ExampleClass.perTypeMethod();
        final ExampleClass receiver = new ExampleClass();
        receiver.perInstanceMethod();
    }
    /**
     * @param milestone may point to an instance of any class that either
     * directly or indirectly inherits from the Temporal type.
     */
    static Temporal reverse(Temporal milestone) {
        return null;
    }
}

class ExampleClass {
    public void perInstanceMethod() {  }
    public static void perTypeMethod() { }
}

class SavingsAccount {
    private static int accountsCount;
    private short accountCode;
    private String holderName;
    private long balance;
    private boolean isActive;
    private final double interestRate = 0.02;
}

class Constants{ static final int BITS_OF_10KB =8*10*1024; }

class Account{
    private static int nrOfAccounts = 0;
    public static int getNumberOfAccounts() { return nrOfAccounts; } 
    private final long created;
    public Account() {
        nrOfAccounts++; // <=> Account.nrOfAccounts++;
        created = currentTimeMillis(); // <=> this.created = currentTimeMillis();
    }
}
