package com.confirm.Util;

import static com.confirm.Service.SystemErrorType.*;


import com.confirm.Service.Result;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.SocketException;
import java.util.*;

//import static com.springboot.cloud.common.core.exception.SystemErrorType.*;

@Slf4j
public class FTPUtil {
    //public static Integer type=null;
    //String code=null;


    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     服务器IP
     * @param ftpPort     服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return FTPClient
     */
    public static FTPClient getFTPClient(String ftpHost, int ftpPort,
                                         String ftpUserName, String ftpPassword) {

        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            // 连接FPT服务器,设置IP及端口
            ftp.connect(ftpHost, ftpPort);
            // 设置用户名和密码
            ftp.login(ftpUserName, ftpPassword);
            // 设置连接超时时间,5000毫秒
            ftp.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            ftp.setControlEncoding("UTF-8");
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                log.info("未连接到FTP，用户名或密码错误");
                ftp.disconnect();
            } else {
                log.info("FTP连接成功");
            }

        } catch (SocketException e) {
            e.printStackTrace();
            log.info("FTP的IP地址可能错误，请正确配置");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("FTP的端口错误,请正确配置");
        }
        return ftp;
    }

    /**
     * 关闭FTP方法
     *
     * @param ftp
     * @return
     */
    public static boolean closeFTP(FTPClient ftp) {

        try {
            ftp.logout();
        } catch (Exception e) {
            log.error("FTP关闭失败");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("FTP关闭失败");
                }
            }
        }

        return false;

    }




    /**
     * FTP文件上传工具类
     *
     * @param ftp
     * @param ftpPaths
     * @return
     */
    public static Result uploadFile(FTPClient ftp, String ftpPaths, MultipartFile file, boolean renameFlag) {
        boolean flag = false;
        InputStream in = null;
        String fileName = null;
        String ftpPath = "";
        String returnFileName = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);

            String [] paths = ftpPaths.split("/");
            ftpPath = getString(ftp, ftpPath, paths);
            //判断FPT目标文件夹时候存在不存在则创建

            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            //上传文件
            //File file = new File(filePath);
            if (renameFlag) {
                fileName = new String(file.getOriginalFilename().getBytes("UTF-8"),"ISO-8859-1");
                StringBuffer filenameBuffer = new StringBuffer(UUID.randomUUID().toString());
                fileName = filenameBuffer.append(fileName.substring(fileName.lastIndexOf("."))).toString();
                returnFileName = fileName;

            } else {
                fileName = new String(file.getOriginalFilename().getBytes("UTF-8"),"ISO-8859-1");
                returnFileName = file.getOriginalFilename();
            }
            in = file.getInputStream();
            String tempName = ftpPath + fileName;
            flag = ftp.storeFile(tempName, in);
            returnFileName = ftpPath + returnFileName;
            //return("上传成功");
            return getResult(flag, returnFileName);
        } catch (Exception e) {
            e.printStackTrace();

            log.error("上传失败,报错信息" + e.toString());
            return Result.fail(FTP_RUNNING_ERROR);
            //return("上传失败,报错信息:" + e.toString());
            //return Result.fail(FTP_RUNNING_ERROR);
        } finally {
            try {
                in.close();
                closeFTP(ftp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param ftp          ftp客户端
     * @param ftpPaths     ftp图片保存路径
     * @param fileBytes    fileBytes图片字节数组
     * @param fileName     图片名称
     * @return             保存结果
     */
    public static boolean uploadFileBytes(FTPClient ftp, String ftpPaths, byte[] fileBytes, String  fileName) {
        boolean result = false;
        String ftpPath = "";
        InputStream in = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            String [] paths = ftpPaths.split("/");
            ftpPath = getString(ftp, ftpPath, paths);
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            in = new ByteArrayInputStream(fileBytes);
            String tempName = ftpPath + fileName;
            result  = ftp.storeFile(tempName, in);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败,报错信息" + e.toString());
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String getString(FTPClient ftp, String ftpPath, String[] paths) throws IOException {
        if(paths.length >1){
            for(int i =0; i<paths.length; i++){
                ftpPath = ftpPath + paths[i]+"/";
                if (!ftp.changeWorkingDirectory(ftpPath)) {
                    ftp.makeDirectory(ftpPath);
                }
            }
        }
        return ftpPath;
    }

    /**
     * FTP文件上传工具类
     *
     * @param ftp
     * @param ftpPaths
     * @return
     */
   public static  Result uploadFile(FTPClient ftp, String ftpPaths,  MultipartFile[] files, boolean renameFlag) {

        boolean flag = false;
        InputStream in = null;
        String fileName = null;
        String ftpPath = "";
        String returnFileName = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            String [] paths = ftpPaths.split("/");
            ftpPath = getString(ftp, ftpPath, paths);
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            //上传文件
            Map<String,Object> fileMap= new HashMap<String,Object>();
            List<String> list=new ArrayList<>();
            for (int i=0; i<files.length; i++
                    ) {
                if (renameFlag) {
                    fileName = new String(files[i].getOriginalFilename().getBytes("UTF-8"),"ISO-8859-1");
                    StringBuffer filenameBuffer = new StringBuffer(UUID.randomUUID().toString());
                    fileName = filenameBuffer.append(fileName.substring(fileName.lastIndexOf("."))).toString();
                    returnFileName = fileName;
                } else {
                    fileName = new String(files[i].getOriginalFilename().getBytes("UTF-8"),"ISO-8859-1");
                    returnFileName = files[i].getOriginalFilename();
                    //list.add(returnFileName);
                }
                in = files[i].getInputStream();
                String tempName = ftpPath + fileName;
                returnFileName = ftpPath +returnFileName;
                fileMap.put("file"+i,returnFileName);
                flag = ftp.storeFile(tempName, in);
                list.add(returnFileName);
            }
           // String s= JSONObject.toJSONString(list);
           /* if ((!("").equals(s))) {
                // 获取Sql插入语句
                String sql="insert into filepath(Filepath)values('"+s+"')";
                // 获取DB对象
                dbconnector db = dbconnector.createInstance();
                db.connectDB();
                int ret = db.executeUpdate(sql);
                if (ret != 0) {
                    //type=200;
                   //code= JSONObject.toJSON(type.toString());
                    log.info("路径插入成功");
                    db.closeDB();
                    //return(code);*/

               // } else {
                    //type=400;
                   // String code= "type="+JSONObject.toJSONString(type.toString());
                    //log.info("路径插入失败");
                  //  db.closeDB();
                   // return(code);
               // }
            //}
           // String code=JSONObject.toJSONString(type.toString());
          //  System.out.println(s);
           // System.out.println(list);
            return getResult(flag, fileMap);

            //return type;
        } catch (Exception e) {
            e.printStackTrace();
          //  type=400;
           //String code= "type="+JSONObject.toJSONString(type.toString());
            log.error("上传失败,报错信息" + e.toString());
            //return type;
            return Result.fail(FTP_RUNNING_ERROR);
            //return ("上传失败,报错信息" + e.toString());
        } finally {
            try {
                in.close();
                closeFTP(ftp);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * FTP文件上传工具类
     *
     * @param ftp
     * @param ftpPath
     * @return
     */
    public static  Result uploadFile(FTPClient ftp, String ftpPath, String file, boolean renameFlag, String fileName) {
        boolean flag = false;
        Integer result = 0;
        InputStream in = null;
        String rename = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            //上传文件

            int index = file.indexOf("base64,") + 7;
            String newImage = file.substring(index, file.length());
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(newImage);
            if (renameFlag) {
                StringBuffer filenameBuffer = new StringBuffer(UUID.randomUUID().toString());
                fileName = filenameBuffer.append(fileName.substring(fileName.lastIndexOf("."))).toString();
            }
            in = new ByteArrayInputStream(b);
            String tempName = ftpPath + fileName;

            flag = ftp.storeFile(tempName, in);

            return getResult(flag, tempName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败,报错信息" + e.toString());
            return Result.fail(FTP_RUNNING_ERROR);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private static Result getResult(boolean flag, Object rename) {
        if (flag) {
            log.info("上传成功");
            return Result.success(rename);
        } else {
            log.error("上传失败");
            return Result.fail(FTP_ULOAD_ERROR);
        }
    }

    /*private static String getResult(boolean flag, Object rename) {
        if (flag) {
            log.info("上传成功");

            return ("上传成功");
        } else {
            log.error("上传失败");
            return ("上传失败");
        }
    }*/





    }