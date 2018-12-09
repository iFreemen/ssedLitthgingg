package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.EquipmentMapper;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.mapper.DataDetailMapper;
import com.heqichao.springBootDemo.module.mqtt.MqttUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional(rollbackFor = { Exception.class })
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentMapper eMapper ;
    @Autowired
    private DataDetailMapper dMapper ;

    @Override
    public PageInfo queryEquipmentList() {
    	Map map = RequestContext.getContext().getParamMap();
    	String eid = StringUtil.getStringByMap(map,"eid");
    	Integer gid = StringUtil.getIntegerByMap(map,"gid");
    	String type = StringUtil.getStringByMap(map,"type");
    	String seleStatus = StringUtil.getStringByMap(map,"seleStatus");
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(eMapper.getEquipments(
        		ServletUtil.getSessionUser().getCompetence(),
        		ServletUtil.getSessionUser().getId(),
        		ServletUtil.getSessionUser().getParentId(),
        		gid,eid,type,seleStatus
        		));
    	return pageInfo;
    }
    @Override
    public PageInfo queryEquipmentPage() {
    	Map map = RequestContext.getContext().getParamMap();
    	String eid = StringUtil.getStringByMap(map,"eid");
    	Integer gid = StringUtil.getIntegerByMap(map,"gid");
    	String type = StringUtil.getStringByMap(map,"type");
    	String seleStatus = StringUtil.getStringByMap(map,"seleStatus");
    	List<Map<String,Object>> newLst = new ArrayList<Map<String,Object>>();
    	List<Equipment> eLst = eMapper.getEquipments(
        		ServletUtil.getSessionUser().getCompetence(),
        		ServletUtil.getSessionUser().getId(),
        		ServletUtil.getSessionUser().getParentId(),
        		gid,eid,type,seleStatus
        		);
    	for (Equipment equ : eLst) {
    		Map<String,Object> newClu= new HashMap<String,Object>();
    		newClu.put("name", equ.getName());
    		newClu.put("devId", equ.getDevId());
    		newClu.put("type", equ.getTypeName());
    		newClu.put("online", equ.getOnline());
    		newClu.put("dataPoints",dMapper.queryDetailByDevId(equ.getDevId()));
    		newLst.add(newClu);
    	}
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(newLst);
        return pageInfo;
    }
    /**
     * 根据uid查找所有设备
     */
    @Override
    public List<Map<String,String>> getUserEquipmentIdList(Integer uid) {
    	return eMapper.getUserEquipmentIdList(uid);
    }
    /**
     * 根据uid查找父客户
     */
    @Override
    public Integer getUserParent(Integer uid) {
    	return eMapper.getUserParent(uid);
    }
    /**
     * 根据dev_id查找设备信息
     */
    @Override
    public Equipment getEquipmentInfo(String  devId) {
    	return eMapper.getEquById(devId);
    }
    
    @Override
    public Equipment getEquById() {
    	Map map = RequestContext.getContext().getParamMap();
    	String devId = StringUtil.getStringByMap(map,"devId");
    	return eMapper.getEquById(devId);
    }
    
    /**
     * 查找所有设备dev_id
     */
    @Override
    public List<String> getEquipmentIdListAll() {
    	return eMapper.getEquipmentIdListAll();
    }
    // 根据杆塔ID设置状态
    @Override
    public void setEquStatus(String eid,String status) {
    	 eMapper.setEquStatus(eid,status);
    }
    // 根据状态获取有效设备杆塔ID数组
    @Override
    public List<String> getEquipmentByStatus(String status) {
    	return eMapper.getEquipmentByStatus(status);
    }

	@Override
	public int updateRange(String eid, Integer range) {
		return eMapper.updateRange(eid, range);
	}

	@Override
	public Integer queryRange(String eid) {
		return eMapper.queryRange(eid);
	}

	@Override
    public ResponeResult insertEqu(Map map) {
    	Equipment equ = new Equipment(map);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(equ.getName() == null ||equ.getDevId() == null || uid == null || cmp == 4) {
    		return new ResponeResult(true,"Add Equipment Input Error!","errorMsg");
    	}
    	if(eMapper.duplicatedEid(equ.getDevId(),equ.getUid())) {
    		return new ResponeResult(true,"设备编号重复","errorMsg");
    	}
		equ.setAddUid(uid);
		equ.setValid("N");
		if(eMapper.insertEquipment(equ)>0) {
			List<String> mqId = new ArrayList<String>();
			mqId.add(equ.getDevId());
			try {
				MqttUtil.subscribeTopicMes(mqId);
				return new ResponeResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
    	return  new ResponeResult(true,"Add Equipment fail","errorMsg");
    }
	@Override
	public ResponeResult editEqu(Map map) {
		Equipment equ = new Equipment(map);
		Integer uid = ServletUtil.getSessionUser().getId();
		Integer cmp = ServletUtil.getSessionUser().getCompetence();
		if(equ.getName() == null ||equ.getDevId() == null || uid == null || cmp == 4) {
			return new ResponeResult(true,"Edit Equipment Input Error!","errorMsg");
		}
		if(eMapper.duplicatedEidEdit(equ.getDevId(),equ.getUid())) {
			return new ResponeResult(true,"设备编号重复","errorMsg");
		}
		equ.setUdpUid(uid);
		equ.setValid("N");
		if(eMapper.editEquipment(equ)>0) {
				return new ResponeResult();
		}
		return  new ResponeResult(true,"Edit Equipment fail","errorMsg");
	}
	@Override
	public ResponeResult getEquEditById() {
		Map map = RequestContext.getContext().getParamMap();
		String devId = StringUtil.getStringByMap(map,"devId");
		Integer id = StringUtil.getIntegerByMap(map,"id");
		Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(cmp == 4) {
    		return new ResponeResult(true,"无编辑权限","errorMsg");
    		
    	}
    	if(cmp != 2 && !eMapper.duplicatedEid(devId,uid)) {
    		return new ResponeResult(true,"无编辑权限","errorMsg");
    		
    	}
		return new ResponeResult(eMapper.getEquEditById(devId,id));
	}
    
    @Override
    public ResponeResult deleteEquByID(Map map) {
    	Integer eid = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"eid"));
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  eid == null || udid == null || cmp == 4) {
    		return new ResponeResult(true,"Delete fail!","errorMsg");
    	}else {
    		if(eMapper.delEquById(eid,udid)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Delete Equipment fail","errorMsg");
    }

}
