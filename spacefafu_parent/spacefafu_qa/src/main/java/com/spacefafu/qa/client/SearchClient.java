package com.spacefafu.qa.client;

import com.spacefafu.qa.pojo.Communion;
import entity.Result;
import entity.StatusCode;
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
