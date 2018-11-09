package com.heqichao.springBootDemo.module.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.module.entity.LiteLog;

public interface LiteNAService {
	// 正常
	static final String NORMAL = "N"; 
	// 清除状态
	static final String CLEAR = "C"; 
	// 删除
	static final String DELETE = "D"; 

	Object getDataChange() throws Exception;

	void chg();

	String liangPost(Map vmap) throws Exception;

	List<LiteLog> queryAll();

	void deleteAll();

	PageInfo queryLites();



	void chg(Object map);

	PageInfo queryNBLightLog();
}
