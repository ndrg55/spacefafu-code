package com.spacefafu.base.service;

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

import com.spacefafu.base.dao.AdvertisementDao;
import com.spacefafu.base.pojo.Advertisement;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class AdvertisementService {

	@Autowired
	private AdvertisementDao advertisementDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Advertisement> findAll() {
		return advertisementDao.findAll(new Sort(Sort.Direction.DESC,"sort"));
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Advertisement> findSearch(Map whereMap, int page, int size) {
		Specification<Advertisement> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return advertisementDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Advertisement> findSearch(Map whereMap) {
		Specification<Advertisement> specification = createSpecification(whereMap);
		return advertisementDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Advertisement findById(String id) {
		return advertisementDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param advertisement
	 */
	public void add(Advertisement advertisement) {
		advertisement.setId( idWorker.nextId()+"" );
		advertisementDao.save(advertisement);
	}

	/**
	 * 修改
	 * @param advertisement
	 */
	public void update(Advertisement advertisement) {
		advertisementDao.save(advertisement);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		advertisementDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Advertisement> createSpecification(Map searchMap) {

		return new Specification<Advertisement>() {

			@Override
			public Predicate toPredicate(Root<Advertisement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 
                if (searchMap.get("tip")!=null && !"".equals(searchMap.get("tip"))) {
                	predicateList.add(cb.like(root.get("tip").as(String.class), "%"+(String)searchMap.get("tip")+"%"));
                }
                // 
                if (searchMap.get("sort")!=null && !"".equals(searchMap.get("sort"))) {
                	predicateList.add(cb.like(root.get("sort").as(String.class), "%"+(String)searchMap.get("sort")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
