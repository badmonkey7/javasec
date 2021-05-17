package javasec.deserialize;

import java.io.*;

public class DeserializeTest {
    public static void main(String[] args) {
        DeserializeDemo demo = new DeserializeDemo("badmonkey");
        demo.check();
        try(
                FileOutputStream fos = new FileOutputStream("src/javasec/deserialize/demo.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(demo);
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

        try(
                FileInputStream fis = new FileInputStream("src/javasec/deserialize/fake.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ){
           DeserializeDemo demoTest = (DeserializeDemo) ois.readObject();
           demoTest.check();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
