# java反序列化漏洞

java序列化是一种将java对象转成字节流的机制，反序列化则是根据序列化的字节流新建一个对象。本文讲解java反序列漏洞(入门级)

## java序列化

java中的对象保存在内容中，不再使用时会被gc(garbage collector)回收。通常来说我们会将内存中的对象反序列化为字节流，用于再网络上传输。具体实现时，只需要再相应的类上实现`Serializable`接口即可。

**工作原理**

java在序列化的时候通过反射获取对象的所属类的属性信息，这些属性的数据会被序列化，如果属性是一个对象，那么对其进行递归的序列化。

> 序列化的时候只需要保存属性的数据，方法什么的不需要(

## Java反序列化

反序列化是序列化的逆过程，通过序列化的字节流重新创建一个属性值相同对象，不过需要注意的是反序列化的jvm中加载对应的类

**工作原理**

首先创建一个`object`对象，然后根据反射获取需要的属性，并将字节流中的值填入到属性中去。

## Java反序列化漏洞

java反序列化漏洞是指恶意用户通过修改对象的序列化字节流，使得java程序在反序列化时得到的对象的属性得到污染，进而对系统造成危害，例如在反序列化对象时造成任意代码执行。

### 简单的反序列化漏洞

由于java反序列化不会调用类的构造方法创建新的对象，那么在构造方法中对类的一系列检查可以很容易可以bypass，比如检查对象的创建时间必须早于1999年xxx，反序列化时可以伪造一个2021年创建的对象从而bypass对于时间的检查。

```java
public class DeserializeDemo implements Serializable {
    private String info;
    private int createTime;
    DeserializeDemo(String info){
        this.info = info;
        this.createTime = 2021;
    }
    public void check(){
        if(createTime>2099){
            System.out.println(createTime+" check passed!");
        }else{
            System.out.println(createTime+" check failed!");
        }
    }
}
```

首先先序列化

```java
public class DeserializeTest {
    public static void main(String[] args) {
        DeserializeDemo demo = new DeserializeDemo("badmonkey");
        demo.check();
        try(
                FileOutputStream fos = new FileOutputStream("src/javasec/deserialize/demo.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(demo);
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

        try(
                FileInputStream fis = new FileInputStream("src/javasec/deserialize/fake.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ){
           DeserializeDemo demoTest = (DeserializeDemo) ois.readObject();
           demoTest.check();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
```

查看序列化的字节值，如下图所示

![hex](https://i.loli.net/2021/05/17/YUjbw72pJgDSPir.png)

尝试修改文件的内容(2021十六进制对应0x7e5 2999对应0xbb7)0x7e5改为0xbb7,然后反序列化，再次进行check

![test-result](https://i.loli.net/2021/05/17/njhxLT5H4dqEAFz.png)

上述的例子比较简单，只是更改了数据，没有太大的危害(

### 常规的反序列漏洞

如果一系列的对象被反序列化，有可能达到任意代码执行的效果，首先需要先了解一下gadgets和chains的概念。

#### Gadgets&&Chains

类比pwn中rop链构造时使用的代码片段，这里的gadgets代表了可以执行代码的函数或者类(本质也是一种代码碎片)，这些gadgets可以被恶意用户重用。在反序列化的时候一些魔术方法会自动调用，如`readObject()`方法。如果`readObject`方法被重载，那么可能会形成链(

```java
public class Gadget implements Serializable {
    private Runnable command;

    public Gadget(Command command) {
        this.command = command;
    }
    private final void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        command.run();// 会自动执行
    }
}
```

上述的类的`readObject`方法被重载，且执行了一个Runnable成员，如果Runnable成员是下面这个类的实例，那么很容易控制command达到任意代码执行的效果。

```java
public class Command implements Runnable, Serializable {

   private String command;

   public Command(String command) {
       this.command = command;
   }

    @Override
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            String result = scanner.hasNext()?scanner.next():"";
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

因为command成员是一个对象，所以会递归反序列化，构造payload

```java
public class GadgetChainDemo {
    public static void main(String[] args) {
        Command command = new Command("whoami"); // 伪造Runnable，服务端程序中的Runnable并不是我们自己写的这个可以执行任意代码的Command，但是都是Runnable类(
        Gadget gadget = new Gadget(command);
        // 序列化
        try(
                FileOutputStream fos = new FileOutputStream("src/javasec/deserialize/gadget.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(gadget);
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        // 利用的过程
        try(
                FileInputStream fis = new FileInputStream("src/javasec/deserialize/gadget.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ){
            Gadget fake = (Gadget) ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
```

整个利用流程的调用链如下

```text
Gadget -> readObject() -> command.run() -> Runtime.getRuntime().exec()
```

可以看到反序列化漏洞需要精心构造和寻找调用链，虽然比较耗费精力，但是一旦找到危害极大(

## 参考链接

https://snyk.io/blog/serialization-and-deserialization-in-java/

