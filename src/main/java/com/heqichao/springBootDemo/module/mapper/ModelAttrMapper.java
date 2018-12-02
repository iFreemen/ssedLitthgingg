package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by heqichao on 2018-7-15.
 */
public interface ModelAttrMapper {

    @Delete("<script>"
            +"delete from model_attr where model_id = #{modelID} "
            +"</script>")
    int deleteByModelId( @Param("modelID") Integer modelID);

    @Delete("<script>"
            +"delete from model_attr where id = #{id} "
            +"</script>")
    int deleteById(@Param("id") Integer id);

    @Select("<script>"
            +"select * from model_attr  where model_id = #{modelID}  order by order_no"
            +"</script>")
    List<ModelAttr> queryByModelId(@Param("modelID") Integer modelID);

    @Insert("<script>"
            +"insert into model_attr (add_date,udp_date,add_uid,udp_uid,model_id,attr_name,data_type,value_type,number_format,unit,expression,order_no) values "
            + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
            + "(#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.modelId},#{o.attrName},#{o.dataType},#{o.valueType},#{o.numberFormat},#{o.unit},#{o.expression},#{o.orderNo})"
            + "</foreach>"
            +"</script>")
    int saveModelAttr(@Param("list") List<ModelAttr> list);


}
