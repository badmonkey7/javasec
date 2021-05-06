package javasec.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileInputStreamDemo {
    public static void main(String[] args) {
        File file = new File("/etc/passwd");
        try{
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int count = 0;
            while ((count = fis.read(bytes)) != -1){
                bos.write(bytes, 0, count);
            }
            System.out.println(bos.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
