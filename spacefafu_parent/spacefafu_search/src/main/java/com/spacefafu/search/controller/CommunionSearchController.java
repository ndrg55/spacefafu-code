package com.spacefafu.search.controller;

import com.spacefafu.search.pojo.Communion;
import com.spacefafu.search.service.CommunionSearchSerivce;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communion")
public class CommunionSearchController {
    @Autowired
    private CommunionSearchSerivce communionSearchSerivce;
    @RequestMapping(value="/search/{keywords}/{page}/{size}",method=RequestMethod.GET)
    public Result findByTitleLike(@PathVariable String keywords,
                                  @PathVariable int page, @PathVariable int size){
        Page<Communion> communionPage =communionSearchSerivce.findByTitleOrContentLike(keywords,page,size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Communion>(communionPage.getTotalElements(),
                communionPage.getContent()));
    }

    @RequestMapping(value="/search/{keywords}/{page}/{size}/{type}",method=RequestMethod.GET)
    public Result findByTitleLikeType(@PathVariable String keywords,
                                  @PathVariable int page, @PathVariable int size,@PathVariable String type){
        System.out.println(type);
        List<Communion> communionPage =communionSearchSerivce.findByTitleOrContentLikeType(keywords,page,size,type);
        return new Result(true, StatusCode.OK, "查询成功" ,new PageResult<Communion>((long) communionPage.size(),communionPage)
        );
    }

    @RequestMapping(method= RequestMethod.POST)
    public Result save(@RequestBody Communion communion){
        communionSearchSerivce.add(communion);
        return new Result(true, StatusCode.OK, "操作成功");
    }
    @RequestMapping(value="/{contentId}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String contentId){
        communionSearchSerivce.delete(contentId);
        return new Result(true, StatusCode.OK, "操作成功");
    }


}
