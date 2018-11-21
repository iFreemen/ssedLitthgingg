package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-7-15.
 */
public interface ModelAttrMapper {

    @Update("<script>"
            +"delete from model_attr where model_id = #{modelID} "
            +"</script>")
    int deleteByModelId( @Param("modelID") String modelID);

    @Select("<script>"
            +"select * from model_attr  where model_id = #{modelID}  order by order_no"
            +"</script>")
     List<ModelAttr> queryByModelId( @Param("modelID") String modelID);

    @Insert("<script>"
            +"insert into model_attr (id,add_date,udp_date,add_uid,udp_uid,model_id,attr_name,data_type,value_type,number_format,unit,expression,order_no) values "
            + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
                    + "(#{o.id},#{o.add_date},#{o.udp_date},#{o.add_uid},#{o.udp_uid},#{o.model_id},#{o.attr_name},#{o.data_type},#{o.value_type},"
                    + "#{o.number_format},#{o.unit}, #{o.expression}, #{o.order_no}）"
            + "</foreach>"
            +"</script>")
    int saveModelAttr(@Param("list") List<Model> list);


}
