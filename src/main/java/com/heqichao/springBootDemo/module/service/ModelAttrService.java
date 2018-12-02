package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.List;
import java.util.Set;

/**
 * Created by heqichao on 2018-11-19.
 */
public interface ModelAttrService {
    void saveModelAttr(List<ModelAttr> list);
    List<ModelAttr> queryByModelId(Integer modelId);
    void deleteByModelId(Integer modelId);

    void deleteByAttrId(Integer id);
}
