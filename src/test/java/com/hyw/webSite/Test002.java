package com.hyw.webSite;

import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.NUpdateWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.dto.IPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test002<T> {

	@Autowired
	private DataService dataService;

	@Test
	public void test() {

//		Connection connection = dataService.getConnection();
//		List<TableFieldInfo> tableFieldInfoList = dataService.getTableFieldList(LoanBalance.class);
//		List<TableFieldInfo> tableFieldInfoList = dataService.getTableFieldList(ConfigDatabaseInfo.class);


		IPage<ConfigDatabaseInfo> cdiPage = dataService.listByPage(new NQueryWrapper<ConfigDatabaseInfo>()
			.setTable(ConfigDatabaseInfo.class).setCurPage(0).setCurRecord(0).setPageSize(5));

		int count = 0;
		String dataBaseName = "dev_test_001";

		count = dataService.count(new NQueryWrapper<ConfigDatabaseInfo>().setTable(ConfigDatabaseInfo.class));

		//save
		int insertSeq = count + 1;
		ConfigDatabaseInfo configDatabaseInfo = new ConfigDatabaseInfo();
		configDatabaseInfo.setConfigDatabaseInfoId(insertSeq);
		configDatabaseInfo.setDatabaseName(dataBaseName);
		configDatabaseInfo.setDatabaseType("type");
		configDatabaseInfo.setDatabaseDriver("driver");
		configDatabaseInfo.setDatabaseAddr("addr");
		configDatabaseInfo.setDatabaseAttr("attr");
		count = dataService.save(configDatabaseInfo);

		count = dataService.count(new NQueryWrapper<ConfigDatabaseInfo>().setTable(ConfigDatabaseInfo.class));

		//update
		count = dataService.update(new NUpdateWrapper<ConfigDatabaseInfo>()
				.set(ConfigDatabaseInfo::getDatabaseLabel,"xxxx")
				.eq(ConfigDatabaseInfo::getConfigDatabaseInfoId,insertSeq)
		);

		//JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);

		configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
			.eq(ConfigDatabaseInfo::getConfigDatabaseInfoId,insertSeq));
		System.out.println(JSONObject.toJSON(configDatabaseInfo));

		configDatabaseInfo.setDatabaseLabel("xxxx2222");
		count = dataService.update(new NUpdateWrapper<>().updateByAptKey(configDatabaseInfo,ConfigDatabaseInfo::getConfigDatabaseInfoId));
		configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
				.eq(ConfigDatabaseInfo::getDatabaseName,dataBaseName));
		System.out.println(JSONObject.toJSON(configDatabaseInfo));

		int id = configDatabaseInfo.getConfigDatabaseInfoId();
		count = dataService.delete(new NUpdateWrapper<>().deleteByAptKey(configDatabaseInfo,ConfigDatabaseInfo::getConfigDatabaseInfoId));
		configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
				.eq(ConfigDatabaseInfo::getConfigDatabaseInfoId,id));

		List<ConfigDatabaseInfo> configs = dataService.list(new NQueryWrapper<ConfigDatabaseInfo>().setTable(ConfigDatabaseInfo.class));
		count =0;
	}
}
