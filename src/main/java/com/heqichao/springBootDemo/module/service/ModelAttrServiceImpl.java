package com.heqichao.springBootDemo.module.service;

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
    public Set<ModelAttr> queryByModelId(Integer modelId) {
        return modelAttrMapper.queryByModelId(modelId);
    }

    @Override
    public void deleteByModelId(Integer modelId) {
        modelAttrMapper.deleteByModelId(modelId);
    }

    @Override
    public void deleteByAttrId(Integer id) {
        modelAttrMapper.deleteById(id);
    }
}
