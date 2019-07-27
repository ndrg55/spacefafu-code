package com.spacefafu.user.pojo;

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
@Table(name="tb_messagenum")
public class Messagenum implements Serializable{

	@Id
	private String userid;//


	
	private Long messagenum;//

	
	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Long getMessagenum() {		
		return messagenum;
	}
	public void setMessagenum(Long messagenum) {
		this.messagenum = messagenum;
	}


	
}
