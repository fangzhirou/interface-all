package com.confirm.Server;
import com.confirm.controller.Listening;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component  // 被Spring容器管理
@Order(1)   // 如果多个自定义ApplicationRunner，用来表明执行顺序

public class PushAlarm implements ApplicationRunner{
    //counfirst一开始连接就测出总条数
   public static  Integer countfirst=null;
   //实时的数据条数记录
    public static Integer countnow=null;
    Listening listening=new Listening();
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("------------->" + "项目启动，now =" + new Date());
        countfirst=listening.count();
        this.myTimer();
    }

    public void myTimer() {

        String userId = null; // userId 为空时，会推送给连接此 WebSocket 的所有人

        Runnable runnable1 = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    //实时赋值，调用方法查询数据库内条数
                    countnow=listening.count();
                   // System.out.println("runnable:"+countnow);
                    String message=null;
                    //条数不相同的时候就会取最新的一条记录发送
                    if(!(countnow==countfirst))
                    {
                        message=listening.newmessage();
                        WebSocketServer.sendInfo(message, userId);
                    }
                    //WebSocketServer.sendInfo(message, userId); // 推送
                    Thread.sleep(5000);
                }
            }

        };
        Thread thread1 = new Thread(runnable1);
        thread1.start();
    }
}
