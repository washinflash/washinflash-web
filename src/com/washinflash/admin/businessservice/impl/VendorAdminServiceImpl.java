package com.washinflash.admin.businessservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.washinflash.admin.businessservice.VendorAdminService;
import com.washinflash.admin.dao.VendorAdminDAO;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.VendorDetails;

@Service
public class VendorAdminServiceImpl implements VendorAdminService {

	//private static final Logger log = Logger.getLogger(OrderAdminServiceImpl.class);

	@Autowired
	private VendorAdminDAO vendorAdminDAO;	
	

	@Override
	public AdminSearchResult searchVendorOrder(AdminSearchCriteria criteria) throws SystemException {
		
		return vendorAdminDAO.searchVendorOrder(criteria);
	}
	
	@Override
	public void updateVendorOrder(OrderDetailsBean bean) throws SystemException {
		
		vendorAdminDAO.updateVendorOrder(bean);
	}

	@Override
	public List<VendorDetails> getVendorList(String venType) throws SystemException {
		
		return vendorAdminDAO.getVendorList(venType);
	}
}
