<%--
  Created by IntelliJ IDEA.
  User: 唐东
  Date: 2021/1/26
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <style>
        .border_distance{
            border:2px solid #CD8090;
        }
        .border_distance tr td{
            border:1px solid #CD6889;
        }
    </style>
</head>
<body>
<h2 align="center">学生信息管理</h2>
<hr>
<table class="border_distance" width="800" align="center"  bgcolor="white">
    <thead>
    <tr align="center">
        <td colspan="5">
            <a href="addStudent.jsp">添加学生信息</a>
        </td>
        <td colspan="5">
            <a href="/studentmanager/listStudentServlet">返回主页</a>
        </td>
    </tr>
    <tr  align="center">
        <form action="/studentmanager/selectStudentServlet" method="get">
            <td>
                <input type="text" name="name"/>
            </td>
            <td align="center" colspan="9">
                <input type="submit" value="查&nbsp;询"/>
            </td>
        </form>
    </tr>
    <tr bgcolor="#DB7093">
        <td align="center" width="30">学号</td>
        <td align="center" width="60">姓名</td>
        <td align="center" width="100">出生日期</td>
        <td align="center" width="200">备注</td>
        <td align="center" width="60">平均分</td>
        <td align="center" colspan="2" width="100">操作</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.listName}" var="student">
        <tr>
            <td style="width: 30px">${student.id}</td>
            <td style="width: 60px">${student.name}</td>
            <td style="width: 100px">${student.birthday}</td>
            <td style="width: 80px">${student.description}</td>
            <td style="width: 80px">${student.avgscore}</td>
            <td style="width: 80px">
                <a href="${pageContext.request.contextPath}/toUpdateStudentServlet?id=${student.id}">修改信息</a>
            </td>
            <td style="width: 80px">
                <a href="javascript: if(window.confirm('是否删除？')){window.location.href='${pageContext.request.contextPath}/deleteStudentServlet?id=${student.id}'}">删除信息</a>
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="7">
            <c:forEach items="${requestScope.pageNum}" var="page">
                <a href="/studentmanager/selectStudentServlet?page=${page}">${page}</a>
            </c:forEach>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>
