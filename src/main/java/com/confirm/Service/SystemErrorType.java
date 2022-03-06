package com.confirm.Service;

import lombok.Getter;

/**
 * 系统级别3个0
 */
@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),
    UNKNOWN_ERROR("000400", "未知错误"),

    GATEWAY_NOT_FOUND_SERVICE("100404", "服务未找到"),
    GATEWAY_ERROR("100500", "网关异常"),
    GATEWAY_CONNECT_TIME_OUT("100002", "网关超时"),

    INVALID_TOKEN("200001", "无效token"),
    NO_PERMISSION("200200", "没有权限"),
    ACCESS_TOKEN_EXPIRE("200300", "accessToken过期"),
    REFRESH_TOKEN_EXPIRE("200400", "refreshToken过期，请重新登录"),
    NO_REFRESH_TOKEN("2004002","非法refreshToken,请重新登录"),
    NO_ADMIN_ROLE("200500", "没有管理员角色"),
    SERVICE_NOT_FOUND("200100", "方法名不存在"),
    ARGUMENT_NOT_VALID("200700", "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT("200900", "上传文件大小超过限制"),

    DUPLICATE_PRIMARY_KEY("300000", "唯一键冲突"),
    ADD_ERROR("300001", "添加失败"),
    DEL_ERROR("300002", "删除失败"),
    UPDATE_ERROR("300003", "更新失败"),
    PROCEDURE_ROLLBACK("300900", "事务回滚"),
    EXEC_ERROR("400001","任务执行失败"),
    STOP_ERROR("400002","任务不存在"),
    START_ERROR("400003","任务启动失败"),
    FTP_ULOAD_ERROR("500001","FTP上传失败"),
    FTP_DOWNLOAD_ERROR("500002","FTP下载失败"),
    FTP_FILE_NOT_FOUND("500003","FTP下载文件未找到"),
    FTP_RUNNING_ERROR("500004","FTP运行时异常，请查看日志"),
    GET_CPU_ERROR("511","获取系统CPU信息失败"),
    MACHINE_NOT_AUTHORIZED("511","您的机器没有授权,如需使用请授权！"),
    MACHINE_AUTHORIZATION_EXPIRED("511","您的机器授权码过期了！");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String mesg;

    SystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }
}