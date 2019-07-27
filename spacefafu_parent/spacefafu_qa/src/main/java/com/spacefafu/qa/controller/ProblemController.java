package com.spacefafu.qa.controller;

import com.spacefafu.qa.client.LabelClient;
import com.spacefafu.qa.client.SearchClient;
import com.spacefafu.qa.client.UserClient;
import com.spacefafu.qa.pojo.Communion;
import com.spacefafu.qa.pojo.Problem;
import com.spacefafu.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private LabelClient labelClient;
	@Autowired
	private UserClient userClient;

	@Autowired
	private SearchClient searchClient;

	@RequestMapping(value = "/search/test",method = RequestMethod.POST)
	public Result addSearch(@RequestBody Communion communion){
		communion.setContentType("1");
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		communion.setTime(dateFormat.format(new Date()));
		Result result = searchClient.save(communion);
		return result;
	}

	@RequestMapping(value = "/label/{labelid}")
	public Result findLabelById(@PathVariable String labelid){
		Result result = labelClient.findById(labelid);
		return result;
	}
	//test user
	@RequestMapping(value = "/user/{userid}")
	public Result findUserById(@PathVariable String userid){
		Result result = userClient.findById(userid);
		Problem problem =new Problem();
		problem.setUser(result.getData());
		result.setData(problem);
//		ObjectMapper mapper=new ObjectMapper();
//		try {
//			System.out.println(result.getData());
//			System.out.println(result.getData().toString());
//			User user = mapper.readValue(result.getData().toString(), User.class);
//
//			System.out.println(user);
//			System.out.println(user.getNickname());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}


		return result;
	}
	/**
	 * 根据标签ID查询最新问题列表
	 * @param labelid
	 * @return
	 */
	@RequestMapping(value = "/newlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
	public Result newlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size){
		Page<Problem> pageData = problemService.newlist(labelid, page, size);

		return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}
	@RequestMapping(value = "/hotlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
	public Result hotlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size){
		Page<Problem> pageData = problemService.hotlist(labelid, page, size);
		return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@RequestMapping(value = "/waitlist/{labelid}/{page}/{size}", method = RequestMethod.GET)
	public Result waitlist(@PathVariable String labelid, @PathVariable int page, @PathVariable int size){
		Page<Problem> pageData = problemService.waitlist(labelid, page, size);
		return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findById(id));
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
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem  ){
		problemService.add(problem);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param problem
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
