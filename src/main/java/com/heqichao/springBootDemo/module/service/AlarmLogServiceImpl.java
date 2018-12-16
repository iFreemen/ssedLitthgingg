package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.AlarmLog;
import com.heqichao.springBootDemo.module.mapper.AlarmLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-12-16.
 */
@Service
@Transactional
public class AlarmLogServiceImpl implements AlarmLogService {
    @Autowired
    private AlarmLogMapper alarmLogMapper;
    @Override
    public void save(List<AlarmLog> logs) {
        if(logs!=null && logs.size()>0){
            alarmLogMapper.save(logs);
        }

    }

    @Override
    public void updateNormalStatus(String devId, List<Integer> attrId,Date date ) {
        if(StringUtil.isNotEmpty(devId) && attrId!=null && attrId.size()>0 ){
            alarmLogMapper.updateStatus(NOIMAL_STATUS,date,devId,attrId,ALARM_STATUS);
        }

    }

    @Override
    public PageInfo queryAlarmLog(String devId, Integer attrId) {
        PageUtil.setPage();
        return new PageInfo(alarmLogMapper.queryAlarmLogByDevIdAttrId(devId,attrId,ALARM_STATUS));
    }

    @Override
    public List<AlarmLog> queryAlarmByDevId(String devId) {
        return null;
    }
}
