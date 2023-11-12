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
<!--  PAGINATION plugin -->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
	//
		$("#importActivityBtn").click(function (){
		// 	发请求，有参数手机参数
			let fileName = $("#activityFile").val();//这个只能获取文件名
			let suffix = (fileName.substr(fileName.lastIndexOf(".")+1)).toLowerCase();
			if(suffix!="xls"){
				alert("只支持xls文件")
				return
			}
			let file=$("#activityFile")[0].files[0]
			if(file.size>1024*1024*5){//单位是字节
				alert("文件大小不能超过5M")
			}
			// FormData是ajax给我们定义的一个接口（不是java里的接口，这里的接口是广义上的接口，别人写的代码，你可以去调），使用这个类可以模拟键值对（其实前两中方式都是键值对的方式要么一个对象里面是键值对，要么你自己拼成键值对xx=xx&xx=xxx），向后台提交参数
			// 这个类的作用不但能向后台提交字符串数据，也能提交二进制数据，我们前两种方式只能提交字符串数据。formdate最大的优势是他两种提交数据的方式都支持
			const formData = new FormData();
			formData.append("activityFile",file)//这些参数名，必须保证能让mvc注入成功
			$.ajax({
				url:'workbench/activity/importActivity.do',
				data:formData,
				type:'post',
				dateType:'json',
				processData:false,//默认情况下，ajax会把所有的参数转成字符串，为了转成默认编码做准备的。这个可以设置ajax向后台提交参数之前，是否将参数统一转成字符串
				contentType:false,//设置ajax向后台提交参数之前，是否把所有的参数按urlencoded编码[此编码只能对字符串进行编码]
				success:function (data) {
					if(data.code=='1'){
						alert("成功导入"+data.retDate+"条记录")
						queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'))
						$("#importActivityModal").modal("hide");
					}else{
						$("#importActivityModal").modal("show");
						alert(data.message)

					}


				}
			})

		})
	// 	给批量导出按钮添加单击事件
		$("#exportActivityAllBtn").click(function (){
			window.location.href="workbench/activity/exportAllActivitiesByExcel.do";
		})
	// 	给更新按钮绑定单击事件
	// 	用户自己输入的建议去空格
		$("#updateBtn").click(function(){
			let id = $("#tBody input[type='checkbox']:checked").val();
			let owner = $("#edit-marketActivityOwner").val();
			let name = $("#edit-marketActivityName").val();
			let startTime = $("#edit-startTime").val();
			let endTime = $('#edit-endTime').val();
			let cost = $("#edit-cost").val();
			let describe = $("#edit-describe").val();
			// 表单验证，要先验证数据合不合法


			//验证数据合不合法
			$.ajax({
				url:"workbench/activity/updateActivityById.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startTime:startTime,
					endTime:endTime,
					cost:cost,
					describe:describe
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.code=="1"){
						console.log("当前页号"+$("#demo_pag1").bs_pagination("getOption","rowsPerPage"))
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination("getOption","currentPage"),$("#demo_pag1").bs_pagination("getOption","rowsPerPage"))
						$("#editActivityModal").modal("hide");
					}else{
						alert("系统忙，请联系管理员")
						$("#editActivityModal").modal("show");
					}

				}
			})

		})
	// 	给修改按钮添加单击事件
		$("#editActivityBtn").click(function (){
			let checkedBox = $("#tBody input[type='checkbox']:checked");
			var size = $("#tBody input[type='checkbox']:checked").size();
			if(size<1){
				alert("请选择要修改的记录")
				return
			}else if(size>1){
				alert("只能选择一条记录")
				return
			}else{
				let id = checkedBox.val();
				$.ajax({
					url:"workbench/activity/queryActivityById.do",
					data:{
						id:id
					},
					type:'post',
					dataType:'json',
					success:function (data){
						console.log("111111111")
						$("#edit-marketActivityOwner").val(data.owner)
						$("#edit-marketActivityName").val(data.name)
						$("#edit-startTime").val(data.startDate)
						$('#edit-endTime').val(data.endDate)
						$("#edit-cost").val(data.cost)
						$("#edit-describe").val(data.description)
						$("#editActivityModal").modal("show");
					}
				})

			}
		})
	// 	给删除按钮添加单击事件 deleteActivityBtn
		$("#deleteActivityBtn").click(function (){
		// 	发请求收集参数
			const checkedIds = $("#tBody input[type='checkbox']:checked");
			let ids="";
			$.each(checkedIds,function (index,id){//里面还有一个this指定就是数组里面的元素
				ids+="id="+this.value+"&"
			})
			ids=ids.substring(0,ids.length-1);
			$.ajax({
				url:"workbench/activity/deleteActivityIds.do",
				data:ids,
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						alert("删除成功")
						queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowPerPage'))
					}else{
						alert(data.message);
					}
				},
				beforeSend:function(){
					if(checkedIds.size()==0){
						alert("请选择要删除的记录");
						return false;
					}else{
						return window.confirm("确定删除吗")
					}
				}
			})
		})

	// 	给创建按钮添加单击事件
		$("#createActivityBtn").click(function (){
		// 	初始化操作,重置form表单里的所有的组件，一开始什么样子就是什么样子.但是只是js的方法只能使用dom对象调用
		// 	jquery对象转dom对象
		// 	$("#createActivityForm").get[0].reset()
			$("#createActivityForm")[0].reset()
		// 	弹出创建市场活动的模态窗口
			$("#createActivityModal").modal("show")
		})
	// 	给保存按钮添加单击事件
		$("#saveCreateActivityBtn").click(function(){
		// 	发请求，处理响应
			let owner=$("#create-marketActivityOwner").val()//如果你用text你会获取所有的人名。但是数据库推荐保存id，因为如果人家改名怎么办!!!!注意！！！！
			let name=$.trim($("#create-marketActivityName").val())
			let startDate = $("#create-startTime").val();
			let endDate = $("#create-endTime").val();
			let cost = $.trim($("#create-cost").val());
			let description =$.trim($("#create-describe").val());
			$.ajax({
                beforeSend:function(){
					if(owner==""){
						alert("所有者不能为空");
						return false;
					}
					if(name==""){
						alert("名称不能为空");
						return false;
					}
					if(startDate==""||endDate==""){
						alert("开始时间或者结束时间不能为空");
						return false;
					}
					if(startDate>=endDate){
						alert("开始时间或者结束时间不能颠倒");
						return false;
					}
                    let regex=/^(([1-9]\d*)|0)$/;
                    if (!regex.test(cost)) {
                        alert("请输入正确的非负整数");
                        return false;
                    }
					return true;
				},
                url:"workbench/activity/saveCreateActivity.do",
                data:{
                    owner:owner,
                    name:name,
                    startDate:startDate,
                    endDate:endDate,
                    cost:cost,
                    description:description
                },
                type:'post',
                dataType:'json',
                success:function (data){
                    if(data.code=="1"){
                        // 关闭选中的模态窗口
                        $("#createActivityModal").modal("hide")
                    //     刷新市场活动列表
						queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'))
                    }else{
                        alert(data.message)
                        $("#createActivityModal").modal("show")
                    }
                }
			})

		})
		queryActivityByConditionForPage(1);

	// 	给查询按钮添加单击事件
		$("#queryActivityBtn").click(function(){
			queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		})
	// 	给全选按钮绑定单击事件
		$("#checkAll").click(function (){
		// 	如果点完之后全选按钮是选中状态。则列表中的checkbox全部选中
		// 	let status = $("#checkAll").prop("checked");
		// 	这个函数里面有一个内置对象，表示当前这个函数发生的哪个dom对象(在这里就是那个全选按钮)
		// 	if(this.checked){
		// 	// 	列表中的checkbox也全部选中
		// 		$("#tBody input[type='checkbox']").prop("checked",true)//"#tBody>input[type='checkbox']"这是父子选择器，限制是只能获取一级子标签。这里很显然不是一级子标签。但是"#tBody input[type='checkbox']"这个使用空格是获取这个标签下的所有的标签
		// 	}else{
		// 		$("#tBody input[type='checkbox']").prop("checked",false)
		// 	}
			$("#tBody input[type='checkbox']").prop("checked",this.checked)
		})

	// $("#tBody input[type='checkbox']:checked").size()==$("#tBody input[type='checkbox']").size()?$("#checkAll").prop("checked",true):$("#checkAll").prop("checked",false)
		$("#tBody").on("click","input[type='checkbox']",function (){
			console.log(this);//this就是发生事件的input的dom对象
            console.log($("#tBody"))
			$("#tBody input[type='checkbox']:checked").size()==$("#tBody input[type='checkbox']").size()?$("#checkAll").prop("checked",true):$("#checkAll").prop("checked",false)
		})

	});
// 	jquery中封装函数要在入口函数外边封装
	function queryActivityByConditionForPage(no,size=10){
		// 	当市场活动主页面加载完成，查询所有数据的第一页以及所有数据的总条数
		let name = $.trim($("#query-name").val());
		let owner = $.trim($("#query-id").val());
		let startDate = $("#query-startDate").val();
		let endDate = $("#query-endDate").val();
		let pageNo=no;
		let pageSize=size;
		// 	查询数据一般不需要做表单验证，一般直接查数据
		// 	发送请求
		$.ajax({
			url: 'workbench/activity/queryActivityByConditionForPage.do',
			data: {
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize,
			},
			type:'post',
			dataType: 'json',
			success:function (data){
				let row=data.totalRow
				// 显示总条数
				// $("#totalRowB").text(row)
				// 	显示市场活动列表
				//  遍历list集合
				let htmlStr=""
				$.each(data.activities,function (index,obj){

					console.log(obj.name)
					console.log(obj.endDate)
					htmlStr+="<tr className=\"active\">"
					htmlStr+="<td><input type=\"checkbox\"  value=\""+obj.id+"\"/></td>"
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onClick=\"window.location.href='workbench/activity/detailActivity.do?activityId="+obj.id+"'\">"+obj.name+"</a></td>"
					htmlStr+="<td>"+obj.owner+"</td>"
					htmlStr+="<td>"+obj.startDate+"</td>"
					htmlStr+="<td>"+obj.endDate+"</td>"
					htmlStr+="</tr>"
				})
				$("#tBody").html(htmlStr)
                $("#checkAll").prop("checked",false);
				let totalPages=Math.ceil(data.totalRow/pageSize)
			// 	调用分页工具函数，显示翻页信息
				// 设置翻页插件的参数
				$("#demo_pag1").bs_pagination({
					totalPages: totalPages,//必填参数
					currentPage:no,//当前页号。默认为1.相当于pageNo
					rowsPerPage:size,//每页显示条数，默认为10
					totalRows:row,//总条数，不填的时候总页数和每页显示条数决定
					visiblePageLinks:5,//最多可以显示的卡片数
					showGoToPage:true,//控制跳转的是否显示，默认为true
					showRowsPerPage:true,//是否显示控制每页条数的功能
					showRowsInfo: true,//是否显示页数信息默认为true
					navGoToPageContainerClass: "col-xs-8 col-sm-6 col-md-3 row-space",
					onChangePage:function(event,pageObj){
						//     用户每次切换页号的时候默认执行的函数，包括修改每页显示条数的时候（这也属于修改页号）
						//     这个函数会给你返回切换页号之后的ageNo和pageSize(pageObj,就代表了整个翻页对象，每次切换之后这个对象就会变)
						console.log(pageObj.currentPage)//你只要一改pageObj.rowsPerPage,当前也默认回到一
						queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage)
					}

				});
			}
		})
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="createActivityForm" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${users}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="date" class="form-control" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label" >结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input  class="form-control" id="create-endTime" type="date">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <c:forEach items="${users}" var="user">
<%--									  items表示你要遍历哪一个集合或者数组
										  var表示user--%>
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="date" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="date" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color:gray">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-id">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="date" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="date" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="queryActivityBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>

				</table>
			</div>
			
<%--			<div style="height: 50px; position: relative;top: 30px;">--%>
<%--				<div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowB">50</b>条记录</button>--%>
<%--				</div>--%>
<%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--					<div class="btn-group">--%>
<%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--							10--%>
<%--							<span class="caret"></span>--%>
<%--						</button>--%>
<%--						<ul class="dropdown-menu" role="menu">--%>
<%--							<li><a href="#">20</a></li>--%>
<%--							<li><a href="#">30</a></li>--%>
<%--						</ul>--%>
<%--					</div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--				</div>--%>
<%--				<div style="position: relative;top: -88px; left: 285px;">--%>
<%--					<nav>--%>
<%--						<ul class="pagination">--%>
<%--							<li class="disabled"><a href="#">首页</a></li>--%>
<%--							<li class="disabled"><a href="#">上一页</a></li>--%>
<%--							<li class="active"><a href="#">1</a></li>--%>
<%--							<li><a href="#">2</a></li>--%>
<%--							<li><a href="#">3</a></li>--%>
<%--							<li><a href="#">4</a></li>--%>
<%--							<li><a href="#">5</a></li>--%>
<%--							<li><a href="#">下一页</a></li>--%>
<%--							<li class="disabled"><a href="#">末页</a></li>--%>
<%--						</ul>--%>
<%--					</nav>--%>
<%--				</div>--%>
<%--			</div>--%>

			<!--  Just create a div and give it an ID -->
			<div id="demo_pag1" style="position: relative;top: -10px"></div>
		</div>
		
	</div>
</body>
</html>