import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import util.ECDHUtil;
import util.HexUtil;
import util.SM2Util;
import util.SM4Util;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Demo {

    public static void main(String[] args) {
        keyPairDemo();
//        AsymmetricCipherKeyPairDemo();
//        keyPairChangeDemo();
//        AsymmetricCipherKeyPairChangeDemo();
//        KeyPairECDHDemo();
//        AsymmetricCipherKeyPairECDHDemo();
        sm4Demo();
    }


    private static void keyPairDemo() {
        KeyPair kp = SM2Util.createKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        System.out.println("公钥格式 " + publicKey.getFormat()); // X.509
        System.out.println("公钥 Hex " + HexUtil.encodeHex(publicKey.getEncoded()));
        System.out.println("私钥格式 " + privateKey.getFormat()); // PKCS#8
        System.out.println("私钥 Hex " + HexUtil.encodeHex(privateKey.getEncoded()));
    }

    private static void AsymmetricCipherKeyPairDemo() {
        AsymmetricCipherKeyPair keyPair = SM2Util.createAsymmetricCipherKeyPair();
        // 获取公钥和私钥
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();

        System.out.println("公钥 Hex 压缩 " + HexUtil.encodeHex(publicKey.getQ().getEncoded(true)));
        System.out.println("公钥 Hex 非压缩 " + HexUtil.encodeHex(publicKey.getQ().getEncoded(false)));
        System.out.println("私钥 bigint " + privateKey.getD());
        System.out.println("私钥 16 进制 " + privateKey.getD().toString(16));
    }

    /**
     * 测试 byte[] 转换为公私钥的正确性
     */
    private static void keyPairChangeDemo() {
        KeyPair kp = SM2Util.createKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        System.out.println("公钥 Hex " + HexUtil.encodeHex(publicKey.getEncoded()));
        System.out.println("私钥 Hex " + HexUtil.encodeHex(privateKey.getEncoded()));

        PublicKey publicKey2 = SM2Util.changeBytes2PublicKey(publicKey.getEncoded());
        System.out.println("公钥2 Hex " + HexUtil.encodeHex(publicKey2.getEncoded()));

        PrivateKey privateKey2 = SM2Util.changeBytes2PrivateKey(privateKey.getEncoded());
        System.out.println("私钥 Hex " + HexUtil.encodeHex(privateKey2.getEncoded()));

    }

    private static void AsymmetricCipherKeyPairChangeDemo() {
        AsymmetricCipherKeyPair keyPair = SM2Util.createAsymmetricCipherKeyPair();
        // 获取公钥和私钥
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();

        String publicHex = HexUtil.encodeHex(publicKey.getQ().getEncoded(false));

        ECPublicKeyParameters publicKey2 = SM2Util.getPublicKey(publicHex);
        System.out.println("公钥 Hex " + publicHex);
        System.out.println("公钥2 Hex " + HexUtil.encodeHex(publicKey2.getQ().getEncoded(false)));

        String privateHex = privateKey.getD().toString(16);
        ECPrivateKeyParameters privateKey2 = SM2Util.getPrivateKey(privateHex);
        System.out.println("私钥 Hex " + privateHex);
        System.out.println("私钥2 Hex " + privateKey2.getD().toString(16));
    }


    private static void KeyPairECDHDemo() {
        try {
            KeyPair localKeyPair = SM2Util.createKeyPair();
            // 模拟服务端秘钥
            KeyPair serverKeyPair = SM2Util.createKeyPair();
            // 本地私钥 + 服务端公钥
            byte[] keyByte = ECDHUtil.ECDH(SM2Util.changeBytes2PublicKey(serverKeyPair.getPublic().getEncoded()), localKeyPair.getPrivate());
            // 本地公钥 + 服务端私钥: 模拟服务端协商结果
            byte[] keyByte2 = ECDHUtil.ECDH(SM2Util.changeBytes2PublicKey(localKeyPair.getPublic().getEncoded()), serverKeyPair.getPrivate());

            System.out.println("ECHD = " + HexUtil.encodeHex(keyByte));
            System.out.println("ECHD2 = " + HexUtil.encodeHex(keyByte2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void AsymmetricCipherKeyPairECDHDemo() {
        try {
            AsymmetricCipherKeyPair localKeyPair = SM2Util.createAsymmetricCipherKeyPair();
            ECPublicKeyParameters publicKey = (ECPublicKeyParameters) localKeyPair.getPublic();
            ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) localKeyPair.getPrivate();

            AsymmetricCipherKeyPair serverKeyPair = SM2Util.createAsymmetricCipherKeyPair();
            ECPublicKeyParameters publicKey2 = (ECPublicKeyParameters) serverKeyPair.getPublic();
            ECPrivateKeyParameters privateKey2 = (ECPrivateKeyParameters) serverKeyPair.getPrivate();


            // 本地私钥 + 服务端公钥
            String key = ECDHUtil.asymmetricCipherKeyPairECDH(privateKey, HexUtil.encodeHex(publicKey2.getQ().getEncoded(false)));
            // 本地公钥 + 服务端私钥: 模拟服务端协商结果
            String key2 = ECDHUtil.asymmetricCipherKeyPairECDH(privateKey2, HexUtil.encodeHex(publicKey.getQ().getEncoded(false)));


            System.out.println("ECHD = " + key.length());
            System.out.println("ECHD = " + key);
            System.out.println("ECHD2 = " + key2.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sm4Demo() {
        try {
            String data = "{\n" +
                    "    \"access_token\": \"220010000057752c67c313b05cf7d71204fa4444c8ca54d0b\",\n" +
                    "    \"refresh_token\": \"220010000c42ca6fb5dc264c86d808a5082624b7823bdac3d\",\n" +
                    "    \"expiration\": 1.703472656542E12,\n" +
                    "    \"created_at\": 1.687920656543E12,\n" +
                    "    \"uid\": 198366486,\n" +
                    "    \"scope\": \"all\",\n" +
                    "    \"if_first\": false\n" +
                    "}";

            data ="[\n" +
                    "    {\n" +
                    "        \"access_token\": \"220010000057752c67c313b05cf7d71204fa4444c8ca54d0b\",\n" +
                    "        \"refresh_token\": \"220010000c42ca6fb5dc264c86d808a5082624b7823bdac3d\",\n" +
                    "        \"expiration\": 1.703472656542E12,\n" +
                    "        \"created_at\": 1.687920656543E12,\n" +
                    "        \"uid\": 198366486,\n" +
                    "        \"scope\": \"all\",\n" +
                    "        \"if_first\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"access_token\": \"220010000057752c67c313b05cf7d71204fa4444c8ca54d0b\",\n" +
                    "        \"refresh_token\": \"220010000c42ca6fb5dc264c86d808a5082624b7823bdac3d\",\n" +
                    "        \"expiration\": 1.703472656542E12,\n" +
                    "        \"created_at\": 1.687920656543E12,\n" +
                    "        \"uid\": 198366486,\n" +
                    "        \"scope\": \"all\",\n" +
                    "        \"if_first\": false\n" +
                    "    }\n" +
                    "]";

//            data = "\"adfa\"";

            byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
            byte[] key = new byte[16];

            byte[] cipherByte = SM4Util.internalDo(key, dataByte, true);
            byte[] plainByte = SM4Util.internalDo(key, cipherByte, false);
            System.out.println("解密后 = "+new String(plainByte));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
