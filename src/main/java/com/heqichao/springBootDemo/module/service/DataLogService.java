package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.DataDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-28.
 */
public interface DataLogService {

    //有效
    public static final String ENABLE_STATUS="N";
    //无效
    public static final String UN_ENABLE_STATUS="D";
    //无效
    public static final String ERROR_STATUS="ERR";
    /**
     * 保存数据
     * @param devId 设备ID
     * @param mesage 所接收到的数据内容
     */
    void saveDataLog(String devId,String mesage);

    List<DataDetail> queryDataDetail(String devId, String key, String startTime, String endTime);

    void deleteDataLog(String... devId);

    Map querqueryEquAttrLog(String devId, String attrKey, String startTime, String endTime);
}
