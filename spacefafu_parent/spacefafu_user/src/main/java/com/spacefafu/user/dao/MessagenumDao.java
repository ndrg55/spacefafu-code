package com.spacefafu.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spacefafu.user.pojo.Messagenum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface MessagenumDao extends JpaRepository<Messagenum,String>,JpaSpecificationExecutor<Messagenum>{
    @Modifying
    @Transactional
    @Query(value = "update tb_messagenum set messagenum=messagenum-1 where userid=?",nativeQuery = true)
    public void updatenum(String userid);
    @Modifying
    @Transactional
    @Query(value = "update tb_messagenum set messagenum=0 where userid=?",nativeQuery = true)
    public void updatenumling(String userid);
    @Modifying
    @Transactional
    @Query(value = "update tb_messagenum set messagenum=messagenum+1 where userid=?",nativeQuery = true)
    public void updatenumup(String userid);
}
