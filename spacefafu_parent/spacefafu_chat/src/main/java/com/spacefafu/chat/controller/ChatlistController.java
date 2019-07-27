package com.spacefafu.chat.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spacefafu.chat.pojo.Chatlist;
import com.spacefafu.chat.service.ChatlistService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/chatlist")
public class ChatlistController {

	@Autowired
	private ChatlistService chatlistService;

	@RequestMapping(value = "/unread/{userid}",method= RequestMethod.GET)
	public Result getUnreadNum(@PathVariable String userid){

		HashMap map = new HashMap();
		map.put("unreadnum",chatlistService.getUnreadNum(userid));
		return new Result(true,StatusCode.OK,"查询成功",map);
	}

	/**
	 * 置为0
	 * @return
	 */
	@RequestMapping(value = "/{userid}/{touserid}",method= RequestMethod.PUT)
	public Result updateunreadling(@PathVariable String userid,@PathVariable String touserid){
		chatlistService.updateunreadling(userid,touserid);
		return new Result(true,StatusCode.OK,"更改成功");
	}



	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",chatlistService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",chatlistService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Chatlist> pageList = chatlistService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Chatlist>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",chatlistService.findSearch(searchMap));
    }
	
//	/**
//	 * 增加
//	 * @param chatlist
//	 */
//	@RequestMapping(method=RequestMethod.POST)
//	public Result add(@RequestBody Chatlist chatlist  ){
//		chatlistService.add(chatlist);
//		return new Result(true,StatusCode.OK,"增加成功");
//	}
//
	/**
	 * 修改
	 * @param chatlist
	 */
//	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
//	public Result update(@RequestBody Chatlist chatlist, @PathVariable String id ){
//		chatlist.setId(id);
//		chatlistService.update(chatlist);
//		return new Result(true,StatusCode.OK,"修改成功");
//	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		chatlistService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
