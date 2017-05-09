<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
<head>
<title></title>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/style.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery-2.1.1.min.js"></script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

</head>

<body>

	<div id="loginMessageSection">
		<p class="errorMessage">${errorMessage}</p>
		<p class="infoMessage">${infoMessage}</p>
	</div>
	
	<div id="login">
		<h1>
			<strong>Washinflash admin sign in</strong>
		</h1>
		<form:form commandName="employeeDetails" action="${pageContext.request.contextPath}/admin/login/validateLogin.do" method="post">
			<fieldset>
				<p>
					<form:errors path="email" class="errorMessage"/> 
					<input type="text" name="email" value="Email Id" onBlur="if(this.value=='')this.value='Email Id'" onFocus="if(this.value=='Email Id')this.value='' "/>
				</p>
				<p>
					<form:errors path="password" class="errorMessage"/>
					<input type="password" name="password" value="Password" onBlur="if(this.value=='')this.value='Password'" onFocus="if(this.value=='Password')this.value='' "/>
				</p>
				<p>
					<input type="submit" value="Login"/>
				</p>
			</fieldset>
		</form:form>
	</div>
	<footer>
	<div id="footer">Washinflash admin web application</div>
	</footer>

</body>
</html>