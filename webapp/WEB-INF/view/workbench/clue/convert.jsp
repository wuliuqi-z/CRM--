<%@page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<link href="https://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	<%--    引入jquery--%>
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<%--    引入bootstrap框架--%>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css"/>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript">
	$(function(){
		// 给转换按钮添加单击事件
		$("#saveConvertClueBtn").click(function (){
		// 	手机参数
			let clueId='${clue.id}'
			let money = $.trim($("#amountOfMoney").val());
			let name = $.trim($("#tradeName").val());
			let expectedDate = $("#expectedClosingDate").val();
			let stage = $("#stage").val();
			let activityId = $("#activity").attr("name");
			let isCreateTran = $("#isCreateTransaction").prop("checked");
            console.log(isCreateTran)
			$.ajax({
				url:'workbench/clue/convertClue.do',
				data:{
					clueId:clueId,
					money:money,
					name:name,
                    exceptedDate:expectedDate,
					stage:stage,
					activityId:activityId,
                    isCreatedTran:isCreateTran
				},
				type:'post',
				dataType: "json",
				success:function (data){
					if(data.code=="1"){
						console.log("成功了")
						window.location.href="workbench/clue/index.do"
					}else{
						alert(data.message)
					}
				}
			})


		})
		// 给市场活动源的搜索按钮添加单击事件
		$("#searchBtn").click(function(){
			$("#searchActivityModal input[type='text']").val("");
			$("#searchActivityModal input[type='text']").html("");
			$("#tbodyActivity").html("");
			$("#searchActivityModal").modal("show")
		})
		// 给查询窗口绑定键盘弹起事件
		$("#searchActivityModal input[type='text']").keyup(function(){
			let activityName =$.trim($("#searchActivityModal input[type='text']").val());
			let clueId='${clue.id}'
			$.ajax({
				beforeSend:function(){
					if(activityName==""||activityName==null){
						return false
					}
					return true;
				},
				url:'workbench/clue/queryActivityByNameClueId.do',
				data:{
					activityName:activityName,
					clueId:clueId
				},
				type:'post',
				dataType:'json',
				success:function(data){
					console.log("成功了")
					let htmlStr="";
					$.each(data.retDate,function (index,activity){
						htmlStr+=`<tr>
									<td><input type="radio" name="activity" value="${'${activity.id}'}"/></td>
									<td>${'${activity.name}'}</td>
									<td>${"${activity.startDate}"}</td>
									<td>${"${activity.endDate}"}</td>
									<td>${"${activity.owner}"}</td>
								  </tr>`
					})
					$("#tbodyActivity").html(htmlStr);
				}

			})


		})
		$("#searchActivityModal").on("click","input[type='radio']",function (){
			let text=$("#searchActivityModal input[type='radio']:checked").parent().next().text()
			$("#activity").val(text);
			$("#activity").attr("name",$("#searchActivityModal input[type='radio']:checked").attr("value"))
			$("#searchActivityModal").modal("hide")
		})
		$("#searchActivityModal input[type='text']").keyup(function (){

		})
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="tbodyActivity">
<%--							<tr>--%>
<%--								<td><input type="radio" name="activity"/></td>--%>
<%--								<td>发传单1</td>--%>
<%--								<td>2020-10-10</td>--%>
<%--								<td>2020-10-20</td>--%>
<%--								<td>zhangsan</td>--%>
<%--							</tr>--%>
<%--							<tr>--%>
<%--								<td><input type="radio" name="activity"/></td>--%>
<%--								<td>发传单2</td>--%>
<%--								<td>2020-10-10</td>--%>
<%--								<td>2020-10-20</td>--%>
<%--								<td>zhangsan</td>--%>
<%--							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${clue.fullname}${clue.appellation}-${clue.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${clue.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${clue.fullname}${clue.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form>
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" value="${clue.company}-">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="date" class="form-control" id="expectedClosingDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control">
		    	<option></option>
			<c:forEach items="${stageList}" var="stage">
				<option value="${stage.id}">${stage.value}</option>
			</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a id="searchBtn" href="javascript:void(0);"   style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${clue.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="saveConvertClueBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>