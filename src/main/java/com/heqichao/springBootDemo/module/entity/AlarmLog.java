package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2018-11-19.
 */
@Component("alarm_log")
public class AlarmLog extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1031545717859719314L;
	//设备ID
    private String devId;
    //模板id
    private Integer modelId;
    //属性Id
    private Integer attrId;

    private Integer settingId;

    private String alramType;

    //数据值
    private String  dataValue;
    //单位
    private String  unit;

    private String devType;

    //数据状态
    private String dataStatus;

    //手动处理时的记录

    public String getAlramType() {
        return alramType;
    }

    public void setAlramType(String alramType) {
        this.alramType = alramType;
    }

    private String record;

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevId() {
        return devId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
