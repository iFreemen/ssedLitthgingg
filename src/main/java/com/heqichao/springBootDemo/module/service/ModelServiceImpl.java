package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.*;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.ModelMapper;
import com.heqichao.springBootDemo.module.model.ModelUtil;
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

    @Autowired
    private EquipmentService equipmentService;

    @Override
    public Integer saveOrUpdateModel(Integer modelId,String modelName,List<Map> attrs) {
        Date date = new Date();
        if(!UserUtil.hasCRDPermission() ){
            throw new ResponeException("没有该权限操作！");
        }

        Integer userId =ServletUtil.getSessionUser().getId();
        //保存
        Model model =new Model(modelName,userId,date);
        if(modelId==null){
             modelMapper.saveModel(model);
             modelId=model.getId();
        }else{
            //已存在更新名称
            modelMapper.updateModelName(modelId,modelName,userId,date);
        }
        //删除所有的属性重新保存
        modelAttrService.deleteByModelId(modelId);
        if(attrs!=null && attrs.size()>0){
            List<ModelAttr> list =new ArrayList<>();
            for(int i=0;i< attrs.size();i++){
                Map map=attrs.get(i);
                ModelAttr modelAttr =new ModelAttr();
                BeanUtil.copyProperties(modelAttr,map);
                modelAttr.setOrderNo(i+1);
                modelAttr.setModelId(modelId);
                modelAttr.setAddDate(date);
                modelAttr.setAddUid(userId);
                modelAttr.setUdpDate(date);
                modelAttr.setUdpUid(userId);
                list.add(modelAttr);
            }
            modelAttrService.saveModelAttr(list);
        }
        return modelId;
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
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        if(modelId!=null){
            modelMapper.deleteByModelId(modelId);
            //删除所有的属性
            modelAttrService.deleteByModelId(modelId);
        }
    }

    @Override
    public Map queryType() {
        Map map =new HashMap();
        map.put("TYPE",ModelUtil.getTypeNames());
        map.put("SUBTYPE",ModelUtil.getIntSubTypeNames());
        return map;
    }

    @Override
    public PageInfo queryUserPageModel(String modelName) {
        List<Model> list =new ArrayList<>();
        List<Map> returnList =new ArrayList<>();
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        Integer userId =ServletUtil.getSessionUser().getId();
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                PageUtil.setPage();
                list=modelMapper.queryAll(modelName);
            }else {
                Integer parentId =equipmentService.getUserParent(userId);
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                userList.add(parentId);
                PageUtil.setPage();
                list= modelMapper.queryByUserIds(userList,modelName);
            }
        }
        if(list!=null && list.size()>0){
            //补充人员名字
            for(Model model :list){
                Map map =new HashMap();
                BeanUtil.copyProperties(map,model);
                map.put("addName",UserCache.getUserName(model.getAddUid()));
                map.put("udpName",UserCache.getUserName(model.getUdpUid()));
                returnList.add(map);
            }
        }

        PageInfo pageInfo = new PageInfo(returnList);
        return pageInfo;
    }

    @Override
    public List<Map> queryUserModel() {
        List<Model> list =new ArrayList<>();
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        Integer userId =ServletUtil.getSessionUser().getId();
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                PageUtil.setPage();
                list=modelMapper.queryAll("");
            }else {
                Integer parentId =equipmentService.getUserParent(userId);
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                userList.add(parentId);
                PageUtil.setPage();
                list= modelMapper.queryByUserIds(userList,"");
            }
        }
        List<Map> returnList =new ArrayList<>();
        if(list!=null && list.size()>0){
            for(Model model : list){
                Map map =new HashMap();
                map.put(model.getId(),model.getModelName());
                returnList.add(map);
            }
        }
        return returnList;
    }
}
