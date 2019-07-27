package com.spacefafu.sms.client;

import com.spacefafu.sms.pojo.Messageinfo;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient("spacefafu-user")
public interface UserClient {
//    @RequestMapping(value="/user/{id}",method= RequestMethod.GET)
//    public Result findById(@PathVariable("id") String id);

    /**
     * 增加
     * @param
     */
    @RequestMapping(value="/messageinfo" , method=RequestMethod.POST)
    public Result add(@RequestBody Messageinfo messageinfo  );
}
