package com.leadroyal.isee.healthhelper.util;


import com.leadroyal.isee.healthhelper.proto.HealthData;

import org.tautua.markdownpapers.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by LeadroyaL on 2016/11/21.
 */

public class ParseUtils {


    public static String read(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;
        if (inputStream.available() > 0) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            return new String(buffer);
        }
        return null;
    }


    public static String b2S(byte[] input) {
        if (input == null)
            return "this obj is null";
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] tp = new char[input.length * 2];
        for (int i = 0; i < input.length; i++) {
            int v = input[i] & 0xFF;
            tp[i * 2] = hexArray[v >>> 4];
            tp[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(tp);
    }

}
