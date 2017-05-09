package com.washinflash.admin.dao;

import java.util.List;

import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.VendorDetails;


public interface VendorAdminDAO {

	public AdminSearchResult searchVendorOrder(AdminSearchCriteria criteria) throws SystemException;
	public void updateVendorOrder(OrderDetailsBean bean) throws SystemException;
	public List<VendorDetails> getVendorList(String venType) throws SystemException;

}
