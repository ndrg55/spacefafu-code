package com.spacefafu.user.controller;
import java.util.HashMap;
import java.util.Map;

import com.spacefafu.user.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spacefafu.user.pojo.User;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */


@RequestMapping("/user")
@RestController
public class UserController {



	@Autowired
	private UserService userService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	JwtUtil jwtUtil;
	/**
	 * 用户登陆

	 * @return
	 */

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result login(@RequestBody Map<String,String> loginMap){
		User user = userService.findByMobileAndPassword(loginMap.get("mobile"),loginMap.get("password"));
		if(user!=null){
			String token = jwtUtil.createJWT(user.getId(),user.getNickname(), "user");
			Map map=new HashMap();
			map.put("token",token);
			map.put("id",user.getId());
			map.put("name",user.getNickname());//昵称
			map.put("avatar",user.getAvatar());//头像
			return new Result(true,StatusCode.OK,"登陆成功",map);
		}else{
			return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
		}
	}
	/**
	 * 发送短信验证码
	 * @param mobile
	 */

	@RequestMapping(value="/sendsms/{mobile}",method=RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile ){
		userService.sendSms(mobile);
		Result result = new Result(true, StatusCode.OK, "发送成功");
		System.out.println(result.getMessage());
		return result;
	}
	/**
	 * 用户注册
	 * @param user
	 */
	@RequestMapping(value="/register/{code}",method=RequestMethod.POST)
	public Result register( @RequestBody User user ,@PathVariable String
			code){
		userService.add(user,code);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	/**
	 * 重置密码
	 * @param user
	 */
	@RequestMapping(value="/reset/{code}",method=RequestMethod.POST)
	public Result reset( @RequestBody User user ,@PathVariable String
			code){
		userService.reset(user,code);
		return new Result(true,StatusCode.OK,"重置成功  密码为123456");
	}
	/**
	 * 修改密码
	 * @param
	 */
	@RequestMapping(value="/password",method= RequestMethod.PUT)
	public Result updatePassword(@RequestBody Map searchMap){
		System.out.println(searchMap);
		userService.updatePassword(searchMap);

		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		System.out.println(id);
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		Claims claims = (Claims) request.getAttribute("admin_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	/**
	 * 更新好友粉丝数和用户关注数
	 * @return
	 */
	@RequestMapping(value = "/{userid}/{friendid}/{x}", method = RequestMethod.PUT)
	public void updatefanscountandfollowcount(@PathVariable String userid, @PathVariable String friendid, @PathVariable int x){
		userService.updatefanscountandfollowcount(x, userid, friendid);
	}
}
