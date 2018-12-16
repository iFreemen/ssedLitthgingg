package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.util.UserUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.ModelAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by heqichao on 2018-11-19.
 */
@Service
@Transactional
public class ModelAttrServiceImpl implements ModelAttrService {

    @Autowired
    private ModelAttrMapper modelAttrMapper;

    @Override
    public void saveModelAttr(List<ModelAttr> list) {
        modelAttrMapper.saveModelAttr(list);
    }

    @Override
    public List<ModelAttr> queryByModelId(Integer modelId) {
        return modelAttrMapper.queryByModelId(modelId);
    }
    // Muzzy
    @Override
    public List<ModelAttr> queryAttrByModelId(Integer modelId) {
    	return modelAttrMapper.queryAttrByModelId(modelId);
    }
    // End Muzzy 

    @Override
    public void deleteByModelId(Integer modelId) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        modelAttrMapper.deleteByModelId(modelId);
    }

    @Override
    public void deleteByAttrId(Integer id) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        modelAttrMapper.deleteById(id);
    }
}
