package javasec.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiTestImpl extends UnicastRemoteObject implements RmiTest {
    private static final long serialVersionUID = 1L;
    protected RmiTestImpl() throws RemoteException{
        super();
    }
    public String test() throws RemoteException{
        return "Remote Server test!";
    }
}
