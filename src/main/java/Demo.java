import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import util.HexUtil;
import util.SM2Util;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Demo {

    public static void main(String[] args) {
        keyPairDemo();
        AsymmetricCipherKeyPairDemo();
    }

    private static void keyPairDemo() {
        KeyPair kp = SM2Util.createKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        System.out.println("公钥格式 " + publicKey.getFormat());
        System.out.println("公钥 Hex " + HexUtil.encodeHex(publicKey.getEncoded()));
        System.out.println("私钥格式 " + privateKey.getFormat());
        System.out.println("私钥 Hex " + HexUtil.encodeHex(privateKey.getEncoded()));
    }

    private static void AsymmetricCipherKeyPairDemo() {
        AsymmetricCipherKeyPair keyPair = SM2Util.createAsymmetricCipherKeyPair();
        // 获取公钥和私钥
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();


        System.out.println("hepan 公钥 Hex 压缩 " + HexUtil.encodeHex(publicKey.getQ().getEncoded(true)));
        System.out.println("hepan 公钥 Hex 非压缩 " + HexUtil.encodeHex(publicKey.getQ().getEncoded(false)));
        System.out.println("hepan 私钥 bigint " + privateKey.getD());
        System.out.println("hepan 私钥 16 进制 " + privateKey.getD().toString(16));
    }
}
