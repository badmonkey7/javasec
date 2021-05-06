# Java本地命令执行

有四种命令执行

- `Runtime.getRuntime().exec("命令")`
- `ProcessBuilder("命令").start()`
- `ProcessImpl反射start方法`
- 调用`JNI`

关系：

exec->ProcessBuilder.start->ProcessImpl->forkAndExec

除了`JNI`之外都比较简单，`JNI`方式的系统调用等后续补充。