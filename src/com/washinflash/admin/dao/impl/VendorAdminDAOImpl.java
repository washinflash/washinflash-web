package com.washinflash.admin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.washinflash.admin.dao.VendorAdminDAO;
import com.washinflash.admin.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.admin.mapper.AdminVendorSearchMapper;
import com.washinflash.admin.mapper.VendorDetailsMapper;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.admin.model.SearchOrderDetails;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.VendorDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;


@Component
public class VendorAdminDAOImpl extends SpringJDBCTemplate implements VendorAdminDAO {
	
	
	@Override
	public AdminSearchResult searchVendorOrder(AdminSearchCriteria criteria) throws SystemException {

		StringBuffer searchQuery = new StringBuffer();
		
		String selectClause = "DET.ORDER_ID, MAP.ORDER_SERVICE_MAPPING_ID, DET.ORDER_REFERENCE, DET.PICKUP_DATE, MAP.ASSIGNED_VENDOR_ON, MAP.VENDOR_NAME, " +
				"MAP.MOBILE_NO, DET.DELIVERY_DATE, HIST.STATUS, MAP.ORDER_STATUS, ADDR.PIN_CODE, SERV.SERVICE_TYPE_DESC";
		
		String selectCountClause = "COUNT(*)";
		
		String fromClause = "ORDER_DETAILS DET, " +
				"(SELECT * FROM ORDER_HISTORY WHERE ORDER_HISTORY_ID IN (SELECT MAX(ORDER_HISTORY_ID) FROM ORDER_HISTORY WHERE STATUS <> 'DELETED' GROUP BY ORDER_ID)) HIST, " + 
				"(SELECT MAP.ORDER_ID, MAP.ORDER_SERVICE_MAPPING_ID, MAP.SERVICE_TYPE_ID, MAP.ASSIGNED_VENDOR_ON, IFNULL(MAP.ORDER_STATUS, 'PICKEDUP') ORDER_STATUS, VEND.VENDOR_DETAILS_ID, VEND.VENDOR_NAME, VEND.MOBILE_NO FROM ORDER_SERVICE_MAPPING MAP LEFT OUTER JOIN VENDOR_DETAILS VEND ON VEND.VENDOR_DETAILS_ID = MAP.ASSIGNED_VENDOR_ID) MAP, " +
				"SERVICE_TYPE SERV, " +
				"USER_ADDRESS ADDR";
		String orderbyClause = "DET.ORDER_ID DESC, MAP.SERVICE_TYPE_ID";
		String limitClause = " LIMIT ? OFFSET ? ";
		
		StringBuffer whereClause = new StringBuffer();
		
		whereClause.append("DET.ORDER_ID = HIST.ORDER_ID AND DET.ADDRESS_ID = ADDR.ADDRESS_ID " +
				"AND DET.STATUS = ? AND SERV.SERVICE_TYPE_ID = MAP.SERVICE_TYPE_ID AND MAP.ORDER_ID = DET.ORDER_ID  AND SERV.SERVICE_TYPE_DESC <> ? ");
		
		List<Object> queryParam = new ArrayList<>();
		queryParam.add(StatusConstant.ACTIVE.toString());
		queryParam.add(GenericConstant.DONATE_CHARITY);
		
		String status = criteria.getLatestStatus();
		
		if(!GenericUtils.isEmpty(status)) {
			if(GenericConstant.ALL_OPEN_STATUS.equals(status)) {
				whereClause.append(" AND HIST.STATUS IN (" + "?, ?" + ")");
				queryParam.add(StatusConstant.PICKEDUP.toString());
				queryParam.add(StatusConstant.WASHING.toString());
			} else {
				whereClause.append(" AND HIST.STATUS = ? ");
				queryParam.add(criteria.getLatestStatus());				
			}
		} else {
			whereClause.append(" AND HIST.STATUS IN (" + "?, ?, ?" + ")");
			queryParam.add(StatusConstant.PICKEDUP.toString());
			queryParam.add(StatusConstant.WASHING.toString());
			queryParam.add(StatusConstant.WASHED.toString());
		}
		if(!GenericUtils.isEmpty(criteria.getCustPickupDate())) {
			whereClause.append(" AND DET.PICKUP_DATE = ? ");
			queryParam.add(GenericUtils.getDateFromString(criteria.getCustPickupDate()));
		}
		if(!GenericUtils.isEmpty(criteria.getVendPickupDate())) {
			whereClause.append(" AND DATE(MAP.ASSIGNED_VENDOR_ON) = ? ");
			queryParam.add(GenericUtils.getDateFromString(criteria.getVendPickupDate()));
		}
		if(!GenericUtils.isEmpty(criteria.getDeliveryDate())) {
			whereClause.append(" AND DET.DELIVERY_DATE = ? ");
			queryParam.add(GenericUtils.getDateFromString(criteria.getDeliveryDate()));
		}
		if(criteria.getVendorId() != 0) {
			whereClause.append(" AND MAP.VENDOR_DETAILS_ID  = ? ");
			queryParam.add(criteria.getVendorId());
		}
		if(!GenericUtils.isEmpty(criteria.getServiceType())) {
			whereClause.append(" AND SERV.SERVICE_TYPE_DESC = ? ");
			queryParam.add(criteria.getServiceType());
		}
		if(!GenericUtils.isEmpty(criteria.getSpecialField())) {
			whereClause.append(" AND (UPPER(DET.ORDER_REFERENCE) LIKE ? ")
			.append(" OR UPPER(MAP.VENDOR_NAME) LIKE ? ")
			.append(" OR UPPER(MAP.MOBILE_NO) LIKE ? ")
			.append(" OR UPPER(ADDR.PIN_CODE) LIKE ? ) ");
			
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
		}
		
		
		StringBuffer countQuery = new StringBuffer()
		.append(" SELECT ")
		.append(selectCountClause)
		.append(" FROM ")
		.append(fromClause)
		.append(" WHERE ")
		.append(whereClause);
		
		int recordCount = getJDBCTemplateObject().queryForObject(countQuery.toString(), queryParam.toArray(), Integer.class);
		int startIndex = 0;
		int prevLastRecordIndex = criteria.getLastRecordIndex();
		int pageSize = GenericConstant.SEARCH_PAGE_SIZE;
		
		if(GenericConstant.SEARCH_TYPE_SEARCH.equalsIgnoreCase(criteria.getSearchType()) || 
				GenericConstant.SEARCH_TYPE_FIRST.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = 0;
		} else if(GenericConstant.SEARCH_TYPE_REFRESH.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = prevLastRecordIndex - ((prevLastRecordIndex % pageSize));
		} else if(GenericConstant.SEARCH_TYPE_PREV.equalsIgnoreCase(criteria.getSearchType())) {
			int prevPageRecordCount = (prevLastRecordIndex % pageSize) + 1;
			startIndex = prevLastRecordIndex - pageSize - prevPageRecordCount + 1;
		} else if(GenericConstant.SEARCH_TYPE_NEXT.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = prevLastRecordIndex + 1;
		} else if(GenericConstant.SEARCH_TYPE_LAST.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = recordCount - (recordCount % pageSize);
		} else {
			startIndex = 0;
		}
		
		if(startIndex < 0) {
			startIndex = 0;
		}
		
		searchQuery.append(" SELECT ")
		.append(selectClause)
		.append(" FROM ")
		.append(fromClause)
		.append(" WHERE ")
		.append(whereClause)
		.append(" ORDER BY ")
		.append(orderbyClause);
		
		if(!criteria.isFetchAllOrders()) {
			searchQuery.append(limitClause);
			queryParam.add(pageSize);
			queryParam.add(startIndex);
		}
		
		List<SearchOrderDetails> orderDetailsList = getJDBCTemplateObject().query(searchQuery.toString(), queryParam.toArray(),
				new AdminVendorSearchMapper());
		
		orderDetailsList = (orderDetailsList == null) ? new ArrayList<SearchOrderDetails>() : orderDetailsList;
		
		int newLastRecordIndex = (recordCount < (startIndex + pageSize)) ? (recordCount - 1) : (startIndex + pageSize - 1);
		
		AdminSearchResult searchResult = new AdminSearchResult();
		searchResult.setOrderDetailsList(orderDetailsList);
		searchResult.setLastRecordIndex(newLastRecordIndex);
		
		if(startIndex == 0) {
			searchResult.setDisableFirstRecordButton(true);
			searchResult.setDisablePrevRecordButton(true);
		}		
		if(newLastRecordIndex >= (recordCount - 1)) {
			searchResult.setDisableLastRecordButton(true);
			searchResult.setDisableNextRecordButton(true);
		}
		
		return searchResult;
	}
	
	
	@Override
	public void updateVendorOrder(OrderDetailsBean bean) throws SystemException {
	
		int[] selectedOrderServices = bean.getSelectedOrderService();
		int[] selectedOrders = bean.getSelectedOrder();
		
		if(selectedOrderServices != null) {

			for(int i = 0; i < selectedOrderServices.length; i++) {
				
				int orderServiceMapId = selectedOrderServices[i];
				int orderId = selectedOrders[i];
				
				if(StatusConstant.WASHING.toString().equals(bean.getUpdateType()) && bean.getAssignToVendor() != 0) {

					String updateSQL = "UPDATE ORDER_SERVICE_MAPPING SET UPDATED_ON = ?, ASSIGNED_VENDOR_ID = ?, ASSIGNED_VENDOR_ON = ?, ORDER_STATUS = ? " +
							"WHERE ORDER_SERVICE_MAPPING_ID = ? AND STATUS = ?";
					getJDBCTemplateObject().update(updateSQL, GenericUtils.getCurrentDateTime(), bean.getAssignToVendor(), GenericUtils.getCurrentDateTime(), 
							StatusConstant.WASHING.toString(), orderServiceMapId, StatusConstant.ACTIVE.toString());
					
					String selectSQL = "SELECT COUNT(*) FROM ORDER_SERVICE_MAPPING MAP, SERVICE_TYPE SERV " +
							"WHERE SERV.SERVICE_TYPE_ID = MAP.SERVICE_TYPE_ID AND SERV.SERVICE_TYPE_DESC <> ? AND MAP.ORDER_ID = ? " +
							"AND MAP.ORDER_STATUS IS NULL AND MAP.STATUS = ?";
					int recordCount = getJDBCTemplateObject().queryForObject(
							selectSQL, new Object[] { GenericConstant.DONATE_CHARITY, orderId, StatusConstant.ACTIVE.toString() }, Integer.class);
					
					if(recordCount == 0) {
						String insertSQL = "INSERT INTO ORDER_HISTORY (ORDER_ID, UPDATED_ON, COMMENTS, STATUS) VALUES(?, ?, ?, ?)";
						getJDBCTemplateObject().update(insertSQL, orderId, 
								GenericUtils.getCurrentDateTime(), null, StatusConstant.WASHING.toString());
					}
					
				} else if(StatusConstant.WASHED.toString().equals(bean.getUpdateType())) {
					
					String updateSQL = "UPDATE ORDER_SERVICE_MAPPING SET UPDATED_ON = ?, ORDER_STATUS = ? WHERE ORDER_SERVICE_MAPPING_ID = ? AND STATUS = ?";
					getJDBCTemplateObject().update(updateSQL, GenericUtils.getCurrentDateTime(), StatusConstant.WASHED.toString(), 
							orderServiceMapId, StatusConstant.ACTIVE.toString());

					String selectSQL = "SELECT COUNT(*) FROM ORDER_SERVICE_MAPPING MAP, SERVICE_TYPE SERV " +
							"WHERE SERV.SERVICE_TYPE_ID = MAP.SERVICE_TYPE_ID AND SERV.SERVICE_TYPE_DESC <> ? AND MAP.ORDER_ID = ? " +
							"AND IFNULL(MAP.ORDER_STATUS, ?) <> ? AND MAP.STATUS = ?";
					int recordCount = getJDBCTemplateObject().queryForObject(
							selectSQL, new Object[] { GenericConstant.DONATE_CHARITY, orderId, StatusConstant.PICKEDUP.toString(), 
									StatusConstant.WASHED.toString(), StatusConstant.ACTIVE.toString() }, Integer.class);
					
					if(recordCount == 0) {
						String insertSQL = "INSERT INTO ORDER_HISTORY (ORDER_ID, UPDATED_ON, COMMENTS, STATUS) VALUES(?, ?, ?, ?)";
						getJDBCTemplateObject().update(insertSQL, orderId, 
								GenericUtils.getCurrentDateTime(), null, StatusConstant.WASHED.toString());
					}
					
				}

			}
		}
		
	}
	
	@Override
	public List<VendorDetails> getVendorList(String venType) throws SystemException {
		
		StringBuffer query = new StringBuffer("SELECT * FROM VENDOR_DETAILS WHERE STATUS = ? ");
		List<Object> queryParam = new ArrayList<>();
		queryParam.add(StatusConstant.ACTIVE.toString());
		
		if(!GenericUtils.isEmpty(venType)) {
			query.append(" AND VENDOR_TYPE = ?");
			queryParam.add(venType);
		}
		
		List<VendorDetails> vendorList = getJDBCTemplateObject().query(query.toString(), queryParam.toArray(),
				new VendorDetailsMapper());
		
		
		return vendorList;
	}
		
}
