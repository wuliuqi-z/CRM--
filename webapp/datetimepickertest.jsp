<%@page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
<%--    引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--    引入bootstrap框架--%>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css"/>
<%--    引入bootstrap——datetimepicker插件开发包--%>
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"/>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <title>演示bs_datetimerpicker插件</title>
    <script type="text/javascript">
        $(function (){
        //     当容器加载完成之后，对容器调用工具函数
            $("#myDate").datetimepicker({
                language:'zh-CN',//语言
                format:'yyyy-mm-dd',
                minView:'month',//可以选择的最小视图
                autoclose:true,//设置选择完日期之后是否自动关闭日历
                initialDate:new Date(),//默认的日期
                todayBtn:true,//设置是否显示今天按钮。默认为false
                clearBtn:true//是否显示清空按钮
            })
        })
    </script>
</head>
<body>
<form action="http://localhost" method="get">
    <input type="text"  id="myDate" name="111"readonly>
    <input type="submit" value="提交">
</form>

    <input type="date" >

</body>
</html>
