package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.module.entity.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-19.
 */
public interface ModelService {
    Integer saveOrUpdateModel(Integer modelId,String modelName,List<Map> attrs);
    Map queryModelAndAttrsByModelId(Integer modelId);
    void deleteByModelId(Integer modelId);

    /**
     * 获取数据类型和数值类型数据
     * @return
     */
    Map queryType();

    /**
     * 分页查询模板列表
     * @param modelName 模糊查询：模板名称
     * @return
     */
    PageInfo queryUserPageModel(String modelName);

    /**
     * 查找当前用户所拥有的模板
     * @return
     */
//    List<Map> queryUserModel();
    Map<String, Integer> queryUserModel();
}
