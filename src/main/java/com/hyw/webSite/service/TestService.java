package com.hyw.webSite.service;

import com.hyw.webSite.dao.SysParam;
import com.hyw.gdata.DataService;
import com.hyw.gdata.NQueryWrapper;
import com.hyw.gdata.annotation.NTransactional;
import com.hyw.webSite.exception.BizException;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private DataService dataService;

    @NTransactional(rollbackWhen = BizException.class)
    public String test001(){
//        SysParam sysParam = new SysParam();
//        sysParam.setParamRoot("dataSwitch").setParamName("data");
//        dataService.save(sysParam);

        List<SysParam> sysParamList = dataService.list(new NQueryWrapper<SysParam>().setTable(SysParam.class));
        System.out.println("查询记录数："+sysParamList.size());
        for(SysParam sysParam:sysParamList){
            sysParam.setParamDesc("test001");
            dataService.update(sysParam,SysParam::getSysParamId);
        }


//        BizException.trueThrow(true,"rollback");
//        throw new RuntimeException();
        return "success";
    }

    @Test
    public void test002(){
        String filePath = "D:\\002385\\20230322-费用计提规则.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                VoiceManager voiceManager = VoiceManager.getInstance();
                Voice voice = voiceManager.getVoice("kevin16"); // 使用指定的语音合成引擎，这里使用的是FreeTTS的kevin16
                if (voice != null) {
                    voice.allocate();
                    voice.speak(line); // 将文本内容转换为语音输出
                    voice.deallocate();
                } else {
                    System.err.println("找不到指定的语音合成引擎！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
