package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("about")
@Slf4j
public class About extends RequestFunUnit<String, About.QryVariable> {

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, About.QryVariable variable) {

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TEXT); //以表格形式显示

        return "about";
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestPubDto {
    }
}
