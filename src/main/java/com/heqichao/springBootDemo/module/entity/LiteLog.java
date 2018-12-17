package com.heqichao.springBootDemo.module.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.heqichao.springBootDemo.base.entity.BaseEntity;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2018-8-21.
 */
@Component("lite_log")
public class LiteLog extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6819883273688164511L;
	private String deviceId;
    private String message;
    private String currenState;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date eventTime;
    
    public LiteLog() {
    	
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrenState() {
        return currenState;
    }

    public void setCurrenState(String currenState) {
        this.currenState = currenState;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
    

    public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public LiteLog(String message, String currenState, Date eventTime) {
        this.message = message;
        this.currenState = currenState;
        this.eventTime = eventTime;
        this.addDate =new Date();
    }
}
