package javasec.cmdexec;

import java.io.InputStream;
import java.util.Scanner;

public class ProcessBuilderDemo {
    public static void main(String[] args) {
        String cmd = "pwd";
        try{
            InputStream inputStream = new ProcessBuilder(cmd).start().getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String result = scanner.hasNext()?scanner.next():"";
            System.out.println(result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
