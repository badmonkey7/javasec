package javasec.deserialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GadgetChainDemo {
    public static void main(String[] args) {
        Command command = new Command("whoami"); // 伪造Runnable，服务端程序中的Runnable并不是我们自己写的这个可以执行任意代码的Command，但是都是Runnable类(
        Gadget gadget = new Gadget(command);
        // 序列化
        try(
                FileOutputStream fos = new FileOutputStream("src/javasec/deserialize/gadget.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(gadget);
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        // 利用的过程
        try(
                FileInputStream fis = new FileInputStream("src/javasec/deserialize/gadget.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ){
            Gadget fake = (Gadget) ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
