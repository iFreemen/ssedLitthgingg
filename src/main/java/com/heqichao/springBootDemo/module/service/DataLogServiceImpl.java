package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.Base64Encrypt;
import com.heqichao.springBootDemo.base.util.MathUtil;
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
    public void saveDataLog(String devId,String data, String srcData,String devType){


        if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(data)){
            Date date =new Date();
            //去除前后的主数据
            String mainData="";
            DataLog dataLog =new DataLog();
            dataLog.setSrcData(srcData);
            dataLog.setData(data);
            dataLog.setAddDate(date);
            dataLog.setUdpDate(date);
            dataLog.setDevId(devId);
            dataLog.setDevType(devType);
            List<DataDetail> dataDetails =new ArrayList<>();
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
                return;
            }

            try{
                //默认去除前面没用3字节
                int frontByte =3;
                //去除后面没用2字节
                int backByte=2;

                //判断数据是否有波形 aabbccddeeffgghh
                //1. 先获取cc的值（数据区长度校验）
                Integer dtaLenInt = Math.toIntExact(Long.parseLong(data.substring(4,6), 16));
                //2.获取数据区字节长度 ddeeff（减去开头跟结尾）
                int dataSrcLen =(data.length()-3*2-2*2)/2;
                if(dtaLenInt==dataSrcLen){
                    //无波形
                }else {
                    //3.取ccdd的值
                    dtaLenInt = Math.toIntExact(Long.parseLong(data.substring(4,8), 16));
                    //4..获取数据区字节长度 eeff（减去开头跟结尾）
                    dataSrcLen =(data.length()-4*2-2*2)/2;
                    if(dtaLenInt==dataSrcLen){
                        //有波形的 去除前面没用4字节
                        frontByte=4;
                    }else{
                        //异常数据
                        dataLog.setDataStatus(DataLogService.ERROR_STATUS);
                        return;
                    }
                }

                //去除前后的主数据
                mainData=data.substring(frontByte*2,data.length()-backByte*2);
                dataLog.setMainData(mainData);
                dataLog.setDataStatus(DataLogService.ENABLE_STATUS);
                String content="";
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
                    dataDetails.add(dataDetail);
                }

            }catch (Exception e){
                dataLog.setDataStatus(DataLogService.ERROR_STATUS);
            }finally {
                dataLogMapper.save(dataLog);
                if(dataDetails.size()>0){
                    for(DataDetail dataDetail :dataDetails){
                        dataDetail.setLogId(dataLog.getId());
                    }
                    dataDetailMapper.save(dataDetails);
                }
            }

        }
    }


    @Override
    public void saveDataLog(String devId, String srcData,String devType) {
        //转译数据
        if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(srcData)){
            String[] transDatas = Base64Encrypt.decodeToHexStr(srcData);
            if(transDatas==null || transDatas.length<1){
                return;
            }
            String data=StringUtil.getString(transDatas,"");
            DataLogService dataLogService= (DataLogService) ApplicationContextUtil.getApplicationContext().getBean("dataLogServiceImpl");
            dataLogService.saveDataLog(devId,data,srcData,devType);
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
