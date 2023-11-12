<%@page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <%--    引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--    引入bootstrap框架--%>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css"/>
<%--    导入pagination插件--%>
    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <script type="text/javascript">
        $(function() {
            $("#demo_pag1").bs_pagination({
                totalPages: 100,
                currentPage:1,//当前页号。默认为1.相当于pageNo
                rowsPerPage:10,//每页显示条数，默认为10
                totalRows:1000,//总条数，不填的时候总页数和每页显示条数决定
                visiblePageLinks:10,//最多可以显示的卡片数
                showGoToPage:true,//控制跳转的是否显示，默认为true
                showRowsPerPage:true,//是否显示控制每页条数的功能
                showRowsInfo: true,//是否显示页数信息默认为true
                onChangePage:function(event,pageObj){
                //     用户每次切换页号的时候默认执行的函数，包括修改每页显示条数的时候（这也属于修改页号）
                //     这个函数会给你返回切换页号之后的ageNo和pageSize(pageObj,就代表了整个翻页对象，每次切换之后这个对象就会变)

                }

            });
        });
    </script>
</head>
<body>
<!--  Just create a div and give it an ID -->

<div id="demo_pag1"></div>
</body>
</html>
