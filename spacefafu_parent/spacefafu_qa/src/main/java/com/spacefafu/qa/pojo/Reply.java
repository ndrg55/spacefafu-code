package com.spacefafu.qa.pojo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_reply")
public class Reply implements Serializable{

	@Id
	private String id;//编号


	
	private String problemid;//问题ID
	private String content;//回答内容
	private java.util.Date createtime;//创建日期

	private String userid;//回答人ID

    private String parentid;
    private String targetuserid;
    @Transient
	private List<Reply> childReplys;
    @Transient
	private Object user;
    @Transient
	private Object targetUser;

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public Object getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(Object targetUser) {
		this.targetUser = targetUser;
	}

	public List<Reply> getChildReplys() {
		return childReplys;
	}

	public void setChildReplys(List<Reply> childReplys) {
		this.childReplys = childReplys;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTargetuserid() {
		return targetuserid;
	}

	public void setTargetuserid(String targetuserid) {
		this.targetuserid = targetuserid;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getProblemid() {		
		return problemid;
	}
	public void setProblemid(String problemid) {
		this.problemid = problemid;
	}

	public String getContent() {		
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}



	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}




	
}
