package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.*;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.ModelMapper;
import com.heqichao.springBootDemo.module.model.AttrEnum;
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
    public Integer saveOrUpdateModel(Integer modelId,String modelName,List<Map> attrs,String deleteIds) {
        Date date = new Date();
        if(!UserUtil.hasCRDPermission() ){
            throw new ResponeException("没有该权限操作！");
        }
        if(StringUtil.isEmpty(modelName)){
            throw new ResponeException("请输入模板名称！");
        }
        Integer count = modelMapper.queryCountByModelName(modelId,modelName);
        if(count>0){
            throw new ResponeException("模板名称 :"+modelName+" 已存在！");
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
        if(attrs!=null && attrs.size()>0){
            checkHeartBeatProxy(attrs);
            List<ModelAttr> saveList =new ArrayList<>();
            List<ModelAttr> updateList =new ArrayList<>();
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
                if(modelAttr.getId()==null){
                    saveList.add(modelAttr);
                }else{
                    updateList.add(modelAttr);
                }
            }
            modelAttrService.saveModelAttr(saveList);
            modelAttrService.updateModelAttr(updateList);

        }
        if(StringUtil.isNotEmpty(deleteIds)){
            String[] ids =deleteIds.split(",");
            List<Integer> intIds =new ArrayList<>();
            try{
                if(ids!=null && ids.length>0){
                    for(String id :ids){
                        Integer i =Integer.parseInt(id);
                        intIds.add(i);
                    }
                }
                modelAttrService.deleteByAttrId(intIds);
            }catch (Exception e){}

        }
        return modelId;
    }

    /**
     * 检查是否为心跳协议
     */
    private void checkHeartBeatProxy(List<Map> attrs){
        if(attrs!=null && attrs.size()==1){
            String valueType = (String) attrs.get(0).get("valueType");
            String dataType = (String) attrs.get(0).get("dataType");
            String mess ="当前模板格式与心跳协议冲突！请重新设置";
            if(AttrEnum.ALARM_TYPE.getType().equals(dataType) || AttrEnum.SWITCH_TYPE.getType().equals(dataType)){
                throw new ResponeException(mess);
            }else if(AttrEnum.INT_TYPE__TWO_SIGNED.getType().equals(dataType)){
                if(AttrEnum.INT_TYPE__TWO_SIGNED.getSubType().equals(valueType) || AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType().equals(valueType)){
                    throw new ResponeException(mess);
                }
            }
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
        PageInfo pageInfo =null;
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                PageUtil.setPage();
                pageInfo=new PageInfo(modelMapper.queryAll(modelName));
            }else {
                Integer parentId =equipmentService.getUserParent(userId);
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                userList.add(parentId);
                PageUtil.setPage();
                pageInfo=new PageInfo(modelMapper.queryByUserIds(userList,modelName));
            }
        }
        list=pageInfo.getList();
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
        pageInfo.setList(returnList);
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
    
    // Muzzy
    @Override
    public Map<String, Integer> queryUserAttr() {
    	Map map = RequestContext.getContext().getParamMap();
    	Integer mid = StringUtil.getIntegerByMap(map,"mid");
    	List<ModelAttr> list =new ArrayList<>();
    	list=modelAttrService.queryAttrByModelId(mid);
    	Map<String, Integer> res = new HashMap<>();
    	if(list!=null && list.size()>0){
    		res =  list.stream().collect(
    				Collectors.toMap(ModelAttr::getAttrName,ModelAttr::getId, (k1,k2)->k1)
    				);
    	}
    	return res;
    }
    // End Muzzy

    @Override
    public Map<String,List> queryExportInfo(Integer model) {
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
           List<Map> mapList = modelMapper.queryExportInfo(userList,model);
            if(mapList!=null && mapList.size()>0){
                map = new HashMap<String,List>();
                for(Map m :mapList){
                    //翻译
                    m.put("data_type",ModelUtil.getNameByType((String) m.get("data_type")));
                    m.put("value_type",ModelUtil.getSubeNameByTyp((String) m.get("value_type")));
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

    @Override
    public void saveImport(Map map) {
        if(!UserUtil.hasCRDPermission() ){
            throw new ResponeException("没有该权限操作！");
        }
        if(map==null || map.size()<1){
            return;
        }
        Iterator entries = map.entrySet().iterator();
        ModelService modelService = (ModelService) ApplicationContextUtil.getApplicationContext().getBean("modelServiceImpl");
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String modelName = (String) entry.getKey();
            List<Map> attrList =new ArrayList<>();
            if(entry.getValue() instanceof ArrayList){
                List attrs= (List) entry.getValue();
                if(attrs!=null && attrs.size()>1){
                    //去掉第一行标题
                    for(int i=1;i<attrs.size();i++){
                        List<String> row = (List<String>) attrs.get(i);
                        Map rowMap =  CollectionUtil.listStringTranToMap(row,code,true);
                        attrList.add(rowMap);
                    }
                }
                checkAttr(modelName,attrList);
                modelService.saveOrUpdateModel(null,modelName,attrList,null);
            }
        }
    }



    private void checkAttr(String modelName,List<Map> attrList){
        Map nameMap =new HashMap();
        if(modelName.length()>20){
            throw new ResponeException("模板名称长度不允许超过20："+modelName);
        }

        if(attrList!=null && attrList.size()>0){
            for(int i=0;i<attrList.size();i++){
                Map map =attrList.get(i);
                String name = (String) map.get("attrName");
                if(StringUtil.isEmpty(name)){
                    throw new ResponeException("属性名称不允许为空");
                }
                if(nameMap.get(name)!=null){
                    throw new ResponeException("属性名称不允许重复："+name);
                }
                nameMap.put(name,new Object());
                if(name.length()> 20){
                    throw new ResponeException("属性名称长度不允许超过20："+modelName);
                }

                String dataTypeName = (String) map.get("dataType");
                if(StringUtil.isEmpty(dataTypeName)){
                    throw new ResponeException("数据类型不允许为空");
                }
                String dataType =ModelUtil.getTypeByName(dataTypeName);
                if(StringUtil.isEmpty(dataType)){
                    throw new ResponeException("无效的数据类型："+dataTypeName);
                }else{
                    map.put("dataType",dataType);
                }

                if(AttrEnum.WAVE_TYPE.getType().equals(dataType) && i != attrList.size()-1){
                    throw new ResponeException("波形只能设置在最后！"+dataTypeName);
                }

                String valueTypeName = (String) map.get("valueType");
                if(StringUtil.isNotEmpty(valueTypeName)){
                    String valueType =ModelUtil.getSubTypeByName(valueTypeName);
                    map.put("valueType",valueType);
                }

                String numberFormat = (String) map.get("numberFormat");
                if(StringUtil.isNotEmpty(numberFormat)){
                    try{
                        int num = Integer.parseInt(numberFormat);
                        if(num>9 || num <0){
                            throw new ResponeException("小数位数在0到9之间");
                        }
                        map.put("numberFormat",num);
                    }catch (Exception e){
                        throw new ResponeException("无效的小数位数："+numberFormat);
                    }
                }

                String unit = (String) map.get("unit");
                if(StringUtil.isNotEmpty(unit)){
                    if(unit.length()> 20){
                        throw new ResponeException("单位长度不允许超过20："+unit);
                    }
                }

                String expression = (String) map.get("expression");
                if(StringUtil.isNotEmpty(expression)){
                    if(unit.length()> 20){
                        throw new ResponeException("公式长度不允许超过20："+expression);
                    }
                }

                String memo = (String) map.get("memo");
                if(StringUtil.isNotEmpty(expression)){
                    if(unit.length()> 20){
                        throw new ResponeException("备注长度不允许超过20："+memo);
                    }
                }
            }
        }

    }

}
