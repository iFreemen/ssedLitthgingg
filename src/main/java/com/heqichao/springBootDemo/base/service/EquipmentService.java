package com.heqichao.springBootDemo.base.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.param.ResponeResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Muzzy Xu.
 * 
 */


public interface EquipmentService {
	//设备只有在线和离线线状态，所以把故障状态也改为下线状态B
	String NORMAL = "N";
	String BREAKDOWN = "B";

	String ON_LINE="0";
	String OFF_LINE="1";

	String EQUIPMENT_LORA="L";
	String EQUIPMENT_NB="N";
	String EQUIPMENT_GPRS="G";


	PageInfo queryEquipmentList();

	List<Map<String, String>> getUserEquipmentIdList(Integer uid);

	ResponeResult insertEqu(Map map);

	ResponeResult deleteEquByID(Map map);


	void setEquStatus(String eid, String status);

	List<String> getEquipmentByStatus(String status);

	/**
	 * 更新设备的量程
	 * @param eid
	 * @param range
	 * @return
	 */
	int updateRange(String eid, Integer range);

	/**
	 * 查询设备的量程
	 * @param eid
	 * @return
	 */
	Integer queryRange(String eid);

	Integer getUserParent(Integer uid);

	Equipment getEquipmentInfo(String devId);

	List<String> getEquipmentIdListAll();

	PageInfo queryEquipmentPage();

	Equipment getEquById();

	/**
	 * 根据类型、在线状态查找设备ID
	 * @param type_cd
	 * @param online
	 * @return
	 */
	List<String> queryByTypeAndOnline( String type_cd, String online);

	ResponeResult getEquEditById();

	ResponeResult editEqu(Map map);


	/**
	 * 更新设备在线离线状态
	 * @param online
	 * @param list
	 * @param date
	 */
	void updateOnlineStatus(String online , List<String> list,Date date);
}
