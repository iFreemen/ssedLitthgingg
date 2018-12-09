package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.*;

/**
 * Created by heqichao on 2018-11-19.
 */
public class ModelUtil {

    private static  Map typeKeyMap=new HashMap();  //key:name
    private static  Map typeNameMap=new HashMap(); //name:key
    private static  Map subTypeKeyMap=new HashMap(); //name:key
    private static  Map subTypeNmeMap=new HashMap(); //key:name

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

    public static String getNameByType(String key){
        if(StringUtil.isEmpty(key)){
            return "";
        }
        if(typeKeyMap.size()<1){
            getTypeNames();
        }
        return (String) typeKeyMap.get(key);
    }

    public static String getTypeByName(String name){
        if(StringUtil.isEmpty(name)){
            return "";
        }
        if(typeNameMap.size()<1){
            getTypeNames();
        }
        return (String) typeNameMap.get(name);
    }

    public static String getSubeNameByTyp(String key){
        if(StringUtil.isEmpty(key)){
            return "";
        }
        if(subTypeKeyMap.size()<1){
            getIntSubTypeNames();
        }
        return (String) subTypeKeyMap.get(key);
    }

    public static String getSubTypeByName(String key){
        if(StringUtil.isEmpty(key)){
            return "";
        }
        if(subTypeNmeMap.size()<1){
            getIntSubTypeNames();
        }
        return (String) subTypeNmeMap.get(key);
    }

    /**
     * 获取主类型信息
     * @return
     */
    public static Map getTypeNames(){
        if(typeKeyMap.size()<1){
            AttrEnum[] attrs = AttrEnum.values();
            if(attrs!=null && attrs.length>0){
                for(AttrEnum att :attrs){
                    String type =att.getType();
                    String typeName =att.getTypeName();
                    //防重复记录数值类型
                    if(typeKeyMap.get(type)==null){
                        typeKeyMap.put(type,typeName);
                        typeNameMap.put(typeName,type);
                    }
                }
            }
        }
        return typeKeyMap;
    }

    /**
     * 获取数值子类型信息
     * @return
     */
    public static Map getIntSubTypeNames(){
        if(subTypeKeyMap.size()<1){
            AttrEnum[] attrs = AttrEnum.values();
            if(attrs!=null && attrs.length>0){
                for(AttrEnum att :attrs){
                    String type =att.getType();
                    if("INT_TYPE".equals(type)){
                        subTypeKeyMap.put(att.getSubType(),att.getSubTypeName());
                        subTypeNmeMap.put(att.getSubTypeName(),att.getSubType());
                    }
                }
            }
        }

        return subTypeKeyMap;
    }

}
