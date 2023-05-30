package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.dto.TableFieldInfo;
import com.hyw.webSite.exception.IfThrow;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.utils.excel.ExcelUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@Service("exportExcel")
@Slf4j
public class ExportExcel extends RequestFunUnit<List<String>, ExportExcel.QryVariable> {

    @Autowired
    private DataService dataService;

    @Value("${export.file.local.path}")
    private String exportFileLocalPath;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QryVariable variable){
        //输入检查
        IfThrow.isTrue(Objects.isNull(variable.getDbName()),"db不允许为空值!");
        IfThrow.isTrue(Objects.isNull(variable.getLibName()),"lib不允许为空值!");
        IfThrow.isTrue(Objects.isNull(variable.getSql()),"sql不允许为空值!");
        IfThrow.isTrue(Objects.isNull(variable.getExportFileName()),"导出文件名不允许为空值!");
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public List<String> execLogic(RequestDto requestDto, QryVariable variable) {
        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_DOWNLOAD_FILE); //以表格形式显示

        return genExcel(variable);
    }

    public List<String> genExcel(QryVariable variable){
        List<String> filePathList = new ArrayList<>();
        int pageSize = 200000;
        //取脚本记录的字段名称
        Map<String, String> headFieldName = new LinkedHashMap<>();
        Connection connection = dataService.getDatabaseConnection(variable.getDbName(),variable.getLibName());
        List<TableFieldInfo> tableFieldInfoList = dataService.getTableFieldList(variable.getSql() + " limit 1", connection);
        tableFieldInfoList.forEach(tf -> headFieldName.put(tf.getFieldName(), tf.getComment()));

        String fileFullPath = exportFileLocalPath + variable.getExportFileName() + ".xlsx";
        int index = 0;
        int totalCnt = 0;
        int limitStartIndex = 0;
        String sqlLimit = variable.getSql() + " LIMIT " + limitStartIndex + "," + pageSize;
        List<Map<String, Object>> dataMapList = dataService.mapList(new NQueryWrapper<>().setConnection(connection).setSql(sqlLimit));
        while (dataMapList.size() > 0) {
            if(totalCnt >= variable.getFileMaxRecordNum()) {
                System.out.println(fileFullPath);
                index++;
                totalCnt = 0;
                fileFullPath = exportFileLocalPath +
                        (index > 0 ? variable.getExportFileName() + "_" + index : variable.getExportFileName()) + ".xlsx";
            }
            File file = new File(fileFullPath);
            if(!filePathList.contains(fileFullPath)) filePathList.add(fileFullPath);
            ExcelUtil.writeRecordIntoExcel(file,variable.getExportSheetName(),dataMapList,headFieldName);
            log.info("已完成文件："+file.getAbsolutePath());

            totalCnt = totalCnt + dataMapList.size();
            dataMapList.clear();
            limitStartIndex = limitStartIndex + pageSize;
            sqlLimit = variable.getSql() + " LIMIT " + limitStartIndex + "," + pageSize;
            dataMapList = dataService.mapList(new NQueryWrapper<>().setConnection(connection).setSql(sqlLimit));
//            sheetName = sheetName + index;
        }

        return filePathList;
    }

    private String getProjectResourcePath(){
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String path = applicationHome.getDir().getAbsolutePath();
        return applicationHome.getDir().getAbsolutePath();
    }
    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestPubDto {
        private String dbName;
        private String libName;
        private String sql;
        private String exportFileName;
        private String exportSheetName;
        private Integer fileMaxRecordNum;
    }
}