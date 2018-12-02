CREATE DEFINER=`sseduser`@`%` PROCEDURE `p_update_groups`(IN `name` VARCHAR(50), IN `gid` INT, IN `tree_root` INT, IN `uid` INT, IN `g_sort` INT)
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT '更新节点'
BEGIN

DECLARE old_pid int(11);-- 原父节点
DECLARE sys_date datetime;-- 更新时间

select sysdate() into sys_date;
select pid into old_pid from group_equ where id = gid;
update group_equ set name = name, pid = tree_root, grp_sort = g_sort, udp_date = sys_date, udp_uid =uid where id = gid;

if old_pid <> tree_root then 

call p_grp_upd_bat(gid,tree_root,uid,sys_date);

end if;
   
END