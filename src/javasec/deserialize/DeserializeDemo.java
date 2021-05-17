package javasec.deserialize;

import java.io.Serializable;

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
