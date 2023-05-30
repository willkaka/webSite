package com.hyw.webSite;

import com.hyw.webSite.exception.BizException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Test007 {
    public static void main(String[] args){
        BigDecimal amt = new BigDecimal("1.01");
        LocalDate curDate = LocalDate.now();
        BizException.trueThrow(true,"xxx");
        BizException.trueThrow(true,"xxx:{},amt:{},date:{}",123132,amt,curDate);
    }
}
