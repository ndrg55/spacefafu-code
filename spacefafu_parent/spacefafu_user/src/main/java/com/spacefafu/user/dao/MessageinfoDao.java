package com.spacefafu.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spacefafu.user.pojo.Messageinfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface MessageinfoDao extends JpaRepository<Messageinfo,String>,JpaSpecificationExecutor<Messageinfo>{
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tb_messageinfo WHERE userid = ?",nativeQuery = true)
    public void deleteall(String userid);
	
}
