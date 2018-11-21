package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-19.
 */
public interface ModelService {
    void saveOrUpdateModel(Integer modelId,String modelName,List<Map> attrs);
    Map queryModelAndAttrsByModelId(Integer modelId);
    void deleteByModelId(Integer modelId);
}
