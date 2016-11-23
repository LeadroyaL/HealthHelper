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
            return String.valueOf(buffer);
        }
        return null;
    }

    public static String parseBodies(HealthData.Body[] bodies) {
        // TODO parse
        return null;
    }

//    public static String parse
}
