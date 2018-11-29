package com.heqichao.springBootDemo.base.control;

import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heqichao on 2018-2-14.
 */
public class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected   Integer getIntegerParam( String key){
        Integer integer =null;
        Map map = RequestContext.getContext().getParamMap();
        if(map!=null && StringUtil.isNotEmpty(key)){
            Object value =map.get(key);
            if(value!=null){
                if(value instanceof Integer){
                    integer= (Integer) value;
                }else if(value instanceof String){
                    if(StringUtil.isNotEmpty((String)value)){
                        try{
                            integer=  Integer.parseInt((String) value);
                        }catch (Exception e){
                            logger.error("getIntegerParam error ! "+key,e);
                            throw  new ResponeException("获取数值型参数有误："+key,e);
                        }
                    }

                }
            }
        }
        return integer;
    }

    protected  Map getParamMap(){
        Map map = RequestContext.getContext().getParamMap();
        if(map==null){
            map =new HashMap();
        }
        return  map;
    }
}
