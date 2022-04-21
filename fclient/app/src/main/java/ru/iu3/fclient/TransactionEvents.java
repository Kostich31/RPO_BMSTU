package ru.iu3.fclient;

public interface TransactionEvents {
    // Номер попытки и сумма транзакции, которая пойдёт в интерфейс
    String enterPin(int ptc, String amount);
    void transactionResult(boolean result);
}
