package com.heqichao.springBootDemo.module.mapper;
import com.heqichao.springBootDemo.module.entity.Model;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-7-15.
 */
public interface ModelMapper {

    @Delete("<script>"
            +"delete from model where id = #{modelID} "
            +"</script>")
    int deleteByModelId(@Param("modelID") Integer modelID);

    @Select("<script>"
            +"select * from model  where id = #{modelID}   "
            +"</script>")
     List<Model> queryByModelId(@Param("modelID") Integer modelID);

    @Insert("<script>"
            +"insert into model (add_date,udp_date,add_uid,udp_uid,model_name) values (#{addDate},#{udpDate},#{addUid},#{udpUid},#{modelName})"
            +"</script>")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int saveModel(Model model);

    @Update("<script>"
            +"update model set model_name =#{modelName},udp_uid=#{udpId},udp_date=#{udpDate} where id=#{modelID}"
            +"</script>")
    void updateModelName(@Param("modelID") Integer modelID, @Param("modelName") String modelName, @Param("udpId") Integer udpId, @Param("udpDate") Date udpDate);

    @Select("<script>"
            +"select * from model  where 1=1  "
            + "<if test =\"addUid !=null  and addUid !='' \"> and add_uid = #{addUid}  </if>"  // 用户只查自己 管理员查全部
            + "<if test =\"modelName !=null  and modelName!='' \"> and model_name like CONCAT(CONCAT('%',#{modelName}),'%')  </if>"
            +"</script>")
    List<Model> queryByUserId(@Param("addUid") Integer addUid,@Param("modelName")String modelName);
}
