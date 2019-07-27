package com.spacefafu.spit.controller;

import com.spacefafu.spit.pojo.Spit;
import com.spacefafu.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    SpitService spitService;
    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 点赞
     * @param id
     * @return
     */
    @RequestMapping(value="/thumbup/{id}/{userid}",method=RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String id,@PathVariable String userid){
        Object o = redisTemplate.opsForValue().get("thumbup_" + userid + "_" + id);
        if(o!=null){
            return new Result(false,StatusCode.REPERROR,"你已经点过赞了");
        }
        redisTemplate.opsForValue().set( "thumbup_"+userid+"_"+ id,"1");
        spitService.updateThumbup(id);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
    /**
     * 根据上级ID查询吐槽分页数据
     * @param page
     * @param size
     * @return
     */

    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size){
        Page<Spit> pageData = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(pageData.getTotalElements(), pageData.getContent()));
    }
    /**
     * 根据用户ID查询吐槽分页数据
     * @param page
     * @param size
     * @return
     */

    @RequestMapping(value = "/search/{userid}/{page}/{size}", method = RequestMethod.GET)
    public Result findByUserid(@PathVariable String userid, @PathVariable int page, @PathVariable int size){
        Page<Spit> pageData = spitService.findByUserid(userid, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(pageData.getTotalElements(), pageData.getContent()));
    }
    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }
    /**
     * 根据ID查询
     * @param spitId ID
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true, StatusCode.OK, "查询成功", spitService.findById(spitId));
    }
    /**
     * 增加
     * @param spit
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    /*
     * 修改
     * @param spit
     */
    @RequestMapping(value = "/{spitId}", method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId, @RequestBody Spit spit){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, "修改成功");
    }
    /**
     * 删除
     * @param spitId
     */

    @RequestMapping(value = "/{spitId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
