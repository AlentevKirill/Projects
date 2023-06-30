package javaAdvanced.info.kgeorgiy.ja.alentev.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Person extends Remote {

    /** Returns person first name. */
    String getFirstName() throws RemoteException;

    /** Returns person last name. */
    String getLastName() throws RemoteException;

    /** Returns persons number of passport. */
    String getPassportNumber() throws RemoteException;

    Account getAccount(String subId) throws RemoteException;

    Account createAccount(String subID) throws RemoteException;

}
