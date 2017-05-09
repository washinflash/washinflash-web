<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
	<head>
		<title>Order Details</title>
		
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
		
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/style.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/table.css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.min.js"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery-2.1.1.min.js"></script>
		
		<script>
		
			function updateOrderDetails() {
				
				var orderDetails = {};
				orderDetails["actualNoClothes"] = $("#actualNoClothes").val();
				orderDetails["totalPrice"] = $("#totalPrice").val();
				orderDetails["orderId"] = $("#orderId").val();
				
				$("#updateOrderDetails").prop("disabled", true);
		
				$.ajax({
					type : "POST",
					contentType : "application/json",
					data : JSON.stringify(orderDetails),
					url : '<%=request.getContextPath()%>/admin/order/updateOrderDetails.do',
					dataType : "html",
					success : function(output) {
						alert("Successfully updated order details.");
						$("#updateOrderDetails").prop("disabled", false);
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("Something went wrong. Please try again.");
						$("#updateOrderDetails").prop("disabled", false);
					}
				});
	
			}
			
		</script>
	
	</head>
	
	<body>
	 <header>
		<p align="center" style="font-weight: bold">Order Details</p>
	 </header>
	 <div>
	 	<input type="hidden" name="orderId" id="orderId" value="${adminOrderDetails.orderDetails.orderId}">
		<div class="orderDetailsSection">
			<p class="orderSubHeader">Order Details</p>
			<table class="ordrDetailsTable">
				<tr>
					<td class="orderColumn" width="14%">Order Ref:</td>
					<td width="24%">${adminOrderDetails.orderDetails.orderRef}</td>
					<td class="orderColumn" width="14%">Status:</td>
					<td width="17%">${adminOrderDetails.orderDetails.latestStatus}</td>
					<td class="orderColumn" width="14%">Delivery Type:</td>
					<td width="17%">${adminOrderDetails.orderDetails.deliveryType}</td>					
				</tr>
				<tr>
					<td class="orderColumn">Order Time:</td>
					<td>${adminOrderDetails.orderDetails.orderDate}</td>
					<td class="orderColumn">Pick-Up Date:</td>
					<td>${adminOrderDetails.orderDetails.pickupDate}</td>
					<td class="orderColumn">Pick-Up Time:</td>
					<td>${adminOrderDetails.orderDetails.pickupTime}</td>					
				</tr>
				<tr>
					<td class="orderColumn">Update Date:</td>
					<td>${adminOrderDetails.orderDetails.updateTime}</td>
					<td class="orderColumn">Delivery Date:</td>
					<td>${adminOrderDetails.orderDetails.deliveryDate}</td>
					<td class="orderColumn">Delivery Time:</td>
					<td>${adminOrderDetails.orderDetails.deliveryTime}</td>					
				</tr>
				<tr>
					<td class="orderColumn">Approx Clothes:</td>
					<td>${adminOrderDetails.orderDetails.approxNoClothes}</td>
					<td class="orderColumn">Actual Clothes:</td>
					<c:choose>
						<c:when test="${adminOrderDetails.enableOrderDetailsUpdate}">
							<td><input type="text" value="${adminOrderDetails.orderDetails.actualNoClothes}" class="customtextbox" maxlength="10" size="10" id="actualNoClothes"/></td>
						</c:when>
						<c:otherwise>
							<td>${adminOrderDetails.orderDetails.actualNoClothes}</td>
						</c:otherwise>
					</c:choose>					
					<td class="orderColumn">Total Price:</td>
					<c:choose>
						<c:when test="${adminOrderDetails.enableOrderDetailsUpdate}">
							<td><input type="text" value="${adminOrderDetails.orderDetails.totalPrice}" class="customtextbox" maxlength="10" size="10" id="totalPrice"/></td>
						</c:when>
						<c:otherwise>
							<td>${adminOrderDetails.orderDetails.totalPrice}</td>
						</c:otherwise>
					</c:choose>					
				</tr>
				<tr>
					<td class="orderColumn">Coupon Code:</td>
					<td>${adminOrderDetails.orderDetails.couponCode}</td>
					<td class="orderColumn">Donate Only:</td>
					<td>${adminOrderDetails.orderDetails.donateOnly}</td>
					<td class="orderColumn"></td>
					<td></td>					
				</tr>																							
			</table>
			<c:if test="${adminOrderDetails.enableOrderDetailsUpdate}">
				<p align="center"><input type="button" id="updateOrderDetails" value=" Update " class="customButton" onclick="updateOrderDetails()"/></p>
			</c:if>	
		</div>
		<div class="orderDetailsSection">
			<p class="orderSubHeader">Service Details</p>
			<table class="ordrServiceDetailsTable">
				<tr>
					<td>Service Type</td>
					<td>Service Status</td>
					<td>Vendor Pick Date</td>
					<td>Vendor Name</td>
					<td>Vendor Mobile</td>
					<td>Vendor Email</td>
					<td>Vendor Type</td>	
					<td>Vendor Address</td>						
				</tr>
				<c:forEach var="serviceDetails" items="${adminOrderDetails.serviceTypeFullDetailsList}" varStatus="list">
				<tr>
					<td>${serviceDetails.serviceTypeDesc}</td>
					<td>${serviceDetails.serviceStatus}</td>
					<td>${serviceDetails.vendorPickupDate}</td>
					<td>${serviceDetails.vendorName}</td>
					<td>${serviceDetails.vendorMobileNo}</td>
					<td>${serviceDetails.vendorEmail}</td>
					<td>${serviceDetails.vendorType}</td>
					<td>${serviceDetails.vendorAddress}</td>					
				</tr>
				</c:forEach>																							
			</table>			
		</div>
		<div class="orderDetailsSection">
			<p class="orderSubHeader">Customer Details</p>
			<table class="ordrDetailsTable">
				<tr>
					<td class="orderColumn" width="14%">Name:</td>
					<td width="20%">${adminOrderDetails.userDetails.name}</td>
					<td class="orderColumn" width="12%">Mobile No:</td>
					<td width="17%">${adminOrderDetails.userDetails.mobileNo}</td>
					<td class="orderColumn" width="14%">Email Id:</td>
					<td width="21%">${adminOrderDetails.userDetails.email}</td>					
				</tr>
				<tr>
					<td class="orderColumn">Referral Code:</td>
					<td>${adminOrderDetails.userDetails.referralCode}</td>
					<td class="orderColumn">User Type:</td>
					<td>${adminOrderDetails.userDetails.userType}</td>
					<td class="orderColumn">Facebook Id:</td>
					<td>${adminOrderDetails.userDetails.facebookId}</td>					
				</tr>																								
			</table>			
		</div>
		<div class="orderDetailsSection">
			<p class="orderSubHeader">Pick-Up/Delivery Address Details</p>
			<table class="ordrDetailsTable">
				<tr>
					<td class="orderColumn" width="8%">Name:</td>
					<td width="20%">${adminOrderDetails.address.name}</td>
					<td class="orderColumn" width="10%">Mobile No:</td>
					<td width="12%">${adminOrderDetails.address.mobileNo}</td>
					<td class="orderColumn" width="10%">Address:</td>
					<td width="35%">${adminOrderDetails.address.fullAddress}</td>
					<td width="5%"></td>					
				</tr>																								
			</table>				
		</div>
		<c:if test="${not empty adminOrderDetails.pickedUpBy}">		
			<div class="orderDetailsSection">
				<p class="orderSubHeader">Picked-Up By</p>
				<table class="ordrDetailsTable">
					<tr>
						<td class="orderColumn">Name:</td>
						<td>${adminOrderDetails.pickedUpBy.name}</td>
						<td class="orderColumn">Mobile No:</td>
						<td>${adminOrderDetails.pickedUpBy.mobileNo}</td>
						<td class="orderColumn">Email Id:</td>
						<td>${adminOrderDetails.pickedUpBy.email}</td>					
						<td class="orderColumn">Address:</td>
						<td>${adminOrderDetails.pickedUpBy.address}</td>					
					</tr>																							
				</table>			
			</div>
		</c:if>
		<c:if test="${not empty adminOrderDetails.deliveredBy}">
			<div class="orderDetailsSection">
				<p class="orderSubHeader">Delivered By</p>
				<table class="ordrDetailsTable">
					<tr>
						<td class="orderColumn">Name:</td>
						<td>${adminOrderDetails.deliveredBy.name}</td>
						<td class="orderColumn">Mobile No:</td>
						<td>${adminOrderDetails.deliveredBy.mobileNo}</td>
						<td class="orderColumn">Email Id:</td>
						<td>${adminOrderDetails.deliveredBy.email}</td>					
						<td class="orderColumn">Address:</td>
						<td>${adminOrderDetails.deliveredBy.address}</td>					
					</tr>																								
				</table>				
			</div>
		</c:if>		
	 </div>
	 <footer>
		<div id="footer">
			Washinflash admin web application
		</div>		
	</footer>
	
	</body>
</html>