package com.spacefafu.user.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_messageinfo")
public class Messageinfo implements Serializable{

	@Id
	private String id;//
	private String userid;//
	private String type;//1 互助 2吐槽 3私信
	private String message;//
	private String title;//
	private String fromuserid;//
	private Date time;
	private String contentid;
	@Transient
	private User user;

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {		
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {		
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getFromuserid() {		
		return fromuserid;
	}
	public void setFromuserid(String fromuserid) {
		this.fromuserid = fromuserid;
	}


	
}
