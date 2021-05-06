package javasec.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemProviderDemo {
    static void readFileSystemProvider(){
        try{
            Path path = Paths.get("tmp.txt");
            byte[] bytes = Files.readAllBytes(path);
            System.out.println(new String(bytes));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    static void writeFileSystemProvider(){
        try{
            Path path = Paths.get("tmp.txt");
            String content = "Hello,world";
            Files.write(path, content.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try{
            readFileSystemProvider();
            writeFileSystemProvider();
            readFileSystemProvider();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
