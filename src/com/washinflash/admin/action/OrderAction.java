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

import com.washinflash.admin.businessservice.OrderAdminService;
import com.washinflash.admin.businessservice.UserAdminService;
import com.washinflash.admin.model.AdminOrderDetails;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.admin.model.SearchOrderDetails;
import com.washinflash.admin.util.DownloadUtils;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.EmployeeDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;

@Controller
@RequestMapping("/admin/order")
public class OrderAction {

	private static final Logger log = Logger.getLogger(OrderAction.class);
	
	@Autowired
	private OrderAdminService orderAdminService;
	@Autowired
	private UserAdminService userAdminService;


	@RequestMapping(value="/showSearchOrder.do", method = RequestMethod.POST)
	public String showSearchOrder(ModelMap model) {

		Map<String, String> statusMap = new LinkedHashMap<>();
		statusMap.put("OPEN", "Open");
		statusMap.put("", "Any Status");
		statusMap.put(StatusConstant.BOOKED.toString(), "Booked");
		statusMap.put(StatusConstant.PICKEDUP.toString(), "Picked Up");
		statusMap.put(StatusConstant.WASHED.toString(), "Washed");
		statusMap.put(StatusConstant.PACKAGED.toString(), "Packaged");
		statusMap.put(StatusConstant.DELIVERED.toString(), "Delivered");
		statusMap.put(StatusConstant.CANCELLED.toString(), "Cancelled");

		Map<String, String> pickupTimeMap = new LinkedHashMap<>();
		pickupTimeMap.put("", "Any Time");
		pickupTimeMap.put("9 AM - 11 AM", "9 AM - 11 AM");
		pickupTimeMap.put("11 AM - 1 PM", "11 AM - 1 PM");
		pickupTimeMap.put("1 PM - 3 PM", "1 PM - 3 PM");
		pickupTimeMap.put("3 PM - 5 PM", "3 PM - 5 PM");
		pickupTimeMap.put("5 PM - 7 PM", "5 PM - 7 PM");	
		
		Map<String, String> deliveryTimeMap = new LinkedHashMap<>();
		deliveryTimeMap.put("", "Any Time");
		deliveryTimeMap.put("9 AM - 11 AM", "9 AM - 11 AM");
		deliveryTimeMap.put("11 AM - 1 PM", "11 AM - 1 PM");
		deliveryTimeMap.put("3 PM - 5 PM", "3 PM - 5 PM");
		deliveryTimeMap.put("5 PM - 7 PM", "5 PM - 7 PM");		
		
		Map<String, String> deliveryTypeMap = new LinkedHashMap<>();
		deliveryTypeMap.put("", "Any Type");
		deliveryTypeMap.put("Normal Delivery", "Normal Delivery");
		deliveryTypeMap.put("Express Delivery", "Express Delivery");
		
		Map<String, String> serviceTypeMap = new LinkedHashMap<>();
		serviceTypeMap.put("", "Any Type");
		serviceTypeMap.put("Regular Wash", "Regular Wash");
		serviceTypeMap.put("Premium Wash", "Premium Wash");
		serviceTypeMap.put("Dry Cleaning", "Dry Cleaning");
		serviceTypeMap.put("Donate Charity", "Donate Charity");
		serviceTypeMap.put("DONATE_ONLY", "Donate Only");
		
		Map<Integer, String> employeeMap = new LinkedHashMap<>();
		employeeMap.put(0, "Please select");
		
		try {
			List<EmployeeDetails> employeeList = userAdminService.getEmployeeList(GenericConstant.EMP_TYPE_DELIVERY_RES);
			for(EmployeeDetails emp : employeeList) {
				employeeMap.put(emp.getEmployeeId(), emp.getName());
			}
		} catch (SystemException se) {}
		
		model.put("statusMap", statusMap);
		model.put("pickupTimeMap", pickupTimeMap);
		model.put("deliveryTimeMap", deliveryTimeMap);
		model.put("deliveryTypeMap", deliveryTypeMap);
		model.put("serviceTypeMap", serviceTypeMap);
		model.put("employeeMap", employeeMap);
		
		return "searchOrder";
	}
		
	
	@RequestMapping(value="/searchOrder.do", method = RequestMethod.POST)
	public String searchOrder(@RequestBody AdminSearchCriteria criteria, ModelMap model) {

		AdminSearchResult searchResult = new AdminSearchResult();
		
		try {
			searchResult = orderAdminService.searchOrder(criteria);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		model.put("searchResult", searchResult);
		
		return "searchOrderResult";
	}
	
	
	@RequestMapping(value="/updateOrder.do", method = RequestMethod.POST)
	public String updateOrder(@RequestBody OrderDetailsBean bean) {
		
		try {
			orderAdminService.updateOrder(bean);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		return "empty";
	}
	
	
	@RequestMapping(value="/showOrderDetails.do", method = RequestMethod.GET)
	public String showOrderDetails(@ModelAttribute OrderDetailsBean orderDetailsBean, ModelMap model) {
		
		AdminOrderDetails adminOrderDetails = new AdminOrderDetails();
		
		try {
			adminOrderDetails = orderAdminService.getAdminOrderDetails(orderDetailsBean);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		model.put("adminOrderDetails", adminOrderDetails);
		
		return "orderDetails";
	}

	
	@RequestMapping(value="/updateOrderDetails.do", method = RequestMethod.POST)
	public String updateOrderDetails(@RequestBody OrderDetailsBean bean) {
		
		try {
			orderAdminService.updateOrderDetails(bean);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		return "empty";
	}
	
	
	@RequestMapping(value="/downloadAllOrders.do")
	public void downloadAllOrders(@ModelAttribute AdminSearchCriteria criteria, HttpServletResponse response) {

		AdminSearchResult searchResult = new AdminSearchResult();
		String COMMA_DELIMITER = ",";
		String NEW_LINE_SEPARATOR = "\n";
		
		try {
			criteria.setFetchAllOrders(true);
			searchResult = orderAdminService.searchOrder(criteria);
		}  catch (Exception e) {
			log.error(e.getMessage() + e);
		}
		
		List<SearchOrderDetails> orderList = searchResult.getOrderDetailsList();
		StringBuffer csvContents = new StringBuffer("Order Ref, Status, Customer Name, Customer Mobile No, Area, PIN Code, Pick-Up Date, Pick-Up Time, Delivery Date, Delivery Time, Delivery Type, Service Type" + NEW_LINE_SEPARATOR);
		
		for(SearchOrderDetails orderDetails : orderList) {
			csvContents.append(orderDetails.getOrderRef() + COMMA_DELIMITER)
			.append(orderDetails.getLatestStatus() + COMMA_DELIMITER)
			.append(orderDetails.getName() + COMMA_DELIMITER)
			.append(orderDetails.getMobileNo() + COMMA_DELIMITER)
			.append(orderDetails.getServiceAreaName() + COMMA_DELIMITER)
			.append(orderDetails.getPinCode() + COMMA_DELIMITER)
			.append(orderDetails.getPickupDate() + COMMA_DELIMITER)
			.append(orderDetails.getPickupTime() + COMMA_DELIMITER)
			.append(GenericUtils.replaceNullString(orderDetails.getDeliveryDate()) + COMMA_DELIMITER)
			.append(GenericUtils.replaceNullString(orderDetails.getDeliveryTime()) + COMMA_DELIMITER)
			.append(orderDetails.getDeliveryType() + COMMA_DELIMITER)
			.append(orderDetails.getServiceType().replaceAll(",", " #") + NEW_LINE_SEPARATOR);
		}
		
		DownloadUtils downloadUtils = new DownloadUtils();
		downloadUtils.downloadFile(response, "AllOrders.csv", csvContents.toString().getBytes());
		
	}
}
