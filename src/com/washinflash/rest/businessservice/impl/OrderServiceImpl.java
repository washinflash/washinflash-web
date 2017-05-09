package com.washinflash.rest.businessservice.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.helper.EmailHelper;
import com.washinflash.common.helper.SMSHelper;
import com.washinflash.common.object.model.MyOrderDetails;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.model.OrderTrackDetails;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.object.request.OrderDetailsRequest;
import com.washinflash.common.object.request.UserAddressRequest;
import com.washinflash.common.object.request.UserDetailsRequest;
import com.washinflash.common.object.response.CreateOrderResponse;
import com.washinflash.common.object.response.MyOrderDetailsListResponse;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.common.util.StatusConstant;
import com.washinflash.rest.businessservice.OrderService;
import com.washinflash.rest.dao.OrderDAO;
import com.washinflash.rest.dao.UserDAO;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger log = Logger.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDAO orderDAO;	
	@Autowired
	private UserDAO userDAO;
	

	@Override
	public String createOrder(String reqJsonString, Map<String, Object> appParam) throws SystemException {
		
		OrderDetailsRequest orderDetailsRequest = (OrderDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, OrderDetailsRequest.class);
		String jsonResponse = "";

		try {
			CreateOrderResponse response = new CreateOrderResponse();
			
			UserDetailsRequest userDetailsRequest = new UserDetailsRequest();
			userDetailsRequest.setUserDetailsId(orderDetailsRequest.getUserDetailsId());
			UserDetails userDetails = userDAO.getUserDetails(userDetailsRequest);
			
			String adminPhoneNumber = (String) appParam.get(GenericConstant.APP_PARAM_CONTACT_PHONE_NO_KEY);
			
			Calendar cal = GenericUtils.getISTCalendar();
			
			Date currTime = cal.getTime();
			Date serviceStartTime = GenericUtils.getISTDateFromString("11-06-2016");			
			
			if(currTime.after(serviceStartTime)) {
				String orderRef = orderDAO.createOrder(orderDetailsRequest);
				
				sendConfirmOrderEmail(orderRef, userDetails.getEmail());
				sendConfirmOrderSMS(orderRef, userDetails.getMobileNo());
				
				if(!GenericUtils.isEmpty(adminPhoneNumber)) {
					sendNewOrderNotificationSMSToAdmin(orderRef, adminPhoneNumber, orderDetailsRequest);
				}
				
				response.setSuccessMessage("Your booking has been completed successfully. Reference no is " + orderRef);
				response.setOrderRef(orderRef);
			} else {
				response.setSuccessMessage("Currently we are not taking any booking. We will start our service from 11th June 2016. Please bear with us.");
				response.setOrderRef("Currently we are not taking any booking. We will start our service from 11th June 2016. Please bear with us.");
			}
			
			jsonResponse = GsonUtils.toJsonString(response);
			
		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;

	}

	private void sendConfirmOrderEmail(String refNo, String emailTo) {		
		
		EmailHelper helper = new EmailHelper();
		String msg = "Thank you for placing the booking. Your booking refference no is " + refNo + ".\n\n Thanks,\nWashinflash Team";
		String emailSub = "Booking Confirmation : " + refNo;
		
		helper.sendEmail(GenericConstant.EMAIL_FROM, emailTo, emailSub, msg);
		
	}
	
	private boolean sendConfirmOrderSMS(String refNo, String toMob) {	

		SMSHelper helper = new SMSHelper();
		String msg = "Thank you for placing the booking. Your booking refference no is " + refNo + ".";
		boolean smsSuccess = helper.sendSMS(GenericConstant.SMS_FROM, toMob, msg);

		return smsSuccess;
	}
	
	
	private boolean sendNewOrderNotificationSMSToAdmin(String refNo, String toMob, OrderDetailsRequest orderReq) {	

		SMSHelper helper = new SMSHelper();
		String msg = "New order has been created for pickup on : " + orderReq.getPickupDate() + " @ " + orderReq.getPickupTime() + ". Order ref no is " + refNo + ".";
		boolean smsSuccess = helper.sendSMS(GenericConstant.SMS_FROM, toMob, msg);

		return smsSuccess;
	}
	
	@Override
	public String getMyOrderDetailsList(String reqJsonString) throws SystemException {
		
		OrderDetailsRequest orderDetailsRequest = (OrderDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, OrderDetailsRequest.class);
		String jsonResponse = "";

		try {
			MyOrderDetailsListResponse response = new MyOrderDetailsListResponse();
			List<MyOrderDetails> myOrderDetailsList = new ArrayList<>();
			
			List<OrderDetails> orderDetailsList = orderDAO.getMyOrderDetailsList(orderDetailsRequest);
			
			for(OrderDetails orderDetails : orderDetailsList) {
				MyOrderDetails myOrderDetails = new MyOrderDetails();
				myOrderDetails.setOrderDetails(orderDetails);
				
				UserAddressRequest userAddressRequest = new UserAddressRequest();
				userAddressRequest.setAddressId(orderDetails.getAddressId());
				List<UserAddress> addressList = userDAO.getAddress(userAddressRequest);
				if(addressList != null && addressList.size() > 0) {
					myOrderDetails.setAddress(addressList.get(0));
				}
				
				OrderDetailsRequest orderDetailsReq = new OrderDetailsRequest();
				orderDetailsReq.setOrderId(orderDetails.getOrderId());
				List<String> serviceTypeList = orderDAO.getServiceTypeListForOrder(orderDetailsReq);
				myOrderDetails.setServiceTypeList(serviceTypeList);
				
				OrderTrackDetails orderTrackDetails = new OrderTrackDetails();
				setOrderTrakDetailsInfo(orderTrackDetails, orderDetails);
				myOrderDetails.setOrderTrackDetails(orderTrackDetails);
				
				myOrderDetailsList.add(myOrderDetails);
			}
			
			response.setMyOrderDetailsList(myOrderDetailsList);			
			jsonResponse = GsonUtils.toJsonString(response);
			
		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;

	}
	
	private void setOrderTrakDetailsInfo(OrderTrackDetails orderTrackDetails, OrderDetails orderDetails) {
		
		final String status = orderDetails.getLatestStatus();		
		
		orderTrackDetails.setPickedUp(false);
		orderTrackDetails.setWashed(false);
		orderTrackDetails.setPackaged(false);
		orderTrackDetails.setDelivered(false);
		
		orderTrackDetails.setCancellable(false);
		orderTrackDetails.setReviewable(true);
		
		orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_PENDING_MESSAGE);
		orderTrackDetails.setWashMessage(GenericConstant.DISPLAY_PENDING_MESSAGE);
		orderTrackDetails.setPackagingMessage(GenericConstant.DISPLAY_PENDING_MESSAGE);
		if(GenericConstant.FLAG_YES.equalsIgnoreCase(orderDetails.getDonateOnly())) {
			orderTrackDetails.setDeliveryMessage(GenericConstant.DISPLAY_PENDING_MESSAGE);
		} else {
			orderTrackDetails.setDeliveryMessage(orderDetails.getDeliveryDate());
		}
		orderTrackDetails.setOverallStatus(GenericConstant.DISPLAY_OVERALL_STATUS_INPROGRESS);
		
		if(StatusConstant.BOOKED.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setCancellable(true);
		} else if(StatusConstant.CANCELLED.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_CANCELLED_MESSAGE);
			orderTrackDetails.setWashMessage(GenericConstant.DISPLAY_CANCELLED_MESSAGE);
			orderTrackDetails.setPackagingMessage(GenericConstant.DISPLAY_CANCELLED_MESSAGE);
			orderTrackDetails.setDeliveryMessage(GenericConstant.DISPLAY_CANCELLED_MESSAGE);
			orderTrackDetails.setOverallStatus(GenericConstant.DISPLAY_OVERALL_STATUS_CANCELLED);
			orderTrackDetails.setReviewable(false);
		} else if(StatusConstant.PICKEDUP.toString().equalsIgnoreCase(status) ||
				StatusConstant.WASHING.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setPickedUp(true);
			orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
		} else if(StatusConstant.WASHED.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setPickedUp(true);
			orderTrackDetails.setWashed(true);
			orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setWashMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
		} else if(StatusConstant.PACKAGED.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setPickedUp(true);
			orderTrackDetails.setWashed(true);
			orderTrackDetails.setPackaged(true);
			orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setWashMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setPackagingMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
		} else if(StatusConstant.OUT_FOR_DELIVERY.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setPickedUp(true);
			orderTrackDetails.setWashed(true);
			orderTrackDetails.setPackaged(true);
			orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setWashMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setPackagingMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setDeliveryMessage(GenericConstant.DISPLAY_WAY_TO_DELIVERY_MESSAGE);	
		} else if(StatusConstant.DELIVERED.toString().equalsIgnoreCase(status)) {
			orderTrackDetails.setPickedUp(true);
			orderTrackDetails.setWashed(true);
			orderTrackDetails.setPackaged(true);
			orderTrackDetails.setDelivered(true);
			orderTrackDetails.setPickUpMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setWashMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setPackagingMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);
			orderTrackDetails.setDeliveryMessage(GenericConstant.DISPLAY_COMPLETED_MESSAGE);	
			orderTrackDetails.setOverallStatus(GenericConstant.DISPLAY_OVERALL_STATUS_COMPLETED);
		}
		
	}
	
	@Override
	public String cancelMyOrder(String reqJsonString) throws SystemException {
		
		OrderDetailsRequest orderDetailsRequest = (OrderDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, OrderDetailsRequest.class);
		String jsonResponse = "";

		try {
			
			checkCancellable(orderDetailsRequest);

			OrderDetails updatedOrderDetails = orderDAO.cancelMyOrder(orderDetailsRequest);

			MyOrderDetailsListResponse response = new MyOrderDetailsListResponse();
			List<MyOrderDetails> myOrderDetailsList = new ArrayList<>();
			MyOrderDetails myOrderDetails = new MyOrderDetails();
			
			myOrderDetails.setOrderDetails(updatedOrderDetails);

			OrderTrackDetails orderTrackDetails = new OrderTrackDetails();
			setOrderTrakDetailsInfo(orderTrackDetails, updatedOrderDetails);
			myOrderDetails.setOrderTrackDetails(orderTrackDetails);

			myOrderDetailsList.add(myOrderDetails);

			UserDetailsRequest userDetailsRequest = new UserDetailsRequest();
			userDetailsRequest.setUserDetailsId(orderDetailsRequest.getUserDetailsId());
			UserDetails userDetails = userDAO.getUserDetails(userDetailsRequest);

			sendCancelOrderEmail(updatedOrderDetails.getOrderRef(), userDetails.getEmail());
			sendCancelOrderSMS(updatedOrderDetails.getOrderRef(), userDetails.getMobileNo());

			response.setMyOrderDetailsList(myOrderDetailsList);			
			jsonResponse = GsonUtils.toJsonString(response);
		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}
	
	private void checkCancellable(OrderDetailsRequest orderDetailsRequest) throws SystemException, BusinessException {
		
		OrderDetails orderDetails = orderDAO.getOrderDetails(orderDetailsRequest.getOrderId());
		
		if(!StatusConstant.BOOKED.toString().equals(orderDetails.getLatestStatus())) {
			throw new BusinessException(StatusConstant.FAILED, "You cann't cancel this booking");
		} else {

			Calendar cal = GenericUtils.getISTCalendar();
			Date currTime = cal.getTime();

			String timeSlot = orderDetails.getPickupTime().trim();
			String pickUpDate = orderDetails.getPickupDate().trim();
			
			String splitArray[] = timeSlot.split("-");
			String timeSlotStr = splitArray[0].trim();
			
			String splitStartArray[] = timeSlotStr.split(" ");
			String hour = splitStartArray[0].trim();
			String amPM = splitStartArray[1].trim();
			
			int amPMNumber = Calendar.AM;
			
			if("PM".equalsIgnoreCase(amPM)) {
				amPMNumber = Calendar.PM;	
			}

			try {	
				cal = GenericUtils.getISTCalendar();
				cal.setTime(GenericUtils.getISTDateFromString(pickUpDate));
				cal.set(Calendar.HOUR, GenericUtils.getIntegerFromString(hour));
				cal.set(Calendar.AM_PM, amPMNumber);
				
				//Cancel the order 2 hours prior to the pick up time
				cal.add(Calendar.HOUR_OF_DAY, -2);
				
				
			} catch (Exception e) {}
			
			Date lastCancellationPossibleTime = cal.getTime();
			
			if(lastCancellationPossibleTime.before(currTime)) {
				throw new BusinessException(StatusConstant.FAILED, "You can only cancel the booking 2 hours prior to the pick up time");
			}
		}
	}

	private void sendCancelOrderEmail(String refNo, String emailTo) {	
		
		EmailHelper helper = new EmailHelper();
		String msg = "Your booking with refference no " + refNo + " has been cancelled successfully.\n\n Thanks,\nWashinflash Team";
		String emailSub = "Booking Cancellation : " + refNo;
		
		helper.sendEmail(GenericConstant.EMAIL_FROM, emailTo, emailSub, msg);
		
	}
	
	private boolean sendCancelOrderSMS(String refNo, String toMob) {	

		SMSHelper helper = new SMSHelper();
		String msg = "Your booking with refference no " + refNo + " has been cancelled successfully.";
		boolean smsSuccess = helper.sendSMS(GenericConstant.SMS_FROM, toMob, msg);

		return smsSuccess;
	}
	
}
