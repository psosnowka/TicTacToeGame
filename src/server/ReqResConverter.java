package server;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReqResConverter {
    public static byte[] StringToByte(String message) {
        return message.getBytes(StandardCharsets.UTF_8);
    }

    public static String ByteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
