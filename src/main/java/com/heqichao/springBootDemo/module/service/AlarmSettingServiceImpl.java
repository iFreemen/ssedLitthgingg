package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.mapper.EquipmentMapper;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.util.CollectionUtil;
import com.heqichao.springBootDemo.base.util.ExcelWriter;
import com.heqichao.springBootDemo.base.util.FileUtil;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.AlarmSetting;
import com.heqichao.springBootDemo.module.mapper.AlarmSettingMapper;
import com.heqichao.springBootDemo.module.mqtt.MqttUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional(rollbackFor = { Exception.class })
public class AlarmSettingServiceImpl implements AlarmSettingService {
    @Autowired
    private AlarmSettingMapper aMapper ;

    @Override
    public PageInfo queryAlarmSettingAll() {
    	Map map = RequestContext.getContext().getParamMap();
    	String name = StringUtil.getStringByMap(map,"name");
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(aMapper.queryAlarmSettingAll(name));
    	return pageInfo;
    }
    
    @Override
    public ResponeResult addAlarmSetting(Map map) {
    	AlarmSetting as = new AlarmSetting(map);
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  as.getName() == null || udid == null || cmp == 4) {
    		return new ResponeResult(true,"输入有误或没有权限","errorMsg");
    	}else {
    		as.setAddUid(udid);
    		as.setDataStatus("N");
    		if(aMapper.addAlarmSetting(as)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Add AlarmSetting fail","errorMsg");
    }
    @Override
    public ResponeResult editAlarmSetting(Map map) {
    	AlarmSetting as = new AlarmSetting(map);
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  as.getId() == null || as.getName() == null || udid == null || cmp == 4) {
    		return new ResponeResult(true,"输入有误或没有权限","errorMsg");
    	}else {
    		as.setUdpUid(udid);
    		if(aMapper.editAlarmSetting(as)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Edit AlarmSetting fail","errorMsg");
    }
    @Override
    public ResponeResult delAlarmSetting(Map map) {
    	Integer aid = StringUtil.getIntegerByMap(map,"aid");
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  aid == null || udid == null || cmp == 4) {
    		return new ResponeResult(true,"Delete fail!","errorMsg");
    	}else {
    		if(aMapper.delAlarmSetting(aid,udid)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Delete Equipment fail","errorMsg");
    }

}
