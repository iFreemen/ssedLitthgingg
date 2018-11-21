package com.heqichao.springBootDemo.module.mapper;
import com.heqichao.springBootDemo.module.entity.Model;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-7-15.
 */
public interface ModelMapper {

    @Update("<script>"
            +"delete from model where id = #{modelID} "
            +"</script>")
    int deleteByModelId(@Param("modelID") Integer modelID);

    @Select("<script>"
            +"select * from model  where id = #{modelID}   "
            +"</script>")
     List<Model> queryByModelId(@Param("modelID") Integer modelID);

    @Insert("<script>"
            +"insert into model (id,add_date,udp_date,add_uid,udp_uid,model_name) values (#{id},#{add_date},#{udp_date},#{add_uid},#{udp_uid},#{model_name}）"
            +"</script>")
    int saveModel(Model model);

    @Update("<script>"
            +"update model set model_name =#{modelName},udp_uid=#{udpId},udp_date=#{udpDate} where id=#{modelID}"
            +"</script>")
    void updateModelName(@Param("modelID") Integer modelID, @Param("modelName") String modelName, @Param("udpId") Integer udpId, @Param("udpDate") Date udpDate);

}
