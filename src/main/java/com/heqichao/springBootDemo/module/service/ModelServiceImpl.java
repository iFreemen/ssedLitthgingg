package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.User;
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
import java.util.stream.Collectors;

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
    public Map<String, Integer> queryUserModel() {
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
        // Edit By Muzzy
//        List<Map> returnList =new ArrayList<>();
//        if(list!=null && list.size()>0){
//            for(Model model : list){
//                Map map =new HashMap();
//                map.put(model.getId(),model.getModelName());
//                returnList.add(map);
//            }
//        }
//        return returnList;
        Map<String, Integer> res = new HashMap<>();
        if(list!=null && list.size()>0){
        	res =  list.stream().collect(
				Collectors.toMap(Model::getModelName,Model::getId, (k1,k2)->k1)
			);
        }
        return res;
        // End Muzzy
    }

    @Override
    public Map<String,List> queryExportInfo() {
       Map<String,List> map =null;
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        Integer userId =ServletUtil.getSessionUser().getId();
        List<Integer> userList =new ArrayList<>();
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                userList=null;
            }else {
                Integer parentId =equipmentService.getUserParent(userId);
                userList.add(userId);
                userList.add(parentId);
            }
           List<Map> mapList = modelMapper.queryExportInfo(userList);
            if(mapList!=null && mapList.size()>0){
                map = new HashMap<String,List>();
                for(Map m :mapList){
                    //翻译
                    m.put("data_type",ModelUtil.getTypeName((String) m.get("data_type")));
                    m.put("value_type",ModelUtil.getSubTypeName((String) m.get("value_type")));
                    String modelId = (String) m.get("model_id");
                    String modelName = (String) m.get("model_name");
                    if(map.get(modelId+EXCEL_NAME_SPLIT+modelName) == null){
                        map.put(modelId+EXCEL_NAME_SPLIT+modelName,new ArrayList<Map>());
                    }
                    map.get(modelId+EXCEL_NAME_SPLIT+modelName).add(m);
                }
            }
        }
        return map;
    }
}
