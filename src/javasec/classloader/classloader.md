# ClassLoader 类加载机制 

`java`程序在运行前需要编译成`class`文件，而类初始化的时候会采用`java.lang.ClassLoader`加载类字节码(`class`文件)，然后在`JVM`中执行

如下图所示

![JVM结构](https://i.loli.net/2021/04/28/yxR8G3Mp25aKgho.jpg)

## ClassLoader

所有的`java`类必须在`jvm`中加载运行，而`ClassLoader`则是负责加载类文件的。一共有三种类加载器，`BootStrap ClassLoader`,`Extension ClassLoader`,`AppClassLoader`,其中`AppClassLoader`是默认的类加载器`BootStrap ClassLoader`使用C++在JVM中实现，使用`getClassLoader`方法可以获取当前类的加载器，如果是被`BootStrap ClassLoader`加载则返回`null`

`ClassLoader`的几个方法

- `loadClass`
- `findClass`
- `findLoadedClass`
- `defineClass`
- `resolveClass`

## Java类动态加载方式

**显示加载**：java反射，或者`ClassLoader`加载一个类对象，可以认为是动态加载类，可以自定义类加载器去加载任意的类。

```java
// 反射 加载类
Class.forName("java.lang.Runtime");
// ClassLoader 加载类
this.getClass().getClassLoader().loadClass("java.lang.Runtime");
```

`Class.forName("类名")`默认会初始化被加载类的静态属性和静态方法，而`ClassLoader.loadClass`则不会初始化默认的类方法。

## ClassLoader类加载流程

```java
package site.badmoney.javasec;
public class HelloWorld(){
    .....
}
```

`ClassLoader`加载`HelloWorld`的流程如下

1. `ClassLoader`调用`public Class<?> loadClass(String name)`加载`HelloWorld`类
2. 调用`findLoadedClass`检查`HelloWorld`类是否已经被加载到`JVM`中
3. 如果当前的`ClassLoader`，采用`new ClassLoader(父类加载器)`创建，就使用父类加载器加载`HelloWord`，反之使用`BootStrap ClassLoader`加载
4. 如果上一步无法加载类，那么调用自身的`findClass`加载`HelloWorld`，**划重点**
5. 如果自身的`findClass`没有被重写，那么抛出异常，反之通过`findClass`找到`HelloWord`的类字节码，然后调用`defineClass`在JVM中注册该类
6. 如果采用了`resolveClass`，那么需要链接类
7. 返回一个被`JVM`加载后的`java.lang.Class`对象(虽然是对象，但是是类！！)

## 自定义ClassLoader

`java.lang.ClassLoader`是所有类加载器的父类，通过继承，重写`findClass`方法实现不同的`ClassLoader`，比如用于加载`jar`包的`java.net.URLClassLoader`可以加载本地的Class或者远程的文件

类加载器

```java
package javasec.classloader;

import java.lang.reflect.Method;

public class SelfClassLoader extends ClassLoader{
    private static String className = "javasec.cmd.Cmd";
    // 包名必须和字节码匹配
    // 编译字节码的jdk版本必须和当前jdk版本相同
    private static byte[] classBytes = new byte[]{ -54,-2,-70,-66,0,0,0,55,0,93,10,0,22,0,36,9,0,37,0,38,8,0,39,10,0,40,0,41,7,0,42,8,0,43,10,0,5,0,44,7,0,45,10,0,8,0,46,7,0,47,10,0,10,0,48,10,0,10,0,49,18,0,0,0,53,10,0,40,0,54,10,0,10,0,55,7,0,56,10,0,16,0,57,10,0,8,0,55,7,0,58,10,0,19,0,59,7,0,60,7,0,61,1,0,6,60,105,110,105,116,62,1,0,3,40,41,86,1,0,4,67,111,100,101,1,0,15,76,105,110,101,78,117,109,98,101,114,84,97,98,108,101,1,0,5,72,101,108,108,111,1,0,20,40,41,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,1,0,4,109,97,105,110,1,0,22,40,91,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,41,86,1,0,13,83,116,97,99,107,77,97,112,84,97,98,108,101,7,0,62,7,0,63,1,0,10,83,111,117,114,99,101,70,105,108,101,1,0,8,67,109,100,46,106,97,118,97,12,0,23,0,24,7,0,64,12,0,65,0,66,1,0,11,72,101,108,108,111,44,87,111,114,108,100,7,0,67,12,0,68,0,69,1,0,12,106,97,118,97,47,105,111,47,70,105,108,101,1,0,25,115,114,99,47,106,97,118,97,115,101,99,47,99,109,100,47,67,109,100,46,99,108,97,115,115,12,0,23,0,69,1,0,23,106,97,118,97,47,105,111,47,70,105,108,101,73,110,112,117,116,83,116,114,101,97,109,12,0,23,0,70,1,0,27,106,97,118,97,47,105,111,47,66,117,102,102,101,114,101,100,73,110,112,117,116,83,116,114,101,97,109,12,0,23,0,71,12,0,72,0,73,1,0,16,66,111,111,116,115,116,114,97,112,77,101,116,104,111,100,115,15,6,0,74,8,0,75,12,0,76,0,77,12,0,78,0,69,12,0,79,0,24,1,0,19,106,97,118,97,47,108,97,110,103,47,84,104,114,111,119,97,98,108,101,12,0,80,0,81,1,0,19,106,97,118,97,47,108,97,110,103,47,69,120,99,101,112,116,105,111,110,12,0,82,0,24,1,0,15,106,97,118,97,115,101,99,47,99,109,100,47,67,109,100,1,0,16,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,1,0,19,91,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,1,0,2,91,66,1,0,16,106,97,118,97,47,108,97,110,103,47,83,121,115,116,101,109,1,0,3,111,117,116,1,0,21,76,106,97,118,97,47,105,111,47,80,114,105,110,116,83,116,114,101,97,109,59,1,0,19,106,97,118,97,47,105,111,47,80,114,105,110,116,83,116,114,101,97,109,1,0,7,112,114,105,110,116,108,110,1,0,21,40,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,41,86,1,0,17,40,76,106,97,118,97,47,105,111,47,70,105,108,101,59,41,86,1,0,24,40,76,106,97,118,97,47,105,111,47,73,110,112,117,116,83,116,114,101,97,109,59,41,86,1,0,12,114,101,97,100,65,108,108,66,121,116,101,115,1,0,4,40,41,91,66,10,0,83,0,84,1,0,2,1,44,1,0,23,109,97,107,101,67,111,110,99,97,116,87,105,116,104,67,111,110,115,116,97,110,116,115,1,0,21,40,66,41,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,1,0,5,112,114,105,110,116,1,0,5,99,108,111,115,101,1,0,13,97,100,100,83,117,112,112,114,101,115,115,101,100,1,0,24,40,76,106,97,118,97,47,108,97,110,103,47,84,104,114,111,119,97,98,108,101,59,41,86,1,0,15,112,114,105,110,116,83,116,97,99,107,84,114,97,99,101,7,0,85,12,0,76,0,89,1,0,36,106,97,118,97,47,108,97,110,103,47,105,110,118,111,107,101,47,83,116,114,105,110,103,67,111,110,99,97,116,70,97,99,116,111,114,121,7,0,91,1,0,6,76,111,111,107,117,112,1,0,12,73,110,110,101,114,67,108,97,115,115,101,115,1,0,-104,40,76,106,97,118,97,47,108,97,110,103,47,105,110,118,111,107,101,47,77,101,116,104,111,100,72,97,110,100,108,101,115,36,76,111,111,107,117,112,59,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,76,106,97,118,97,47,108,97,110,103,47,105,110,118,111,107,101,47,77,101,116,104,111,100,84,121,112,101,59,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,91,76,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,59,41,76,106,97,118,97,47,108,97,110,103,47,105,110,118,111,107,101,47,67,97,108,108,83,105,116,101,59,7,0,92,1,0,37,106,97,118,97,47,108,97,110,103,47,105,110,118,111,107,101,47,77,101,116,104,111,100,72,97,110,100,108,101,115,36,76,111,111,107,117,112,1,0,30,106,97,118,97,47,108,97,110,103,47,105,110,118,111,107,101,47,77,101,116,104,111,100,72,97,110,100,108,101,115,0,33,0,21,0,22,0,0,0,0,0,3,0,1,0,23,0,24,0,1,0,25,0,0,0,29,0,1,0,1,0,0,0,5,42,-73,0,1,-79,0,0,0,1,0,26,0,0,0,6,0,1,0,0,0,5,0,1,0,27,0,28,0,1,0,25,0,0,0,39,0,2,0,1,0,0,0,11,-78,0,2,18,3,-74,0,4,18,3,-80,0,0,0,1,0,26,0,0,0,10,0,2,0,0,0,7,0,8,0,8,0,9,0,29,0,30,0,1,0,25,0,0,1,123,0,3,0,9,0,0,0,-115,-69,0,5,89,18,6,-73,0,7,76,-69,0,8,89,43,-73,0,9,77,-69,0,10,89,44,-73,0,11,78,45,-74,0,12,58,4,25,4,58,5,25,5,-66,54,6,3,54,7,21,7,21,6,-94,0,29,25,5,21,7,51,54,8,-78,0,2,21,8,-70,0,13,0,0,-74,0,14,-124,7,1,-89,-1,-30,45,-74,0,15,-89,0,24,58,4,45,-74,0,15,-89,0,12,58,5,25,4,25,5,-74,0,17,25,4,-65,44,-74,0,18,-89,0,21,78,44,-74,0,18,-89,0,11,58,4,45,25,4,-74,0,17,45,-65,-89,0,8,77,44,-74,0,20,-79,0,5,0,28,0,79,0,86,0,16,0,88,0,92,0,95,0,16,0,19,0,107,0,114,0,16,0,115,0,119,0,122,0,16,0,10,0,-124,0,-121,0,19,0,2,0,26,0,0,0,58,0,14,0,0,0,12,0,10,0,13,0,28,0,14,0,34,0,15,0,60,0,16,0,73,0,15,0,79,0,18,0,86,0,13,0,107,0,18,0,114,0,13,0,-124,0,20,0,-121,0,18,0,-120,0,19,0,-116,0,21,0,31,0,0,0,116,0,12,-1,0,46,0,8,7,0,32,7,0,5,7,0,8,7,0,10,7,0,33,7,0,33,1,1,0,0,-1,0,32,0,4,7,0,32,7,0,5,7,0,8,7,0,10,0,0,70,7,0,16,-1,0,8,0,5,7,0,32,7,0,5,7,0,8,7,0,10,7,0,16,0,1,7,0,16,8,-7,0,2,70,7,0,16,-1,0,7,0,4,7,0,32,7,0,5,7,0,8,7,0,16,0,1,7,0,16,7,-7,0,1,66,7,0,19,4,0,3,0,34,0,0,0,2,0,35,0,88,0,0,0,10,0,1,0,86,0,90,0,87,0,25,0,50,0,0,0,8,0,1,0,51,0,1,0,52
};
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(name.equals(className)){
            return defineClass(className,classBytes,0,classBytes.length);
        }
        return super.findClass(name);
    }

    public static void main(String[] args) {
        SelfClassLoader selfClassLoader = new SelfClassLoader();
        try{
            Class  loadedClass = selfClassLoader.loadClass(className);
            Object instance = loadedClass.newInstance();
            Method method  = instance.getClass().getMethod("Hello");
            method.invoke(instance);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
```

cmd类

```java
package javasec.cmd;

import java.io.*;
import java.security.PublicKey;

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
```

远程加载jar包

```java
 public static void main(String[] args) {
        try {
//            URL url = new URL("file:///home/badmonkey/code/java/javasec/src/javasec/cmd/cmd.jar");
            URL url = new URL("file:///home/badmonkey/code/java/javasec/src/javasec/cmd/Cmd.class");
            URLClassLoader ucl = new URLClassLoader(new URL[]{url});
            Class cmd = ucl.loadClass(className);
            String command = "whoami";
            Method exec = cmd.getMethod("exec",String.class);
            Process process = (Process) exec.invoke(null,command);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true){
                String res = null;
                res = bfr.readLine();
                if(res == null){
                    break;
                }
                System.out.println(res);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

