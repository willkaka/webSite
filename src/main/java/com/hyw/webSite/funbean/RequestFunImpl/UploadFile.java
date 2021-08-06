package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dao.WebConfigReq;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.utils.excel.ExcelTemplateUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Service("uploadFile")
@Slf4j
public class UploadFile extends RequestFunUnit<String, UploadFile.QryVariable> {

    @Autowired
    private ExcelTemplateUtil excelTemplateUtil;

    @Autowired
    private DataService dataService;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(UploadFile.QryVariable variable){
        //输入检查
        if(Objects.isNull(variable.getFile())){
            throw new BizException("导入文件不允许为空值!");
        }
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, UploadFile.QryVariable variable) {

//        List<WebConfigReq> webConfigReqList =
//                excelTemplateUtil.getObjectList("web_config_req",variable.getUploadFile(),WebConfigReq.class);

        return "success";
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestFunUnit.Variable {
        private MultipartFile file;
    }
}
