package util;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class SM4Util {

    public static byte[] internalDo(byte[] key, byte[] data, boolean encrypt) throws Exception {
        CBCBlockCipher blockCipher = new CBCBlockCipher(new SM4Engine());
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
        cipher.init(encrypt, new ParametersWithIV(new KeyParameter(key), key));
        byte[] encryptedData = new byte[cipher.getOutputSize(data.length)];
        int length = cipher.processBytes(data, 0, data.length, encryptedData, 0);
        length += cipher.doFinal(encryptedData, length);
        return Arrays.copyOfRange(encryptedData, 0, length);

    }


    public static byte[] enc(byte[] key, byte[] data, boolean encrypt) throws Exception {
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new SM4Engine(), new PKCS7Padding());
        CipherParameters cipherParameters = new KeyParameter(key);
        cipher.init(encrypt, cipherParameters);
        int outLength = cipher.getOutputSize(data.length);
        byte[] resultData = new byte[outLength];
        cipher.processBytes(data,0,data.length,resultData,0);
//        cipher.doFinal(resultData, 16);
        return resultData;

    }

    public static final String ALGORITHM_NAME = "SM4";

    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    public static byte[] sm4Encrypt(byte[] key, byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
            return cipher.doFinal(plainText);
        } catch (Exception ignored) {
        }
        return null;
    }
}
