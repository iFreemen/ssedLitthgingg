package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.base.param.ResponeResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Muzzy Xu.
 * 
 */


public interface AlarmSettingService {

	PageInfo queryAlarmSettingAll();

	ResponeResult delAlarmSetting(Map map);

	ResponeResult addAlarmSetting(Map map);

	ResponeResult editAlarmSetting(Map map);
	
}
