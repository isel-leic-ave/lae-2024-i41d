package pt.isel;

import java.time.LocalDate;

class Constants {
    static final int BITS_OF_10KB = 8*10*1024; // <=> Kotlin const val
}

class SavingsAccount {
    private static int accountsCount;
    private final LocalDate createdDate = LocalDate.now(); // <=> Kotlin val
    private short accountCode;
    private String holderName;
    private long balance;
    private boolean isActive;
    private static final double DEFAULT_INTEREST_REATE = 0.07;
    private final double interestRate = DEFAULT_INTEREST_REATE;
}