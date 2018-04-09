package com.zm.order.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserInfo;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "{version}/user/vip/{centerId}/{userId}", method = RequestMethod.GET)
	public boolean getVipUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId,
			@PathVariable("centerId") Integer centerId);

	@RequestMapping(value = "{version}/user/identity/{userId}", method = RequestMethod.GET)
	public UserInfo getUser(@PathVariable("version") Double version, @PathVariable("userId") Integer userId);

	@RequestMapping(value = "auth/{version}/user/register/{code}", method = RequestMethod.POST)
	public ResultModel registerUser(@PathVariable("version") Double version, @RequestBody UserInfo info,
			@PathVariable("code") String code);
	
	@RequestMapping(value = "{version}/verifyEffective/{shopId}/{pushUserId}", method = RequestMethod.GET)
	public boolean verifyEffective(@PathVariable("version") Double version, @PathVariable("shopId") Integer shopId,
			@PathVariable("pushUserId") Integer pushUserId);
}
