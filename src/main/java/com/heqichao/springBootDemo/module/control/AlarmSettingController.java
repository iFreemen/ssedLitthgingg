package com.heqichao.springBootDemo.module.control;

import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.ExcelReader;
import com.heqichao.springBootDemo.base.util.FileUtil;
import com.heqichao.springBootDemo.module.service.AlarmSettingService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Muzzy Xu.
 * 
 */


@RestController
@RequestMapping(value = "/service")
public class AlarmSettingController extends BaseController{

    @Autowired
    private AlarmSettingService aService;

    
    @RequestMapping(value = "/getAlarmSettings")
    public ResponeResult queryAlarmSettingAll() {
    	return new ResponeResult(aService.queryAlarmSettingAll());
    }

    @RequestMapping(value = "/delAlarmSetting" )
    @ResponseBody
    public ResponeResult delAlarmSetting(@RequestBody Map map) throws Exception {
    	return aService.delAlarmSetting(map);
    }
    @RequestMapping(value = "/addAlarmSetting" )
    @ResponseBody
    public ResponeResult addAlarmSetting(@RequestBody Map map) throws Exception {
    	return aService.addAlarmSetting(map);
    }
    @RequestMapping(value = "/editAlarmSetting" )
    @ResponseBody
    public ResponeResult editAlarmSetting(@RequestBody Map map) throws Exception {
    	return aService.editAlarmSetting(map);
    }
}
