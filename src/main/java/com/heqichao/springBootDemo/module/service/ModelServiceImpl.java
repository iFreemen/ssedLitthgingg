package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.util.BeanUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by heqichao on 2018-11-19.
 */
@Service
@Transactional
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelAttrService modelAttrService;

    @Override
    public void saveOrUpdateModel(Integer modelId,String modelName,List<Map> attrs) {
        Date date = new Date();
        Integer userId =ServletUtil.getSessionUser().getId();
        //保存
        if(modelId!=null){
            Model model =new Model(modelName,userId,date);
            modelId= modelMapper.saveModel(model);
        }else{
            //已存在更新名称
            modelMapper.updateModelName(modelId,modelName,userId,date);
        }
        //删除所有的属性重新保存
        modelAttrService.deleteByModelId(modelId);
        if(attrs!=null && attrs.size()>0){
            List<ModelAttr> list =new ArrayList<>();
            for(Map map:attrs){
                ModelAttr modelAttr =new ModelAttr();
                BeanUtil.copyProperties(modelAttr,map);
                if(modelAttr.getUdpDate()==null){
                    modelAttr.setUdpDate(date);
                }
               if(modelAttr.getUdpUid()==null){
                   modelAttr.setUdpUid(userId);
               }
                modelAttr.setModel_id(modelId);
                list.add(modelAttr);
            }
            modelAttrService.saveModelAttr(list);
        }
    }


    @Override
    public Map queryModelAndAttrsByModelId(Integer modelId) {
        Map map =new HashMap();
        if(modelId!=null){
            List<Model> models =modelMapper.queryByModelId(modelId);
            List<ModelAttr> attrs =modelAttrService.queryByModelId(modelId);
            if(models !=null && models.size()>0){
                map.put("model",models.get(0));
            }else{
                map.put("model",new HashMap<>());
            }
            if(attrs!=null && attrs.size()>0){
                map.put("attrs",attrs);
            }else{
                map.put("attrs",new ArrayList<>());
            }
        }
        return map;
    }

    @Override
    public void deleteByModelId(Integer modelId) {
        if(modelId!=null){
            modelMapper.deleteByModelId(modelId);
            //删除所有的属性
            modelAttrService.deleteByModelId(modelId);
        }
    }
}
