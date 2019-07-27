package com.spacefafu.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spacefafu.base.pojo.Advertisement;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface AdvertisementDao extends JpaRepository<Advertisement,String>,JpaSpecificationExecutor<Advertisement>{
	
}
