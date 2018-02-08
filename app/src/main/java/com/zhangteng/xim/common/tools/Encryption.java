package com.zhangteng.xim.common.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Lanxumit on 2017/12/6.
 */

public class Encryption {

    private static String AES_KEY = "7mm:rYRnadsr1r1o";
    private static String AES_IV = "7mm:rYRnadsr1r1o";

    //加密
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(AES_IV.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64Utils.encodeStr(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //解密
    public static String desEncrypt(String data) {
        try {
            byte[] encrypted1 = Base64Utils.decodeStr(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
