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

    @Update(
            "<script>"
                    + "update alarm_log set  data_status = #{status},udp_date=#{date},record=#{record} where id = #{id} "
                    + "</script>")
    void updateAlarm(@Param("status")String status ,@Param("date")Date date , @Param("record")String record,@Param("id")Integer id);


    @Select("<script>"
            +" select a.*,e.name,m.attr_name,m.data_type from ( alarm_log a inner join equipments e on a.dev_id =e.dev_id ) inner join model_attr m on a.attr_id =m.id where 1=1  and a.dev_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "<if test =\"devId !=null and devId !='' \"> and  a.dev_id = #{devId}  </if>"
            + "<if test =\"attrId !=null  \"> and  a.attr_id =#{attrId}  </if>"
            + "<if test =\"status !=null and status !=''\"> and  a.data_status= #{status}  </if>"
            + "<if test =\"start !=null  and start!=''\"> and a.udp_date &gt;= #{start} </if>" //大于等于
            + "<if test =\"end !=null  and end!='' \"> and a.udp_date &lt;= #{end} </if>"  // 小于等于
            +" order by udp_date desc"
            +"</script>")
    List<Map> queryAlarmLogByDevIdAttrId(@Param("list") List<String> list ,@Param("devId") String devId, @Param("attrId")Integer attrId, @Param("status")String status, @Param("start") String start, @Param("end") String end);

    @Select("<script>"
            +"select a.id,a.data_status,a.udp_date,a.alram_type,a.data_value,a.unit,e.name,m.attr_name,m.data_type from (alarm_log a LEFT JOIN equipments e on a.dev_id = e.dev_id) LEFT JOIN model_attr m on a.attr_id = m.id where a.id in ("
            +"select MAX(id) from alarm_log  where 1=1  and dev_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
             + "<if test =\" status !=null and status != ''\">  and  data_status = #{status} </if>"
            + "<if test =\"udpDate !=null \"> and  udp_date &gt; #{udpDate} </if>"
            + "<if test =\"recordIsNull \"> and  record is null </if>"
            +" GROUP BY dev_id,attr_id "
            +") order by a.udp_date desc"
            +"</script>")
    List<Map> queryAlarm(@Param("list") List<String> list ,@Param("status")String status,@Param("udpDate")Date udpDate,@Param("recordIsNull")boolean recordIsNull);

}
