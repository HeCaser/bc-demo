# bouncycastle 包的使用

[bc 包仓库](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on)

## 环境/配置

IntelliJ IDEA 2021.3.3 (Community Edition)

gradle-7.1.1

org.bouncycastle:bcprov-jdk15on:1.69

---
2023-06-19

## sm2 秘钥对生成
> 本章包含两种 sm2 秘钥对生成逻辑: KeyPair, AsymmetricCipherKeyPair

[sm2 工具类](src/main/java/util/SM2Util.java)

### KeyPair 

> 利用 bc 包的 `BouncyCastleProvider` 生成 JDK 中的 `KeyPair` 对象

[详细代码链接](src/main/java/Demo.java)

**总结**:

- 利用了 bc 包的 `BouncyCastleProvider` , 生成的是 JDK 自带的 `KeyPair` 对象
- `KeyPair` 包含公私钥对象, 可以查看 format 和 byte[]
- 公私钥的 byte[] 直接转 String 输出是乱码, Demo 中经过 Hex 编码后输出
- 公私钥的开头部分是固定的
- 私钥长度 > 公钥长度

KeyPair 的公私钥对象, 基本信息举例(Hex 编码后输出):

<img src='img/img_keypair.png'>


### AsymmetricCipherKeyPair


### 常规模式

- ecdh 秘钥协商
- sm4 加解密



