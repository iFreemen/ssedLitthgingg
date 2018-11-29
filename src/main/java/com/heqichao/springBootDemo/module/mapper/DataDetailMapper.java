package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.DataDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by heqichao on 2018-11-28.
 */
public interface DataDetailMapper {

    @Insert(
            "<script>"
            +" insert into data_detail (add_date,udp_date,add_uid,udp_uid,log_id,devId,data_key,data_type,data_value,unit,data_status) values "
            + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
            +"(#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.logId},#{o.devId},#{o.dataKey},#{o.dataType},#{o.dataValue},#{unit},#{o.dataStatus}) "
            + "</foreach>"
            +"</script>"
    )
    int save(@Param("list") List<DataDetail> list);


    @Update(
            "<script>"
                    + "update data_detail set  data_status = #{status} where dev_id in "
                    + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
                    + "#{uid}"
                    + "</foreach>"
                    + "</script>")
    void updateStatus(@Param("status")String status , @Param("list") List<String> list);

    @Select("<script>"
            +"select * from data_detail  where dev_id = #{devId} and date_key = #{dataKey} and data_status = #{status}"
            + "<if test =\"start !=null  and start!=''\"> and ligntningTime &gt;= #{start} </if>" //大于等于
            + "<if test =\"end !=null  and end!='' \"> and ligntningTime &lt;= #{end} </if>"  // 小于等于
            +" order by add_date desc "
            +"</script>")
    List<DataDetail> queryDetail( @Param("devId") String devId, @Param("dataKey") String dataKey,@Param("status") String status, @Param("start") String start, @Param("end") String end);




}

