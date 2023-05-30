package com.hyw.webSite;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Test001 {

    @Test
    public void test(){
//        ConfigDatabaseInfo configDatabaseInfo = new ConfigDatabaseInfo();
//        configDatabaseInfo.setDatabaseLabel("label");
//        configDatabaseInfo.setDatabaseAttr("attr");
//        configDatabaseInfo.setDatabaseAddr("address");
//        configDatabaseInfo.setDatabaseDriver("driver");
//        configDatabaseInfo.setDatabaseType("type");
//        configDatabaseInfo.setDatabaseName("name");
//        System.out.println(SqlUtil.getInsertSql(configDatabaseInfo));


//        System.out.println(SqlUtil.getInsertSqlWithList(list));
//        System.out.println(SqlUtil.getUpdateSql(list,"seq"));
        LocalDate preMonthFirstDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        System.out.println(preMonthFirstDate);
    }
}

@Getter
@Setter
class Source{
    private Integer overdueDays;
}
