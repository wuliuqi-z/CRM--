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
<%--    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>--%>
    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
<%--    <link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css"/>--%>
    <title>演示echarts报表插件</title>
    <script type="text/javascript">
        $(function (){
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '某网站各个商品销售实例',
                    textVerticalAlign :'left',
                    subtext: "测试副标题"
                },
                tooltip: {
                    textStyle:{
                        color:'white'
                    }
                },//提示框
                legend: {
                    data: ['销量']
                },
                yAxis: {
                    data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
                },
                xAxis: {},
                series: [
                    {
                        name: '销量',
                        type: 'funnel',
                        data: [5, 20, 36, 10, 10, 20]
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        })
    </script>
</head>
<body>
    <div id='main' style="width:600px;height: 400px;"></div>
</body>
</html>
