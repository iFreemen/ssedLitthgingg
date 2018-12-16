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
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.module.entity.DataDetail;

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
			+ " order by e.add_date desc </script>")
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
			"(SELECT max(d.add_date) FROM data_detail d WHERE d.data_status = 'N' and d.dev_id= e.dev_id ) mx_date,"+
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " + 
			"  FROM group_equ g,equipments e" + 
			"  left join users u on e.uid=u.id" + 
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
			+ " order by mx_date desc </script>")
	public List<Equipment> getEquipmentsForDevLstOrderBy(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId,
			@Param("gid")Integer gid,
			@Param("sEid")String sEid,
			@Param("sType")String sType,
			@Param("sStatus")String sStatus);
	
	@Select("<script>"
    		+" select d.id,d.data_type,d.udp_date,d.dev_id,d.unit,d.data_name,d.data_value from data_detail d " + 
    		" where d.log_id = (select d2.log_id from data_detail d2  where d2.dev_id=#{devId} and d2.data_status='N' order by d2.add_date desc limit 1) " + 
    		" and d.data_status='N'"
    		+"</script>")
    List<DataDetail> queryDetailByDevId( @Param("devId") String devId);
	
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
	
	@Insert("insert into upload_result (add_uid,res_index,res_status,err_reason,res_key)"
			+ " values(#{addUid},#{resIndex},#{resStatus},#{errReason},#{resKey}) ")
	public int insertUploadResult(UploadResultEntity res);
	
	@Select("select res_index,res_status,err_reason "
			+ " from upload_result where res_key=#{key} ")
	public List<UploadResultEntity> getUploadResult(@Param("key")String key);
	
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

	@Update("<script>"
			+ "update equipments set  online = #{online},udp_date=#{date} where dev_id in "
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
			+ "#{uid}"
			+ "</foreach>"
			+ "</script>")
	void updateOnlineStatus(@Param("online")String online , @Param("list") List<String> list, @Param("date")Date date);


	@Update("update equipments set  udp_date = sysdate(), udp_uid = #{udid}, valid = 'D' where id=#{id} and valid = 'N' ")
	public int delEquById(@Param("id")Integer eid,@Param("udid")Integer udid);
	
	@Update("update equipments set  e_valid = #{status} where eid=#{eid} and valid = 'N' ")
	public int setEquStatus(@Param("eid")String eid,@Param("status")String status);
	
	@Select("select count(1)>0 from equipments where dev_id = #{devId} and valid = 'N' and uid=#{uid} ")
	public boolean duplicatedEid(@Param("devId")String devId,@Param("uid")Integer uid);
	
	@Select("select dev_id from equipments where id = #{id} and valid = 'N'  ")
	public String getEquIdOld(@Param("id")Integer id);

	@Update("update equipments set  e_range = #{range} where eid=#{eid} and valid = 'N'")
	 int updateRange(@Param("eid")String eid,@Param("range")Integer range);

	@Select("select e_range from equipments where eid = #{eid} and valid = 'N'")
	Integer queryRange(@Param("eid")String eid);
	
	@Select("select u.id from users u where u.company=#{uName} and valid = 'N' ")
	Integer getUserIdByName(@Param("uName")String uName);
	
	@Select("select u.id from model u where u.model_name=#{modelName} and add_uid=#{uid} limit 1")
	Integer getModelIdByName(@Param("modelName")String modelName,@Param("uid")Integer uid);
	
	@Select("select u.id from group_equ u where u.name=#{groupName} and uid=#{uid} and u.valid = 'N' limit 1")
	Integer getGroupIdByName(@Param("groupName")String groupName,@Param("uid")Integer uid);
	
	@Select("select u.id from applications u where u.app_name=#{appName} and uid=#{uid} and u.valid = 'N' limit 1")
	Integer getAppIdByName(@Param("appName")String appName,@Param("uid")Integer uid);
	
}
