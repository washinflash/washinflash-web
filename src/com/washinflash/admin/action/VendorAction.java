package com.washinflash.admin.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.washinflash.admin.businessservice.VendorAdminService;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.admin.model.SearchOrderDetails;
import com.washinflash.admin.util.DownloadUtils;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.VendorDetails;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;

@Controller
@RequestMapping("/admin/vendor")
public class VendorAction {

	static final Logger log = Logger.getLogger(VendorAction.class);
	
	@Autowired
	private VendorAdminService vendorAdminService;


	@RequestMapping(value="/showVendorSearchOrder.do", method = RequestMethod.POST)
	public String showVendorSearchOrder(ModelMap model) {

		Map<String, String> statusMap = new LinkedHashMap<>();
		statusMap.put("OPEN", "Open");
		statusMap.put("", "Any Status");
		statusMap.put(StatusConstant.PICKEDUP.toString(), "Picked Up");
		statusMap.put(StatusConstant.WASHING.toString(), "Washing");
		statusMap.put(StatusConstant.WASHED.toString(), "Washed");
		
		Map<String, String> serviceTypeMap = new LinkedHashMap<>();
		serviceTypeMap.put("", "Any Type");
		serviceTypeMap.put("Regular Wash", "Regular Wash");
		serviceTypeMap.put("Premium Wash", "Premium Wash");
		serviceTypeMap.put("Dry Cleaning", "Dry Cleaning");
		
		Map<Integer, String> searchVendorMap = new LinkedHashMap<>();
		searchVendorMap.put(0, "Any Vendor");
		
		Map<Integer, String> vendorMap = new LinkedHashMap<>();
		vendorMap.put(0, "Please select");
		
		try {
			List<VendorDetails> vendorList = vendorAdminService.getVendorList(null);
			for(VendorDetails ven : vendorList) {
				searchVendorMap.put(ven.getVendorId(), ven.getName());
				vendorMap.put(ven.getVendorId(), ven.getName());
			}
		} catch (SystemException se) {}

		
		model.put("statusMap", statusMap);
		model.put("serviceTypeMap", serviceTypeMap);
		model.put("searchVendorMap", searchVendorMap);
		model.put("vendorMap", vendorMap);
		
		return "searchVendorOrder";
	}
		
	
	@RequestMapping(value="/searchVendorOrder.do", method = RequestMethod.POST)
	public String searchVendorOrder(@RequestBody AdminSearchCriteria criteria, ModelMap model) {

		AdminSearchResult searchResult = new AdminSearchResult();
		
		try {
			searchResult = vendorAdminService.searchVendorOrder(criteria);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		model.put("searchResult", searchResult);
		
		return "searchVendorOrderResult";
	}
	
	
	@RequestMapping(value="/updateVendorOrder.do", method = RequestMethod.POST)
	public String updateVendorOrder(@RequestBody OrderDetailsBean bean) {
		
		try {
			vendorAdminService.updateVendorOrder(bean);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		return "empty";
	}
	
	
	@RequestMapping(value="/downloadAllVendorOrders.do")
	public void downloadAllVendorOrders(@ModelAttribute AdminSearchCriteria criteria, HttpServletResponse response) {

		AdminSearchResult searchResult = new AdminSearchResult();
		String COMMA_DELIMITER = ",";
		String NEW_LINE_SEPARATOR = "\n";
		
		try {
			criteria.setFetchAllOrders(true);
			searchResult = vendorAdminService.searchVendorOrder(criteria);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		List<SearchOrderDetails> orderList = searchResult.getOrderDetailsList();
		StringBuffer csvContents = new StringBuffer("Order Ref, Order Status, Service Status, Vendor Name, Vendor Mobile No, Area, PIN Code, Customer Pick-Up Date, Vendor Pick-Up Time, Delivery Date, Service Type" + NEW_LINE_SEPARATOR);
		
		for(SearchOrderDetails orderDetails : orderList) {
			csvContents.append(orderDetails.getOrderRef() + COMMA_DELIMITER)
			.append(orderDetails.getLatestStatus() + COMMA_DELIMITER)
			.append(orderDetails.getServiceStatus() + COMMA_DELIMITER)
			.append(GenericUtils.replaceNullString(orderDetails.getName()) + COMMA_DELIMITER)
			.append(GenericUtils.replaceNullString(orderDetails.getMobileNo()) + COMMA_DELIMITER)
			.append(orderDetails.getServiceAreaName() + COMMA_DELIMITER)
			.append(orderDetails.getPinCode() + COMMA_DELIMITER)
			.append(orderDetails.getPickupDate() + COMMA_DELIMITER)
			.append(GenericUtils.replaceNullString(orderDetails.getVendorPickupDate()) + COMMA_DELIMITER)
			.append(orderDetails.getDeliveryDate() + COMMA_DELIMITER)
			.append(orderDetails.getServiceType() + NEW_LINE_SEPARATOR);
		}
		
		DownloadUtils downloadUtils = new DownloadUtils();
		downloadUtils.downloadFile(response, "AllVendorOrders.csv", csvContents.toString().getBytes());
		
	}
	
}
