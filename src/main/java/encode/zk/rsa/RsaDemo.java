package encode.zk.rsa;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @desc: RSA 加解密
 * @author: zk
 * @time: 2021/5/20 14:53
 */
public class RsaDemo {



    public static void main(String[] args) throws Exception{
        //rent();
//        jdkRSA();
        resetJdkPriPub();
    }

    /**
     * 生成密钥对 => 加解密
     * @throws Exception
     */
    private static void jdkRSA()throws Exception {
        String str="zk is leader";
        String algorithm="RSA";
        // 转换模式
        String transform = "RSA/ECB/PKCS1Padding";

        //实例化秘钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        //初始化，秘钥长度512~16384位，64倍数
        keyPairGenerator.initialize(512);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String priKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String pubKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println("公钥为：" + pubKeyStr);
        System.out.println("私钥为：" + priKeyStr);

        //公钥加密
        Cipher cipher = Cipher.getInstance(transform);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] pubEncryptBytes = cipher.doFinal(str.getBytes());
        String encodeStr = Base64.getEncoder().encodeToString(pubEncryptBytes);
        System.out.println("加密后的字符串为：" + encodeStr);

        //私钥解密
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] priDecryptBytes = cipher.doFinal(pubEncryptBytes);
        String priDecryptStr = new String(priDecryptBytes);
        System.out.println("解密后的字符串为：" + priDecryptStr);

        System.out.println("-----------------------------------------------------------------");
        //测试私钥加密，公钥解密
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        byte[] priBytes = cipher.doFinal("芜湖".getBytes());

        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        byte[] bytes = cipher.doFinal(priBytes);
        System.out.println("通过公钥解密后的结果为："+new String(bytes));


    }

    /**
     * 已知公、私钥字符串 => 加解密
     * @throws Exception
     */
    private static void resetJdkPriPub()throws Exception {
        String value = "zk's blog";
        // 加密算法
        String algorithm = "RSA";
        // 转换模式
        String transform = "RSA/ECB/PKCS1Padding";
        // RSA公钥BASE64字符串
        String rsaPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKBvz9cma+hXNiv2yXg6e1PyZhHVZm3bJXDvTJP2LyXo4vs9grH36Q9kNgr6quHtuU6fEoUxUu2zbEB8dkEWB9UCAwEAAQ==";
        // RSA私钥BASE64字符串
        String rsaPrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAoG/P1yZr6Fc2K/bJeDp7U/JmEdVmbdslcO9Mk/YvJeji+z2CsffpD2Q2Cvqq4e25Tp8ShTFS7bNsQHx2QRYH1QIDAQABAkEAjemZXORdesz52/WVzEVepai6ZHfw/Kdl/PmPMSoIFmz7mk55rprl2Akn2V0odSiHSnMWvDmOUIAvHaHF4Re4wQIhAN5GxVeF7ndyoWasxqIOVb6baNkUrapBM0nacPS4WA8JAiEAuMcvNM2Z1rW74JagoGlSIfRkNUqa+3LTCN/fK7VR2W0CICs/+gYduVjkpSMlW0ENKQH9m1kh/Oiz5xbnujLj676BAiBVGif7wdXgtcLaJYXFW7ygNtcQVFQdCz13EOTQVKpl4QIgY2YyH3vUYI2J68qCGtYjj5iNHUEwwze+Za1R7y0V43k=";

        // ------- 还原公、私钥 --------
        byte[] publicKeyBytes = Base64.getDecoder().decode(rsaPublicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        byte[] privateKeyBytes = Base64.getDecoder().decode(rsaPrivateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // ------- 测试加解密 --------
        Cipher cipher = Cipher.getInstance(transform);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] pubEncryptBytes = cipher.doFinal(value.getBytes());
        System.out.println("RSA公钥加密数据: " + Base64.getEncoder().encodeToString(pubEncryptBytes));

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] priDecryptBytes = cipher.doFinal(pubEncryptBytes);
        System.out.println("RSA私钥解密数据: " + new String(priDecryptBytes));

    }


    private static void rent() {
        int x = 2800 * 12;
        System.out.println("总房租："+x);
        System.out.println("每人要付："+x / 2);
        System.out.println(33000 / 12);
    }


}