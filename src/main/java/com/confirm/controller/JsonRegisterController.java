package com.confirm.controller;
import com.confirm.Service.service;
import org.apache.commons.lang.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
@RestController
//@RequestMapping("/register")
public class JsonRegisterController {

    private static final long serialVersionUID = 369840050351775312L;
    private int type;

    @RequestMapping("/register")
           public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            String username = "", password = "",ID="",place="";


        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }

            System.out.println("getJsonString" + sb.toString());
            JSONObject jsonObject = new JSONObject(sb.toString());
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
            ID=jsonObject.getString("ID");
            place = jsonObject.getString("place");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 新建服务对象
        service serv = new service();

        //判断username是否存在
        if (!serv.execute(ID)) {
            // 验证处理
            boolean loged = serv.register(username, password,ID,place);
                if (loged) {
                    System.out.print("Register Successful !!!");
                    type = 200;
            } else {
                System.out.println("插入失败");
                type = 400;
            }
        } else {
            System.out.println("用户已存在，Failed");
            type = 0;
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //返回状态码
        out.print(serv.toJson(type));
        out.flush();
        out.close();

    }

}