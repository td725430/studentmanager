<%--
  Created by IntelliJ IDEA.
  User: 唐东
  Date: 2021/1/26
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>操作界面</title>
    <style>
        *{
            margin:0px;
            padding:0px;
            box-sizing:border-box;}
        body{
            padding:30px; }
        .BigBox	{
            width:200px;
            height:100px;
            border:8px solid #EEEEEE;
            background-color:white;
            margin:auto;}
        .center-layout{
            margin:15px;
            float:left;}
        .center-layout table{
            border-collapse:separate;
            border-spacing:2px;
            border-color:gray;}
        a:link{
            color:pink;
        }
        a:hover{
            color:green;
        }
        a:active{
            color:yellow;
        }
        a:visited{
            color:red;}
        .right-layout div{
            float:left;}
    </style>
</head>
<body>
<div class="BigBox">
    <div class="center-layout">
        <a href="/studentmanager/addStudent.jsp">新增学生信息</a><br/>
        <a href="/studentmanager/listStudentServlet">学生信息管理</a>
    </div>
</div>
</body>
</html>
