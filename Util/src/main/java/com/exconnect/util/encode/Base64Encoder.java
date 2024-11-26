package com.exconnect.util.encode;

import java.util.Base64;


/**
 * TODO : Check if encoder and decoder are thread safe
 * TODO : Add validations here for input and to check if the string was encoded by base64 while decoding
 */
public class Base64Encoder {

    // Creating encoder and decoder outside so they are statically created once and not in every invocation
    static Base64.Encoder encoder = Base64.getEncoder();
    static Base64.Decoder decoder = Base64.getDecoder();

    public static String encode(String strToEncode) {

        String encodedString = encoder.encodeToString(strToEncode.getBytes());
        return encodedString;

    }

    public static String decode(String strToDecode) {

        String decodedString = new String(decoder.decode(strToDecode.getBytes()));
        return decodedString;

    }
}
