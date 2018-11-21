package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.ModelAttr;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by heqichao on 2018-11-19.
 */
@Service
@Transactional
public class ModelAttrServiceImpl implements ModelAttrService {


    @Override
    public void saveModelAttr(List<ModelAttr> list) {

    }

    @Override
    public List<ModelAttr> queryByModelId(Integer modelId) {
        return null;
    }

    @Override
    public void deleteByModelId(Integer modelId) {

    }
}
