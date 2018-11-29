package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.util.Base64Encrypt;
import com.heqichao.springBootDemo.base.util.StringUtil;
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

    @Override
    public void saveDataLog(String devId, String srcData) {
        Date date =new Date();
        if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(srcData)){
            //先查找该设备所绑定的模板属性
            Integer modId=null;
            if(modId !=null){
                //没有数据或模板则返回
                Set<ModelAttr> attrList =modelAttrService.queryByModelId(modId);
                if(attrList==null || attrList.size()<1){
                    return;
                }
                //转译数据
                String[] transDatas = Base64Encrypt.decodeToHexStr(srcData);
                if(transDatas==null || transDatas.length<1){
                    return;
                }
                String data=StringUtil.getString(transDatas,"");
                //默认去除前面没用3字节
                int frontByte =3;
                //去除后面没用2字节
                int backByte=2;
                //如果是有波形的 前面4字节
                if(ModelUtil.hasWaveType(attrList)){
                    frontByte=4;
                }
                //去除前后的主数据
                String mainData=data.substring(frontByte*2-1,data.length()-backByte*2);
                DataLog dataLog =new DataLog();
                dataLog.setSrcData(srcData);
                dataLog.setData(data);
                dataLog.setMainData(mainData);
                dataLog.setAddDate(date);
                dataLog.setUdpDate(date);
                dataLog.setDevId(devId);
                dataLog.setDataStatus(DataLogService.ENABLE_STATUS);
                dataLogMapper.save(dataLog);
                int logId =dataLog.getId();
                String content="";
                List<DataDetail> dataDetails =new ArrayList<>();
                for(ModelAttr attr:attrList){
                    AttrEnum attrEnum =AttrEnum.getAttrByType(attr.getDataType(),attr.getValueType());
                    if(attrEnum==null){
                        continue;
                    }
                    if(!attrEnum.getType().equals(AttrEnum.WAVE_TYPE.getType())){
                        //如果不是波形 要截取对应长度
                        content=mainData.substring(0,attrEnum.getLength()*2);
                        mainData=mainData.substring(attrEnum.getLength()*2-1,mainData.length());
                    }else{
                        content=mainData;
                    }
                    //计算
                    String res =ModelUtil.getData(attr,content);
                    DataDetail dataDetail =new DataDetail();
                    dataDetail.setAddDate(date);
                    dataDetail.setUdpDate(date);
                    dataDetail.setDataKey(attr.getAttrName());
                    dataDetail.setDataType(attrEnum.getSubType());
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
    }

    @Override
    public List<DataDetail> queryDataDetail(String devId, String key, String startTime, String endTime) {
        return dataDetailMapper.queryDetail(devId,key,ENABLE_STATUS,startTime,endTime);
    }

    @Override
    public void deleteDataLog(String... devId) {
        if(devId!=null && devId.length>0){
           List<String > ids = Arrays.asList(devId);
            dataLogMapper.updateStatus(UN_ENABLE_STATUS,ids);
            dataDetailMapper.updateStatus(UN_ENABLE_STATUS,ids);
        }
    }
}
