package com.spacefafu.spit.client;


import com.spacefafu.spit.pojo.Communion;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient("spacefafu-search")
public interface SearchClient {
    @RequestMapping(value = "/communion", method= RequestMethod.POST)
    public Result save(@RequestBody Communion communion);
}
