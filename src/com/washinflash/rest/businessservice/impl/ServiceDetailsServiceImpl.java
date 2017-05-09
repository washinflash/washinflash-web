package com.washinflash.rest.businessservice.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.ServiceCategoryDetails;
import com.washinflash.common.object.model.ServiceDetails;
import com.washinflash.common.object.request.ServiceDetailsRequest;
import com.washinflash.common.object.response.PriceListResponse;
import com.washinflash.common.object.response.ServiceDetailsParamResponse;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.rest.businessservice.ServiceDetailsService;
import com.washinflash.rest.dao.ApplicationDAO;
import com.washinflash.rest.dao.ServiceDetailsDAO;

@Service
public class ServiceDetailsServiceImpl implements ServiceDetailsService {

	private static final Logger log = Logger.getLogger(ServiceDetailsService.class);

	@Autowired
	private ServiceDetailsDAO serviceDetailsDAO;
	@Autowired
	private ApplicationDAO applicationDAO;


	@Override
	public String getPriceList(String reqJsonString) throws SystemException {

		//ServiceDetailsRequest serviceDetailsRequest = (ServiceDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, ServiceDetailsRequest.class);
		String jsonResponse = "";

		try {
			
			PriceListResponse response = new PriceListResponse();
			List<List<ServiceDetails>> regularPriceCatList = new ArrayList<>();
			List<List<ServiceDetails>> premiumPriceCatList = new ArrayList<>();
			List<List<ServiceDetails>> dryCleaningPriceCatList = new ArrayList<>();
			
			List<ServiceCategoryDetails> regCatList = applicationDAO.getServiceCategoryList(GenericConstant.SERVICE_TYPE_REGULAR_WASH);
			List<ServiceCategoryDetails> preCatList = applicationDAO.getServiceCategoryList(GenericConstant.SERVICE_TYPE_PREMIUM_WASH);
			List<ServiceCategoryDetails> dryCatList = applicationDAO.getServiceCategoryList(GenericConstant.SERVICE_TYPE_DRY_CLEAN);
			
			for(ServiceCategoryDetails cat : regCatList) {
				List<ServiceDetails> regPriceList = serviceDetailsDAO.getPriceList(cat.getServiceCat());
				if(regPriceList != null && regPriceList.size() > 0) {
					regularPriceCatList.add(regPriceList);
				}
			}
			for(ServiceCategoryDetails cat : preCatList) {
				List<ServiceDetails> prePriceList = serviceDetailsDAO.getPriceList(cat.getServiceCat());
				if(prePriceList != null && prePriceList.size() > 0) {
					premiumPriceCatList.add(prePriceList);
				}
			}
			for(ServiceCategoryDetails cat : dryCatList) {
				List<ServiceDetails> dryPriceList = serviceDetailsDAO.getPriceList(cat.getServiceCat());
				if(dryPriceList != null && dryPriceList.size() > 0) {
					dryCleaningPriceCatList.add(dryPriceList);
				}
			}
			
			response.setRegularPriceCatList(regularPriceCatList);
			response.setPremiumPriceCatList(premiumPriceCatList);
			response.setDryCleaningPriceCatList(dryCleaningPriceCatList);

			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException se) {
			log.debug(se.getCause() + se.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}

	@Override
	public String getServiceDetailsParam(String reqJsonString, Map<String, Object> appParam) throws SystemException {

		ServiceDetailsRequest serviceDetailsRequest = (ServiceDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, ServiceDetailsRequest.class);
		
		String jsonResponse = "";

		try {
			ServiceDetailsParamResponse response = new ServiceDetailsParamResponse();
			Map<String, Object> appParamMap = appParam;

			int minServiceDays = GenericUtils.getIntegerFromString((String) appParamMap.get(GenericConstant.APP_PARAM_MIN_SERVICE_DAYS_KEY));
			int noPickupDays = GenericUtils.getIntegerFromString((String) appParamMap.get(GenericConstant.APP_PARAM_NO_PICKUP_DAYS_KEY));
			int noDeliveryDays = GenericUtils.getIntegerFromString((String) appParamMap.get(GenericConstant.APP_PARAM_NO_DELIVERY_DAYS_KEY));

			int deliveryTime = serviceDetailsRequest.getDeliveryTime();
			if(deliveryTime > 0) {
				// Get no of days from hour 
				minServiceDays = (int) Math.ceil(deliveryTime/24.0);
			}
			
			response.setMinServiceDays(minServiceDays);
			response.setNoOfPickupDays(noPickupDays);
			response.setNoOfDeliveryDays(noDeliveryDays);

			String weeklyCloseDay = (String) appParamMap.get(GenericConstant.APP_PARAM_WEEKLY_CLOSE_DAY_KEY);
			String pickupTimeSlotsStr = (String) appParamMap.get(GenericConstant.APP_PARAM_PICKUP_TIME_SLOTS_KEY);
			String deliveryTimeSlotsStr = (String) appParamMap.get(GenericConstant.APP_PARAM_DELIVERY_TIME_SLOTS_KEY);

			List<String> pickupTimeSlotList = getSplitDataList(pickupTimeSlotsStr);

			response.setPickupTimeSlotList(pickupTimeSlotList);
			response.setDeliveryTimeSlotList(getSplitDataList(deliveryTimeSlotsStr));

			List<String> nextHolidayList = serviceDetailsDAO.getNextHolidays(60);

			List<String> nextAvblBusinessDaysList = getNextAvailableBusinessDaysList(
					(noPickupDays + minServiceDays + noDeliveryDays), weeklyCloseDay, nextHolidayList);
			
			int pickupDisableCount = getPickupTimeDisableCount(pickupTimeSlotList, nextAvblBusinessDaysList);
			
			if(pickupDisableCount == pickupTimeSlotList.size()) {
				pickupDisableCount = 0;
				nextAvblBusinessDaysList.remove(0);
			}

			response.setAvailableDaysList(nextAvblBusinessDaysList);
			
			response.setDisablePickupSlotCountForFirstDay(pickupDisableCount);
			//Delivery disable count should be same as pickup disable count
			response.setDisableDeliverySlotCountForFirstDay(pickupDisableCount);
			
			response.setTodayDateStr(getNextDateFromCurrDate(0));
			response.setTomorrowDateStr(getNextDateFromCurrDate(1));
			
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException se) {
			log.debug(se.getCause() + se.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}	

	private String getNextDateFromCurrDate(int offset) {
		
		Calendar cal = GenericUtils.getISTCalendar();
		SimpleDateFormat format = GenericUtils.getISTSimpleDateFormat(GenericConstant.DEFAULT_DATE_FORMAT);
		cal.add(Calendar.DATE, offset);
		String dateStr = format.format(cal.getTime());
			
		return dateStr;
	}

	private List<String> getNextAvailableBusinessDaysList(int noOfRecord, String weeklyCloseDay, 
			List<String> nextHolidayList) throws SystemException {
		
		Calendar cal = GenericUtils.getISTCalendar();
		List<String> nextAvailableBusinessDaysList = new ArrayList<>();
		SimpleDateFormat format = GenericUtils.getISTSimpleDateFormat(GenericConstant.DEFAULT_DATE_FORMAT);

		while(true) {

			String dateStr = format.format(cal.getTime());
			if(!nextHolidayList.contains(dateStr) && !isWeeklyHoliday(cal.getTime(), weeklyCloseDay)) {
				nextAvailableBusinessDaysList.add(dateStr);
			}
			cal.add(Calendar.DATE, 1);

			if(nextAvailableBusinessDaysList.size() >= noOfRecord) {
				break;
			}
		}		

		return nextAvailableBusinessDaysList;
	}

	private boolean isWeeklyHoliday(Date date, String day) {

		boolean isWeeklyHoliday = false;
		SimpleDateFormat format = GenericUtils.getISTSimpleDateFormat("EEEE");
		
		if(format.format(date).equalsIgnoreCase(day)) {
			isWeeklyHoliday = true;
		}
		return isWeeklyHoliday;
	}


	private int getPickupTimeDisableCount(List<String> timeSlotList, List<String> nextAvblBusinessDaysList) {

		int count = 0;
		Calendar cal = GenericUtils.getISTCalendar();
		SimpleDateFormat format = GenericUtils.getISTSimpleDateFormat(GenericConstant.DEFAULT_DATE_FORMAT);
		if(!nextAvblBusinessDaysList.contains(format.format(cal.getTime()))) {
			return 0;
		}
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		SimpleDateFormat meridiemHourFormat = GenericUtils.getISTSimpleDateFormat("hh aa");

		for (String timeSlot : timeSlotList) {

			String splitArray[] = timeSlot.split("-");
			String timeSlotStr = splitArray[0].trim();
			int slotHour = 0;

			try {
				Date timeSlotDate = meridiemHourFormat.parse(timeSlotStr);
				cal.setTime(timeSlotDate);
				slotHour = cal.get(Calendar.HOUR_OF_DAY);
			} catch (Exception e) {}

			if (slotHour <= currentHour) {
				count++;
			} else {
				break;
			}
		}

		return count;
	}	

	private List<String> getSplitDataList(String dataListStr) {
		List<String> splitDataList = new ArrayList<>();

		if(dataListStr != null) {
			String splitArray[] = dataListStr.split(GenericConstant.DATA_SPLIT_STR);
			for(String item : splitArray) {
				splitDataList.add(item.trim());
			}	
		}		
		return splitDataList;
	}

}
