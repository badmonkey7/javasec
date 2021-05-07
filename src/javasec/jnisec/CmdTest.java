package javasec.jnisec;

import java.io.File;
import java.lang.reflect.Method;

public class CmdTest {
    private static final String className = "javasec.jnisec.CmdDemo";
    private static final byte[] classBytes = new byte[]{-54,-2,-70,-66,0,0,0,55,0,15,10,0,3,0,12,7,0,13,7,0,14,1,0,6,60,105,110,105,116,62,1,0,3,40,41,86,1,0,4,67,111,100,101,1,0,15,76,105,110,101,78,117,109,98,101,114,84,97,98,108,101,1,0,4,101,120,101,99,1,0,38,40,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,41,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,1,0,10,83,111,117,114,99,101,70,105,108,101,1,0,12,67,109,100,68,101,109,111,46,106,97,118,97,12,0,4,0,5,1,0,22,106,97,118,97,115,101,99,47,106,110,105,115,101,99,47,67,109,100,68,101,109,111,1,0,16,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,0,33,0,2,0,3,0,0,0,0,0,2,0,1,0,4,0,5,0,1,0,6,0,0,0,29,0,1,0,1,0,0,0,5,42,-73,0,1,-79,0,0,0,1,0,7,0,0,0,6,0,1,0,0,0,3,1,9,0,8,0,9,0,0,0,1,0,10,0,0,0,2,0,11};

    public static void main(String[] args) {
        String cmd = "pwd";
        try {
            ClassLoader cl = new ClassLoader(CmdTest.class.getClassLoader()){
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    try {
                        return super.findClass(name);
                    }catch (ClassNotFoundException e){
                        return defineClass(className,classBytes,0,classBytes.length);
                    }
                }
            };
            File libPath = new File("src/javasec/jnisec/libcmd.so");
            Class cmdClass = cl.loadClass(className);
            // 反射java.lang.ClassLoader.loadLibrary0 加载动态链接库
            Method loadLibrary0 = ClassLoader.class.getDeclaredMethod("loadLibrary0",Class.class,File.class);
            loadLibrary0.setAccessible(true);
            loadLibrary0.invoke(cl,cmdClass,libPath);
            // 使用system加载动态链接库 必须是绝对路径
//            System.load("/home/badmonkey/code/java/javasec/src/javasec/jnisec/libcmd.so");
            String content = (String) cmdClass.getMethod("exec",String.class).invoke(null,cmd);
            System.out.println(content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
