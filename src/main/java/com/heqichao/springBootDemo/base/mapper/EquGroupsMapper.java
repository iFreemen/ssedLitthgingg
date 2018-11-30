package com.heqichao.springBootDemo.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.heqichao.springBootDemo.base.entity.GroupsEntity;
import com.heqichao.springBootDemo.base.entity.User;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface EquGroupsMapper {
	
	@Select("<script>SELECT c.id,c.name,c.pid,c.grp_sort FROM group_equ AS c " + 
			"    INNER JOIN group_tree t on c.id = t.descendant " + 
			"    WHERE t.ancestor = 1 and c.valid = 'N' and (uid = #{uid} or uid = 0 )"
			+ " order by grp_sort </script>")
	public List<GroupsEntity> getGroups(@Param("uid")Integer uid);
	
	@Select("select id,company from users where competence = 3 and valid = 'N'")
	public List<User> getCompanySelectList();
	
	@Select("select count(1)>0 from users where (ACCOUNT = #{account} or company = #{company}) and valid = 'N' ")
	public boolean duplicatedAccount(@Param("account")String account,@Param("company")String company);
	
	@Select("select id from users where ACCOUNT = #{account} and password = #{password} and valid = 'N' limit 1")
	public Integer checkPassword(@Param("account") String account,@Param("password") String password);
	
	@Insert("insert into users (parent_uid,account,password,company,contact,phone,fax,email,site,remark,competence,valid,add_date,udp_uid)"
			+ " values(#{parentId},#{account},#{password},#{company},#{contact},#{phone},#{fax},#{email},#{site},#{remark},#{competence}, 'N', sysdate(), #{upadteUID}) ")
	public int insertUser(User user);
	
	@Update("update users set password=#{password}, udp_date = sysdate(), udp_uid = #{udid} where id=#{id} and valid = 'N' ")
	public int updateUserPassword(@Param("id")Integer uid,@Param("udid")Integer udid,@Param("password") String password);
	
	@Update("update users set company=#{company}, contact=#{contact}, phone=#{phone}, fax=#{fax}, email=#{email}, site=#{site}, remark=#{remark}, udp_date = sysdate(), udp_uid = #{id} where id=#{id} and valid = 'N' ")
	public int updateUserInfo(User user);
	
	@Select({ "call p_insert_groups("
			+ "#{name,mode=IN,jdbcType=VARCHAR},"
			+ "#{treeRoot,mode=IN,jdbcType=INTEGER},"
			+ "#{uid,mode=IN,jdbcType=INTEGER},"
			+ "#{aid,mode=IN,jdbcType=INTEGER},"
			+ "#{gSort,mode=IN,jdbcType=INTEGER})" })
	@Options(statementType=StatementType.CALLABLE)
	public void insertGroups(
			@Param("name") String name,
			@Param("treeRoot")Integer treeRoot,
			@Param("uid")Integer uid,
			@Param("aid")Integer aid,
			@Param("gSort")Integer gSort);
	
	@Select({ "call p_update_groups("
			+ "#{name,mode=IN,jdbcType=VARCHAR},"
			+ "#{gid,mode=IN,jdbcType=INTEGER},"
			+ "#{treeRoot,mode=IN,jdbcType=INTEGER},"
			+ "#{uid,mode=IN,jdbcType=INTEGER},"
			+ "#{gSort,mode=IN,jdbcType=INTEGER})" })
	@Options(statementType=StatementType.CALLABLE)
	public void updateGroups(
			@Param("name") String name,
			@Param("gid")Integer gid,
			@Param("treeRoot")Integer treeRoot,
			@Param("uid")Integer uid,
			@Param("gSort")Integer gSort);

}
