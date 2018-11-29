package com.heqichao.springBootDemo.module.control;

import com.alibaba.fastjson.JSONArray;
import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import com.heqichao.springBootDemo.module.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by heqichao on 2018-11-19.
 */
@RestController
@RequestMapping(value = "/service")
public class ModelController extends BaseController{

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelAttrService modelAttrService;

    private  Logger logger = LoggerFactory.getLogger(ModelController.class);

    @RequestMapping(value = "/queryModelType")
    ResponeResult queryTyps() {
        return new ResponeResult(modelService.queryType());
    }

    @RequestMapping(value = "/deleteModelAttr")
    ResponeResult deleteModelAttr() {
        ResponeResult responeResult =new ResponeResult();
        Integer id = getIntegerParam("id");
        if(id!=null){
            modelAttrService.deleteByAttrId(id);
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("删除数据异常，请稍后重试");
        }
        return responeResult;
    }

    @RequestMapping(value = "/saveOrUpdateModel")
    ResponeResult saveOrUpdateModel(){
        Map map = getParamMap();
        Integer modelId =getIntegerParam("id");
        String modelName = (String) map.get("modelName");
        List<Map> list =new ArrayList<>();
        String listJson =(String) map.get("list");
        try{
            list = JSONArray.parseArray(listJson,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponeResult(modelService.saveOrUpdateModel(modelId,modelName,list));
    }

    @RequestMapping(value = "/queryAllByModelId")
    ResponeResult queryAllByModelId(){
        ResponeResult responeResult =new ResponeResult();
        Integer modelId = getIntegerParam("modelId");
        if(modelId!=null){
            responeResult.setResultObj(modelService.queryModelAndAttrsByModelId(modelId));
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("获取数据异常，请稍后重试");
        }
        return responeResult;
    }

    /**
     * 分页查询模板列表
     * @return
     */
    @RequestMapping(value = "/queryModelList")
    ResponeResult queryModelList(){
        Map map = getParamMap();
        return new ResponeResult(modelService.queryUserPageModel((String) map.get("model_name")));
    }

    @RequestMapping(value = "/deleteModel")
    ResponeResult deleteModel() {
        ResponeResult responeResult =new ResponeResult();
        Map map = getParamMap();
        Integer id = getIntegerParam("id");
        if(id!=null){
            modelService.deleteByModelId(id);
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("删除模板数据异常，请稍后重试");
        }
        return responeResult;
    }

    /**
     * 查找当前用户所拥有的模板(用户设备选择模板)
     * @return
     */
    @RequestMapping(value = "/queryUserModel")
    ResponeResult queryUserModel(){
        return new ResponeResult(modelService.queryUserModel());
    }
}
