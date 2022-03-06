package com.confirm.Service;

import org.springframework.web.multipart.MultipartFile;
public interface IFtpService {
    /**
     *
     * @param ftpPath 文件上传位置 传空默认根目录 格式：/test/
     * @param file // formData 形式文件
     * @param renameFlag 是否重名 false 保持原有文件名 true 重命名为随机uuid
     * @return
     */
    Result UploadFtpFile(String ftpPath, MultipartFile file, boolean renameFlag);

    /**
     *
     * @param ftpPath     图片上传位置
     * @param imageBase   图片base64
     * @return
     */
    //Result UploadBase64File(String ftpPath, String imageBase);


    Result uploadFile(String ftpPaths, MultipartFile[] files, boolean renameFlag);
}
