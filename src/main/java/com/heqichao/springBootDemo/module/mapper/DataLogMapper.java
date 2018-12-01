package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.DataLog;
import com.heqichao.springBootDemo.module.entity.LightningLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by heqichao on 2018-11-28.
 */
public interface DataLogMapper {

    @Insert(
            "<script>"
            +" insert into data_log (add_date,udp_date,add_uid,udp_uid,dev_id,src_data,data,main_data,data_status) " +
                    " values(#{addDate},#{udpDate},#{addUid},#{udpUid},#{devId},#{srcData},#{data},#{mainData},#{dataStatus}) "
            +"</script>"
    )
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int save(DataLog log);

    @Update(
            "<script>"
            + "update data_log set  data_status = #{status} where dev_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "</script>")
    void updateStatus( @Param("status")String status ,@Param("list") List<String> list);
}