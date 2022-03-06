package com.confirm.controller;
import com.confirm.Service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.confirm.Util.UserAgentUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

@RestController
public class LogController {

    private int type;

    private static final long serialVersionUID = 369840050351775312L;

    @RequestMapping("/logInsert")

   public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
       StringBuffer buffer=new StringBuffer();
        BufferedReader reader=request.getReader();
        String s="";
        while ((s=reader.readLine())!=null){
            buffer.append(s);
        }
        String ip="",deviceType="", osVersion="", username="",deviceManufacturer="",Time="";
        JSONObject jsonObject=new JSONObject(buffer.toString());
        ip=jsonObject.getString("ip");
        deviceType=jsonObject.getString("deviceType");
        osVersion=jsonObject.getString("osVersion");
        username=jsonObject.getString("username");
        deviceManufacturer=jsonObject.getString("deviceManufacturer");
        Time=jsonObject.getString("Time");
        service serv = new service();
        if(!serv.execute(Time)) {
            boolean logging = serv.log(ip, deviceType, osVersion, username, deviceManufacturer, Time);
            if (logging) {
                System.out.print("log insert Successful !!!");
                type = 200;
            } else {
                System.out.println("插入失败");
                type = 400;
            }
        }else {
            System.out.println("重复");
            type = 0;
        }
        // 返回信息到客户端

        response.setCharacterEncoding("UTF-8");
        response.setContentType("test");
        PrintWriter out = response.getWriter();

        //返回状态码
        out.print(serv.toJson(type));
        out.flush();
        out.close();


    }

    }
