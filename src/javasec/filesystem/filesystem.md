# Java文件系统

内置了两类文件系统,`java.io`和`java.nio`其中`java.nio`由`sun.nio`实现

![](https://i.loli.net/2021/04/30/lC9XndxFUQOsuI5.png)

`java.io.FileSystem`是抽象类，具体的实现是通过`JNI`调用`native`方法实现的

nio文件系统和io文件系统是完全独立的，合理利用nio文件系统可以绕过io的waf或者rasp.

## Java文件名空字节截断漏洞

由于java文件读写都是使用C实现的，C实现读写文件时有空字节截断漏洞，而不是java本身的漏洞。在其他许多语言中都存在空字节截断漏洞，但是在`java8`之后已经将空字节截断漏洞修复了。修复的方法如下

```java
/**
 * Check if the file has an invalid path. Currently, the inspection of
 * a file path is very limited, and it only covers Nul character check.
 * Returning true means the path is definitely invalid/garbage. But
 * returning false does not guarantee that the path is valid.
 *
 * @return true if the file path is invalid.
 */
 final boolean isInvalid() {
     if (status == null) {
         status = (this.path.indexOf('\u0000') < 0) ? PathStatus.CHECKED
                                                    : PathStatus.INVALID;
     }
     return status == PathStatus.INVALID;
 }
```

空字节截断漏洞可以用于文件上传，因为通常来说文件上传时会检查文件的名字，例如`endwith`,正则匹配等等，只有通过检查才会进行文件操作，通过`\u0000`截断可以创建任意文件。

