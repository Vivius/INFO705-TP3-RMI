package info705.tp3.ftp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Ftp extends Remote {
    byte[] get(String filenmae) throws RemoteException;
    void put(String filename, byte[] file) throws RemoteException;
    void cd(String dir) throws RemoteException;
    List<String> ls() throws RemoteException;
    String pwd() throws RemoteException;
}
