package javaAdvanced.info.kgeorgiy.ja.alentev.bank;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

public class LocalPerson implements Person, Serializable {

    private String firstName;
    private String lastName;
    private String passportNumber;
    private ConcurrentMap<String, Account> personalAccounts;

    public LocalPerson(
            String firstName,
            String lastName,
            String passportNumber,
            ConcurrentMap<String, Account> personalAccounts
    ) throws RemoteException {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.personalAccounts = personalAccounts;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPassportNumber() {
        return passportNumber;
    }

    @Override
    public synchronized Account getAccount(String subId) {
        return personalAccounts.get(createAccountId(subId));
    }

    @Override
    public synchronized Account createAccount(String subId) {
        String id = createAccountId(subId);
        System.out.println("User: " + getLastName() + " " + getFirstName() + " creates new account");
        return personalAccounts.putIfAbsent(id, new LocalAccount(id));
    }

    private String createAccountId(String subId) {
        return passportNumber + ":" + subId;
    }
}
