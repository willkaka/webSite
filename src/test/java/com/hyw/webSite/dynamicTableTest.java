package com.hyw.webSite;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.service.DynamicTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes= Application.class)
public class dynamicTableTest {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Test
    public void test001() throws Exception {

        List<ConfigDatabaseInfo> configDatabaseInfos = new ArrayList<>();
        ConfigDatabaseInfo qpCdi1 = new ConfigDatabaseInfo();
        qpCdi1.setDatabaseName("local_mysql_01");
        configDatabaseInfos.add(qpCdi1);

        ConfigDatabaseInfo qpCdi2 = new ConfigDatabaseInfo();
        qpCdi2.setDatabaseName("local_mysql_02");
        configDatabaseInfos.add(qpCdi2);

        System.out.println(configDatabaseInfos);
    }

    @Test
    public void getTableRecords() throws Exception {
//        DynamicTableDto dynamicTableDto = new DynamicTableDto();
//        dynamicTableDto.setTableName("config_database_info");
//        dynamicTableDto.setClassName("com.hyw.webSite.dao.ConfigDatabaseInfo");
//
//        List<?> list = dynamicTableService.readRecords(dynamicTableDto);

//        ConfigDatabaseInfo qpCdi = new ConfigDatabaseInfo();
//        qpCdi.setDatabaseName("local_mysql");
//
//        List<?> list = dynamicTableService.readRecords(qpCdi);
//        ConfigDatabaseInfo configDatabaseInfo = dynamicTableService.readRecord(qpCdi);
//
//        List<?> list2 = dynamicTableService.readRecords(new ConfigDatabaseInfo());
//        System.out.println(list2);

        ConfigDatabaseInfo qpCdi2 = new ConfigDatabaseInfo();
        qpCdi2.setDatabaseName("local_mysql_xx");
        ConfigDatabaseInfo configDatabaseInfo = dynamicTableService.readRecord(qpCdi2);

        int count = dynamicTableService.delete(configDatabaseInfo);
        System.out.println(count);


    }

}
