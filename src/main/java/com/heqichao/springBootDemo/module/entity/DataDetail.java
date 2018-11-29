package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2018-11-27.
 */
@Component("data_detail")
public class DataDetail extends BaseEntity{

    private Integer logId;

    private String devId;

    private String dataKey;

    private String dataType;

    private String dataValue;

    private String unit;

    private String dataStatus;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }


    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
