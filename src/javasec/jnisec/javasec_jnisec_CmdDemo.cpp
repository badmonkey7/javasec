#include <iostream>
#include <stdlib.h>
#include <string.h>
#include "javasec_jnisec_CmdDemo.h"
using namespace std;
JNIEXPORT jstring JNICALL Java_javasec_jnisec_CmdDemo_exec(JNIEnv *env, jclass jclass, jstring str){
    if(str!=NULL){
        jboolean jsCopy = JNI_TRUE;
        //convert java string to c char pointer
        const char *cmd = env->GetStringUTFChars(str,&jsCopy);
        FILE *fd = popen(cmd,"r");
        if(fd!=NULL){
            string result;
            char buffer[128];
            while(fgets(buffer,sizeof(buffer),fd)!=NULL){
                result += buffer;
            }
            pclose(fd);
            return env->NewStringUTF(result.c_str());
        }
    }
    return NULL;
}
// g++ -fPIC -I"/home/badmonkey/.jdks/corretto-11.0.9/include" -I"/home/badmonkey/.jdks/corretto-11.0.9/include/linux" -shared -o libcmd.so ./jnisec/javasec_jnisec_CmdDemo.cpp