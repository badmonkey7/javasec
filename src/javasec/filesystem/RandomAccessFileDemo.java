package javasec.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;

public class RandomAccessFileDemo {
    public static void readRandomAccessFile(){
        File file = new File("tmp.txt");
        try{
            RandomAccessFile raf = new RandomAccessFile(file,"r");
            int count = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            while((count = raf.read(bytes))!=-1){
                out.write(bytes,0,count);
            }
            System.out.println(out.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void writeRandomAccessFile(){
        try {
            File file = new File("tmp.txt");
            String content = "goodbye,world";
            RandomAccessFile raf = new RandomAccessFile(file,"rw");
            raf.write(content.getBytes());
            raf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            readRandomAccessFile();
            writeRandomAccessFile();
            readRandomAccessFile();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
