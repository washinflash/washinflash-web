
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

		$('#pickupDate').datepicker({
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
		search["pickupDate"] = $("#pickupDate").val();
		search["pickupTime"] = $("#pickupTime").val();
		search["deliveryDate"] = $("#deliveryDate").val();
		search["deliveryTime"] = $("#deliveryTime").val();
		search["deliveryType"] = $("#deliveryType").val();
		search["serviceType"] = $("#serviceType").val();
		search["specialField"] = $("#specialField").val();
		search["lastRecordIndex"] = $("#lastRecordIndex").val();
		search["searchType"] = searchType;

		disableSearchButtons(true);

		$.ajax({
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(search),
			url : contextPath + '/admin/order/searchOrder.do',
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
	
	function openPickedupDialog() {
		
		var dialog = $("#pickedupAdditionalDetailsDialog").dialog({
			autoOpen : false,
			width : 300,
			height : 150,
			closed : false,
			cache : false,
			resizable: false,
			modal : true,
			title:"Please select pick up details",
			buttons: {
	            "Cancel": function() {
	            	$(this).dialog("close");
	            },
	            "Save": function() {
	            	var pickedupBy = $("#pickedupby").val();
	            	if(pickedupBy == 0) {
		               alert('Please select picked up by'); 
	            	} else {
	            		$(this).dialog("close");            		
		            	updateSelectedOrders('PICKEDUP', pickedupBy, '0');
	            	}
	            }
	        }
			
		});
		
		dialog.dialog("open");
	}
	
	function openPackagedDialog() {
		
		var dialog = $("#packagedAdditionalDetailsDialog").dialog({
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
	            	updateSelectedOrders('PACKAGED', '0', '0');
	            	$(this).dialog("close");
	            }
	        }
			
		});
		
		dialog.dialog("open");
	}
	
	function openDeliveredDialog() {
		
		var dialog = $("#deliveredAdditionalDetailsDialog").dialog({
			autoOpen : false,
			width : 300,
			height : 150,
			closed : false,
			cache : false,
			resizable: false,
			modal : true,
			title:"Please select delivery details",
			buttons: {
	            "Cancel": function() {
	            	$(this).dialog("close");
	            },
	            "Save": function() {
	            	var deliveredby = $("#deliveredby").val();
	            	if(deliveredby == 0) {
		               alert('Please select delivered by'); 
	            	} else {
	            		$(this).dialog("close");
		            	updateSelectedOrders('DELIVERED', '0', deliveredby);
	            	}
	            }
	        }
			
		});
		
		dialog.dialog("open");
	}
	
	function updateSelectedOrders(status, pickedupBy, deliveredBy) {
		var updateOrderBean = {};	
		var selectedOrders = [];
		
		$("input:checkbox[name='orderCheckbox']:checked").each(function() {
			selectedOrders.push($(this).val());
		});
		
		updateOrderBean["selectedOrder"] = selectedOrders;
		updateOrderBean["updateType"] = status;
		updateOrderBean["pickedupBy"] = pickedupBy;
		updateOrderBean["deliveredBy"] = deliveredBy;

		disableSearchButtons(true);

		$.ajax({
			type : "POST",
			contentType : "application/json",
			data : JSON.stringify(updateOrderBean),
			url : contextPath + '/admin/order/updateOrder.do',
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
	
	function downloadAllOrders() {
		
		document.forms['download'].submit();
		
	}
	
</script>

<div>

<form name="download" action="${pageContext.request.contextPath}/admin/order/downloadAllOrders.do" method="post">

<div class="searchTable">
<table>
	<tr>
		<td width="10%">Status</td>
		<td>Pick-Up Date</td>
		<td>Pick-Up Time</td>
		<td>Delivery Date</td>
		<td>Delivery Time</td>
		<td>Delivery Type</td>
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
		<td><input type="text" class="customtextbox" maxlength="10" size="10" id="pickupDate" name="pickupDate"/></td>
		<td>
			<select id="pickupTime" name="pickupTime">
				<c:forEach var="entry" items="${pickupTimeMap}">
					<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
				</c:forEach>					
			</select>
		</td>
		<td><input type="text" class="customtextbox" maxlength="10" size="10" id="deliveryDate" name="deliveryDate"/></td>
		<td>
			<select id="deliveryTime" name="deliveryTime">
				<c:forEach var="entry" items="${deliveryTimeMap}">
					<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
				</c:forEach>
			</select>
		</td>
		<td>
			<select id="deliveryType" name="deliveryType">
				<c:forEach var="entry" items="${deliveryTypeMap}">
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
	
	<div id="pickedupAdditionalDetailsDialog">
		<table>
			<tr>
				<td>Picked up by:</td>
				<td>
					<select id="pickedupby">
						<c:forEach var="entry" items="${employeeMap}">
							<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	</div>

	<div id="packagedAdditionalDetailsDialog">	
		<p>Are you sure to mark the selected orderes as Packaged?</p>
	</div>
	
	<div id="deliveredAdditionalDetailsDialog">	
		<table>
			<tr>
				<td>Delivered by:</td>
				<td>
					<select id="deliveredby">
						<c:forEach var="entry" items="${employeeMap}">
							<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="actionbuttontablediv">
		<table id="actionbuttontable">
			<tr>
				<td><input type="button" id="firstRecordButton" value="<< First" class="customButton" onclick="updateSearchResult('FIRST')" /></td>
				<td><input type="button" id="prevRecordButton" value="<< Prev" class="customButton" onclick="updateSearchResult('PREV')" /></td>
				<td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/>
				<td><input type="button" id="markPickupButton" value="Mark Picked Up" class="customButton" onclick="openPickedupDialog()" /></td>				
				<td><input type="button" id="markPackagedButton" value="Mark Packaged" class="customButton" onclick="openPackagedDialog()" /></td>
				<td><input type="button" id="markDeliveredButton" value="Mark Delivered" class="customButton" onclick="openDeliveredDialog()" /></td>	
				<td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/><td/>
				<td><input type="button" id="nextRecordButton" value="Next >>" class="customButton" onclick="updateSearchResult('NEXT')" /></td>
				<td><input type="button" id="lastRecordButton" value="Last >>" class="customButton" onclick="updateSearchResult('LAST')" /></td>				
			</tr>
		</table>
		<p class="download"><input type="button" id="downloadButton" value="Download All" class="customButton" onclick="downloadAllOrders()" /></p>
	</div>
</div>

	<div id="waitDialog">
		<img src="${pageContext.request.contextPath}/resources/image/progressbar-red.gif" width="45" height="45" alt="Loading..." style="position: fixed; top: 50%; left: 50%;" />
	</div>	

</div>