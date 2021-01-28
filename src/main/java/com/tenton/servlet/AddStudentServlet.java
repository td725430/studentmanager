package com.tenton.servlet;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenton.pojo.Student;
import com.tenton.utils.RedisConnectionUtil;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @Date: 2021/1/26
 * @Author: Tenton
 * @Description: 增加学生信息
 */
@WebServlet(name = "AddStudentServlet",urlPatterns = "/addStudentServlet")
public class AddStudentServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码格式，防止乱码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //用于将json数据装换成实体类对象
        ObjectMapper mapper = new ObjectMapper();
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //从redis中获取所有学生数据
        Set<String> students = jedis.zrange("student", 0, -1);
        //学号
        String id = request.getParameter("id");
        //判断此学号是否已经存在
        boolean flag = false;
        A:for (String str : students) {
            //将json数据装换成实体类对象
            Student student = mapper.readValue(str, Student.class);
            if (id.equals(student.getId()) == true){
                flag = true;
                break A;
            }
        }
        if (flag){
            response.getWriter().write("此学号已有对应学生信息，2秒后自动跳转到主界面");
            response.setHeader("Refresh", "2;URL=/listStudentServlet");
        }else {
            //姓名
            String name = request.getParameter("name");
            //出生日期
            String birth = request.getParameter("birthday");
            //日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = sdf.parse(birth);
            //备注
            String description = request.getParameter("description");
            //平均分
            int avgscore = Integer.parseInt(request.getParameter("avgscore"));
            //创建一个学生实体，封装从前端接收到的数据
            Student student = new Student(id, name, birthday, description, avgscore);
            //将学生实体转换为json字符串格式
            String jsonString = JSON.toJSONString(student);
            jedis.zadd("student", avgscore, jsonString);
            response.sendRedirect("/listStudentServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
