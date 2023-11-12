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
<%--    引入BootStrap下的typeahead插件--%>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <title>演示自动补全插件</title>
    <script type="text/javascript">
        $(function (){
        //     当容器加载完成之后对容器调用插件函数
            $("#customerName").typeahead({
                source:function (jquery,process){//process本身是个函数，可以将数组类型的json字符串赋值给source
                //     输入关键字的时候每次键盘弹起，都会自动触发本函数，然后我们可以向后台发送请求，查询客户表中所有的公司名称，以json[]字符串的形式返回到前台，赋值给source这个参数
                    $.ajax({
                        url:'workbench/transaction/queryAllCustomerName.do',
                        data:{
                          search:jquery
                        },
                        type:'post',
                        dataType:'json',
                        success:function(data){//['','','']
                            process(data)
                        }
                    })

                }
            })
        })
    </script>
</head>
<body>
    <input type="text" id="customerName">
</body>
</html>
