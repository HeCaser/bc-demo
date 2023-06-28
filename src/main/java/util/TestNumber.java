package util;

import netscape.javascript.JSObject;

import java.nio.charset.StandardCharsets;

public class TestNumber {

    public static void main(String[] args) {
        try {
            String dataJson = "{\n" +
                    "    \"access_token\": \"220010000057752c67c313b05cf7d71204fa4444c8ca54d0b\",\n" +
                    "    \"refresh_token\": \"220010000c42ca6fb5dc264c86d808a5082624b7823bdac3d\",\n" +
                    "    \"expiration\": 1703472656542,\n" +
                    "    \"created_at\": 1687920656543,\n" +
                    "    \"uid\": 198366486,\n" +
                    "    \"scope\": \"all\",\n" +
                    "    \"if_first\": false\n" +
                    "}";
            byte[] data = dataJson.getBytes(StandardCharsets.UTF_8);
            System.out.println("加密前字符串 = "+new String(data));
            byte[] key = new byte[16];

            byte[] cipherByte = SM4Util.internalDo(key,data,true);
            String cipherText = HexUtil.encodeHex(cipherByte);
            System.out.println("加密后 =  "+cipherText);

            byte[] plainByte=  SM4Util.internalDo(key,cipherByte,false);
            System.out.println("解密后 = "+new String(plainByte));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
