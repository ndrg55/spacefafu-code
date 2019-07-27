package com.spacefafu.base.pojo;

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
@Table(name="tb_advertisement")
public class Advertisement implements Serializable{

	@Id
	private String id;//


	
	private String image;//
	private String state;//
	private String tip;//
	private String sort;//
	private String url;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {		
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getTip() {		
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getSort() {		
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}


	
}
