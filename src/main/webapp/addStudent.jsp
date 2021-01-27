<%--
  Created by IntelliJ IDEA.
  User: 唐东
  Date: 2021/1/26
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        *{
            margin:0px;
            padding:0px;
            box-sizing:border-box;}
        body{
            padding:30px; }
        .BigBox	{
            width:600px;
            height:300px;
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
        .td-left{
            width:100px;
            text-align:right;
            height:45px;
        }
        .td-right{
            padding-left:50px;
        }
        #id,#name,#birthday,#description,#avgscore{
            width:250px;
            height:32px;
            border:1px solid #a6a6a6;
            border-radius:5px;
            padding-left:10px;
        }
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
    <script type="text/javascript">
        /* 表单的js校验  */
        window.onload=function(){
            document.getElementById("form").onsubmit=function(){
                return checkId()&&checkName()&&checkDescription()&&checkAvgscore();
            }
            document.getElementById("id").onblur=checkId;
            document.getElementById("name").onblur=checkName;
            document.getElementById("description").onblur=checkDescription;
            document.getElementById("avgscore").onblur=checkAvgscore;
        }
        function checkId(){
            var id=document.getElementById("id").value;
            var reg=/^\d{1,40}$/;
            var flag=reg.test(id);
            var s_id=document.getElementById("s_id");
            if(flag)
            {
                s_id.innerHTML=" ";
            }else {
                s_id.innerHTML="Id格式有误！";
            }
        }
        function checkName(){
            var name=document.getElementById("name").value;
            var reg=/^[\u4e00-\u9fa5a-zA-Z]{1,40}$/;
            var flag=reg.test(name);
            var s_name=document.getElementById("s_name");
            if(flag)
            {
                s_name.innerHTML=" ";
            }else {
                s_name.innerHTML="姓名格式有误！";
            }
        }
        function checkDescription(){
            var description=document.getElementById("description").value;
            var reg=/^[u4E00-u9FA5]{0,255}|[a-zA-Z]{0,255}$/;
            var flag=reg.test(description);
            var s_description=document.getElementById("s_description");
            if(flag)
            {
                s_description.innerHTML=" ";
            }else {
                s_description.innerHTML="备注格式有误！";
            }
        }
        function checkAvgscore(){
            var avgscore=document.getElementById("avgscore").value;
            var reg=/^[0-9]*$/;
            var flag=reg.test(avgscore);
            var s_avgscore=document.getElementById("s_avgscore");
            if(flag)
            {
                s_avgscore.innerHTML=" ";
            }else {
                s_avgscore.innerHTML="平均分格式有误！";
            }
        }
    </script>
</head>
<body>
<div class="BigBox">
    <div class="center-layout">
        <form action="/addStudentServlet" method="post" id="form">
            <table>
                <tbody>
                <tr>
                    <td class="td-left">
                        <label for="id">学号</label>
                    </td>
                    <td class="td-right">
                        <input type="text" name="id" id="id" placeholder="请输入学号">
                        <span id="s_id" class="error"></span>
                    </td>
                </tr>
                <tr>
                    <td class="td-left">
                        <label for="name">姓名</label>
                    </td>
                    <td class="td-right">
                        <input type="name" name="name" id="name" placeholder="请输入姓名">
                        <span id="s_name" class="error"></span>
                    </td>
                </tr>
                <tr>
                    <td class="td-left">
                        <label for="birthday">出生日期</label>
                    </td>
                    <td class="td-right">
                        <input type="date" name="birthday" id="birthday"/>
                </tr>
                <tr>
                    <td class="td-left">
                        <label for="description">备注</label>
                    </td>
                    <td class="td-right">
                        <input type="text" name="description" id="description" placeholder="请输入备注">
                        <span id="s_description" class="error"></span>
                    </td>
                </tr>
                <tr>
                    <td class="td-left">
                        <label for="avgscore">平均分</label>
                    </td>
                    <td class="td-right">
                        <input type="text" name="avgscore" id="avgscore" placeholder="请输入平均分">
                        <span id="s_avgscore" class="error"></span>
                    </td>
                </tr>
                <tr align="center">
                    <td colspan="10">
                        <input style="width: 100px" type="submit" value="提&nbsp;&nbsp;&nbsp;&nbsp;交"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
</body>
</html>
