package com.furp.webSocket;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    private Session session;
    private String userId;

    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        try {

            this.session = session;
            String queryString = session.getQueryString();
            String tokenValue = null;
            if (queryString != null){
                Map<String,String> paramMap = HttpUtil.decodeParamMap(
                        queryString,
                        CharsetUtil.CHARSET_UTF_8
                );
                tokenValue = paramMap.get("token");
            }

            Object loginId = StpUtil.getLoginIdByToken(tokenValue);

            if (loginId == null) {
                session.close();
                return;
            }

            this.userId =loginId.toString();

            sessionPool.put(this.userId, session);

            System.out.println("websocket"+this.userId+"已经连接");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.close();
            } catch (IOException ex) {
            }
        }

    }

    @OnMessage
    public void onMessage(String message, Session session){
        if(message.contains("ping")){
            try {
                session.getBasicRemote().sendText("{\"type\":\"pong\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose() {
        if (this.userId != null) {
            sessionPool.remove(this.userId);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

        error.printStackTrace();
    }

    private void closeSession(Session session) {
        try { session.close(); } catch (Exception e) {}
    }

    public static void sendInfo(String userId, String type, String data){
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()){
            try {
                JSONObject msg = new JSONObject();
                msg.set("type", type);
                msg.set("data", data);
                synchronized (session) {
                    session.getBasicRemote().sendText(msg.toString());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("用户不在线，跳过实时消息推送");
        }
    }

}