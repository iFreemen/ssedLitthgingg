package com.heqichao.springBootDemo.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.heqichao.springBootDemo.base.entity.Equipment;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface EquipmentMapper {
	
	@Select("SELECT id,dev_id,type,amount,range,alarms,IFNULL((select max(ligntningCount) from lightning_log where devEUI = equipment.eid and lightning_log.valid = '1111'),0) as total,e_status,online_time,remark,uid"
			+ " FROM equipments where id = #{id}  and valid = 'N' ")
	public Equipment getEquipmentById(@Param("id") Integer id);
	
	@Select("SELECT dev_id FROM equipments where uid = #{uid}  and valid = 'N' ")
	public List<String> getUserEquipmentIdList(@Param("uid") Integer uid);
	
	@Select("SELECT e.dev_id FROM equipments e,users u where e.uid = u.uid  and u.id=#{uid} and e.valid = 'N' ")
	public List<String> getUserEquipmentIdListByParent(@Param("uid") Integer uid);
	
	@Select("SELECT dev_id FROM equipments where online = #{status}  and valid = 'N' ")
	public List<String> getEquipmentByStatus(@Param("status") String  status);
	
	@Select("SELECT dev_id FROM equipments where valid = 'N' ")
	public List<String> getEquipmentIdListAll();
	
	@Select("<script>SELECT id,name,dev_id,type_cd,model_id,group_id,group_adm_id,app_id,"
			+ "verification,support_code,supporter,site,remark,online,uid,udp_date"
			+ " FROM equipments where valid = 'N'  "
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"
			+ "<if test =\"sEid !=null  and sEid!='' \"> and dev_id like CONCAT(CONCAT('%',#{sEid}),'%')  </if>"
			+ "<if test =\"sType !=null  and sType!='' \"> and type_cd like CONCAT(CONCAT('%',#{sType}),'%')  </if>"
			+ "<if test =\"sStatus !=null  and sStatus!='' \"> and online = #{sStatus}  </if>"
			+ " </script>")
	public List<Equipment> getEquipments(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId,
			@Param("sEid")String sEid,
			@Param("sType")String sType,
			@Param("sStatus")String sStatus);
	
	@Insert("insert into equipments (eid,e_type,amount,e_range,alarms,e_status,online_time,remark,uid,status,update_uid)"
			+ " values(#{eid},#{eType},#{amount},#{eRange},#{alarms},#{eStatus},sysdate(),#{remark},#{ownId},'N',#{updateUid}) ")
	public int insertEquipment(Equipment equ);
	

	
	@Update("update equipments set  update_time = sysdate(), update_uid = #{udid}, valid = 'N' where id=#{id} and valid = 'N' ")
	public int delEquById(@Param("id")Integer eid,@Param("udid")Integer udid);
	
	@Update("update equipments set  e_valid = #{status} where eid=#{eid} and valid = 'N' ")
	public int setEquStatus(@Param("eid")String eid,@Param("status")String status);
	
	@Select("select count(1)>0 from equipments where eid = #{eid} and valid = 'N' ")
	public boolean duplicatedEid(@Param("eid")String eid);

	@Update("update equipments set  e_range = #{range} where eid=#{eid} and valid = 'N'")
	 int updateRange(@Param("eid")String eid,@Param("range")Integer range);

	@Select("select e_range from equipments where eid = #{eid} and valid = 'N'")
	Integer queryRange(@Param("eid")String eid);
}
