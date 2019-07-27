package com.spacefafu.spit.dao;

import com.spacefafu.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpitDao extends MongoRepository<Spit,String> {
    public Page<Spit> findByParentid(String parentid, Pageable pageable);
    public Page<Spit> findByUseridAndParentid(String userid,String parentid, Pageable pageable);
}
