package com.spacefafu.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.spacefafu.user.dao.MessagenumDao;
import com.spacefafu.user.pojo.Messagenum;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import com.spacefafu.user.dao.UserDao;
import com.spacefafu.user.pojo.User;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    MessagenumDao messagenumDao;

    /**
     * 根据手机号和密码查询用户
     * @param mobile
     * @param password
     * @return
     */
    public User findByMobileAndPassword(String mobile,String password){
        User user = userDao.findByMobile(mobile);

        if(user!=null && encoder.matches(password,user.getPassword())){
            return user;
        }else{
            return null;
        }
    }


    /**
     * 发送短信验证码
     * @param mobile 手机号
     */
    public void sendSms(String mobile) {
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println("验证码为： " + code);
        //redis中
        redisTemplate.opsForValue().set("smscode_" + mobile, code, 10, TimeUnit.MINUTES);
        //放入rabbitMQ中
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("code", code + "");
        rabbitTemplate.convertAndSend("sms", map);
    }

    /**
     * 增加
     *
     * @param user 用户
     * @param code 用户填写的验证码
     */
    public void add(User user, String code) {
         //判断验证码是否正确
        String syscode =(String) redisTemplate.opsForValue().get("smscode_" + user.getMobile());
        //提取系统正确的验证码
        if (syscode == null) {
            throw new RuntimeException("请点击获取短信验证码");
        }
        if (!syscode.equals(code)) {
            throw new RuntimeException("验证码输入不正确");
        }
        user.setId(idWorker.nextId() + "");
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
        //密码加密
        String newpassword = encoder.encode(user.getPassword());//加密后的密码
        user.setPassword(newpassword);
        //默认头像
        user.setAvatar("https://zhb-oss.oss-cn-beijing.aliyuncs.com/default.jpg");
        userDao.save(user);

        //同时创建通知中心
        Messagenum messagenum =new Messagenum() ;
        messagenum.setUserid(user.getId());
        messagenum.setMessagenum(0L);
        messagenumDao.save(messagenum);

    }
    /**
     * 重置
     *
     * @param user 用户
     * @param code 用户填写的验证码
     */
    public void reset(User user, String code) {
        //判断验证码是否正确
        String syscode =(String) redisTemplate.opsForValue().get("smscode_" + user.getMobile());
        //提取系统正确的验证码
        if (syscode == null) {
            throw new RuntimeException("请点击获取短信验证码");
        }
        if (!syscode.equals(code)) {
            throw new RuntimeException("验证码输入不正确");
        }
        String mobile = user.getMobile();
        //查找该手机的对应的用户
        User resultuser = userDao.findByMobile(mobile);
        if(resultuser==null){
            throw new RuntimeException("该手机不存在");
        }


        //密码加密
        String newpassword = encoder.encode("123456");//加密后的密码
        resultuser.setPassword(newpassword);
        userDao.save(resultuser);
    }

    //修改密码
    public void updatePassword(Map searchMap) {
        String userid= (String) searchMap.get("userid");
        String oldpassword= (String) searchMap.get("oldpassword");
        String newpassword= (String) searchMap.get("newpassword");
        newpassword = encoder.encode(newpassword);//加密后的密码

        System.out.println("旧密码"+oldpassword);
        User user = userDao.findById(userid).get();

        if(user!=null && encoder.matches(oldpassword,user.getPassword())){
            user.setPassword(newpassword);
            userDao.save(user);
        }else{
            throw new RuntimeException("密码错误");
        }



    }
    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }
    @Transactional
    public void updatefanscountandfollowcount(int x, String userid, String friendid) {
        userDao.updatefanscount(x, friendid);
        userDao.updatefollowcount(x, userid);
    }

}
