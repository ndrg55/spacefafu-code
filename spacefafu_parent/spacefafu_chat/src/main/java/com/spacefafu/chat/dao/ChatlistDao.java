package com.spacefafu.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spacefafu.chat.pojo.Chatlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ChatlistDao extends JpaRepository<Chatlist,String>,JpaSpecificationExecutor<Chatlist>{
    @Modifying
    @Transactional
    @Query(value = "update chatlist set unread=0 where userid=? and fromuserid=?",nativeQuery = true)
    void updateunreadling(String userid,String fromuserid);

    @Modifying
    @Transactional
    @Query(value = "update chatlist set unread=unread+1 where userid=? and fromuserid=?",nativeQuery = true)
    void updateunreadup(String userid,String fromuserid);

    @Query(value = "select * from chatlist  where userid=? and fromuserid=?",nativeQuery = true)
    List<Chatlist> getunreadup(String userid, String fromuserid);

    @Query(value = "select sum(c.unread) from chatlist c where userid=? ",nativeQuery = true)
    Long getUnreadNum(String userid);
}
