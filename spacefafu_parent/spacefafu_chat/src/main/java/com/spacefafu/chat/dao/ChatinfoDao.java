package com.spacefafu.chat.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spacefafu.chat.pojo.Chatinfo;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ChatinfoDao extends JpaRepository<Chatinfo,String>,JpaSpecificationExecutor<Chatinfo>{
	@Query(value = "select * from chatinfo where (userid=?1 and touserid=?2) or (userid=?2 and touserid=?1) ORDER BY createtime DESC",nativeQuery = true)
    Page<Chatinfo> getchat(String userid, String touserid, Pageable pageable);


}
