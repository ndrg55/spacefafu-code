package com.spacefafu.qa.dao;

import com.spacefafu.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    @Query(value = "SELECT * FROM tb_problem ,tb_pl WHERE problemid=id AND labelid=? AND visits=0 ORDER BY createtime DESC",nativeQuery = true)
    public Page<Problem> newlist(String labelid, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem, tb_pl WHERE id = problemid AND labelid=? AND visits=0 ORDER BY reply DESC", nativeQuery = true)
    public Page<Problem> hotlist(String labelid, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem, tb_pl WHERE id = problemid AND labelid=? AND visits=0 AND reply=0 ORDER BY createtime DESC", nativeQuery = true)
    public Page<Problem> waitlist(String labelid, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem  ORDER BY createtime DESC",nativeQuery = true)
    public Page<Problem> newlistall(Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem   ORDER BY reply DESC", nativeQuery = true)
    public Page<Problem> hotlistall(Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem WHERE   reply=0 ORDER BY createtime DESC", nativeQuery = true)
    public Page<Problem> waitlistall(Pageable pageable);

    @Modifying
    @Query(value = "insert into tb_pl values (?,?)", nativeQuery = true)
    public void addpl(String problemid, String labelid);
}
