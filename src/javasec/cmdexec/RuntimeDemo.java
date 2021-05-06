package javasec.cmdexec;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Scanner;

public class RuntimeDemo {
    public static void main(String[] args){
        String rt = new String(new byte[] {106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 82, 117, 110, 116, 105, 109, 101});
        String cmd = "pwd";
        try{
            Class<?> rtClass = Class.forName(rt);
            Method getRT= (Method) rtClass.getMethod(new String(new byte[]{103, 101, 116, 82, 117, 110, 116, 105, 109, 101}));
            Method ex3c= (Method) rtClass.getMethod(new String(new byte[]{101, 120, 101, 99}),String.class);
            // process 对象
            Process process = (Process) ex3c.invoke(getRT.invoke(null,new Object[]{}),cmd);
            //
            String getInput = new String(new byte[]{103, 101, 116, 73, 110, 112, 117, 116, 83, 116, 114, 101, 97, 109});
            Method getInputS = process.getClass().getMethod(getInput);
            getInputS.setAccessible(true);
//            Scanner scanner = new Scanner(process.getInputStream());
            Scanner scanner = new Scanner((InputStream)getInputS.invoke(process,new Object[]{})).useDelimiter("\\A");
            String result = scanner.hasNext()?scanner.next():"";
            System.out.println(result);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
