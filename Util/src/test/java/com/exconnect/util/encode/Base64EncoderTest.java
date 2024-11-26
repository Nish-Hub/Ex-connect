package com.exconnect.util.encode;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64EncoderTest {

    public void testEncode() {
        String str = "Hello , Exconnect";
        String encodedString = Base64Encoder.encode(str);
        assertEquals(encodedString, Base64.getEncoder().encodeToString(str.getBytes()));
    }

    public void testDecode() {
        String str = "SGVsbG8sIEphdmEgMjEh";
        String decodedString = Base64Encoder.decode(str);
        assertEquals(decodedString, Base64.getDecoder().decode(str.getBytes()));
    }
}