package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.EquipmentMapper;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.mqtt.MqttUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.ResultSet;
import java.util.ArrayList;
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
    public ResponeResult queryEquipmentPage() {
    	Map map = RequestContext.getContext().getParamMap();
    	Integer crrNum = StringUtil.getIntegerByMap(map,"crrNum");
    	Integer pagSize = StringUtil.getIntegerByMap(map,"pagSize");
    	Integer gid = StringUtil.getIntegerByMap(map,"gid");
    	String devName = StringUtil.getStringByMap(map,"devName");
    	ResultSet equipments = null;
    	Integer res= null;
    	eMapper.getEquPage(crrNum,
    			pagSize, ServletUtil.getSessionUser().getId(), gid, devName);
    	System.out.println(res);
    	return new ResponeResult(eMapper.getEquPage(crrNum,
    			pagSize, ServletUtil.getSessionUser().getId(), gid, devName));
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
    	return eMapper.getEquipmentInfo(devId);
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
    	if(eMapper.duplicatedEid(equ.getDevId(),uid)) {
    		return new ResponeResult(true,"杆塔Id重复","errorMsg");
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
