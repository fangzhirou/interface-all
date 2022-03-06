package com.confirm.Util;
import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class Base64Util {
    /**
     * 采用BASE64算法对字节数组进行加密
     * @param baseBuff 原字节数组
     * @return 加密后的字符串
     */
    public static final String encode(byte[] baseBuff){
        return new BASE64Encoder().encode(baseBuff);
    }

    /**
     * 字符串解密，采用BASE64的算法
     * @param encoder 需要解密的字符串
     * @return 解密后的字符串
     */
    public static final String decode(String encoder){
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(encoder);
            return new String(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
