
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>

	$(function() {
			
		$('#actionbuttontablediv').show();

		var disableFirstRecordButton = "${searchResult.disableFirstRecordButton}" === 'true';
		var disablePrevRecordButton = "${searchResult.disablePrevRecordButton}" === 'true';
		var disableNextRecordButton = "${searchResult.disableNextRecordButton}" === 'true';
		var disableLastRecordButton = "${searchResult.disableLastRecordButton}" === 'true';		
		
		$("#firstRecordButton").prop("disabled", disableFirstRecordButton);
		$("#prevRecordButton").prop("disabled", disablePrevRecordButton);
		$("#nextRecordButton").prop("disabled", disableNextRecordButton);
		$("#lastRecordButton").prop("disabled", disableLastRecordButton);
		
		var lastRecordIndex = "${searchResult.lastRecordIndex}";
		$('#lastRecordIndex').val(lastRecordIndex);
		
		$("#assignVendorButton").prop("disabled", true);
		$("#markWashedButton").prop("disabled", true);
		
	});

	function toggleSelection(source) {
		
		var checkboxes = document.getElementsByName('orderCheckbox');
		for( var i = 0, n = checkboxes.length; i < n; i++) {
			checkboxes[i].checked = source.checked;
		}
		
		if(!source.checked) {
			$("#assignVendorButton").prop("disabled", true);
			$("#markWashedButton").prop("disabled", true);		
		} else {			
			enableDiableButtons();
		}
	}

	function enableDiableButtons() {

		var checkboxall = document.getElementById('orderCheckboxAll');
		var checkboxes = document.getElementsByName('orderCheckbox');

		var enableAssignVendorButton = true;
		var enableWashedButton = true;

		var checkSelectAllCheckbox = true;

		var noneSelected = true;

		for ( var i = 0, n = checkboxes.length; i < n; i++) {
			if (checkboxes[i].checked) {
				noneSelected = false;
				var status = document.getElementById("orderStatus_"
						+ checkboxes[i].id).innerHTML;
				if (status === 'PICKEDUP') {
					enableAssignVendorButton = enableAssignVendorButton && true;
					enableWashedButton = enableWashedButton && false;
				} else if (status === 'WASHING') {
					enableAssignVendorButton = enableAssignVendorButton && false;
					enableWashedButton = enableWashedButton && true;
				} else {
					enableAssignVendorButton = enableAssignVendorButton && false;
					enableWashedButton = enableWashedButton && false;
				}
			} else {
				checkSelectAllCheckbox = false;
			}
		}
		if (noneSelected) {
			enableAssignVendorButton = false;
			enableWashedButton = false;
		}

		$("#assignVendorButton").prop("disabled", !enableAssignVendorButton);
		$("#markWashedButton").prop("disabled", !enableWashedButton);
		checkboxall.checked = checkSelectAllCheckbox;
	}
	
</script>

<div>

<c:choose>
	<c:when test="${fn:length(searchResult.orderDetailsList) gt 0}"> 
	
	<div class="searchResultTable">
		<table>
			<tr>
				<td><input type="checkbox" id="orderCheckboxAll" onClick="toggleSelection(this)"/></td>
				<td nowrap="nowrap">Order Ref</td>
				<td nowrap="nowrap">Order Status</td>
				<td nowrap="nowrap">Service Status</td>
				<td nowrap="nowrap">Ven Name</td>
				<td nowrap="nowrap">Ven Mobile No</td>
				<td nowrap="nowrap">Area</td>
				<td nowrap="nowrap">PIN</td>
				<td nowrap="nowrap">Cust Pick Date</td>
				<td nowrap="nowrap">Vendor Pick Time</td>
				<td nowrap="nowrap">Del Date</td>
				<td nowrap="nowrap">Service Type</td>
			</tr>
			
			<c:forEach var="searchResult" items="${searchResult.orderDetailsList}" varStatus="list">
				<tr>
					<td><input type="checkbox" name="orderCheckbox" value="${searchResult.orderId}_${searchResult.orderServiceMappingId}" onClick="enableDiableButtons()" id="${list.index}"/></td>
					<td><p>${searchResult.orderRef}</p></td>
					<td><p>${searchResult.latestStatus}</p></td>
					<td><p id="orderStatus_${list.index}">${searchResult.serviceStatus}</p></td>
					<td><p>${searchResult.name}</p></td>
					<td><p>${searchResult.mobileNo}</p></td>
					<td><p>${searchResult.serviceAreaName}</p></td>
					<td><p>${searchResult.pinCode}</p></td>
					<td><p>${searchResult.pickupDate}</p></td>
					<td><p>${searchResult.vendorPickupDate}</p></td>
					<td><p>${searchResult.deliveryDate}</p></td>
					<td><p>${searchResult.serviceType}</p></td>
			</tr>
			</c:forEach>
			
		</table>
	</div>
	
	</c:when>
	<c:otherwise>
		<div>
			<p id="noresultpara">No order found for the search criteria provided.</p>
			<script>
				$(function() {
					$('#actionbuttontablediv').hide();
				});
			</script>
		</div>
	</c:otherwise>   
</c:choose>

</div>