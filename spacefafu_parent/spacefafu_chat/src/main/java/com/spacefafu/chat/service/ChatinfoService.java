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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.spacefafu.chat.dao.ChatinfoDao;
import com.spacefafu.chat.pojo.Chatinfo;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ChatinfoService {

	@Autowired
	private ChatinfoDao chatinfoDao;
	
	@Autowired
	private IdWorker idWorker;

	public Page<Chatinfo> getchat(String userid, String touserid, int page, int size){
		PageRequest pageRequest = PageRequest.of(page-1, size);
		return chatinfoDao.getchat(userid,touserid,pageRequest);
	}
	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Chatinfo> findAll() {
		return chatinfoDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Chatinfo> findSearch(Map whereMap, int page, int size) {
		Specification<Chatinfo> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return chatinfoDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Chatinfo> findSearch(Map whereMap) {
		Specification<Chatinfo> specification = createSpecification(whereMap);
		return chatinfoDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Chatinfo findById(String id) {
		return chatinfoDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param chatinfo
	 */
	public void add(Chatinfo chatinfo) {
		chatinfoDao.save(chatinfo);
	}

	/**
	 * 修改
	 * @param chatinfo
	 */
	public void update(Chatinfo chatinfo) {
		chatinfoDao.save(chatinfo);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		chatinfoDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Chatinfo> createSpecification(Map searchMap) {

		return new Specification<Chatinfo>() {

			@Override
			public Predicate toPredicate(Root<Chatinfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 
                if (searchMap.get("touserid")!=null && !"".equals(searchMap.get("touserid"))) {
                	predicateList.add(cb.like(root.get("touserid").as(String.class), "%"+(String)searchMap.get("touserid")+"%"));
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
