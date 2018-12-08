package com.heqichao.springBootDemo.module.control;

import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by heqichao on 2018-12-1.
 */
@RestController
@RequestMapping(value = "/service")
public class DataLogController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private ModelAttrService modelAttrService;


    @RequestMapping(value = "/queryEquAttrLog")
    ResponeResult queryEquAttrLog() {
        Map map =new HashMap();
        Map param =getParamMap();
        String devId =(String) param.get("devId");
        String  attrKey=(String) param.get("attrKey");
        String  queryLog=(String) param.get("queryLog");
        //初始化
        //设备列表 初始化设备id为第一个
        if(StringUtil.isEmpty(devId) ){
            List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
            if(devList!=null && devList.size()>0){
                Map<String, String> devMap =devList.get(0);
                devId=devMap.get("dev_id");
            }
            map.put("devList",devList);
        }
        //属性列表 初始化属性key为第一个
        if(StringUtil.isEmpty(attrKey)){
            List<ModelAttr> attrList =new ArrayList<>();
            Equipment equipment  =equipmentService.getEquipmentInfo(devId);
            if(equipment!=null && equipment.getModelId() !=null){
                attrList =modelAttrService.queryByModelId(equipment.getModelId());
                if(attrList!=null && attrList.size()>0){
                    ModelAttr attr =attrList.get(0);
                    attrKey=attr.getAttrName();
                    map.put("unit",attr.getUnit());
                    map.put("dataType",attr.getDataType());
                }
            }
            map.put("attrList",attrList);
        }

        //查找数据 StringUtil.isNotEmpty(queryLog) && "TRUE".equals(queryLog.toUpperCase())
        if(true){
            String start= (String) param.get("start");
            String end= (String) param.get("end");
            if(StringUtil.isNotEmpty(end)){
                end=end+" 23:59:59";
            }
            map.putAll(dataLogService.querqueryEquAttrLog(devId,attrKey,start,end));
        }
        map.put("devId",devId);
        map.put("attrKey",attrKey);

        return new ResponeResult(map);
    }



    @RequestMapping(value = "/deleteAllDataLog")
    ResponeResult deleteAllDataLog() {
        List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
        if(devList!=null && devList.size()>0){
            String[] devids =new String[devList.size()];
            for(int i=0;i<devList.size();i++){
                Map<String, String> devMap=devList.get(i);
                devids[i]=devMap.get("dev_id");
            }
            dataLogService.deleteDataLog(devids);
        }

        return new ResponeResult();
    }
}
