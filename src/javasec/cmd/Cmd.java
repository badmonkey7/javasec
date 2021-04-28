package javasec.cmd;

import java.io.*;

public class Cmd {
    public String Hello(){
        System.out.println("Hello,World");
        return "Hello,World";
    }
    public static Process exec(String cmd) throws IOException{
        return Runtime.getRuntime().exec(cmd);
    }
    public static void main(String[] args) {
        File file = new File("src/javasec/cmd/Cmd.class");
        try(FileInputStream fis = new FileInputStream(file);BufferedInputStream bis = new BufferedInputStream(fis);){
            byte[] byteCode = bis.readAllBytes();
            for (byte b:byteCode){
                System.out.print(b+",");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
