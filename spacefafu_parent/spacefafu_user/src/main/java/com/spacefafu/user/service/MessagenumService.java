package com.spacefafu.user.service;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.spacefafu.user.dao.MessagenumDao;
import com.spacefafu.user.pojo.Messagenum;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class MessagenumService {

	@Autowired
	private MessagenumDao messagenumDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Messagenum> findAll() {
		return messagenumDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Messagenum> findSearch(Map whereMap, int page, int size) {
		Specification<Messagenum> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return messagenumDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Messagenum> findSearch(Map whereMap) {
		Specification<Messagenum> specification = createSpecification(whereMap);
		return messagenumDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Messagenum findById(String id) {
		return messagenumDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param messagenum
	 */
	public void add(Messagenum messagenum) {

		messagenumDao.save(messagenum);

	}

	/**
	 * 修改
	 * @param messagenum
	 */
	public void update(Messagenum messagenum) {
		messagenumDao.save(messagenum);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		messagenumDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Messagenum> createSpecification(Map searchMap) {

		return new Specification<Messagenum>() {

			@Override
			public Predicate toPredicate(Root<Messagenum> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
