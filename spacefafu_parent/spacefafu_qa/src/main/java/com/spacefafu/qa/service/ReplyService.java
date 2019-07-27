package com.spacefafu.qa.service;

import com.spacefafu.qa.client.UserClient;
import com.spacefafu.qa.pojo.Reply;
import com.spacefafu.qa.dao.ReplyDao;
import entity.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class ReplyService {

	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private UserClient userClient;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * 根据userid关联user ---list
	 * @param Replys
	 */
	private void relateUser(List<Reply> Replys ){
		for (Reply reply:Replys) {
			Result result = userClient.findById(reply.getUserid());
			Object user = result.getData();
			reply.setUser(user);
		}
	}
	/**
	 * 根据userid关联user ---single
	 * @param reply
	 */
	private void relateUser(Reply reply ){

		Result result = userClient.findById(reply.getUserid());
		Object user = result.getData();
		reply.setUser(user);

	}
	/**
	 * 根据userid关联user ---list
	 * @param Replys
	 */
	private void relateTargetUser(List<Reply> Replys ){
		for (Reply reply:Replys) {
			Result result = userClient.findById(reply.getTargetuserid());
			Object user = result.getData();
			reply.setTargetUser(user);
		}
	}
	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Reply> findAll() {
		return replyDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Reply> findSearch(Map whereMap, int page, int size) {
		Specification<Reply> specification = createSpecification(whereMap);

		Sort sort=new Sort(Sort.Direction.ASC,"createtime");
		PageRequest pageRequest =  PageRequest.of(page-1, size,sort);
		Page<Reply> replyPage = replyDao.findAll(specification, pageRequest);
		relateUser(replyPage.getContent());
		return replyPage;
	}
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Reply> getChilds(Map whereMap, int page, int size) {
		Specification<Reply> specification = createSpecification(whereMap);
		System.out.println(page);
		Sort sort=new Sort(Sort.Direction.ASC,"createtime");
		PageRequest pageRequest =  PageRequest.of(page-1, size,sort);
		Page<Reply> replyPage = replyDao.findAll(specification, pageRequest);
		relateUser(replyPage.getContent());
		relateTargetUser(replyPage.getContent());
		return replyPage;
	}
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Reply> findSearch(Map whereMap) {
		Specification<Reply> specification = createSpecification(whereMap);
		return replyDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Reply findById(String id) {
		return replyDao.findById(id).get();
	}



	/**
	 * 增加
	 * @param reply
	 */
	public void add(Reply reply) {
		reply.setId( idWorker.nextId()+"" );
		reply.setCreatetime(new Date());
		replyDao.save(reply);
		//通知消息
		Map<String, String> map = new HashMap();
		map.put("id", idWorker.nextId()+"");
		map.put("userid", reply.getTargetuserid());
		map.put("type","1");
		map.put("message","评论了你的互助");
		String txtcontent = reply.getContent().replaceAll("</?[^>]+>", ""); //剔出<html>的标签
		txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
		map.put("title",txtcontent);
		map.put("fromuserid",reply.getUserid());
		map.put("contentid",reply.getProblemid());

		rabbitTemplate.convertAndSend("messageinfo",map);
		//TODO
		replyDao.addreplynum(reply.getProblemid());

	}

	/**
	 * 修改
	 * @param reply
	 */
	public void update(Reply reply) {
		replyDao.save(reply);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		replyDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Reply> createSpecification(Map searchMap) {

		return new Specification<Reply>() {

			@Override
			public Predicate toPredicate(Root<Reply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 编号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 问题ID
                if (searchMap.get("problemid")!=null && !"".equals(searchMap.get("problemid"))) {
                	predicateList.add(cb.like(root.get("problemid").as(String.class), "%"+(String)searchMap.get("problemid")+"%"));
                }
                // 回答内容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 回答人ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 回答人昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
				// 回复的父编号
				if (searchMap.get("parentid")!=null && !"".equals(searchMap.get("parentid"))) {
					predicateList.add(cb.equal(root.get("parentid").as(String.class), (String)searchMap.get("parentid")));
				}
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
