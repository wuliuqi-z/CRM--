<%@page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>文件上传测试</title>
</head>
<body>
    <form action="workbench/activity/fileUpLoad.do" method="post" enctype="multipart/form-data">
<%--        文件上传的表单不能随便写，必须满足三个条件才能上传
        1.表单组件标签有很多，但是只能用type=file
        2.请求方式只能用post
        3.form表单的编码格式只能用：myltipart/form-data。浏览器默认的编码格式是：urlencoded（互联网上传输数据的一种常见的编码格式，这种编码格式只能对文本数据进行编码【字符串数据】
        浏览器每次向后台提交参数，都会首先讲所有的参数转换成字符串，然后讲这些数据进行urlencoded编码）
        但是现在我提交的是二进制数据，二进制数据文件能转成字符串吗？--%>
        <input type="text" name="userName"><br>
        <input type="file" name="myFile"><br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
