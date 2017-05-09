package com.washinflash.rest.dao;

import java.util.List;

import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.request.OrderDetailsRequest;

public interface OrderDAO {

	public String createOrder(OrderDetailsRequest orderDetailsRequest) throws BusinessException, SystemException;
	public List<OrderDetails> getMyOrderDetailsList(OrderDetailsRequest orderDetailsRequest) throws SystemException;
	public List<String> getServiceTypeListForOrder(OrderDetailsRequest orderDetailsRequest) throws SystemException;
	public OrderDetails cancelMyOrder(OrderDetailsRequest orderDetailsRequest) throws BusinessException, SystemException;
	public OrderDetails getOrderDetails(int orderId) throws SystemException;
}
