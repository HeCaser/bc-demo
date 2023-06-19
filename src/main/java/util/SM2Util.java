package util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SM2Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String PROVIDER = "BC";


    /**
     * 生成 sm2 秘钥对
     * KeyPair 模式
     */
    public static KeyPair createKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", PROVIDER);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("sm2p256v1");
            keyGen.initialize(ecSpec);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            System.err.println("err = " + e);
            return null;
        }
    }

    /**
     * 生成 sm2 秘钥对
     * AsymmetricCipherKeyPair 模式
     */
    public static AsymmetricCipherKeyPair createAsymmetricCipherKeyPair() {
        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        // 生成密钥对
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParameters, new SecureRandom());
        generator.init(keyGenParams);
        AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }


    /**
     * 转换字节数组为公钥对象
     * 注意: 针对 x059 格式的公钥
     */
    public static PublicKey changeBytes2PublicKey(byte[] bytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", PROVIDER);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 转换字节数组为私钥对象
     * 注意: 针对 pkcs8 格式的公钥
     */
    public static PrivateKey changeBytes2PrivateKey(byte[] bytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", PROVIDER);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取公钥对象
     *
     * @param publicKeyString: 公钥 Hex 编码字符串, 04 开头的非压缩格式
     * @return 公钥对象
     */
    public static ECPublicKeyParameters getPublicKey(String publicKeyString) {
        // 去掉 04 开头, 取出公钥的 x y 坐标.
        String keyContent = publicKeyString.substring(2);
        String xHex = keyContent.substring(0, keyContent.length() / 2);
        BigInteger x = new BigInteger(xHex, 16);
        String yHex = keyContent.substring(keyContent.length() / 2);
        BigInteger y = new BigInteger(yHex, 16);

        //构造domain参数 + 构造 ECCurve对象
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters);
        ECCurve curve = sm2ECParameters.getCurve();

        // 使用 curve 和公钥点坐标构造 ECPublicKeyParameters 对象
        ECPoint q = curve.createPoint(x, y);
        ECPublicKeyParameters publicKey = new ECPublicKeyParameters(q, domainParameters);
        return publicKey;
    }


    /**
     * 获取私钥对象
     * @param privateKeyString: Hex 编码的字符串, 表示一个 bigint
     * @return 私钥对象
     */
    public static ECPrivateKeyParameters getPrivateKey(String privateKeyString) {
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters);
        BigInteger bigInteger = new BigInteger(privateKeyString, 16);
        ECPrivateKeyParameters privateKey = new ECPrivateKeyParameters(bigInteger, domainParameters);
        return privateKey;
    }

}
