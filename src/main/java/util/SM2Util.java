package util;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

public class SM2Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String PROVIDER = "BC";


    /**
     * @return
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



}
