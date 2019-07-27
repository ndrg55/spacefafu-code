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

import com.spacefafu.user.dao.MessagenumDao;
import com.spacefafu.user.pojo.Messagenum;
import com.spacefafu.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.spacefafu.user.dao.MessageinfoDao;
import com.spacefafu.user.pojo.Messageinfo;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class MessageinfoService {

	@Autowired
	private MessageinfoDao messageinfoDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private UserService userService;
	@Autowired
	private MessagenumDao messagenumDao;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Messageinfo> findAll() {
		List<Messageinfo> lists = messageinfoDao.findAll();
		for (Messageinfo messageinfo:lists) {
			User fromUser = userService.findById(messageinfo.getFromuserid());
			messageinfo.setUser(fromUser);
		}
		return lists;
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Messageinfo> findSearch(Map whereMap, int page, int size) {
		Specification<Messageinfo> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size,new Sort(Sort.Direction.DESC,"time"));
		Page<Messageinfo> messageinfoPage = messageinfoDao.findAll(specification, pageRequest);
		for (Messageinfo messageinfo:messageinfoPage.getContent()) {
			User fromUser = userService.findById(messageinfo.getFromuserid());
			messageinfo.setUser(fromUser);
		}
		return messageinfoPage;
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Messageinfo> findSearch(Map whereMap) {
		Specification<Messageinfo> specification = createSpecification(whereMap);
		List<Messageinfo> lists = messageinfoDao.findAll(specification,new Sort(Sort.Direction.DESC,"time"));
		for (Messageinfo messageinfo:lists) {
			User fromUser = userService.findById(messageinfo.getFromuserid());
			messageinfo.setUser(fromUser);
		}
		return lists;
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Messageinfo findById(String id) {
		return messageinfoDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param messageinfo
	 */
	public void add(Messageinfo messageinfo) {

		messageinfoDao.save(messageinfo);
		messagenumDao.updatenumup(messageinfo.getUserid());
	}

	/**
	 * 修改
	 * @param messageinfo
	 */
	public void update(Messageinfo messageinfo) {
		messageinfoDao.save(messageinfo);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id,String userid) {
		messageinfoDao.deleteById(id);
		messagenumDao.updatenum(userid);

	}

	/**
	 * 删除
	 * @param
	 */
	public void deleteall(String userid) {
		messageinfoDao.deleteall(userid);
		messagenumDao.updatenumling(userid);

	}
	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Messageinfo> createSpecification(Map searchMap) {

		return new Specification<Messageinfo>() {

			@Override
			public Predicate toPredicate(Root<Messageinfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 1 互助 2吐槽 3私信
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                	predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
                // 
                if (searchMap.get("message")!=null && !"".equals(searchMap.get("message"))) {
                	predicateList.add(cb.like(root.get("message").as(String.class), "%"+(String)searchMap.get("message")+"%"));
                }
                // 
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 
                if (searchMap.get("fromuserid")!=null && !"".equals(searchMap.get("fromuserid"))) {
                	predicateList.add(cb.like(root.get("fromuserid").as(String.class), "%"+(String)searchMap.get("fromuserid")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
