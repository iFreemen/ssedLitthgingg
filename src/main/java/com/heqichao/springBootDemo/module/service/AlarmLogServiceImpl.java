package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.DateUtil;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.AlarmLog;
import com.heqichao.springBootDemo.module.mapper.AlarmLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by heqichao on 2018-12-16.
 */
@Service
@Transactional
public class AlarmLogServiceImpl implements AlarmLogService {
    @Autowired
    private AlarmLogMapper alarmLogMapper;

    @Autowired
    private EquipmentService equipmentService;
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
    public PageInfo queryAlarmLog(String devId, Integer attrId,String status, String startTime, String endTime) {
        PageUtil.setPage();
        return new PageInfo(alarmLogMapper.queryAlarmLogByDevIdAttrId(devId,attrId, status,  startTime,  endTime));
    }

    @Override
    public void updateAlarm(Map map) {
        String record =StringUtil.getStringByMap(map,"record");
        String status =StringUtil.getStringByMap(map,"status");
        Integer id =StringUtil.getIntegerByMap(map,"id");
        if(id!=null){
            alarmLogMapper.updateAlarm(status,new Date(),record,id);
        }

    }


    @Override
    public Map queryAlarm(Map param) {
        Map map=new HashMap();
        List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
        List<String> devIds =new ArrayList<>();
        if(devList!=null && devList.size()>0){
          for(Map m:devList){
              String devId= (String) m.get("dev_id");
              if(StringUtil.isNotEmpty(devId)){
                  devIds.add(devId);
              }
          }
          //查找系统的报警总数
            int alarmCount =0;
            List<Map> list = alarmLogMapper.queryAlarm(devIds,ALARM_STATUS,null);
            if(list!=null){
                alarmCount=list.size();
            }
            map.put("alarmCount",alarmCount);

            //查找30秒内的变化数据
            String queryChange =  StringUtil.getStringByMap(param,"queryChange");
            if(StringUtil.isNotEmpty(queryChange) && "TRUE".equals(queryChange)){
                list = alarmLogMapper.queryAlarm(devIds,null, DateUtil.addSecond(new Date(),-30));
                if(list!=null && list.size()>0){
                    map.put("chaneList",list);
                }
            }

        }
        return map;
    }
    
    // Muzzy 获取报警最新5条记录
    @Override
    public List<AlarmLog>  queryAlarmNewestFive() {
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer pid = ServletUtil.getSessionUser().getParentId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
        return alarmLogMapper.queryAlarmNewestFive(udid,pid,cmp);
    }
    // End Muzzy
}
