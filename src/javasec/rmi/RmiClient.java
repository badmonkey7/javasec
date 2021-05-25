package javasec.rmi;

import java.rmi.Naming;
import static javasec.rmi.RmiServer.ServerName;
public class RmiClient {

    public static void main(String[] args) {
        try{
            RmiTest rt = (RmiTest) Naming.lookup(ServerName);
            System.out.println(rt.test());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
