package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.AlarmLog;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-12-16.
 */
public interface AlarmLogMapper {
    @Insert(
            "<script>"
                    +" insert into alarm_log (add_date,udp_date,add_uid,udp_uid,model_id,dev_id,attr_id,setting_id,data_value,unit,data_status,dev_type,alram_type) values"
                    + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
                    +" (#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.modelId},#{o.devId},#{o.attrId},#{o.settingId},#{o.dataValue},#{o.unit},#{o.dataStatus},#{o.devType},#{o.alramType}) "
                    + "</foreach>"
                    +"</script>"
    )
    int save(@Param("list") List<AlarmLog> list);

    @Update(
            "<script>"
            + "update alarm_log set  data_status = #{status},udp_date=#{date} where dev_id = #{devId} and  data_status =#{srcStatus} and attr_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "</script>")
    void updateStatus(@Param("status")String status ,@Param("date")Date date , @Param("devId")String devId, @Param("list")List<Integer> list,@Param("srcStatus")String srcStatus);


    @Select("<script>"
            +" select a.*,e.name,m.attr_name from ( alarm_log a inner join equipments e on a.dev_id =e.dev_id ) inner join model_attr m on a.attr_id =m.id where a.dev_id = #{devId} and a.data_status= #{status}"
            + "<if test =\"attrId !=null \"> and  a.attr_id =#{attrId}  </if>"
            +" order by add_date desc"
            +"</script>")
    List<Map> queryAlarmLogByDevIdAttrId(@Param("devId") String devId, @Param("attrId")Integer attrId, @Param("status")String status);

    @Select("<script>"
            +"select * from alarm_log  where 1=1  and model_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "and data_status= #{status} "
            +" order by add_date desc"
            +"</script>")
    List<AlarmLog> queryByUserIds(@Param("list") List<String> list,@Param("status")String status);

}
