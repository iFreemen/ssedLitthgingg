package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.EquGroupsMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.heqichao.springBootDemo.base.entity.GroupsEntity;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.TreeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {
	
    @Autowired
    private EquGroupsMapper gMapper ;
    


    @Override
    public void getGroups () {
    	User user = ServletUtil.getSessionUser();
    	Integer competence = user.getCompetence();
    	Integer id = user.getId();
    	List<GroupsEntity> s = gMapper.getGroups(competence, id);
    	List<GroupsEntity> treeList = TreeUtil.listToTree(s);
    	String message = JSON.toJSONString(treeList,SerializerFeature.PrettyFormat);
    	System.out.println(JSON.parse(message));
    	
    }

}
