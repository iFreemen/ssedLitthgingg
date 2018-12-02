package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.Base64Encrypt;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.base.util.UserUtil;
import com.heqichao.springBootDemo.module.entity.DataDetail;
import com.heqichao.springBootDemo.module.entity.DataLog;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.DataDetailMapper;
import com.heqichao.springBootDemo.module.mapper.DataLogMapper;
import com.heqichao.springBootDemo.module.model.AttrEnum;
import com.heqichao.springBootDemo.module.model.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by heqichao on 2018-11-28.
 */
@Service
@Transactional
public class DataLogServiceImpl implements DataLogService {

    @Autowired
    private ModelAttrService modelAttrService;
    @Autowired
    private DataLogMapper dataLogMapper;
    @Autowired
    private DataDetailMapper dataDetailMapper;

    @Autowired
    private EquipmentService equipmentService;


    @Autowired
    private UserService userService;

    @Override
    public void saveDataLog(String devId, String srcData) {
        Date date =new Date();
        //去除前后的主数据
        String mainData="";
        if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(srcData)){
            //转译数据
            String[] transDatas = Base64Encrypt.decodeToHexStr(srcData);
            if(transDatas==null || transDatas.length<1){
                return;
            }
            String data=StringUtil.getString(transDatas,"");
            DataLog dataLog =new DataLog();
            dataLog.setSrcData(srcData);
            dataLog.setData(data);
            dataLog.setAddDate(date);
            dataLog.setUdpDate(date);
            dataLog.setDevId(devId);

            //先查找该设备所绑定的模板属性
            Equipment equipment = equipmentService.getEquipmentInfo(devId);
            Integer modId=null;
            List<ModelAttr> attrList =new ArrayList<>();
            if(equipment!=null && equipment.getModelId() !=null){
                modId= equipment.getModelId();
                attrList = modelAttrService.queryByModelId(modId);
            }
            if(equipment==null || equipment.getModelId() ==null || attrList == null || attrList.size() < 1) {
                //没有数据或模板则返回
                dataLog.setDataStatus(DataLogService.ERROR_STATUS);
                dataLogMapper.save(dataLog);
                return;
            }
            //默认去除前面没用3字节
            int frontByte =3;
            //去除后面没用2字节
            int backByte=2;
            //如果是有波形的 前面4字节
            if(ModelUtil.hasWaveType(attrList)){
                frontByte=4;
            }
            //去除前后的主数据
            mainData=data.substring(frontByte*2,data.length()-backByte*2);
            dataLog.setMainData(mainData);
            dataLog.setDataStatus(DataLogService.ENABLE_STATUS);
            dataLogMapper.save(dataLog);

            int logId =dataLog.getId();
            String content="";
            List<DataDetail> dataDetails =new ArrayList<>();
            for(int i=0;i<attrList.size();i++){
                ModelAttr attr=attrList.get(i);
                AttrEnum attrEnum =AttrEnum.getAttrByType(attr.getDataType(),attr.getValueType());
                if(attrEnum==null){
                    continue;
                }
                if(!attrEnum.getType().equals(AttrEnum.WAVE_TYPE.getType())){
                    //如果不是波形 要截取对应长度
                    content=mainData.substring(0,attrEnum.getLength()*2);
                    mainData=mainData.substring(attrEnum.getLength()*2,mainData.length());
                }else{
                    content=mainData;
                }
                DataDetail dataDetail =new DataDetail();
                dataDetail.setOrderNo(i);
                dataDetail.setDataSrc(content);
                //计算
                String res =ModelUtil.getData(attr,content);
                dataDetail.setAddDate(date);
                dataDetail.setUdpDate(date);
                dataDetail.setDataName(attr.getAttrName());
                dataDetail.setDataType(attrEnum.getType());
                dataDetail.setDataValue(res);
                dataDetail.setDevId(devId);
                dataDetail.setUnit(attr.getUnit());
                dataDetail.setDataStatus(DataLogService.ENABLE_STATUS);
                dataDetail.setLogId(logId);
                dataDetails.add(dataDetail);
            }
            dataDetailMapper.save(dataDetails);

        }
    }

    @Override
    public List<DataDetail> queryDataDetail(String devId, String key, String startTime, String endTime) {
        return dataDetailMapper.queryDetail(devId,key,ENABLE_STATUS,startTime,endTime);
    }

    @Override
    public void deleteDataLog(String... devId) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        if(devId!=null && devId.length>0){
           List<String > ids = Arrays.asList(devId);
            dataLogMapper.updateStatus(UN_ENABLE_STATUS,ids);
            dataDetailMapper.updateStatus(UN_ENABLE_STATUS,ids);
        }
    }

    @Override
    public Map querqueryEquAttrLog(String devId, String attrKey, String startTime, String endTime) {
        Map map= new HashMap();
        if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(attrKey)){
            map.put("log",queryDataDetail(devId, attrKey, startTime, endTime));
            Equipment equipment =equipmentService.getEquipmentInfo(devId);
            if(equipment.getUid()!=null){
                User user = userService.querById(equipment.getUid());
                if(user!=null){
                    map.put("equipUserName",user.getAccount());
                }else{
                    map.put("equipUserName","");
                }
            }
            map.put("equip",equipment);
        }
        return map;
    }
}
