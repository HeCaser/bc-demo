package util;

import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;

import javax.crypto.KeyAgreement;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ECDHUtil {

    /**
     * ECDH 秘钥协商
     *
     * @param publicKey:  公钥, 一般来自其他平台(例如服务端)
     * @param privateKey: 私钥, 本地持有
     * @return 协商后的秘钥
     * @throws Exception
     */
    public static byte[] ECDH(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
        keyAgree.init(privateKey);
        keyAgree.doPhase(publicKey, true);
        byte[] secret = keyAgree.generateSecret();
        return secret;
    }

    /**
     * @param privateKey
     * @param publicKeyHex
     * @return
     */
    public static String asymmetricCipherKeyPairECDH(ECPrivateKeyParameters privateKey, String publicKeyHex) {
        ECDHBasicAgreement agreement = new ECDHBasicAgreement();
        agreement.init(privateKey);
        final BigInteger bigInteger = agreement.calculateAgreement(SM2Util.getPublicKey(publicKeyHex));
        return HexUtil.encodeHex(bigInteger.toByteArray());
    }


}
