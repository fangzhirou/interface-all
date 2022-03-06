package com.confirm.Service;

import com.confirm.Config.FtpInformation;
import com.confirm.Util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public  class FtpService implements IFtpService {

    //@Autowired
    // ISysConfigService sysConfigService;
    /**
     * 获取ftpClient
     *
     * @return 返回ftpClient
     */
    Integer code;

    public FTPClient getFtpClient() {
        //获取ftp对象、将ftp连接信息放入
        code = 100;
        FtpInformation ftp =new FtpInformation();
        String ftpPost = ftp.getHost(code);
        Integer ftpPort = ftp.getPort(code);
        String ftpUsername = ftp.getFtpUsername(code);
        String ftpPassword = ftp.getFtpPassword(code);
       FTPClient ftpClient = FTPUtil.getFTPClient(ftpPost, ftpPort, ftpUsername, ftpPassword);



      // FTPClient ftpClient=FTPUtil.getFTPClient("192.168.3.130",21,"ftp","ftp");
        return ftpClient;
    }
    @Override
    public Result UploadFtpFile(String ftpPath, MultipartFile file, boolean renameFlag) {
        //上传文件上ftp
        return FTPUtil.uploadFile(getFtpClient(), ftpPath, file, renameFlag);//
    }

    @Override
    public Result uploadFile(String ftpPaths, MultipartFile[] files, boolean renameFlag) {
        return FTPUtil.uploadFile(getFtpClient(), ftpPaths, files, renameFlag);
    }




   /*@Override
    public Result UploadBase64File(String ftpPath, String imageBase) {
        //上传文件上ftp
       if (imageBase.equals(""))
            return Result.fail("baseFile不能为空");
        String[] baseFileSplit = imageBase.split(";");
        if (!imageBase.startsWith("data:")) {
            return Result.fail("baseFile字段要为base64");
        }
        String baseFileHead = baseFileSplit[0];
        //解析文件的后缀名称
        String[] baseFileHeadSplit = baseFileHead.split("/");
        if(baseFileHeadSplit.length < 2)
            return Result.fail("baseFile字段要为base64");
        String fileSuffix = baseFileHeadSplit[1];
        String fileName = UUID.randomUUID() + "." + fileSuffix;
       return(FTPUtil.uploadFile(getFtpClient(), ftpPath, imageBase, true, fileName));
        //return null;
    }*/




}
