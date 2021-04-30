# Unsafe类

`sun.misc.Unsafe`是java底层api提供的一个java类(仅限在Java内部使用，反射可以调用)，`Unsafe`提供了非常底层的`内存，CAS,线程调度，类，对象`等操作，字如其名`Unsafe`提供的所有方法都是不安全的。

## 获取Unsafe对象

`Unsafe`是Java内部API,外部禁止调用，在编译Java类的时候如果检测到引用了`Unsafe`类也会有禁止使用的警告，`Unsafe`部分源码

```java
public final class Unsafe {

    static {
        Reflection.registerMethodsToFilter(Unsafe.class, "getUnsafe");
    }

    private Unsafe() {}

    private static final Unsafe theUnsafe = new Unsafe();
    private static final jdk.internal.misc.Unsafe theInternalUnsafe = jdk.internal.misc.Unsafe.getUnsafe();
    @CallerSensitive
    public static Unsafe getUnsafe() {
        Class<?> caller = Reflection.getCallerClass();
        if (!VM.isSystemDomainLoader(caller.getClassLoader()))
            throw new SecurityException("Unsafe");
        return theUnsafe;
    }
}
```

 `Unsafe`类不能被继承，不能通过new 的方式创建`Unsafe`实例，使用`getUnsafe`方法获取`Unsafe`实例时，会检查caller的类加载器，只有`BootStrap ClassLoader`才可以调用。

**使用反射获取Unsafe**

```java
Field theUnsafe = Unsafe.getClass().getDelcaredField("theUnsafe");// 获取theUnsafe 成员
theUnsafe.setAccessible(true);// 设置为公有
Unsafe unsafe = (Unsafe) theUnsafe.get(null);// 静态成员
```

使用构造方法获取`Unsafe`对象

```java
Constructor constructor = Unsafe.getDelcaredConstrutor();
constructor.setAccessible(true);
Unsafe unsafe = (Unsafe) constructor.newInstance();
```

## allocateInstance 无视构造方法创建类实例

如果因为某种原因不能直接反射得到某个类的实例，那么可以使用`Unsafe`的`allocateInstance`方法绕过这个限制。

```java
public class UnSafeTest {

   private UnSafeTest() {
      // 假设RASP在这个构造方法中插入了Hook代码，我们可以利用Unsafe来创建类实例
      System.out.println("init...");
   }

}
// RASP 编译执行时动态检测是否存在安全问题
```

获取实例

`UnsafeTest test = (UnsafeTest) unsafe.allocateInstance(UnsafeTest.class);`

在渗透测试中，如果RASP限制了`java.io.FileInputStream`类的构造方法导致我们无法读文件，或者限制了`UNIXProcess/ProcessImpl`类的构造方法导致我们无法执行命令，可以使用Unsafe创建对应的实例。

## defineClass创建类对象

通常来说，可以使用ClassLoader类的defineClass向JVM中注册一个类，但是如果ClassLoader被限制那么可以使用`Unsafe`注册类

```java
// 使用Unsafe 向JVM中注册
Class helloWorldClass = unsafe1.defineClass("类名","类字节码",0,"字节码长度");
```

`Unsafe`还可以通过`defineAnonymousClass`方法创建内部类

**注意:**

`java 8`以前可以使用上述方法，`java 8`不能使用，`java 11`已经把`defineClass`移除了，但是保留了`defineAnonymousClass`

```java
public Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
    return theInternalUnsafe.defineAnonymousClass(hostClass, data, cpPatches);
}
// cpPatches 可以实现同一个class多次动态加载
```

## 参考链接

https://paper.seebug.org/513/

https://paper.seebug.org/449/

