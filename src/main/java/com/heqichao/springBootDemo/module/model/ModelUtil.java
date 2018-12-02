package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.*;

/**
 * Created by heqichao on 2018-11-19.
 */
public class ModelUtil {

    /**
     * 判断模板是否有使用波形
     * @param list
     * @return
     */
    public static boolean hasWaveType(List<ModelAttr> list){
        if(list!=null && list.size()>0){
            for(ModelAttr attr :list){
                if(AttrEnum.WAVE_TYPE.getType().equals(attr.getDataType())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据数据属性进行转译
     * @param attr
     * @param data
     * @return
     */
    public static String getData(ModelAttr attr, String data){
        String result="";
        if(attr==null || StringUtil.isEmpty(attr.getDataType()) || StringUtil.isEmpty(data)){
            return result;
        }
        AttrEnum attrEnum =AttrEnum.getAttrByType(attr.getDataType(),attr.getValueType());
        if(attrEnum == null){
            return result;
        }
        result= attrEnum.execute(attr, data);
        //加单位
       /* if(StringUtil.isNotEmpty(attr.getUnit())){
            result=result+attr.getUnit();
        }*/
        return result;
    }

    /**
     * 获取主类型信息
     * @return
     */
    public static Map getTypeNames(){
        AttrEnum[] attrs = AttrEnum.values();
        Map keyMap=new HashMap();
        if(attrs!=null && attrs.length>0){
            for(AttrEnum att :attrs){
                String type =att.getType();
                String typeName =att.getTypeName();
                //防重复记录数值类型
                if(keyMap.get(type)==null){
                    keyMap.put(type,typeName);
                }
            }
        }
        return keyMap;
    }

    /**
     * 获取数值子类型信息
     * @return
     */
    public static Map getIntSubTypeNames(){
        AttrEnum[] attrs = AttrEnum.values();
        Map keyMap=new HashMap();
        if(attrs!=null && attrs.length>0){
            for(AttrEnum att :attrs){
                String type =att.getType();
                if("INT_TYPE".equals(type)){
                    keyMap.put(att.getSubType(),att.getSubTypeName());
                }
            }
        }
        return keyMap;
    }

}
