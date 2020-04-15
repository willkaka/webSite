package com.hyw.webSite.service;

import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.model.SpringDatabaseConfig;
import com.hyw.webSite.utils.DbUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Service
public class SpringDataSourceService {

    @Autowired
    private SpringDatabaseConfig springDatabaseConfig;

    @Autowired
    private DataSource dataSource;
//    @Resource
//    protected TransactionFactory transactionFactory;

    /**
     * 获取spring数据库连接
     * @return Connection
     */
    public Connection getSpringDatabaseConnection(){
        String dbType="";
        if(springDatabaseConfig.getDriverClass().toLowerCase().contains("sqlite")) dbType= Constant.DB_TYPE_SQLITE;
        if(springDatabaseConfig.getDriverClass().toLowerCase().contains("oracle")) dbType= Constant.DB_TYPE_ORACLE;
        if(springDatabaseConfig.getDriverClass().toLowerCase().contains("mysql")) dbType= Constant.DB_TYPE_MYSQL;
        return DbUtil.getConnection(dbType,
                springDatabaseConfig.getDriverClass(),springDatabaseConfig.getUrl(),null,null,
                springDatabaseConfig.getUserName(),springDatabaseConfig.getPassword());
    }

    public Connection getCurSpringDbConnection() {
        SpringManagedTransactionFactory transactionFactory = new SpringManagedTransactionFactory();
        Transaction transaction = transactionFactory.newTransaction(dataSource, null, false);
        Connection connection = null;
        try {
            connection = transaction.getConnection();
        } catch (Exception e) {
            log.error("数据库连接出错！", e);
            throw new BizException("数据库连接出错！");
        }
        return connection;
    }
}
