/**  
 * Project Name:authserver  
 * File Name:UserController.java  
 * Package Name:com.zm.gateway.controller  
 * Date:Aug 8, 201711:44:44 PM  
 * Copyright (c) 2017, 306494983@qq.com All Rights Reserved.  
 *  
*/

package com.zm.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.auth.model.ResultPojo;
import com.zm.auth.model.UserInfo;
import com.zm.auth.service.AuthService;

/**
 * ClassName: UserController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Aug 8, 2017 11:44:44 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@RestController
@RequestMapping(value = "/auth")
public class UserController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthService authService;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultPojo createAuthenticationToken(@RequestBody UserInfo userInfo) throws AuthenticationException {
		try {
			return new ResultPojo(authService.login(userInfo));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request)
			throws AuthenticationException {
		String token = request.getHeader(tokenHeader);
		String refreshedToken = authService.refresh(token);
		if (refreshedToken == null) {
			return ResponseEntity.badRequest().body(null);
		} else {
			return ResponseEntity.ok(refreshedToken);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResultPojo register(@RequestBody UserInfo userInfo) throws AuthenticationException {
		try {
			return new ResultPojo(authService.register(userInfo));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public ResultPojo check(@RequestBody UserInfo userInfo) throws AuthenticationException {
		try {
			return new ResultPojo(authService.checkAccount(userInfo));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResultPojo("401", e.getMessage());
		}
	}

}
