package com.zm.supplier.bussiness.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.zm.supplier.pojo.Express;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SupplierEntity;

public interface SupplierService {

	List<Express> listExpressBySupplierId(Integer supplierId);

	void sendOrder(List<OrderInfo> infoList);

	/**  
	 * queryByPage:查询分页效果. <br/>  
	 *  
	 * @author hebin  
	 * @param supplier
	 * @return  
	 * @since JDK 1.7  
	 */
	Page<SupplierEntity> queryByPage(SupplierEntity supplier);

	/**  
	 * saveSupplier:插入供应商表. <br/>  
	 *  
	 * @author hebin  
	 * @param entity  
	 * @since JDK 1.7  
	 */
	void saveSupplier(SupplierEntity entity);

	/**  
	 * queryById:根据编号查询. <br/>  
	 *  
	 * @author hebin  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	SupplierEntity queryById(int id);

	/**  
	 * checkOrderStatus:查询订单状态. <br/>  
	 *  
	 * @author wqy  
	 * @param id
	 * @return  
	 * @since JDK 1.7  
	 */
	List<OrderStatus> checkOrderStatus(List<OrderIdAndSupplierId> list);
}
