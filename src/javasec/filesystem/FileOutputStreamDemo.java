package javasec.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileOutputStreamDemo {
    public static void main(String[] args) {
        File file = new File("tmp.txt");
        String content = "hello,world";
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
