package com.hyw.webSite.utils;

import com.hyw.webSite.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
public class HttpUtil {

    /**
     * 获取指定HTML标签的指定属性的值
     * @param source 要匹配的源文本
     * @param element 标签名称
     * @param attr 标签的属性名称
     * @return 属性值列表
     *
     * eg.
     * String source = "<a title=中国体育报 href=''>aaa</a><a title='北京日报' href=''>bbb</a>";
     * List<String> list = match(source, "a", "title");
     */
    public static List<String> match(String source, String element, String attr) {
        List<String> result = new ArrayList<String>();
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    /**
     *
     * @param reg 正则表达式
     * @param source 查找源
     * @return 结果集
     */
    public static List<String> search(String reg, String source) {
        //System.out.println(source);
        List<String> result = new ArrayList<String>();
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            for(int i=0;i<m.groupCount();i++){
                System.out.print(" "+m.group(i));
            }
            System.out.println(" ");
            if(m.groupCount()>0 && StringUtil.isNotBlank(m.group(0))) {
                String r = m.group(0);
                result.add(r);
            }
        }
//        System.out.println();
//        System.out.println(m.matches());
        log.info("HttpUtil.search,result="+result);
        return result;
    }

    public static List<Map<String,String>> searchAllGroup(String reg, String source){
        List<Map<String,String>> resultListMap = new ArrayList<>();
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            Map<String,String> record = new HashMap<>();
            for(int i=0;i<=m.groupCount();i++){
                record.put(String.valueOf(i),m.group(i));
            }
            resultListMap.add(record);
        }
        return resultListMap;
    }

    /**
     * 读取http地址，返回网页的文本
     * @param urlPath http地址
     * @return 返回网页的文本
     */
    public static String getHttpRequestData(String urlPath,String webCharset) {
        // 首先抓取异常并处理
        StringBuilder returnString = new StringBuilder("1");
        try {
            // 代码实现以GET请求方式为主,POST跳过
            /** 1 GET方式请求数据 start*/
            // 1  创建URL对象,接收用户传递访问地址对象链接
            URL url = new URL(urlPath);
            // 2 打开用户传递URL参数地址
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            // 3 设置HTTP请求的一些参数信息
            connect.setRequestMethod("GET"); // 参数必须大写
            connect.connect();
            // 4 获取URL请求到的数据，并创建数据流接收
            InputStream isString = connect.getInputStream();
            // 5 构建一个字符流缓冲对象,承载URL读取到的数据
            BufferedReader isRead = new BufferedReader(new InputStreamReader(isString, webCharset));
            // 6 输出打印获取到的文件流
            String str = "";
            while ((str = isRead.readLine()) != null) {
                returnString.append(str.trim());
            }
            // 7 关闭流
            isString.close();
            connect.disconnect();
            // 8 JSON转List对象
            // do somthings
        }catch(UnknownHostException e){
            throw new BizException("http地址("+urlPath+"),不可用!");
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnString.toString();
    }



    private CloseableHttpClient closeableHttpClient = null;
    @Value("${proxy.ip:}")
    private String proxyIp;
    @Value("${proxy.port:0}")
    private int proxyPort;

    //没有忽略证书创建 CloseableHttpClient
    private CloseableHttpClient createHttpClientWithProxy() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //代理
        if (StringUtils.isNotBlank(proxyIp)) {
            HttpHost proxy = new HttpHost(proxyIp, proxyPort, "http");
            httpClientBuilder.setProxy(proxy);
        }
        httpClientBuilder.setMaxConnTotal(200);
        httpClientBuilder.setMaxConnPerRoute(100);
        closeableHttpClient = httpClientBuilder.build();
        return closeableHttpClient;
    }




    /**
     * 忽视所有证书验证-使用org.apache.httpcomponents 4.5版本
     */
    public static CloseableHttpClient acceptsUntrustedCertsHttpClient(String proxyHost, int proxyPort)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        httpClientBuilder.setSslcontext(sslContext);

        // don't check Hostnames, either.
        // use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
//        final X509HostnameVerifier hostnameVerifier = getX509HostnameVerifier();
        //HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;//org.apache.httpcomponents 4.3.6版本没有的
        // here's the special part:
        // -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        // -- and create a Registry, to register it.
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, null);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();
        // now, we create connection-manager using our Registry. allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(200);
        connMgr.setDefaultMaxPerRoute(100);
        httpClientBuilder.setConnectionManager(connMgr);

        HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
        httpClientBuilder.setProxy(httpHost);

        // finally, build the HttpClient;
        CloseableHttpClient client = httpClientBuilder.build();
        return client;
    }



    /**
     * 忽视所有证书验证使用org.apache.httpcomponents 4.3.6版本
     */
    public CloseableHttpClient createNoVerifyClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sc);
            HttpClientBuilder httpClientBuilder = HttpClients.custom().setSSLSocketFactory(sslsf);
            //代理
            if (StringUtils.isNotBlank(proxyIp)) {
                HttpHost proxy = new HttpHost(proxyIp, proxyPort);
                httpClientBuilder.setProxy(proxy);
            }
            httpClientBuilder.setMaxConnTotal(200);
            httpClientBuilder.setMaxConnPerRoute(100);
            closeableHttpClient = httpClientBuilder.build();
            return closeableHttpClient;
        } catch (KeyManagementException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return HttpClients.createDefault();
    }


    public static void main(String[] args){
        List<String> result = HttpUtil.match("<a title=中国体育报 href=''>aaa</a><a title='北京日报' href=''>bbb</a>","a","title");
        System.out.println(result);
        //<a[^<>]*?\\sclass=['\"]?(.*?)['\"]?\\s.*?>
        result = HttpUtil.search("<a[^<>]*?\\stitle=['\"]?(.*?)['\"]?\\s.*?>","<a title=中国体育报 href=''>aaa</a><a title='北京日报' href=''>bbb</a>");
        System.out.println(result);
    }
}
