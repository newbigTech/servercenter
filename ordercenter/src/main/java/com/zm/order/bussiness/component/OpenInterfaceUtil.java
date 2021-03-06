package com.zm.order.bussiness.component;

import com.zm.order.pojo.ButtJointOrder;
import com.zm.order.pojo.ErrorCodeEnum;
import com.zm.order.pojo.OrderGoods;
import com.zm.order.pojo.ResultModel;
import com.zm.order.pojo.UserDetail;
import com.zm.order.pojo.UserInfo;
import com.zm.order.utils.RegularUtil;

public class OpenInterfaceUtil {

	public static UserInfo packUser(ButtJointOrder orderInfo) {
		UserInfo user = new UserInfo();
		user.setPhone(orderInfo.getPhone());
		user.setPhoneValidate(1);
		user.setCenterId(orderInfo.getCenterId());
		UserDetail detail = new UserDetail();
		detail.setIdNum(orderInfo.getNumId());
		detail.setName(orderInfo.getName());
		user.setUserDetail(detail);
		return user;
	}
	
	public static ResultModel paramValidate(ButtJointOrder orderInfo){
		if(!orderInfo.validate() || !orderInfo.getOrderDetail().validate()){
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		if(orderInfo.getOrderDetail() == null){
			return new ResultModel(false, ErrorCodeEnum.ORDER_MISS_DETAIL.getErrorCode(),
					ErrorCodeEnum.ORDER_MISS_DETAIL.getErrorMsg());
		}
		if(orderInfo.getOrderGoodsList() == null){
			return new ResultModel(false, ErrorCodeEnum.ORDER_MISS_GOODS.getErrorCode(),
					ErrorCodeEnum.ORDER_MISS_GOODS.getErrorMsg());
		}
		if(orderInfo.getOrderDetail().getPayment() > 2000){
			return new ResultModel(false, ErrorCodeEnum.OUT_OF_PRICE.getErrorCode(),
					ErrorCodeEnum.OUT_OF_PRICE.getErrorMsg());
		}
		if (!RegularUtil.isPhone(orderInfo.getPhone())) {
			return new ResultModel(false, ErrorCodeEnum.BUYER_PHONE_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.BUYER_PHONE_VALIDATE_ERROR.getErrorMsg());
		}
		if(!RegularUtil.isIdentify(orderInfo.getNumId())){
			return new ResultModel(false, ErrorCodeEnum.IDENTIFY_VALIDATE_ERROR.getErrorCode(),
					ErrorCodeEnum.IDENTIFY_VALIDATE_ERROR.getErrorMsg());
		}
		if(!RegularUtil.isPhone(orderInfo.getOrderDetail().getReceivePhone())){
			return new ResultModel(false, ErrorCodeEnum.RECEIVE_PHONE_ERROR.getErrorCode(),
					ErrorCodeEnum.RECEIVE_PHONE_ERROR.getErrorMsg());
		}
		for(OrderGoods goods : orderInfo.getOrderGoodsList()){
			if(!goods.validate()){
				return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
						ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
			}
		}
		
		return null;
	}
}
