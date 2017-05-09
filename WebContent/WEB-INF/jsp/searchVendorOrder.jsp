
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/jquery-ui.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/table.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.min.js"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery-ui.min.js"/>

<!-- To change the calendar background colour, next, previous image -->
<style type="text/css">

	.ui-datepicker {
	    background: #777;
	    border: 1px solid #555;
	    color: #EEE;
	}
	
 	.ui-datepicker-next .ui-icon  {
		background:url(${pageContext.request.contextPath}/resources/image/arrow-next.png);
	}
	
	.ui-datepicker-prev .ui-icon  {
		background:url(${pageContext.request.contextPath}/resources/image/arrow-prev.png);
	} 
	
</style>

<script>

	var contextPath = "<%=request.getContextPath()%>";

	$(function() {
		
		$.ajaxSetup({
	        cache: false
	    });
		$(document)
		.ajaxStart(function() {
			$("#waitDialog").show();
		})
		.ajaxComplete(function() {
			$("#waitDialog").hide();
		});

		$('#custPickupDate').datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$('#vendPickupDate').datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$('#deliveryDate').datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		$('#actionbuttontablediv').hide();
		$('#waitDialog').hide();

	});
	
	function updateSearchResult(searchType) {

		var search = {};
		search["latestStatus"] = $("#latestStatus").val();
		search["custPickupDate"] = $("#custPickupDate").val();
		search["vendPickupDate"] = $("#vendPickupDate").val();
		search["deliveryDate"] = $("#deliveryDate").val();
		search["vendorId"] = $("#vendorId").val();
		search["serviceType"] = $("#serviceType").val();
		search["specialField"] = $("#specialField").val();
		search["lastRecordIndex"] = $("#lastRecordIndex").val();
		search["searchType"] = searchType;

		disableSearchButtons(true);

		$.ajax({
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(search),
			url : contextPath + '/admin/vendor/searchVendorOrder.do',
			dataType : "html",
			success : function(output) {
				$('#searchresultmain').html(output);
				$("#searchbutton").prop("disabled", false);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("Something went wrong. Please try again.");
				$("#searchbutton").prop("disabled", false);
			}
		});

	}

	function disableSearchButtons(disable) {

		$("#searchbutton").prop("disabled", disable);
		$("#firstRecordButton").prop("disabled", disable);
		$("#prevRecordButton").prop("disabled", disable);
		$("#nextRecordButton").prop("disabled", disable);
		$("#lastRecordButton").prop("disabled", disable);

	}
	
	function openVendorDialog() {
		
		var dialog = $("#assignVendorAdditionalDetailsDialog").dialog({
			autoOpen : false,
			width : 300,
			height : 150,
			closed : false,
			cache : false,
			resizable: false,
			modal : true,
			title:"Please select vendor details",
			buttons: {
	            "Cancel": function() {
	            	$(this).dialog("close");
	            },
	            "Assign": function() {
	            	var assignToVendor = $("#assignToVendor").val();
	            	if(assignToVendor == 0) {
		               alert('Please select vendor'); 
	            	} else {
	            		$(this).dialog("close");	            		
		            	updateSelectedOrders('WASHING', assignToVendor);
	            	}
	            }
	        }
			
		});
		
		dialog.dialog("open");
	}

	function openWashedDialog() {
		
		var dialog = $("#washedAdditionalDetailsDialog").dialog({
			autoOpen : false,
			width : 300,
			height : 150,
			closed : false,
			cache : false,
			resizable: false,
			modal : true,
			title:"Confirm",
			buttons: {
	            "Cancel": function() {
	            	$(this).dialog("close");
	            },
	            "OK": function() {
	            	updateSelectedOrders('WASHED', '0');
	            	$(this).dialog("close");
	            }
	        }
			
		});
		
		dialog.dialog("open");
	}
	
	function updateSelectedOrders(status, assignToVendor) {
		var updateOrderBean = {};	
		var selectedOrders = [];
		var selectedOrderService = [];
		
		$("input:checkbox[name='orderCheckbox']:checked").each(function() {
			var valArr = $(this).val().split("_");
			selectedOrders.push(valArr[0]);
			selectedOrderService.push(valArr[1]);
		});
		
		updateOrderBean["selectedOrder"] = selectedOrders;
		updateOrderBean["selectedOrderService"] = selectedOrderService;
		updateOrderBean["updateType"] = status;
		updateOrderBean["assignToVendor"] = assignToVendor;

		disableSearchButtons(true);

		$.ajax({
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(updateOrderBean),
			url : contextPath + '/admin/vendor/updateVendorOrder.do',
			dataType : "html",
			success : function(output) {
				updateSearchResult('REFRESH');
				$("#searchbutton").prop("disabled", false);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("Something went wrong. Please try again." + errorThrown);
				$("#searchbutton").prop("disabled", false);
			}
		});
	}
	
	function downloadAllVendorOrders() {
		
		document.forms['download'].submit();
		
	}
	
</script>

<div>

<form name="download" action="${pageContext.request.contextPath}/admin/vendor/downloadAllVendorOrders.do" method="post">

<div class="searchTable">
<table>
	<tr>
		<td width="10%">Status</td>
		<td>Cust Pick Date</td>
		<td>Vendor Pick Date</td>
		<td>Delivery Date</td>
		<td>Vendor Name</td>
		<td>Service Type</td>
		<td>Other Fields</td>
		<td/>
	</tr>
	<tr>
		<td>
			<select id="latestStatus" name="latestStatus">
				<c:forEach var="entry" items="${statusMap}">
					<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
				</c:forEach>
			</select>
		</td>		
		<td><input type="text" class="customtextbox" maxlength="10" size="10" id="custPickupDate" name="custPickupDate"/></td>
		<td><input type="text" class="customtextbox" maxlength="10" size="10" id="vendPickupDate" name="vendPickupDate"/></td>
		<td><input type="text" class="customtextbox" maxlength="10" size="10" id="deliveryDate" name="deliveryDate"/></td>
		<td>
			<select id="vendorId" name="vendorId">
				<c:forEach var="entry" items="${searchVendorMap}">
					<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
				</c:forEach>
			</select>
		</td>		
		<td>
			<select id="serviceType" name="serviceType">
				<c:forEach var="entry" items="${serviceTypeMap}">
					<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
				</c:forEach>
			</select>
		</td>
		<td><input type="text" class="customtextbox" maxlength="20" size="15" id="specialField" name="specialField"/></td>
		<td><input type="button" value="  Search  " id="searchbutton" class="customButton" onclick="updateSearchResult('SEARCH')"/></td>
	</tr>
</table>
</div>

</form>

<div id="searchresult">

	<input type="hidden" name="lastRecordIndex" id="lastRecordIndex" value="0">

	<div id="searchresultmain">
		<p id="searchhelppara">Please select the search criteria above and click on Search button.</p>
	</div>
	
	<div id="assignVendorAdditionalDetailsDialog">
		<table>
			<tr>
				<td>Assign to vendor:</td>
				<td>
					<select id="assignToVendor">
						<c:forEach var="entry" items="${vendorMap}">
							<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="washedAdditionalDetailsDialog">	
		<p>Are you sure to mark the selected orderes as Washed?</p>
	</div>
	
	<div id="actionbuttontablediv">
		<table id="actionbuttontable">
			<tr>
				<td><input type="button" id="firstRecordButton" value="<< First" class="customButton" onclick="updateSearchResult('FIRST')" /></td>
				<td><input type="button" id="prevRecordButton" value="<< Prev" class="customButton" onclick="updateSearchResult('PREV')" /></td>
				<td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/>
				<td><input type="button" id="assignVendorButton" value="Assign Vendor" class="customButton" onclick="openVendorDialog()" /></td>
				<td><input type="button" id="markWashedButton" value="Mark Washed" class="customButton" onclick="openWashedDialog()" /></td>
				<td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/>
				<td><input type="button" id="nextRecordButton" value="Next >>" class="customButton" onclick="updateSearchResult('NEXT')" /></td>
				<td><input type="button" id="lastRecordButton" value="Last >>" class="customButton" onclick="updateSearchResult('LAST')" /></td>				
			</tr>
		</table>
		<p class="download"><input type="button" id="downloadButton" value="Download All" class="customButton" onclick="downloadAllVendorOrders()" /></p>	
	</div>
</div>

	<div id="waitDialog">
		<img src="${pageContext.request.contextPath}/resources/image/progressbar-red.gif" width="45" height="45" alt="Loading..." style="position: fixed; top: 50%; left: 50%;" />
	</div>	

</div>