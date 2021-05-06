# Java文件系统

内置了两类文件系统,`java.io`和`java.nio`其中`java.nio`由`sun.nio`实现

![](https://i.loli.net/2021/04/30/lC9XndxFUQOsuI5.png)

`java.io.FileSystem`是抽象类，具体的实现是通过`JNI`调用`native`方法实现的

nio文件系统和io文件系统是完全独立的，合理利用nio文件系统可以绕过io的waf或者rasp.

