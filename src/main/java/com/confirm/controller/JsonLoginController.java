package com.confirm.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONException;
import org.json.JSONObject;
import com.confirm.Service.service;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import com.confirm.Service.Result;

@RestController
    public class JsonLoginController extends HttpServlet {
        private static final long serialVersionUID = 369840050351775312L;
        private int type;

        @Override
        @RequestMapping("/login")
        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            String username="";
            String password="";
            //String encode=null;

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

            } catch (JSONException e) {
                e.printStackTrace();
            }


          // 新建服务对象
            service service = new service();
            Result result=new Result();
            // 验证处理
            boolean logged = service.login(username,password);
            if (logged) {
                request.setAttribute("msg","登陆成功");
                //System.out.println("Login Successful !!!");
                type =200;
            } else {
                request.setAttribute("msg","登录失败，请重试！");
                //System.out.println("登录失败，请重试！");
                type= 400;
            }

            // 返回信息到客户端
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            //返回状态码
           out.print(service.toJson(type));
            out.flush();
            out.close();
        }
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request, response);
        }
}
