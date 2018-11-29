package com.heqichao.springBootDemo.base.util;

import java.util.ArrayList;
import java.util.List;

import com.heqichao.springBootDemo.base.entity.GroupsEntity;

public class TreeUtil {

	//将list集合转成树形结构的list集合
	public static List<GroupsEntity> listToTree(List<GroupsEntity> list) {
        //用递归找子。
        List<GroupsEntity> treeList = new ArrayList<GroupsEntity>();
        for (GroupsEntity tree : list) {
            if (tree.getIsRoot()==1) {
                treeList.add(findChildren(tree, list));
            }else {
            	treeList.add(tree);
            }
        }
        return treeList;
    }
 
    //寻找子节点
    private static GroupsEntity findChildren(GroupsEntity tree, List<GroupsEntity> list) {
        for (GroupsEntity node : list) {
            if (tree.getPid()==node.getId()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<GroupsEntity>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
