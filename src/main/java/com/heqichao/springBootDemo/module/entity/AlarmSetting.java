package com.heqichao.springBootDemo.module.entity;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import com.heqichao.springBootDemo.base.util.StringUtil;

@Component("alarm_setting")
public class AlarmSetting extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4267380782792439579L;
	
	private String name;
	private Integer modelId;
	private String modelName;
	private Integer attrId;
	private String attrName;
	private String alramType;
	private String alramTypeName;
	private String dataType;
	private String dataValue;
	private Integer dataA;
	private Integer dataB;
	private String dataStatus;
	
	public AlarmSetting() {
		
	}
	public AlarmSetting(Map map) {
		super.id = StringUtil.getIntegerByMap(map,"id");
		this.name = StringUtil.getStringByMap(map,"name");
		this.modelId = StringUtil.getIntegerByMap(map,"modelId");
		this.attrId = StringUtil.getIntegerByMap(map,"attrId");
		this.alramType = StringUtil.getStringByMap(map,"alramType");
		this.dataA = StringUtil.getIntegerByMap(map,"dataA");
		this.dataB = StringUtil.getIntegerByMap(map,"dataB");
		this.dataStatus = StringUtil.getStringByMap(map,"dataStatus");
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	
	public Integer getAttrId() {
		return attrId;
	}
	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}
	public String getAlramType() {
		return alramType;
	}
	public void setAlramType(String alramType) {
		this.alramType = alramType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Integer getDataA() {
		return dataA;
	}
	public void setDataA(Integer dataA) {
		this.dataA = dataA;
	}
	public Integer getDataB() {
		return dataB;
	}
	public void setDataB(Integer dataB) {
		this.dataB = dataB;
	}
	public String getAlramTypeName() {
		return alramTypeName;
	}
	public String getAlramTypeFmt() {
		String newName=null;
		if(this.alramTypeName !=null) {
			newName=this.alramTypeName;
			if("BAB".equals(this.alramType)) {
				
				newName=newName.replaceAll("B", "，"+this.dataB);
			}else {
				newName=newName.replaceAll("B", ""+this.dataB);
				
			}
			newName=newName.replaceAll("A", this.dataA+"");
			
		}
		return newName;
	}
	public void setAlramTypeName(String alramTypeName) {
		this.alramTypeName = alramTypeName;
	}
	
	
	
}
