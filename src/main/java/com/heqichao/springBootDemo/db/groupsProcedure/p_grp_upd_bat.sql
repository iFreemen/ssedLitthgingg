BEGIN

-- 游标所使用变量需要在定义游标之前申明
DECLARE d_id int(11);
DECLARE del_a_id int(11);
DECLARE ins_a_id int(11);
DECLARE num_tmp int(2);

-- 遍历数据结束标志，两层循环用同一个标志，需要特别主意
DECLARE done INT DEFAULT FALSE;
DECLARE cur_del_a CURSOR FOR SELECT ancestor FROM group_tree WHERE descendant=gid;
DECLARE cur_d CURSOR FOR SELECT descendant FROM group_tree WHERE ancestor=gid;
DECLARE cur_ins_a CURSOR FOR SELECT ancestor FROM group_tree WHERE descendant=tree_root union all select gid;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN cur_d;
  d_loop:LOOP
   FETCH cur_d INTO d_id;
   IF done THEN
       LEAVE d_loop;
   END IF;  
   
   OPEN cur_del_a;
     del_loop:LOOP
	   FETCH cur_del_a INTO del_a_id;
	   IF done THEN
	       LEAVE del_loop;
	   END IF; 
	   update group_tree set valid = 1 where  descendant = d_id and ancestor = del_a_id;
	  -- DELETE FROM group_tree where descendant = d_id and ancestor = del_a_id;
	   END LOOP;
	CLOSE cur_del_a;
	SET done=FALSE; #这个很重要
	
	OPEN cur_ins_a;
     ins_loop:LOOP
	   FETCH cur_ins_a INTO ins_a_id;
	   IF done THEN
	       LEAVE ins_loop;
	   END IF; 
	   select count(1) into num_tmp from group_tree where descendant = d_id and ancestor = ins_a_id and valid = 1;
	   IF num_tmp = 0 THEN 
	      INSERT INTO group_tree(ancestor,descendant,add_uid,udp_uid,udp_date,add_date) 
		   values(ins_a_id,d_id,uid,uid,sys_date,sys_date);
		ELSE
		  update group_tree set valid = 0 where  descendant = d_id and ancestor = ins_a_id;
		END IF;
	   END LOOP;
	CLOSE cur_ins_a;
	SET done=FALSE; #这个很重要
	
	
	
  END LOOP;
CLOSE cur_d;

DELETE FROM group_tree WHERE valid = 1;
/*
如果第二层循环中并列两个甚至多个循环，那么done这个标记就需要在每次二层循环结束的时候置为false；
*/
END