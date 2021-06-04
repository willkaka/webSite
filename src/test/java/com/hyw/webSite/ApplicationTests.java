package com.hyw.webSite;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.service.DataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

	@Autowired
	private DataService dataService;

	@Test
	public void contextLoads() {
		ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new QueryWrapper<ConfigDatabaseInfo>().lambda()
			.eq(ConfigDatabaseInfo::getDatabaseName,"dev"));
//		List<ConfigDatabaseInfo> confList = dataService.list(null);
//		System.out.println(confList);
	}

}
