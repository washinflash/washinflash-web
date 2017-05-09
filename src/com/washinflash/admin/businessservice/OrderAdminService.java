package com.washinflash.admin.businessservice;

import com.washinflash.admin.model.AdminOrderDetails;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.common.exception.SystemException;

public interface OrderAdminService {

	public AdminSearchResult searchOrder(AdminSearchCriteria criteria) throws SystemException;
	public void updateOrder(OrderDetailsBean bean) throws SystemException;
	public void updateOrderDetails(OrderDetailsBean bean) throws SystemException;
	public AdminOrderDetails getAdminOrderDetails(OrderDetailsBean orderDetailsBean) throws SystemException;
	/*public void sendPickupDeliveryNotification() throws SystemException;*/
	
}
