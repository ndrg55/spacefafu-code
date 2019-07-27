package com.spacefafu.chat.pojo;

import javax.persistence.*;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="chatlist")

public class Chatlist implements Serializable{

	@Id
	private String id;
	private String userid;//



	private String fromuserid;//
	private Long unread;//
	private String message;//
	private java.util.Date lasttime;//
    @Transient
    private Object User;

    public Object getUser() {
        return User;
    }

    public void setUser(Object user) {
        User = user;
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

	public String getFromuserid() {		
		return fromuserid;
	}
	public void setFromuserid(String fromuserid) {
		this.fromuserid = fromuserid;
	}

	public Long getUnread() {		
		return unread;
	}
	public void setUnread(Long unread) {
		this.unread = unread;
	}

	public String getMessage() {		
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public java.util.Date getLasttime() {		
		return lasttime;
	}
	public void setLasttime(java.util.Date lasttime) {
		this.lasttime = lasttime;
	}


	
}
