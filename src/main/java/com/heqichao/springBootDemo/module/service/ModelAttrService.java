package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.List;

/**
 * Created by heqichao on 2018-11-19.
 */
public interface ModelAttrService {

    void saveModelAttr(List<ModelAttr> list);
    void updateModelAttr(List<ModelAttr> list);
    List<ModelAttr> queryByModelId(Integer modelId);
    void deleteByModelId(Integer modelId);


    void deleteByAttrId(List<Integer> ids);

	List<ModelAttr> queryAttrByModelId(Integer modelId);

}
