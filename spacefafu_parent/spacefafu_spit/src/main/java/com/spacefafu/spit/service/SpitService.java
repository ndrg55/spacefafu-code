package com.spacefafu.spit.service;

import com.spacefafu.spit.client.SearchClient;
import com.spacefafu.spit.client.UserClient;
import com.spacefafu.spit.dao.SpitDao;
import com.spacefafu.spit.pojo.Communion;
import com.spacefafu.spit.pojo.Spit;
import entity.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpitService {
    @Autowired
    SpitDao spitDao;
    @Autowired
    IdWorker idWorker;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserClient userClient;
    @Autowired
    SearchClient searchClient;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 点赞
     * @param id
     */
    public void updateThumbup(String id){
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update=new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }


    public Page<Spit> findByParentid(String parentid, int page, int size){
        Pageable pageable=PageRequest.of(page-1,size);
        Page<Spit> spitPage = spitDao.findByParentid(parentid, pageable);
        relateUser(spitPage.getContent());
        if(!parentid.equals("0")){
            relateTargetUser(spitPage.getContent());
            for (Spit spit:spitPage.getContent()) {
                Page<Spit> child = findChild(spit.get_id(), 1, 20);
                spit.setChildSpit(child.getContent());
            }
        }

        return  spitPage;
    }

    public Page<Spit> findByUserid(String userid, int page, int size){
        Pageable pageable=PageRequest.of(page-1,size,new Sort(Sort.Direction.DESC,"publishtime"));
        Page<Spit> spitPage = spitDao.findByUseridAndParentid(userid,"0",pageable);

        return  spitPage;
    }

    public Page<Spit> findChild(String parentid, int page, int size){
        Pageable pageable=PageRequest.of(page-1,size);
        Page<Spit> spitPage = spitDao.findByParentid(parentid, pageable);
        relateUser(spitPage.getContent());
            relateTargetUser(spitPage.getContent());

        return  spitPage;
    }

    /**
     * 查询全部记录
     * @return
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }
    /**
     * 根据主键查询实体
     * @param id
     * @return
     */
    public Spit findById(String id){
        Spit spit = spitDao.findById(id).get();
        relateUser(spit);
        return spit;
    }
    /**
     * 增加
     * @param spit
     */
    public void add(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setThumbup(0);//点赞数
        spit.setShare(0);//分享数
        spit.setComment(0);//回复数
        if(spit.getParentid()!=null && !"".equals(spit.getParentid())){//如果存在上级ID,评论
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update udate=new Update();
            udate.inc("comment",1);
            mongoTemplate.updateFirst(query,udate,"spit");

            //通知消息
            Map<String, String> map = new HashMap();
            map.put("id", idWorker.nextId()+"");
            map.put("userid", spit.getNickname());
            map.put("type","2");
            map.put("message","评论了你");
            //去除标签
            String txtcontent = spit.getContent().replaceAll("</?[^>]+>", ""); //剔出<html>的标签
            txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符

            map.put("title",txtcontent);
            map.put("fromuserid",spit.getUserid());
            map.put("contentid",spit.getState());

            rabbitTemplate.convertAndSend("messageinfo",map);
        }else{
            spit.setParentid("0");
            //加入搜索中
            Communion communion=new Communion();
            communion.setContentType("0");
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            communion.setTime(dateFormat.format(new Date()));
            communion.setContentId(spit.get_id());
            communion.setId(idWorker.nextId()+"");
            communion.setUserid(spit.getUserid());
            communion.setContent("点击查看详情");

            //去除标签
            String txtcontent = spit.getContent().replaceAll("</?[^>]+>", ""); //剔出<html>的标签
            txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符


            communion.setTitle(txtcontent);
            searchClient.save(communion);



        }
        spitDao.save(spit);
    }
    /**
     * 修改
     * @param spit
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }
    /**
     * 删除
     * @param id
     */
    public void deleteById(String id){
        spitDao.deleteById(id);
    }



    /**
     * 根据userid关联user ---single
     * @param spit
     */
    private void relateUser(Spit spit ){

        Result result = userClient.findById(spit.getUserid());
        Object user = result.getData();
        spit.setUser(user);

    }
    /**
     * 根据userid关联user ---list
     * @param spits
     */
    private void relateTargetUser(List<Spit> spits ){
        for (Spit spit:spits) {
            Result result = userClient.findById(spit.getNickname());
            Object user = result.getData();
            spit.setTargetUser(user);
        }
    }
    /**
     * 根据userid关联user ---list
     * @param spits
     */
    private void relateUser(List<Spit> spits ){
        for (Spit spit:spits) {
            Result result = userClient.findById(spit.getUserid());
            Object user = result.getData();
            spit.setUser(user);
        }
    }

}
