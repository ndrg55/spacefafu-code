package com.spacefafu.qa.dao;

import com.spacefafu.qa.pojo.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ReplyDao extends JpaRepository<Reply,String>,JpaSpecificationExecutor<Reply>{
    @Modifying
    @Query(value = "update  tb_problem  set reply=reply+1 where id=?", nativeQuery = true)
    public void addreplynum(String id);
}
