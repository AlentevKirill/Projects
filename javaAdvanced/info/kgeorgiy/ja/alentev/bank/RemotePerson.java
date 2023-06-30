package javaAdvanced.info.kgeorgiy.ja.alentev.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemotePerson extends UnicastRemoteObject implements Person {

    private String firstName;
    private String lastName;
    private String passportNumber;

    public RemotePerson(
            String firstName,
            String lastName,
            String passportNumber,
            int port
    ) throws RemoteException {
        super(port);
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
    }

    @Override
    public String getFirstName() throws RemoteException {
        return firstName;
    }

    @Override
    public String getLastName() throws RemoteException {
        return lastName;
    }

    @Override
    public String getPassportNumber() throws RemoteException {
        return passportNumber;
    }

    @Override
    public Account getAccount(String subId) throws RemoteException {
        return null;
    }

    @Override
    public Account createAccount(String subID) throws RemoteException {
        return null;
    }
}
