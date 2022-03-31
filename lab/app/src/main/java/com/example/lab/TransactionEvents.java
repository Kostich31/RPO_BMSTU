package com.example.lab;

public interface TransactionEvents {
    String enterPin(int ptc, String amount);
    void transactionResult(boolean result);
}
