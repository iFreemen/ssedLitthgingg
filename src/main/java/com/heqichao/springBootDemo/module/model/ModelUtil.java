package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by heqichao on 2018-11-19.
 */
public class ModelUtil {

    /**
     * 根据数据属性进行转译
     * @param attr
     * @param data
     * @return
     */
    public static String getData(ModelAttr attr, String data){
        String result="";
        if(attr==null || StringUtil.isEmpty(attr.getData_type()) || StringUtil.isEmpty(data)){
            return result;
        }
        AttrEnum attrEnum =AttrEnum.getAttrByType(attr.getData_type(),attr.getValue_type());
        if(attrEnum == null){
            return result;
        }
        return attrEnum.execute(attr, data);
    }

    /**
     * 获取主类型信息
     * @return
     */
    public static Set<Map> getTypeNames(){
        AttrEnum[] attrs = AttrEnum.values();
        Set<Map> list =new HashSet<>();
        //防重复记录数值类型
        Map keyMap=new HashMap();
        if(attrs!=null && attrs.length>0){
            for(AttrEnum att :attrs){
                String type =att.getType();
                String typeName =att.getTypeName();
                if(keyMap.get(type)==null){
                    Map m =new HashMap();
                    m.put("type",type);
                    m.put("typeName",typeName);
                    keyMap.put(type,typeName);
                    list.add(m);
                }
            }
        }
        return list;
    }

    /**
     * 获取数值子类型信息
     * @return
     */
    public static Set<Map> getIntSubTypeNames(){
        AttrEnum[] attrs = AttrEnum.values();
        Set<Map> list =new HashSet<>();
        if(attrs!=null && attrs.length>0){
            for(AttrEnum att :attrs){
                String type =att.getType();
                if("INT_TYPE".equals(type)){
                    Map m =new HashMap();
                    m.put("subType",att.getSubType());
                    m.put("subTypeName",att.getSubTypeName());
                    list.add(m);
                }
            }
        }
        return list;
    }

}
