package com.spacefafu.search.service;

import com.spacefafu.search.client.UserClient;
import com.spacefafu.search.dao.CommunionSearchDao;
import com.spacefafu.search.pojo.Communion;
import entity.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CommunionSearchSerivce {
    @Autowired
    private CommunionSearchDao communionSearchDao;
    @Autowired
    private UserClient userClient;


    /**
     * 增加文章
     * @param
     */
    public void add(Communion communion){
        communionSearchDao.save(communion);
    }

    /**
     * 删除文章
     * @param
     */
    public void delete(String contentId){
        List<Communion> communions = communionSearchDao.findByContentId(contentId);
        if (communions.size()>0){
            System.out.println(communions.get(0).toString());
            communionSearchDao.delete(communions.get(0));
        }else{
            throw new RuntimeException("不存在");
        }

    }
    /**
     * 搜索
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Communion> findByTitleOrContentLike(String keywords,int page,int size){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Communion> communionPage = communionSearchDao.findByTitleLikeOrContentLike(keywords, keywords, pageable);
        relateUser(communionPage.getContent());

        return communionPage;
    }
    /**
     * 根据userid关联user ---list
     * @param
     */
    private void relateUser(List<Communion> communions ){
        for (Communion ommunion:communions) {
            Result result = userClient.findById(ommunion.getUserid());
            Object user = result.getData();
            ommunion.setUser(user);
        }
    }
    //TODO  此方法未完善 因测试bug 有很多冗余 后期修改
    public List<Communion> findByTitleOrContentLikeType(String keywords, int page, int size, String type) {
        Pageable pageable = PageRequest.of(page-1, size);

        Page<Communion> communionPage=communionSearchDao.findByTitleLikeOrContentLike(keywords,keywords,pageable);
        //////////////////////////////////////
        List<Communion> contents = communionPage.getContent();
        List<Communion> copycontents=new ArrayList<>();
        for (Communion content:contents) {
            copycontents.add(content);
        }
        copycontents.size();
        CollectionUtils.filter(copycontents, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                Communion next= (Communion) o;
                boolean b = next.getContentType().equals(type);
                System.out.println(b);
                return b;
            }
        });

        /////////////////////////////
        relateUser(copycontents);
        return copycontents;
    }
}
