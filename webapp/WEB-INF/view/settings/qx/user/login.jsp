<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	System.out.println(basePath);
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="https://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function () {
			// 给整个浏览器窗口加上键盘按下时间
			$(window).keydown(function(event){
				if(event.keyCode==13){
				// 	让登录按钮模拟发生一次单击事件
					$("#loginBtn").click();//不给它传参数表示在指定元素上面模拟发生一次这个事件
				}
			})

			$("#loginAct").focus(function (){
				$('#msg').html("");
			})
			$("#loginPwd").focus(function (){
				$('#msg').html("");
			})
		// 	给登录按钮添加单击时间

			$("#loginBtn").click(function (){
			// 	收集参数
				let loginAct=$.trim($("#loginAct").val());
				let loginPwd=$.trim($("#loginPwd").val());
				let isRemPwd=$("#loginCb").prop("checked");
			// 	表单数据验证
				if(loginPwd==""||loginAct==""){
					alert("请输入用户名或者密码")
					return
				}
			// // 	在发请求前显示正在验证
			// 	$("#msg").text("正在验证，请稍后");
			// 	发送请求（异步请求）
				$.ajax({
					url:'settings/px/user/login.do',
					data:{
						loginAct:loginAct,
						loginPwd:loginPwd,
						isRemPwd:isRemPwd
					},
					type:'post',
					dataType:'json',
					success:function (data){
						// $("#msg").html("");
						if(data.code=='1'){
						// 	登录成功
							window.location.href='workbench/index.do'
						}else{
							$('#msg').html('<font color="red">'+data.reason+'</font>');
						}
					},
					beforeSend:function (){
						//ajax向后台发送请求之前触发的函数，会自动执行。这函数的执行返回值能决定ajax是否真的向后台发送请求
					// 	如果该函数返回true则执行完他自己的代码之后则ajax会真正向后台发请求。否则返回false。则ajax会放弃向后台发送请求

						// // 	表单数据验证
						// if(loginPwd==""||loginAct==""){
						// 	alert("请输入用户名或者密码")
						// 	return false;
						// }
						// return true;
						$("#msg").text("正在努力验证中......")
						return true;
					}
				})
			});
		})
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;老杜亲传弟子</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
<%--						<%--%>
<%--							String username="";--%>
<%--							String password="";--%>
<%--							Cookie[] cookies = request.getCookies();--%>
<%--							for(Cookie cs:cookies){--%>
<%--								if(cs.getName().equals("username")){--%>
<%--									cs.getValue();--%>
<%--									username=cs.getValue();--%>
<%--								}--%>
<%--								if(cs.getName().equals("password")){--%>
<%--									password=cs.getValue();--%>
<%--								}--%>
<%--							}--%>
<%--						%>--%>
						<input class="form-control" id="loginAct" type="text" placeholder="用户名" value="${cookie.username.value}">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" type="password" placeholder="密码" value="${cookie.password.value}">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.username and not empty cookie.password}">
								<input type="checkbox" id="loginCb" checked> 十天内免登录
							</c:if>
							<c:if test="${empty cookie.username or empty cookie.password}">
								<input type="checkbox" id="loginCb"> 十天内免登录
							</c:if>
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>