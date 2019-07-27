package com.spacefafu.chat.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.spacefafu.chat.client.UserClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.spacefafu.chat.dao.ChatlistDao;
import com.spacefafu.chat.pojo.Chatlist;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ChatlistService {

	@Autowired
	private ChatlistDao chatlistDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserClient userClient;


	public Long getUnreadNum(String userid){
		return chatlistDao.getUnreadNum(userid);
	}
	/**
	 * 将未读数量置为零
	 * @param userid
	 * @param fromuserid
	 */
	public void updateunreadling(String userid,String fromuserid){
		chatlistDao.updateunreadling(userid,fromuserid);
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Chatlist> findAll() {
		return chatlistDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Chatlist> findSearch(Map whereMap, int page, int size) {
		Specification<Chatlist> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size,new Sort(Sort.Direction.DESC,"lasttime"));
		Page<Chatlist> chatlistPage = chatlistDao.findAll(specification, pageRequest);
		relateUser(chatlistPage.getContent());
		return chatlistPage;
	}

	/**
	 * 根据userid关联user ---list
	 * @param
	 */
	private void relateUser(List<Chatlist> chatlists ){
		for (Chatlist chatlist:chatlists) {
			Result result = userClient.findById(chatlist.getFromuserid());
			Object user = result.getData();
			chatlist.setUser(user);
		}
	}
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Chatlist> findSearch(Map whereMap) {
		Specification<Chatlist> specification = createSpecification(whereMap);
		return chatlistDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Chatlist findById(String id) {
		return chatlistDao.findById(id).get();
	}

//	/**
//	 * 增加
//	 * @param chatlist
//	 */
//	public void add(Chatlist chatlist) {
//		chatlist.setId( idWorker.nextId()+"" );
//		chatlistDao.save(chatlist);
//	}

	/**
	 * 修改
	 * @param chatlist
	 */
	public void update(Chatlist chatlist) {
		chatlistDao.save(chatlist);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		chatlistDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Chatlist> createSpecification(Map searchMap) {

		return new Specification<Chatlist>() {

			@Override
			public Predicate toPredicate(Root<Chatlist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 
                if (searchMap.get("fromuserid")!=null && !"".equals(searchMap.get("fromuserid"))) {
                	predicateList.add(cb.like(root.get("fromuserid").as(String.class), "%"+(String)searchMap.get("fromuserid")+"%"));
                }
                // 
                if (searchMap.get("message")!=null && !"".equals(searchMap.get("message"))) {
                	predicateList.add(cb.like(root.get("message").as(String.class), "%"+(String)searchMap.get("message")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
