package javasec.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

// 必须继承 Remote
public interface RmiTest extends Remote {
    String test() throws RemoteException;
}
