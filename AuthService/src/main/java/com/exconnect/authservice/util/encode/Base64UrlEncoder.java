package com.exconnect.authservice.util.encode;

import java.util.Base64;


/**
 * TODO : Check if encoder and decoder are thread safe
 * TODO : Add validations here for input and to check if the string was encoded by base64 while decoding
 */
public class Base64UrlEncoder {

    // Creating encoder and decoder outside so they are statically created once and not in every invocation
    static Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    static Base64.Decoder decoder = Base64.getUrlDecoder();

    public static String encode(String strToEncode) {

        return encode(strToEncode.getBytes());
    }

    public static String encode(byte[] byteArrToEncode) {

        String encodedString = encoder.encodeToString(byteArrToEncode);
        return encodedString;

    }

    public static String decode(String strToDecode) {

        String decodedString = new String(decoder.decode(strToDecode.getBytes()));
        return decodedString;

    }
}
