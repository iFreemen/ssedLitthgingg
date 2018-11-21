package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.base.util.MathUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by heqichao on 2018-11-20.
 */
public enum AttrEnum implements AttrAnalyze{


    INT_TYPE__TWO_UNSIGNED("INT_TYPE","数值型","TWO_UNSIGNED","2字节无符号整数",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data = MathUtil.convertTwoBytesToUnSignedInt(srcContext[0]+"",srcContext[1]+"");
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },

    INT_TYPE__TWO_SIGNED("INT_TYPE","数值型","TWO_SIGNED","2字节有符号整数",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data=MathUtil.convertTwoBytesToSignedInt(srcContext[0]+"",srcContext[1]+"");
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },

    INT_TYPE__FOUR_NOSIGNED_ABCD("INT_TYPE","数值型","FOUR_NOSIGNED_ABCD","4字节无符号整数(AB CD)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data=MathUtil.convertFourBytesToUnSignedInt(srcContext[0]+"",srcContext[1]+"",srcContext[2]+"",srcContext[3]+"");
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },
    INT_TYPE__FOUR_NOSIGNED_CDAB("INT_TYPE","数值型","FOUR_NOSIGNED_CDAB","4字节无符号整数(CD AB)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data=MathUtil.convertFourBytesToUnSignedInt(srcContext[2]+"",srcContext[3]+"",srcContext[0]+"",srcContext[1]+"");
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },

    INT_TYPE__FOUR_SIGNED_ABCD("INT_TYPE","数值型","FOUR_SIGNED_ABCD","4字节有符号整数(AB CD)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data=MathUtil.convertFourBytesToSignedInt(srcContext[0]+"",srcContext[1]+"",srcContext[2]+"",srcContext[3]+"");
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },

    INT_TYPE__FOUR_SIGNED_CDAB("INT_TYPE","数值型","FOUR_SIGNED_CDAB","4字节有符号整数(CD AB)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data=MathUtil.convertFourBytesToSignedInt(srcContext[2]+"",srcContext[3]+"",srcContext[0]+"",srcContext[1]+"");
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },
    INT_TYPE__FOUR_FLOAT_ABCD("INT_TYPE","数值型","FOUR_FLOAT_ABCD","4字节浮点型(AB CD)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data= MathUtil.getFloat(""+srcContext[0]+srcContext[1]+srcContext[2]+srcContext[3]);
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },
    INT_TYPE__FOUR_FLOAT_CDAB("INT_TYPE","数值型","FOUR_FLOAT_CDAB","4字节浮点型(CD AB)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                String data= MathUtil.getFloat(""+srcContext[3]+srcContext[4]+srcContext[0]+srcContext[1]);
                result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumber_format(),data);
            }
            return result;
        }
    },

    ALARM_TYPE("ALARM_TYPE","报警型",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                if("0".equals(srcContext[0]) && "0".equals(srcContext[1])){
                    result="正常";
                }else if("0".equals(srcContext[0]) && "1".equals(srcContext[1])){
                    result="报警";
                }else{
                    result=DATA_ERROR;
                }
            }
            return result;
        }
    },

    DATE_TYPE("DATE_TYPE","时间型",6){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                result= "20"+srcContext[0]+"-"+srcContext[1]+"-"+srcContext[2]+" "+srcContext[3]+":"+srcContext[4]+":"+srcContext[5];
            }
            return result;
        }
    },

    SWITCH_TYPE("SWITCH_TYPE","开关型",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            if(!check(context)){
                result=DATA_ERROR;
            }else{
                char[] srcContext = context.toCharArray();
                if("0".equals(srcContext[0]) && "0".equals(srcContext[1])){
                    result="开";
                }else if("0".equals(srcContext[0]) && "1".equals(srcContext[1])){
                    result="关";
                }else{
                    result=DATA_ERROR;
                }
            }
            return result;
        }
    },

    WAVE_TYPE("WAVE_TYPE","雷击波形",null){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            return null;
        }
    }
    ;

    //主类型
    private String type;
    //子类型
    private String subType;
    //主类型名字
    private String typeName;
    //子类型名字
    private String subTypeName;
    //解析字节长度
    private Integer length;

    public Integer getLength() {
        return length;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    //异常数据
    private static final String DATA_ERROR="DATA_ERROR";
    private static Map<String,AttrEnum> typeEnumMap=new HashMap<String,AttrEnum>();

    static Logger logger = LoggerFactory.getLogger(AttrEnum.class);
    private AttrEnum(String type,String typeName,String subType,String subTypeName,Integer length){
        this.type=type;
        this.subType=subType;
        this.subTypeName=subTypeName;
        this.typeName=typeName;
        this.length=length;
    }
    private AttrEnum(String type,String typeName,Integer length){
        this.type=type;
        this.subType=null;
        this.subTypeName=null;
        this.typeName=typeName;
        this.length=length;
    }

    public static void main(String[] args) {
        System.out.println( Float.intBitsToFloat(Integer.parseInt("3F9D70A4",16)));
        System.out.println( Float.intBitsToFloat(Integer.parseInt("A4709D3F",16)));
    }

    boolean check(String context){
        boolean flag =true;
        if(StringUtil.isEmpty(context)){
            flag = false;
        }else{
            if(this.getLength() != null){
                if(context.length() != this.getLength()){
                    flag=false;
                }
            }
        }
        return flag;
    }

    /**
     * 根据key获取属性枚举
     * @return
     */
    public static AttrEnum getAttrByType(String type,String subType){
        if(StringUtil.isEmpty(type)){
            return null;
        }
        String key =type;
        if(StringUtil.isNotEmpty(subType)){
            key=type+"__"+subType;
        }
        return AttrEnum.valueOf(key);
    }



}
