package com.zm.order.feignclient;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.order.feignclient.model.GoodsConvert;
import com.zm.order.feignclient.model.OrderBussinessModel;
import com.zm.order.pojo.ResultModel;

@FeignClient("goodscenter")
public interface GoodsFeignClient {

	@RequestMapping(value = "{version}/goods/for-order", method = RequestMethod.POST)
	public ResultModel getPriceAndDelStock(@PathVariable("version") Double version,
			@RequestBody List<OrderBussinessModel> list, @RequestParam("supplierId") Integer supplierId,
			@RequestParam("vip") boolean vip, @RequestParam("centerId") Integer centerId,
			@RequestParam("orderFlag") Integer orderFlag,
			@RequestParam(value = "couponIds", required = false) String couponIds,
			@RequestParam(value = "userId", required = false) Integer userId);

	@RequestMapping(value = "auth/{version}/goods/goodsSpecs", method = RequestMethod.GET)
	public ResultModel listGoodsSpecs(@PathVariable("version") Double version, @RequestParam("itemIds") String ids,
			@RequestParam("centerId") Integer centerId, @RequestParam("source") String source);

	@RequestMapping(value = "auth/{version}/goods/active", method = RequestMethod.GET)
	public ResultModel getActivity(@PathVariable("version") Double version, @RequestParam("type") Integer type,
			@RequestParam("typeStatus") Integer typeStatus, @RequestParam("centerId") Integer centerId);

	@RequestMapping(value = "{version}/goods/stockback", method = RequestMethod.POST)
	public ResultModel stockBack(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list,
			@RequestParam("orderFlag") Integer orderFlag);

	@RequestMapping(value = "{version}/goods/costPrice", method = RequestMethod.POST)
	public Double getCostPrice(@PathVariable("version") Double version, @RequestBody List<OrderBussinessModel> list);

	@RequestMapping(value = "{version}/goods/for-buttjoinorder", method = RequestMethod.POST)
	public ResultModel delButtjoinOrderStock(@PathVariable("version") Double version,
			@RequestBody List<OrderBussinessModel> list, @RequestParam("supplierId") Integer supplierId,
			@RequestParam("orderFlag") Integer orderFlag);

	@RequestMapping(value = "{version}/goods/list-itemId", method = RequestMethod.POST)
	public Map<String, GoodsConvert> listSkuAndConversionByItemId(@PathVariable("version") Double version, @RequestBody Set<String> set);
	
	@RequestMapping(value = "{version}/goods/tag/presell", method = RequestMethod.POST)
	public List<String> listPreSellItemIds(@PathVariable("version") Double version);
}
