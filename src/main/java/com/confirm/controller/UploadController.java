package com.confirm.controller;
import com.confirm.Service.Result;
import org.springframework.web.multipart.MultipartFile;
import com.confirm.Service.service;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.confirm.Service.FtpService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.List;
import  java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("/reservation")
public class UploadController {
    int type;
    List<String> imageList= Arrays.asList(".jpg",".jpeg",".png",".bmp",".JPEG");
    private  String imageRootPath="/reservationtest/";

    //private String picPath = "/reservationtest/";
  @Autowired
    private FtpService ftpService;
    /**
     *
     * @param files
     * @return
     */
   public Result saveImage(MultipartFile[] files) throws UnsupportedEncodingException {
       //获取文件后缀名
      String fileName=null;
       for (int i=0; i<files.length; i++
               ) {
           fileName = new String(files[i].getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1");
          // StringBuffer filenameBuffer = new StringBuffer(UUID.randomUUID().toString());
           //fileName = filenameBuffer.append(fileName.substring(fileName.lastIndexOf("."))).toString();
           String extensionFileName = fileName.substring(fileName.lastIndexOf("."));
           //判断是否为图片
           if (!imageList.contains(extensionFileName)){
               System.out.println("----"+extensionFileName);
               return Result.fail("只能上传图片格式文件");
           }
           System.out.println("----"+extensionFileName);
       }
           Result imageUploadResult = ftpService.uploadFile(imageRootPath, files, true);
           if (imageUploadResult.isFail()) {
               return Result.fail("图片保存失败");
           }

       return imageUploadResult;


   }


    /**
     * 上传照片
     * @param files
     * @return
     */
   @PostMapping("/UploadImage")
   public Result uploadImage(MultipartFile[] files) throws UnsupportedEncodingException {
       return saveImage(files);
   }




    @RequestMapping("/add")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = request.getReader();
        String s = "";
        while ((s = reader.readLine()) != null) {
            buffer.append(s);
        }
        String unit= "",time = "",person	 = "", 	place = "", note = "",imagePath="";
        JSONObject jsonObject = new JSONObject(buffer.toString());
        unit = jsonObject.getString("unit");
        time = jsonObject.getString("time");
        person= jsonObject.getString("person");
        place = jsonObject.getString("place");
        note = jsonObject.getString("note");
        imagePath = jsonObject.getString("imagePath");
        service serv = new service();

            boolean r= serv.reservation(unit,time,person,place,note,imagePath);
            if (r) {
                System.out.print("预约记录已保存");
                type = 200;
            } else {
                System.out.println("预约失败");
                type = 400;
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
