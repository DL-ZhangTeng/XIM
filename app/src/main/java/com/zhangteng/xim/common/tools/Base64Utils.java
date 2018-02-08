package com.zhangteng.xim.common.tools;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by Lanxumit on 2017/12/28.
 */

public class Base64Utils {

    //加密
    public static String encodeStr(byte[] plainText) {
        Base64 base64 = new Base64();
        plainText = base64.encode(plainText);
        return new String(plainText);
    }

    //解密
    public static byte[] decodeStr(String encodeStr) {
        byte[] b = encodeStr.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        return b;
    }
}
