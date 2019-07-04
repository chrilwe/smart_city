package com.iot.smart_city.ucenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScUcenterApi;
import com.iot.smart_city.model.ucenter.ScUser;
import com.iot.smart_city.ucenter.service.ScUcenterService;
import com.smart_city.common.base.ScBaseController;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.ucenter.request.ScRegisterRequest;
import com.smart_city.common.ucenter.response.ScRegisterResponse;
/**
 * 会员中心
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/ucenter")
public class ScUcenterController extends ScBaseController implements ScUcenterApi {
	
	@Autowired
	private ScUcenterService scUcenterService;
	
	/**
	 * 根据账号查询
	 */
	@Override
	@GetMapping("/findByUsername")
	public ScUser findUserByUsername(@RequestParam("username")String username) {
		ScUser scUser = scUcenterService.findByUsername(username);
		return scUser;
	}
	
	/**
	 * 注册用户
	 */
	@Override
	@PostMapping("/register")
	public ScRegisterResponse register(@RequestBody ScRegisterRequest registerRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 检查手机号是否已经被注册
	 */
	@Override
	@GetMapping("/phone/check")
	public ScBaseResponse checkPhone(String phone) {
		
		return scUcenterService.checkPhone(phone);
	}
	
	/**
	 * 检查邮箱是否已经被注册
	 */
	@Override
	@GetMapping("/email/check")
	public ScBaseResponse checkEmail(String email) {
		
		return scUcenterService.checkEmail(email);
	}

}
