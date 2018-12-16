package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.module.entity.AlarmLog;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-12-16.
 */
public interface AlarmLogService {
    //正常数据
    public static final String NOIMAL_STATUS="N";
    //报警数据
    public static final String ALARM_STATUS="A";
    //已删除额数据
    public static final String DELETE_STATUS="D";
    void save(List<AlarmLog> logs);

    /**
     * 更新为正常数据
     * @param devId
     * @param attrId
     */
    void updateNormalStatus(String devId,List<Integer> attrId,Date date);

    PageInfo queryAlarmLog(String devId,Integer attrId);

    /**
     * 查找最新 的报警记录
     * @param devId
     * @return
     */
    List<AlarmLog> queryAlarmByDevId(String devId);
}
