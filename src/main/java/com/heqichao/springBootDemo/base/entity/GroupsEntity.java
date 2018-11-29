package com.heqichao.springBootDemo.base.entity;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("group_equ")
public class GroupsEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8774676476245067282L;

	
	private String text;
	private Integer uid;//所属用户
	private Integer pid;//父节点id
	private Integer isRoot;//是否有子节点：0没有;1有
	private String valid;//有效标志，N为正常
	
	private List<GroupsEntity> children;
	
	public GroupsEntity () {
		
	}
	
	@Override
    public String toString() {
        return "Groups{" +
                "id='" + super.id + '\'' +
                ", name='" + text + '\'' +
                ", uid='" + uid + '\'' +
                ", pid='" + pid + '\'' +
                ", isRoot='" + isRoot + '\'' +
                ", valid='" + valid + '\'' +
                ", children=" + children +
                '}';
    }

	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	

	public Integer getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Integer isRoot) {
		this.isRoot = isRoot;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public List<GroupsEntity> getChildren() {
		return children;
	}

	public void setChildren(List<GroupsEntity> children) {
		this.children = children;
	}
	
	
	
	
	
	
}
