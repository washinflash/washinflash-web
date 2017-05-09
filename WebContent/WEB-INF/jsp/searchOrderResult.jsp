
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
		
		$("#markPickupButton").prop("disabled", true);
		$("#markPackagedButton").prop("disabled", true);
		$("#markDeliveredButton").prop("disabled", true);
		
	});

	function toggleSelection(source) {
		
		var checkboxes = document.getElementsByName('orderCheckbox');
		for( var i = 0, n = checkboxes.length; i < n; i++) {
			checkboxes[i].checked = source.checked;
		}
		
		if(!source.checked) {
			$("#markPickupButton").prop("disabled", true);
			$("#markPackagedButton").prop("disabled", true);
			$("#markDeliveredButton").prop("disabled", true);
		
		} else {			
			enableDiableButtons();
		}
	}

	function enableDiableButtons() {

		var checkboxall = document.getElementById('orderCheckboxAll');
		var checkboxes = document.getElementsByName('orderCheckbox');

		var enablePickupButton = true;
		var enablePackagedButton = true;
		var enableDeliveredButton = true;

		var checkSelectAllCheckbox = true;

		var noneSelected = true;

		for ( var i = 0, n = checkboxes.length; i < n; i++) {
			if (checkboxes[i].checked) {
				noneSelected = false;
				var status = document.getElementById("orderStatus_"
						+ checkboxes[i].id).innerHTML;
				if (status === 'BOOKED') {
					enablePickupButton = enablePickupButton && true;
					enablePackagedButton = enablePackagedButton && false;
					enableDeliveredButton = enableDeliveredButton && false;
				} else if (status === 'WASHED') {
					enablePickupButton = enablePickupButton && false;
					enablePackagedButton = enablePackagedButton && true;
					enableDeliveredButton = enableDeliveredButton && false;
				} else if (status === 'PACKAGED') {
					enablePickupButton = enablePickupButton && false;
					enablePackagedButton = enablePackagedButton && false;
					enableDeliveredButton = enableDeliveredButton && true;
				} else {
					enablePickupButton = enablePickupButton && false;
					enablePackagedButton = enablePackagedButton && false;
					enableDeliveredButton = enableDeliveredButton && false;
				}
			} else {
				checkSelectAllCheckbox = false;
			}
		}
		if (noneSelected) {
			enablePickupButton = false;
			enablePackagedButton = false;
			enableDeliveredButton = false;
		}

		$("#markPickupButton").prop("disabled", !enablePickupButton);
		$("#markPackagedButton").prop("disabled", !enablePackagedButton);
		$("#markDeliveredButton").prop("disabled", !enableDeliveredButton);

		checkboxall.checked = checkSelectAllCheckbox;
	}

	function showOrderDetails(action) {

		var pageURL = action;
		var title = "Update Order";
		var popupWidth = 900;
		var popupHeight = 600;

		var left = (screen.width / 2) - (popupWidth / 2);
		var top = (screen.height / 2) - (popupHeight / 2);
		window.open(pageURL, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=YES, resizable=YES, copyhistory=no, width='
								+ popupWidth + ', height=' + popupHeight + ', top=' + top + ', left=' + left);

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
				<td nowrap="nowrap">Status</td>
				<td nowrap="nowrap">Name</td>
				<td nowrap="nowrap">Mobile No</td>
				<td nowrap="nowrap">Area</td>
				<td nowrap="nowrap">PIN</td>
				<td nowrap="nowrap">Pick Date</td>
				<td nowrap="nowrap">Pick Time</td>
				<td nowrap="nowrap">Del Date</td>
				<td nowrap="nowrap">Del Time</td>
				<td nowrap="nowrap">Del Type</td>
				<td nowrap="nowrap">Service Type</td>
				<td />
			</tr>
			<c:forEach var="searchResult" items="${searchResult.orderDetailsList}" varStatus="list">
				<tr>
					<td><input type="checkbox" name="orderCheckbox" value="${searchResult.orderId}" onClick="enableDiableButtons()" id="${list.index}"/></td>
					<td><p>${searchResult.orderRef}</p></td>
					<td><p id="orderStatus_${list.index}">${searchResult.latestStatus}</p></td>
					<td><p>${searchResult.name}</p></td>
					<td><p>${searchResult.mobileNo}</p></td>
					<td><p>${searchResult.serviceAreaName}</p></td>
					<td><p>${searchResult.pinCode}</p></td>
					<td><p>${searchResult.pickupDate}</p></td>
					<td><p>${searchResult.pickupTime}</p></td>
					<td><p>${searchResult.deliveryDate}</p></td>
					<td><p>${searchResult.deliveryTime}</p></td>
					<td><p>${searchResult.deliveryType}</p></td>
					<td><p>${searchResult.serviceType}</p></td>
					<td><p align="center"><input type="button" value=" Details " class="customButton" onclick="showOrderDetails('${pageContext.request.contextPath}/admin/order/showOrderDetails.do?orderId=${searchResult.orderId}')"/></p></td>
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