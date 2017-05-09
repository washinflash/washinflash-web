package com.washinflash.admin.dao;

import java.util.List;

import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.model.ServiceTypeFullDetails;


public interface OrderAdminDAO {

	public AdminSearchResult searchOrder(AdminSearchCriteria criteria) throws SystemException;
	public void updateOrder(OrderDetailsBean bean) throws SystemException;
	public void updateOrderDetails(OrderDetailsBean bean) throws SystemException;
	public OrderDetails getOrderDetails(OrderDetailsBean orderDetailsBean) throws SystemException;
	public List<ServiceTypeFullDetails> getServiceTypeFullDetailsList(int orderid) throws SystemException;
	
}
