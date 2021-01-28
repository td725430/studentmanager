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
 * @Description:
 */
@WebServlet(name = "UpdateStudentServlet",urlPatterns = "/updateStudentServlet")
public class UpdateStudentServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码格式，防止乱码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //学号
        String id = request.getParameter("id");
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
        //用于将json数据装换成实体类对象
        ObjectMapper mapper = new ObjectMapper();
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //从redis中获取所有学生数据
        Set<String> students = jedis.zrange("student", 0, -1);
        //判断删除的学生是否正确
        boolean flag = false;
        Student student = null;
        //遍历，默认是升序排列
        A:for (String str : students) {
            //将json数据装换成实体类对象
            student = mapper.readValue(str, Student.class);
            if (id.equals(student.getId()) == true){
                flag = true;
                break A;
            }
        }
        if (flag){
            String jsonString = JSON.toJSONString(student);
            jedis.zrem("student",jsonString);
            student = new Student(id,name,birthday,description,avgscore);
            String jsonStr = JSON.toJSONString(student);
            jedis.zadd("student",avgscore,jsonStr);
            response.sendRedirect("/studentmanager/listStudentServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
