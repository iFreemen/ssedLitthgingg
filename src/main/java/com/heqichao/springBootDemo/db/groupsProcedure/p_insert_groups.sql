CREATE DEFINER=`sseduser`@`%` PROCEDURE `p_insert_groups`(IN `name` VARCHAR(50), IN `tree_root` INT, IN `uid` INT, IN `aid` INT, IN `g_sort` INT)
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT '插入节点'
BEGIN

DECLARE nid int;-- 新产生的ID

insert into group_equ(name,uid,valid,add_uid,grp_sort,pid) values(name,uid,'N',aid,g_sort,tree_root);

select LAST_INSERT_ID() into nid;
    
INSERT INTO group_tree(ancestor,descendant,add_uid)
    SELECT t.ancestor,nid,aid
    FROM group_tree AS t
    WHERE t.descendant = tree_root
    UNION ALL
    select nid,nid,aid;
END