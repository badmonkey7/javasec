package javasec.cmdexec;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

public class ProcessImplDemo {
    public static void processImpl(){
        String[] cmd = {"pwd"};
        try {
            Class<?> unixClass = Class.forName("java.lang.ProcessImpl");
            // 直接获取静态方法
            Method method = unixClass.getDeclaredMethod("start",new String[]{}.getClass(), Map.class,String.class,ProcessBuilder.Redirect[].class,boolean.class);
            method.setAccessible(true);
            InputStream inputStream = ((Process) method.invoke(null,cmd,null,".",null,true)).getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String result = scanner.hasNext()?scanner.next():"";
            System.out.println(result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        processImpl();
    }
}
