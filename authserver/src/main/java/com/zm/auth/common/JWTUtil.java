package com.zm.auth.common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.zm.auth.model.SecurityUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * ClassName: JWTUtil <br/>
 * Function: JWT工具包. <br/>
 * date: Aug 21, 2017 6:43:48 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class JWTUtil {

	private final static String SECRET = "zmteambuilder";

	public final static String USER_NAME = "username";

	public final static String PASSWORD = "password";
	
	public final static String PLATUSERTYPE="platUserType";
	
	public final static String OPEN_ID = "openid";

	public final static String PLATFORM_ID = "platid";

	public final static int BACKMANAGER = 1;
	
	public final static String APPKEY = "appKey";
	
	public final static Integer EXPIRES = 7200;

	public static String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(DateUtil.getFixDate(2020, 12, 30, 0, 0, 0))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	public static Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public static String getUsernameFromToken(String authToken) {
		return (String) getClaimsFromToken(authToken).get(USER_NAME);
	}

	public static boolean validateToken(String authToken, SecurityUserDetail userDetails) {
		if (authToken == null || "".equals(authToken) || userDetails.getPlatUserType() == 0) {
			return false;
		}

		String token = "";

		switch (userDetails.getPlatUserType()) {
		case (BACKMANAGER):
			token = generatorPlatUser(userDetails);
			break;
		default:
			token = generatorDefault(userDetails);
		}

		return authToken.equals(token);
	}

	/**
	 * validatePlatUser:生成后台用户token. <br/>
	 * 
	 * @author hebin
	 * @param userDetails
	 * @since JDK 1.7
	 */
	private static String generatorPlatUser(SecurityUserDetail userDetails) {
		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(PLATFORM_ID, userDetails.getPlatId());
		claim.put(PLATUSERTYPE, userDetails.getPlatUserType());
		String token = generateToken(claim);
		System.out.println(token);
		return token;
	}

	/**
	 * validateCustomer:生成消费者账号 <br/>
	 * 
	 * @author hebin
	 * @param userDetails
	 * @since JDK 1.7
	 */
	private static String generatorDefault(SecurityUserDetail userDetails) {
		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put(PASSWORD, userDetails.getPassword());
		claim.put(USER_NAME, userDetails.getUsername());
		claim.put(PLATUSERTYPE, userDetails.getPlatUserType());
		String token = generateToken(claim);
		System.out.println(token);
		return token;
	}

	public static String generateLimitTimeToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(DateUtil.getDate(Calendar.SECOND, EXPIRES))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}
	
	public static void main(String[] args) {
		Map<String,Object> claims = new HashMap<>();
		claims.put(APPKEY, "18067100336");
		System.out.println(generateLimitTimeToken(claims));
	}

}
