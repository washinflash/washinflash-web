<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>

	<head>
		<title></title>
		
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/style.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery-2.1.1.min.js"></script>	
			
		<script>

			function updateMainContents(action) {
									
				$.ajax({
                    type : "POST",
                    url : action,
                    dataType : "html",
                    success : function(output) {
                          $('#maincontent').html(output);
                        },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                       alert("An error has occurred making the request: " + errorThrown);
                    }
                 });
										
			}
			
		</script>
		
	</head>
	
	<body>
	 <header>
		<div id="header">
			<A class="homelink" href="${pageContext.request.contextPath}/admin/login/gotoHome.do"><img width="20" height="20" src="${pageContext.request.contextPath}/resources/image/home.png"/></A>
			<p class="homeLogout"><a href="${pageContext.request.contextPath}/admin/login/logout.do">Logout</a></p>
		</div>
	 </header>
	 <div>
		<div id="menusection">
			<ul>
			<li><p>Manage Order</p>
				<ul>
					<li><a href="javascript:updateMainContents('${pageContext.request.contextPath}/admin/order/showSearchOrder.do');">Search Order</a></li>
<%-- 					<li><a href="javascript:updateMainContents('${pageContext.request.contextPath}/admin/order/showSearchOrder.do');">Today's Order</a></li>
					<li><a href="javascript:updateMainContents('${pageContext.request.contextPath}/admin/order/showSearchOrder.do');">Tomorrow's Order</a></li> --%>
				</ul>
			</li>
			<li><p>Manage Vendor</p>
				<ul>
					<li><a href="javascript:updateMainContents('${pageContext.request.contextPath}/admin/vendor/showVendorSearchOrder.do');">Search Vendor Order</a></li>
				</ul>
			</li>			
			</ul>
		</div>

		<section>
		<div id="maincontent">
			<p class="message"><br><br>${msg}</p>
		</div>
	 </section>
	 </div>
	 <footer>
		<div id="footer">
			Washinflash admin web application
		</div>		
	</footer>
	
	</body>
</html>