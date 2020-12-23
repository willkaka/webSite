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


    public static void main(String[] args){
        List<String> result = HttpUtil.match("<a title=中国体育报 href=''>aaa</a><a title='北京日报' href=''>bbb</a>","a","title");
        System.out.println(result);
        //<a[^<>]*?\\sclass=['\"]?(.*?)['\"]?\\s.*?>
        result = HttpUtil.search("<a[^<>]*?\\stitle=['\"]?(.*?)['\"]?\\s.*?>","<a title=中国体育报 href=''>aaa</a><a title='北京日报' href=''>bbb</a>");
        System.out.println(result);
    }
}
