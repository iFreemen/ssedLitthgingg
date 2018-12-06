package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.LiteApplication;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Muzzy Xu.
 */
public interface LiteAppMapper {


	@Select("<script>"
            +"SELECT *  FROM applications  where 1=1 "
            + "<if test =\"cmp == 3 \"> and  uid= #{uid}  </if>"
            + "<if test =\"cmp == 4 \"> and  uid= #{pId}  </if>"
            + "<if test =\"appId !=null  and appId!='' \"> and app like CONCAT(CONCAT('%',#{appId}),'%')  </if>"
            + "<if test =\"appName !=null  and appName!='' \"> and app_name like CONCAT(CONCAT('%',#{appName}),'%')  </if>"
            +" and valid = 'N' order by add_date desc "
            +"</script>")
     List<LiteApplication> queryLiteApps(
    		 @Param("uid") Integer uid,
    		 @Param("pId") Integer pId,
    		 @Param("cmp") Integer cmp,
    		 @Param("appId") String appId,
    		 @Param("appName") String appName);

	@Insert("insert into applications (app,app_name,secret,platform_ip,app_auth,callback_url,subscribe_notifycation,remark,uid,valid,udp_uid)"
			+ " values(#{appId},#{appName},#{secret},#{platformIp},#{appAuth},#{callbackUrl},#{subscribeNotifycation},#{remark},#{ownId},'N',#{updateUid}) ")
	public int insertLiteApplication(LiteApplication app);
	
	@Update("update  applications set app=#{appId},app_name=#{appName},"
			+ "platform_ip=#{platformIp},app_auth=#{appAuth},callback_url=#{callbackUrl},post_asyn_cmd=#{postAsynCmd},subscribe_notifycation=#{subscribeNotifycation},"
			+ "remark=#{remark},uid=#{ownId},update_time=sysdate(),udp_uid=#{updateUid}"
			+ " where id=#{id} and valid = 'N' ")
	public int updateLiteEquipment(LiteApplication app);
	
	@Update("update  applications set secret=#{secret},update_time=sysdate(),udp_uid=#{updateUid}"
			+ " where id=#{id} and valid = 'N' ")
	public int resetAppSecret(LiteApplication app);

	@Select("select count(1)>0 from applications where app = #{appId} and valid = 'N' ")
	public boolean duplicatedAid(@Param("appId")String appId);
	
	@Select("select count(1)>0 from applications where app = #{appId} and valid = 'N' and id not in (#{id}) ")
	public boolean duplicatedEidByUpd(@Param("appId")String appId,@Param("id")Integer id);


    @Delete("update applications set valid = 'D',update_time=sysdate(),udp_uid=#{uid} where valid = 'N' and id= #{id}   ")
    int deleteById(@Param("id")Integer id,@Param("uid")Integer uid);
    
    @Delete("<script>"
    		+"update lite_log l,lite_equipment e set l.l_valid = 'D',"
    		+ "l.updateTime=sysdate(),l.udp_uid=#{uid} where l.valid = 'N' and e.deviceId=l.deviceId "
    		+ "<if test =\"cmp == 3 \"> and  e.uid= #{uid}  </if>"
            + "<if test =\"cmp == 4 \"> and  e.uid= 0  </if>"
    		+ "   "
    		+"</script>")
    int deleteLiteAll(@Param("uid") Integer uid,
   		 @Param("pId") Integer pId,
   		 @Param("cmp") Integer cmp);
    


}
