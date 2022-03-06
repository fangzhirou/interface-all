package com.confirm.controller;
import com.confirm.Service.service;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.lang.math.NumberUtils;
import com.confirm.Server.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;

@RestController
public class VersionController {
    Listening listening=new Listening();
    Integer type=null;
    String version = listening.newversion();
    String DownloadUrl=listening.newurl();
    @RequestMapping("/version")
    public void getversion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        String s = "";
        while ((s = reader.readLine()) != null) {
            buffer.append(s);
        }
        String getversion="";
        JSONObject jsonObject = new JSONObject(buffer.toString());
        getversion=jsonObject.getString("getversion");
        service service = new service();
        //服务器版本号和当前版本号比较，为true的时候，说明服务器版本大于当前版本，应该更新
        if(compareVersions(version,getversion)){
            type=0;

            response.setCharacterEncoding("UTF-8");
            response.setContentType("versioncode");
            PrintWriter out = response.getWriter();
           out.print(service.toJson(type,version,DownloadUrl));
            out.flush();
            out.close();

        }
        else{
            type=1;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("versioncode");
            PrintWriter out = response.getWriter();
            out.print(service.toJson(type));
            out.flush();
            out.close();
        }
    }
    public static boolean compareVersions(String v1, String v2) {
        //判断是否为空数据
        if (v1.equals("") || v2.equals("")) {
            return false;
        }
        String[] str1 = v1.split("\\.");
        String[] str2 = v2.split("\\.");

        if (str1.length == str2.length) {
            for (int i = 0; i < str1.length; i++) {
                if (Integer.parseInt(str1[i]) > Integer.parseInt(str2[i])) {
                    return true;
                } else if (Integer.parseInt(str1[i]) < Integer.parseInt(str2[i])) {
                    return false;
                } else if (Integer.parseInt(str1[i]) == Integer.parseInt(str2[i])) {

                }
            }
        } else {
            if (str1.length > str2.length) {
                for (int i = 0; i < str2.length; i++) {
                    if (Integer.parseInt(str1[i]) > Integer.parseInt(str2[i])) {
                        return true;
                    } else if (Integer.parseInt(str1[i]) < Integer.parseInt(str2[i])) {
                        return false;

                    } else if (Integer.parseInt(str1[i]) == Integer.parseInt(str2[i])) {
                        if (str2.length == 1) {
                            continue;
                        }
                        if (i == str2.length - 1) {

                            for (int j = i; j < str1.length; j++) {
                                if (Integer.parseInt(str1[j]) != 0) {
                                    return true;
                                }
                                if (j == str1.length - 1) {
                                    return false;
                                }

                            }
                            return true;
                        }
                    }
                }
            } else {
                for (int i = 0; i < str1.length; i++) {
                    if (Integer.parseInt(str1[i]) > Integer.parseInt(str2[i])) {
                        return true;
                    } else if (Integer.parseInt(str1[i]) < Integer.parseInt(str2[i])) {
                        return false;

                    } else if (Integer.parseInt(str1[i]) == Integer.parseInt(str2[i])) {
                        if (str1.length == 1) {
                            continue;
                        }
                        if (i == str1.length - 1) {
                            return false;

                        }
                    }

                }
            }
        }
        return false;
    }



}
