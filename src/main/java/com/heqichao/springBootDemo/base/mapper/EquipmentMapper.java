package com.heqichao.springBootDemo.base.mapper;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

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
	
	@Select("<script>select equipments.dev_id,equipments.name from equipments where valid = 'N'  " + 
			"and uid like (" + 
			"select case u.competence " + 
			"when 2 then '%'" + 
			" when 3 then u.id " + 
			" when 4 then u.parent_uid end as u_id " + 
			" from users u where u.id = #{uid} limit 1 ) "
			+ "</script>")
	public List<Map<String,String>> getUserEquipmentIdList(@Param("uid") Integer uid);
	
	@Select("SELECT u.parent_uid FROM users u where u.id=#{uid} and u.valid = 'N' ")
	public Integer getUserParent(@Param("uid") Integer uid);
	
	@Select("SELECT dev_id FROM equipments where online = #{status}  and valid = 'N' ")
	public List<String> getEquipmentByStatus(@Param("status") String  status);
	
	@Select("SELECT * FROM equipments where valid = 'N' and dev_id = #{devId} ")
	public Equipment getEquipmentInfo(@Param("devId") String  devId);
	
	@Select("SELECT e.id,e.name,e.dev_id,e.type_cd,e.model_id,e.group_id,e.group_adm_id,e.app_id,"+
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," + 
			" u.company uName,m.model_name,a.app_name," + 
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " + 
			"  FROM equipments e" + 
			"  left join users u on e.uid=u.id" + 
			"  left join model m on e.model_id=m.id" + 
			"  left join applications a on e.app_id=a.id" + 
			"  where e.valid = 'N' and e.dev_id = #{devId} ")
	public Equipment getEquById(@Param("devId") String  devId);
	
	@Select("SELECT e.id,e.name,e.dev_id,e.type_cd,e.model_id,e.group_id,e.group_adm_id,e.app_id,"+
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," + 
			" u.company uName,m.model_name,a.app_name," + 
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " + 
			"  FROM equipments e" + 
			"  left join users u on e.uid=u.id" + 
			"  left join model m on e.model_id=m.id" + 
			"  left join applications a on e.app_id=a.id" + 
			"  where e.valid = 'N' and e.dev_id = #{devId} and e.id=#{id}")
	public Equipment getEquEditById(@Param("devId") String  devId,@Param("id") Integer  id);
	
	@Select("SELECT dev_id FROM equipments where valid = 'N' ")
	public List<String> getEquipmentIdListAll();
	
	@Select("<script>SELECT e.id,e.name,e.dev_id,e.type_cd,e.model_id,e.group_id,e.group_adm_id,e.app_id," + 
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," + 
			" u.company uName,m.model_name,a.app_name,g.name groupName," + 
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " + 
			"  FROM group_equ g,equipments e" + 
			"  left join users u on e.uid=u.id" + 
//			"  left join group_equ g on e.group_id=g.id" + 
			"  left join model m on e.model_id=m.id" + 
			"  left join applications a on e.app_id=a.id" + 
			"  where e.valid = 'N'  "
			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id"
			+ "<if test=\"gid !=null \"> and e.group_adm_id = #{gid}  </if> </if>"
			+ "<if test=\"competence == 3 \"> and e.group_id=g.id and e.uid = #{id}"
			+ " <if test=\"gid !=null \"> and e.group_id = #{gid}  </if>  </if>"
			+ "<if test=\"competence == 4 \"> and e.group_id=g.id and e.uid = #{parentId} "
			+ "<if test=\"gid !=null \"> and e.group_id = #{gid}  </if>  </if>"
			+ "<if test =\"sEid !=null  and sEid!='' \"> and (e.dev_id like CONCAT('%',#{sEid},'%') or e.name like CONCAT('%',#{sEid},'%'))  </if>"
			+ "<if test =\"sType !=null  and sType!='' \"> and e.type_cd like CONCAT(CONCAT('%',#{sType}),'%')  </if>"
			+ "<if test =\"sStatus !=null  and sStatus!='' \"> and e.online = #{sStatus}  </if>"
			+ " </script>")
	public List<Equipment> getEquipments(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId,
			@Param("gid")Integer gid,
			@Param("sEid")String sEid,
			@Param("sType")String sType,
			@Param("sStatus")String sStatus);
	
	@Select("<script>SELECT e.id,e.name,e.dev_id,e.type_cd,e.model_id,e.group_id,e.group_adm_id,e.app_id," + 
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," + 
			" u.company uName,m.model_name,a.app_name,g.name groupName," + 
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " + 
			"  FROM group_equ g,equipments e" + 
			"  left join users u on e.uid=u.id" + 
			"  left join model m on e.model_id=m.id" + 
			"  left join applications a on e.app_id=a.id" + 
			"  where e.valid = 'N' and e.type_cd= #{type} "
			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id </if>"
			+ "<if test=\"competence == 3 \"> and e.group_id=g.id and e.uid = #{id} </if>"
			+ "<if test=\"competence == 4 \"> and e.group_id=g.id and e.uid = #{parentId} </if>"
			+ " </script>")
	public List<Map<String,Object>> getEquipmentsForExport(
			@Param("competence")Integer competence,
			@Param("uid")Integer uid,
			@Param("parentId")Integer parentId,
			@Param("type")String type);
	
	@Insert("insert into equipments (name,dev_id,type_cd,model_id,group_id,app_id,verification,support_code,supporter,site,address,remark,uid,valid,add_uid,udp_uid,online)"
			+ " values(#{name},#{devId},#{typeCd},#{modelId},#{groupId},#{appId},#{verification},#{supportCode},#{supporter},#{site},#{address},#{remark},#{uid},#{valid},#{addUid},#{addUid},0) ")
	public int insertEquipment(Equipment equ);
	
	@Update("update equipments set name=#{name},dev_id=#{devId},type_cd=#{typeCd},model_id=#{modelId},"
			+ "group_id=#{groupId},app_id=#{appId},verification=#{verification},support_code=#{supportCode},"
			+ "supporter=#{supporter},site=#{site},address=#{address},remark=#{remark},uid=#{uid},valid=#{valid},udp_uid=#{udpUid},udp_date=sysdate()"
			+ " where id=#{id}")
	public int editEquipment(Equipment equ);
	

	@Select("<script>"
			+" select dev_id from equipments  "
			+" where online = #{online} and type_cd =#{type_cd} "
			+"</script>")
	List<String> queryByTypeAndOnline( @Param("type_cd") String type_cd,@Param("online") String online);

	@Update(
			"<script>"
					+ "update equipments set  online = #{online},udp_date=#{date} where dev_id in "
					+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
					+ "#{uid}"
					+ "</foreach>"
					+ "</script>")
	void updateOnlineStatus(@Param("online")String online , @Param("list") List<String> list, @Param("date")Date date);


	@Update("update equipments set  update_time = sysdate(), udp_uid = #{udid}, valid = 'D' where id=#{id} and valid = 'N' ")
	public int delEquById(@Param("id")Integer eid,@Param("udid")Integer udid);
	
	@Update("update equipments set  e_valid = #{status} where eid=#{eid} and valid = 'N' ")
	public int setEquStatus(@Param("eid")String eid,@Param("status")String status);
	
	@Select("select count(1)>0 from equipments where dev_id = #{devId} and valid = 'N' and uid=#{uid} ")
	public boolean duplicatedEid(@Param("devId")String devId,@Param("uid")Integer uid);
	
	@Select("select count(1)>1 from equipments where dev_id = #{devId} and valid = 'N' and uid=#{uid} ")
	public boolean duplicatedEidEdit(@Param("devId")String devId,@Param("uid")Integer uid);

	@Update("update equipments set  e_range = #{range} where eid=#{eid} and valid = 'N'")
	 int updateRange(@Param("eid")String eid,@Param("range")Integer range);

	@Select("select e_range from equipments where eid = #{eid} and valid = 'N'")
	Integer queryRange(@Param("eid")String eid);
	
	@Select({ "call p_equ_enq_page("
			+ "#{crrNum,mode=IN,jdbcType=INTEGER},"
			+ "#{pagSize,mode=IN,jdbcType=INTEGER},"
			+ "#{numUid,mode=IN,jdbcType=INTEGER},"
			+ "#{gid,mode=IN,jdbcType=INTEGER},"
			+ "#{devName,mode=IN,jdbcType=VARCHAR}"
			+ ")" })
	@Options(statementType=StatementType.CALLABLE)
	public List<Equipment> getEquPage(
			@Param("crrNum") Integer crrNum,
			@Param("pagSize")Integer pagSize,
			@Param("numUid")Integer uid,
			@Param("gid")Integer gid,
			@Param("devName")String devName
//			@Param("equipments")ResultSet equipments
//			@Param("res")Integer res
			);
}
