package com.confirm.Server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServlet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import java.util.Iterator;
import java.util.Map;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
@ServerEndpoint("/ws/{userId}")
@Component
public class WebSocketServer {

    static Log log=LogFactory.get(WebSocketServer.class);

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
  private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
   private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
  private String userId="";


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
   public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;

        //判断是否存在
        if(webSocketMap.containsKey(userId)){
            //先删除（原用户）
            webSocketMap.remove(userId);
            //重新加入（刷新用户） 加入set
            webSocketMap.put(userId,this);

        }else{
            //直接加入（新用户）
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
        webSocketMap.put(userId, this);
        log.info("webSocketMap -> " + JSON.toJSONString(webSocketMap));

        addOnlineCount(); // 在线数 +1
        log.info("有新窗口开始监听:" + userId + ",当前在线人数为" + getOnlineCount());

        try {
            sendMessage(JSON.toJSONString("连接成功"));
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    /**
     * 连接关闭调用的方法
     */
   @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(this.userId)){
            webSocketMap.remove(this.userId);
            //人数减少一个
            subOnlineCount();
            log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
   @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口:"+userId+"的信息:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止窜改)
                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
       /* log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }*/
        Iterator entrys = webSocketMap.entrySet().iterator();
        while (entrys.hasNext()) {
            Map.Entry entry = (Map.Entry) entrys.next();

            if (userId == null) {
                webSocketMap.get(entry.getKey()).sendMessage(message);
                log.info("发送消息到：" + entry.getKey() + "，消息：" + message);
            } else if (entry.getKey().equals(userId)) {
                webSocketMap.get(entry.getKey()).sendMessage(message);
                log.info("发送消息到：" + entry.getKey() + "，消息：" + message);
            }

    }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
