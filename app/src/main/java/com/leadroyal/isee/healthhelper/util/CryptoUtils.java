package com.leadroyal.isee.healthhelper.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Extension;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LeadroyaL on 2016/11/21.
 */

public class CryptoUtils {
    public static final String TAG = CryptoUtils.class.getSimpleName();
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static IvParameterSpec iv;
    private static Key AESKey;

    static {
        try {
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            iv = new IvParameterSpec("IV_for_LeadroyaL".getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }


    public static void setAESKey(byte[] bytes) {
        AESKey = new SecretKeySpec(bytes, "AES");
        try {
            encryptCipher.init(Cipher.ENCRYPT_MODE, AESKey, iv);
            decryptCipher.init(Cipher.DECRYPT_MODE, AESKey, iv);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public static byte[] AESEnc(String plain) {
        return AESEnc(plain.getBytes());
    }

    public static byte[] AESEnc(byte[] plain) {
        try {
            return encryptCipher.doFinal(plain);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] AESDec(String plain) {
        return AESDec(plain.getBytes());
    }

    public static byte[] AESDec(byte[] plain) {
        try {
            return decryptCipher.doFinal(plain);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
