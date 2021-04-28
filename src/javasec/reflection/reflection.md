# Java反射机制

~~很重要的特性，java语言动态性的重要体现，各种java底层框架的底层灵魂~~，java还不是很懂，看别人这么说的。通过反射可以获得类的方法，成员等信息。

## 获取Class对象

反射操作的是`java.lang.Class`对象，几种常见的获取方式

1. `类名.class`
2. `Class.forName(类名)`
3. 类加载器`classLoader.loadClass(类名)`

对于数组类型的`Class`对象，需要使用Java类型描述符

```java
// Class<?> 表示不确定的类类型
Class<?> doubleArray = Class.forName("[D");// 等价于 double[].class
Class<?> cStringArray = Class.forName("[[Ljava.lang.String;"); //等价于 String[][].class
// 至于为什么 double[].class 要用 [D ,String[][].class 要用 [[Ljava.lang.String;
//其实可以 System.out.println(String[][].class) 一下，就知道了
```

反射调用内部类的使用需要用`$`代替`.`,比如`javasec.Cmd`有一个`Hello`的内部类，那么调用时，类名应该写为`javasec.Cmd$Hello`

## 反射Runtime

Runtime可以执行系统命令，所以用Runtime当例子。

正常使用`Runtime.getRuntime().exec("ls")`

反射Runtime，知识盒子中的反射方式比较麻烦，但是也有学习的必要，先看一下知识盒子的反射方式

```java
Class runTimeClass = Class.forName("java.lang.Runtime");// 获取类对象
Constructor constructor = runTimeClass.getDeclaredConstructor();// 获取构造方法，Runtime是单例类，构造方法是私有的
constructor.setAccessible(true);// 将方法变为public
Object runTime = constructor.newInstance();//实例化一个Runtime对象
Method exec = (Method) runTime.getMethod("exec",String.class);// 获取exec方法，参数类型为String
Process process = (Process) exec.invoke(runTime,"ls");// 使用invoke 相当于 process = runTime.exec("ls")
```

简单的反射

```java
Class runTimeClass = Class.forName("java.lang.Runtime");
Method exec = (Method) runTimeClass.getMethod("exec",String.class);
Process process = (Process) exec.invoke(null,"ls");// exec 是静态方法不需要实例
```

## 反射调用类方法

获取指定成员方法：

```java
runTimeClass.getMethod("函数名","参数类型","参数类型",..);// 参数类型 必须为类对象 如 String.class
runTimeClass.getDeclaredMethod("函数名","参数类型","参数类型",..);// 参数类型 必须为类对象 如 String.class
// getMethods(),getDeclaredMethods() 获取所有的方法
```

`getDeclaredMethod`可以获取到**当前类所有**的成员方法，而`getMethod`能获取到**当前类和父类公有**的成员方法

获取到方法对象后，需要使用`invoke`调用方法

```java
method.invoke("实例化对象","方法参数值","方法参数值");// 对于类方法 对象填null即可
```

## 反射调用成员变量

获取成员变量,整体上同获取成员方法

```java
runTimeClass.getField("变量名");
runTimeClass.getDeclaredField("变量名");
runTimeClass.getFields();
runTimeClass.getDeclaredFields();
```

获取变量值

`Object field = field.get("实例化对象")`

修改变量值

`field.set("实例化对象","修改后的值")`

修改变量权限

`field.setAccessiable("布尔值")`

