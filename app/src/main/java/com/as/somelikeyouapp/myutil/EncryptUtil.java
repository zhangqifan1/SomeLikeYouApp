package com.as.somelikeyouapp.myutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具
 * @author Max
 * 2016年11月25日15:25:17
 */
public class EncryptUtil {

    private String key;
    private static EncryptUtil instance;

    private EncryptUtil(Context context){
        String serialNo = getDeviceSerialNumber(context);
        //加密随机字符串生成AES key
        key = SHA(serialNo + "#$ERDTS$D%F^Gojikbh").substring(0, 16);
    }

    /**
     * 单例模式
     * @param context context
     */
    public static EncryptUtil getInstance(Context context){
        if (instance == null){
            synchronized (EncryptUtil.class){
                if (instance == null){
                    instance = new EncryptUtil(context);
                }
            }
        }

        return instance;
    }

    /**
     * Gets the hardware serial number of this device.
     *
     * @return serial number or Settings.Secure.ANDROID_ID if not available.
     */
    @SuppressLint("HardwareIds")
    private String getDeviceSerialNumber(Context context) {
        // We're using the Reflection API because Build.SERIAL is only available
        // since API Level 9 (Gingerbread, Android 2.3).
        try {
            String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
            if (TextUtils.isEmpty(deviceSerial)) {
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                return deviceSerial;
            }
        } catch (Exception ignored) {
            // Fall back  to Android_ID
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }


    /**
     * SHA加密
     * @param strText 明文
     */
    private String SHA(final String strText){
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if (strText != null && strText.length() > 0){
            try{
                // SHA 加密开始
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                byte byteBuffer[] = messageDigest.digest();
                StringBuilder strHexString = new StringBuilder();
                for (byte aByteBuffer : byteBuffer) {
                    String hex = Integer.toHexString(0xff & aByteBuffer);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }


    /**
     * AES128加密
     * @param plainText 明文
     */
    String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES128解密
     * @param cipherText 密文
     * @return
     */
    String decrypt(String cipherText) {
        try {
            byte[] encrypted1 = Base64.decode(cipherText, Base64.NO_WRAP);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}