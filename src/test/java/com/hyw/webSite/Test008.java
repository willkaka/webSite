package com.hyw.webSite;

import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.service.TestService;
import com.hyw.webSite.utils.excel.ExcelTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.time.LocalDateTime;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableTransactionManagement
public class Test008 {

	@Autowired
	private TestService testService;

	@Test
	public void test() throws Exception {
		System.out.println(testService.test001());
	}
}
