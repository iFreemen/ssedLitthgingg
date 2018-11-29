package com.heqichao.springBootDemo.base.entity;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.annotation.JSONField;
import com.heqichao.springBootDemo.base.util.StringUtil;

/**
 * @author Muzzy Xu.
 */
@Component("equipment")
public class Equipment extends BaseEntity  {


	private static final long serialVersionUID = -1596449961625624849L;
	private String name;//设备名称
	private String devId;//设备编号
	private String typeCd;//设备类型
	private Integer modelId;
	private Integer groupId;
    private Integer groupAdmId;
    private Integer appId;
    private String modelName;//模板名称
    private String groupName;//分组名称
    private String appName;//应用名称
    private String verification;//验证码
    private String supportCode;//厂商ID
    private String supporter;//厂商名称
    private Integer uid;//所属用户
    private String site;//位置
    private String remark;
    private String online;//在离线
    private String valid;//有效标志
    
    public Equipment() {
    	
    }
    public Equipment(Map map) {
    	this.devId = StringUtil.getStringByMap(map,"devId");
    	this.typeCd = StringUtil.getStringByMap(map,"typeCd");
    	this.modelId = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"modelId"));
    	this.groupId = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"groupId"));
    	this.groupAdmId = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"groupAdmId"));
    	this.appId = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"appId"));
    	this.verification = StringUtil.getStringByMap(map,"verification");
    	this.supportCode = StringUtil.getStringByMap(map,"supportCode");
    	this.supporter = StringUtil.getStringByMap(map,"supporter");
    	this.site = StringUtil.getStringByMap(map,"site");
    	this.uid = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"uid"));
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getTypeCd() {
		return typeCd;
	}
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getGroupAdmId() {
		return groupAdmId;
	}
	public void setGroupAdmId(Integer groupAdmId) {
		this.groupAdmId = groupAdmId;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getVerification() {
		return verification;
	}
	public void setVerification(String verification) {
		this.verification = verification;
	}
	public String getSupportCode() {
		return supportCode;
	}
	public void setSupportCode(String supportCode) {
		this.supportCode = supportCode;
	}
	public String getSupporter() {
		return supporter;
	}
	public void setSupporter(String supporter) {
		this.supporter = supporter;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
    
    
	

}
