package com.spacefafu.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_problem")
public class Problem implements Serializable{

	@Id
	private String id;//ID

	private String title;//标题
	private String content;//内容
	private java.util.Date createtime;//创建日期
	private java.util.Date updatetime;//修改日期
	private String userid;//用户ID

	private Long visits;//浏览量
	private Long thumbup;//点赞数
	private Long reply;//回复数
	private String solve;//是否解决
	private String lid;
	private String lname;
	@Transient
	private Object user;

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}





	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {		
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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

	public java.util.Date getUpdatetime() {		
		return updatetime;
	}
	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}


	public Long getVisits() {		
		return visits;
	}
	public void setVisits(Long visits) {
		this.visits = visits;
	}

	public Long getThumbup() {		
		return thumbup;
	}
	public void setThumbup(Long thumbup) {
		this.thumbup = thumbup;
	}

	public Long getReply() {		
		return reply;
	}
	public void setReply(Long reply) {
		this.reply = reply;
	}

	public String getSolve() {		
		return solve;
	}
	public void setSolve(String solve) {
		this.solve = solve;
	}


	
}
