<%@page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>演示文件下载</title>
    <script type="text/javascript" src="jquery/jQuery.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#fileDownloadBtn").click(function (){
            //     文件下载只能用同步，因为ajax只能解析json格式的字符串
                window.location.href="workbench/activity/fileDownload.do"
            })
        })
    </script>
</head>
<body>
    <input type="button" value="下载吧" id="fileDownloadBtn">
</body>
</html>
