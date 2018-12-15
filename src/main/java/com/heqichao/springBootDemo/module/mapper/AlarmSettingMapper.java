package com.heqichao.springBootDemo.module.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.module.entity.AlarmSetting;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface AlarmSettingMapper {
	
	
	@Select("<script>SELECT e.id,e.name,e.model_id,e.attr_id,e.alram_type,e.data_type,e.data_a,e.data_b,e.data_status,"
			+ " (select param_value from option_tree where code='AlarmSettingCode' and param_key=e.alram_type) alramTypeName, "
			+ " m.model_name modelName,a.attr_name attrName " + 
			"  FROM alarm_setting e" + 
			"  left join model m on e.model_id=m.id" + 
			"  left join model_attr a on e.attr_id=a.id" + 
			"  where e.data_status = 'N'  "
			+ "<if test =\"name !=null  and name!='' \"> and e.name like CONCAT('%',#{name},'%')  </if>"
//			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id </if>"
//			+ "<if test=\"competence == 3 \"> and e.group_id=g.id and e.uid = #{id} </if>"
//			+ "<if test=\"competence == 4 \"> and e.group_id=g.id and e.uid = #{parentId} </if>"
			+ " </script>")
	public List<AlarmSetting> queryAlarmSettingAll(@Param("name")String name);
	
	@Insert("insert into alarm_setting (name,model_id,attr_id,alram_type,data_a,data_b,add_uid,udp_uid)"
			+ " values(#{name},#{modelId},#{attrId},#{alramType},#{dataA},#{dataB},#{addUid},#{addUid}) ")
	public int addAlarmSetting(AlarmSetting as);
	
	
	@Update("update alarm_setting set  name = #{name},model_id=#{modelId},attr_id =#{attrId},alram_type =#{alramType},"
			+ "data_a =#{dataA},data_b =#{dataB},udp_uid =#{udpUid},udp_date =sysdate()  where id=#{id} and data_status = 'N' ")
	public int editAlarmSetting(AlarmSetting as);
	
	@Update("update alarm_setting set  data_status = 'D', udp_date=sysdate(),udp_uid =#{uid}  where id=#{aid} and data_status = 'N' ")
	public int delAlarmSetting(@Param("aid")Integer aid,@Param("uid")Integer uid);
	
}
