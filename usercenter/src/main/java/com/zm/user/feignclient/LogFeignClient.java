package com.zm.user.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zm.user.feignclient.model.LogInfo;
import com.zm.user.pojo.ResultPojo;

@FeignClient("logcenter")
public interface LogFeignClient {

	@RequestMapping(value="1.0/log", method = RequestMethod.POST)
	ResultPojo saveLog(LogInfo logInfo);
}