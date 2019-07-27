package com.spacefafu.chat.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="chatinfo")
public class Chatinfo implements Serializable{

	@Id
	private String id;//


	
	private String userid;//
	private String touserid;//
	private String message;//
	private java.util.Date createtime;//

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTouserid() {		
		return touserid;
	}
	public void setTouserid(String touserid) {
		this.touserid = touserid;
	}

	public String getMessage() {		
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		return "Chatinfo{" +
				"id='" + id + '\'' +
				", userid='" + userid + '\'' +
				", touserid='" + touserid + '\'' +
				", message='" + message + '\'' +
				", createtime=" + createtime +
				'}';
	}
}
