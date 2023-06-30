package javaAdvanced.info.kgeorgiy.ja.alentev.bank;

import java.io.Serializable;

public class LocalAccount implements Account, Serializable {

    private final String id;
    private int amount;

    public LocalAccount(final String id) {
        this.id = id;
        amount = 0;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public synchronized int getAmount() {
        return amount;
    }

    @Override
    public synchronized void setAmount(int amount) {
        this.amount = amount;
    }
}
