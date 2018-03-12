package com.ylfcf.ppp.util;

import com.ylfcf.ppp.ui.YLFApplication;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * 预埋证书到客户端，有可能在证书更新的时候造成老版本访问不了服务器。
 * Created by Administrator on 2017/11/15.
 */

public class SSLContextUtil {
    public static SSLContext getSSLContext() throws Exception {
        // 生成SSLContext对象
        SSLContext sslContext = SSLContext.getInstance("TLS");
        // 从assets中加载证书
        InputStream inStream = YLFApplication.getApplication().getAssets().open("ylfcfapp.crt");

        // 证书工厂
        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        Certificate cer = cerFactory.generateCertificate(inStream);

        // 密钥库
        KeyStore kStore = KeyStore.getInstance("PKCS12");
        kStore.load(null, null);
        kStore.setCertificateEntry("trust", cer);// 加载证书到密钥库中

        // 密钥管理器
        KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyFactory.init(kStore, null);// 加载密钥库到管理器

        // 信任管理器
        TrustManagerFactory tFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tFactory.init(kStore);// 加载密钥库到信任管理器

        // 初始化
        sslContext.init(keyFactory.getKeyManagers(), tFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}
