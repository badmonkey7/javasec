package javasec.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiServer {
    public static final String ServerHost = "localhost";
    public static final int ServerPort = 2333;
    public static final String ServerName = "rmi://"+ServerHost+":"+ServerPort+"/test";

    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(ServerPort);
            Naming.bind(ServerName,new RmiTestImpl());
            System.out.println("启动成功: "+ServerName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
