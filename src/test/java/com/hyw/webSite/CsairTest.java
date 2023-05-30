package com.hyw.webSite;

import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

public class CsairTest {

    @Test
    public void test(){

        String csairHttp = "https://yy.csair.com/OrderYueWS/getOrderTkviewListByGrouTypeAndCity.visa";
        String webCharset = "utf-8";
//        String htmlString = HttpUtil.getHttpRequestData(csairHttp,webCharset);
//        System.out.print(htmlString);

        Map<String,Object> map = new HashMap<>();
        map.put("groupType","89d36fc46b92484a97df0334413688be");
        map.put("cityStartSZM","SZX");
        map.put("cityEndSZM","LHW");
        String parameterData = JSONObject.toJSONString(map);
        String result = "";
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpUtil.acceptsUntrustedCertsHttpClient(csairHttp,80);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost(csairHttp);
        try {
            post.setHeader("Content-Type", "application/json; charset=UTF-8");
            post.setHeader("Accept", MediaType.APPLICATION_JSON.toString());
            post.setHeader("Cookie", "likev_user_id=7579b25d-58e4-4d30-a79f-ef32bfd51c12; _gscu_422057653=967059470gn4hr98; isXionganFlag=false; language=zh_CN; _gscbrs_422057653=1; sid=cc4f98bece0b4b5cb50a2c6c28ac1703; jumpPage=https%3A%2F%2Fb2c.csair.com%2FB2C40%2Fmodules%2Fordernew%2ForderManagementFrame.html%3F_%3D1617156530349; captchaKey=1617156531940-163.125.153.161; c=tUCuL2lU-1617156534775-079595700ef5e-52722494; _fmdata=nfzPxvV8akcdrx09I9gamtliqJaRhdUOsHo3dqN2WQ97j4o9byNds6VdOMtqcnbpk61JMB4AgXQBED7TuYGa1NNFA%2FraVoFGTXAiPfAQkso%3D; _xid=CcmYC4djwWAKG%2F8iuJEvuMLYZhyoXdD94wnB%2Bob%2B0WH7u0hZVSEuqA1n5Sy1A2vN56UsPZ2jGWAkyFmr6z1aaA%3D%3D; cs1246643sso=0d629f1584f1467881757257fdfa5195; memberType=8; loginType=12; aid=8ae8768a6a9da17b016add36c8ef1dcb; identifyStatus=N; userId=18926269916; userType4logCookie=M; userId4logCookie=18926269916; useridCookie=18926269916; userCodeCookie=18926269916; WT-FPC=id=163.125.134.148-2452490192.30829523:lv=1617268458882:ss=1617268458882:fs=1596705947452:pn=1:vn=5; last_session_stm_8mrmut7r76ntg21b=1617268458923; last_session_id_8mrmut7r76ntg21b=98aff991-a622-43cb-f6c4-b0b0f497fb6a; likev_session_etm_8mrmut7r76ntg21b=1617268516820; LOGIN_KEY=447b61ac663f4e1c9a5cb078650b47fa");
            post.setEntity(new StringEntity(parameterData, "UTF-8"));
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) httpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            System.out.println("RESPONSE MSG:" + result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void sss () {
        Properties properties = System.getProperties();
        properties.forEach((s, s2) -> System.out.println(s + "-" + s2));
    }
}

