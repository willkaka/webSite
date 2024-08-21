package com.hyw.webSite;

import com.hyw.webSite.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Test007 {
    public static void main(String[] args){
//        BigDecimal amt = new BigDecimal("1.01");
//        LocalDate curDate = LocalDate.now();
//        BizException.trueThrow(true,"xxx");
//        BizException.trueThrow(true,"xxx:{},amt:{},date:{}",123132,amt,curDate);

        String x = StringUtils.join("6010+6011+6012+6013");
        List<String> list1 = Arrays.asList(x.split("+"));
        List<String> list2 = Arrays.asList(x.replace("+","#").split("#"));

        BigDecimal num = new BigDecimal("123.563");
        //检查小数位
        if(!num.multiply(new BigDecimal("1000").remainder(new BigDecimal("10"))).equals(BigDecimal.ZERO)){
            BizException.trueThrow(true,"xxx");
        }
    }
}
