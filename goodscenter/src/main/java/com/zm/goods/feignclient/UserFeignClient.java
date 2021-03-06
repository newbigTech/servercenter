package com.zm.goods.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.goods.pojo.ResultModel;

@FeignClient("usercenter")
public interface UserFeignClient {

	@RequestMapping(value = "{version}/user/center", method = RequestMethod.GET)
	public ResultModel getCenterId(@PathVariable("version") Double version);
}
