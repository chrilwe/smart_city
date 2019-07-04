package com.iot.smart_city.ucenter.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.iot.smart_city.model.ucenter.ScUser;
import com.iot.smart_city.ucenter.mapper.ScUserMapper;
import com.iot.smart_city.ucenter.service.ScUcenterService;
import com.iot.smart_city.util.mqtt_client.BCryptUtil;
import com.iot.smart_city.util.mqtt_client.EmailValidateUtil;
import com.iot.smart_city.util.mqtt_client.PasswordValidateUtil;
import com.iot.smart_city.util.mqtt_client.PhoneValidateUtil;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.status.ScUserStatus;
import com.smart_city.common.ucenter.request.ScRegisterRequest;
import com.smart_city.common.ucenter.response.ScRegisterResponse;
import com.smart_city.common.ucenter.response.code.ScUcenterCode;
import com.smart_city.common.ucenter.response.msg.ScUcenterMsg;

@Service
public class ScUcenterServiceImpl implements ScUcenterService {
	
	@Autowired
	private ScUserMapper scUserMapper;

	@Override
	public ScUser findByUsername(String username) {
		if(StringUtils.isEmpty(username)) {
			return null;
		}
		return scUserMapper.findByUsername(username);
	}

	@Override
	@Transactional
	public ScRegisterResponse register(ScRegisterRequest registerRequest) {
		if(registerRequest == null) {
			return new ScRegisterResponse(ScUcenterCode.PARAM_NULL,ScUcenterMsg.PARAM_NULL,false);
		}
		String birthday = registerRequest.getBirthday();
		String email = registerRequest.getEmail();
		String name = registerRequest.getName();
		String password = registerRequest.getPassword();
		String phone = registerRequest.getPhone();
		String salt = registerRequest.getSalt();
		String sex = registerRequest.getSex();
		String username = registerRequest.getUsername();
		String userpic = registerRequest.getUserpic();
		String utype = registerRequest.getUtype();
		
		//必选参数不能为空
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) 
				|| StringUtils.isEmpty(phone) || StringUtils.isEmpty(sex) 
				|| StringUtils.isEmpty(email)) {
			return new ScRegisterResponse(ScUcenterCode.ERROR_PARAMS,ScUcenterMsg.ERROR_PARAMS,false);
		}
		//校验密码:格式为6位数字符串
		boolean validate = PasswordValidateUtil.validate(password);
		//校验邮箱
		boolean validate2 = EmailValidateUtil.validate(email);
		//校验手机
		boolean validate3 = PhoneValidateUtil.validate(phone);
		if(!validate || !validate2 || !validate3) {
			return new ScRegisterResponse(ScUcenterCode.PASSWORD_FORMAT_ERROR,ScUcenterMsg.PASSWORD_FORMAT_ERROR,false);
		}
		
		//检查邮箱手机号是否已经被注册
		ScBaseResponse checkPhone = this.checkPhone(phone);
		ScBaseResponse checkEmail = this.checkEmail(email);
		if(!checkPhone.isSuccess()) {
			return new ScRegisterResponse(ScUcenterCode.PHONENUM_ISUSED,ScUcenterMsg.PHONENUM_ISUSED,false);
		}
		if(!checkEmail.isSuccess()) {
			return new ScRegisterResponse(ScUcenterCode.EMAIL_ISUSED,ScUcenterMsg.EMAIL_ISUSED,false);
		}
		
		ScUser scUser = new ScUser();
		scUser.setCreateTime(new Date());
		scUser.setBirthday(birthday);
		scUser.setEmail(email);
		scUser.setId(UUID.randomUUID().toString());
		scUser.setName(name);
		scUser.setPassword(BCryptUtil.encode(password));
		scUser.setPhone(phone);
		scUser.setSalt(salt);
		scUser.setSex(sex);
		scUser.setStatus(ScUserStatus.YES);
		scUser.setUsername(username);
		scUser.setUserpic(userpic);
		scUser.setUtype(utype);
		scUserMapper.add(scUser);
		return new ScRegisterResponse(ScUcenterCode.SUCCESS,ScUcenterMsg.SUCCESS,true);
	}

	@Override
	public ScBaseResponse checkPhone(String phone) {
		if(StringUtils.isEmpty(phone)) {
			return new ScBaseResponse(ScUcenterCode.PARAM_NULL,ScUcenterMsg.PARAM_NULL,false);
		}
		
		ScUser findByPhone = scUserMapper.findByPhone(phone);
		if(findByPhone != null) {
			return new ScBaseResponse(ScUcenterCode.PHONENUM_ISUSED,ScUcenterMsg.PHONENUM_ISUSED,false);
		}
		return new ScBaseResponse(ScUcenterCode.SUCCESS,ScUcenterMsg.SUCCESS,true);
	}

	@Override
	public ScBaseResponse checkEmail(String email) {
		if(StringUtils.isEmpty(email)) {
			return new ScBaseResponse(ScUcenterCode.PARAM_NULL,ScUcenterMsg.PARAM_NULL,false);
		}
		ScUser findByEmail = scUserMapper.findByEmail(email);
		if(findByEmail != null) {
			return new ScBaseResponse(ScUcenterCode.EMAIL_ISUSED,ScUcenterMsg.EMAIL_ISUSED,false);
		}
		return new ScBaseResponse(ScUcenterCode.SUCCESS,ScUcenterMsg.SUCCESS,true);
	}

}
