package com.spacefafu.chat.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient("spacefafu-user")
public interface UserClient {
    @RequestMapping(value="/user/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable("id") String id);
}
