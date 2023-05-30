package com.hyw.webSite;

import com.hyw.webSite.utils.HttpUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test003 {

    @Test
    public static void main(String[] args){

        String sqlDdl = "CREATE TABLE product_info (\n" +
                "  product_no varchar(20) NOT NULL , -- COMMENT '产品编号',\n" +
                "  product_status varchar(5) DEFAULT NULL , -- COMMENT '产品状态',\n" +
                "  effective_date date DEFAULT NULL , -- COMMENT '生效日期',\n" +
                "  expiry_date date DEFAULT NULL , -- COMMENT '失效日期',\n" +
                "  repayment_mode varchar(40) DEFAULT NULL , -- COMMENT '还款方式',\n" +
                "  rate_year decimal(18,6) DEFAULT '0.000000' , -- COMMENT '贷款年率',\n" +
                "  days_year_type varchar(5) DEFAULT NULL , -- COMMENT '年基数类型',\n" +
                "  days_month_type varchar(5) DEFAULT NULL , -- COMMENT '年基数类型',\n" +
                "  created_user varchar(20) DEFAULT NULL , -- COMMENT '创建用户',\n" +
                "  created_time datetime DEFAULT NULL , -- COMMENT '创建时间',\n" +
                "  updated_user varchar(20) DEFAULT NULL , -- COMMENT '最后更新用户',\n" +
                "  updated_time datetime DEFAULT NULL , -- COMMENT '最后更新时间',\n" +
                "  PRIMARY KEY (product_no))";
        sqlDdl="<td>xxx1</td><td width=200px>xxx2</td>";
        // (<td( [^="]*="[^="]*")*>(.*?)</td>)  匹配所有<td xxx> xxx </td>
        String regex = "<td( [^=\"]*=\"[^=\"]*\")*>(.*?)</td>";

        List<Map<String,String>> resultListMap = new ArrayList<>();
        resultListMap = HttpUtil.searchAllGroup(regex,sqlDdl);
        for(Map<String,String> map:resultListMap){
            for(String fileName:map.keySet()){
                System.out.println("key:" + fileName + ",body:" + map.get(fileName));
            }
        }
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(sqlDdl);
//        while (m.find()) {
//            String key = m.group(1);
//            String body = m.group(2);
//            System.out.println("key:" + key + ",body:" + body);
//        }
    }
}
