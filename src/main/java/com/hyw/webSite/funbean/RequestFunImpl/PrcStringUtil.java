package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("prcStringUtil")
@Slf4j
public class PrcStringUtil extends RequestFunUnit<String, PrcStringUtil.QryVariable> {

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(PrcStringUtil.QryVariable variable){
        //输入检查
        if(StringUtil.isBlank(variable.getInputStr())){
            throw new BizException("输入字符串不允许为空值!");
        }
        if(StringUtil.isBlank(variable.getColNum())){
            throw new BizException("列数不允许小于0!");
        }
        try {
            int num = Integer.parseInt(variable.colNum);
        }catch (Exception e){
            throw new BizException("列数必须为整数!");
        }
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, PrcStringUtil.QryVariable variable) {

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TEXT); //以表格形式显示

        String inputString = variable.getInputStr();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.toCharArray()[i];
            switch (c) {
                case '\n':
                    sb.append("#");
                    break;
                case '\r':
                    sb.append("#");
                    break;
                case 32:
                    sb.append("#");
                    break;
                case '\t':
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        String[] list = sb.toString().split("#");
        StringBuffer outString = new StringBuffer();
        int colNum = 0;
        for (String s : list) {
            colNum ++;
            if ("single".equals(variable.getAddString())) outString.append("'");
            if ("double".equals(variable.getAddString())) outString.append("\"");
            outString.append(s);
            if ("single".equals(variable.getAddString())) outString.append("'");
            if ("double".equals(variable.getAddString())) outString.append("\"");
            if("comma".equals(variable.getSeparator())) outString.append(",");
            if("semicolon".equals(variable.getSeparator())) outString.append(";");
            if(colNum % Integer.parseInt(variable.getColNum()) == 0) outString.append("\n");
        }

        return outString.toString();
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestFunUnit.Variable {
        private String inputStr;  //输入字符串
        private String addString; //单双引号
        private String separator; //分隔符
        private String colNum;
    }
}
