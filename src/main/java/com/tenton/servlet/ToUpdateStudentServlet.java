package com.tenton.servlet;

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
@WebServlet(name = "ToUpdateStudentServlet",urlPatterns = "/toUpdateStudentServlet")
public class ToUpdateStudentServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        //判断删除的学生是否正确
        boolean flag = false;
        Student student = null;
        //遍历，默认是升序排列
        A:for (String str : students) {
            //将json数据装换成实体类对象
            student = mapper.readValue(str, Student.class);
            if (id.equals(student.getId())){
                flag = true;
                break A;
            }
        }
        //关闭jedis
        jedis.close();
        if (flag){
            //日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(student.getBirthday());
            //将转换格式后的日期字符串数据转成Date类型赋值给birthday
            Date birthday = java.sql.Date.valueOf(format);
            //重置学生的出生日期
            student.setBirthday(birthday);
            //将学生数据添加到request域中，传递给前端
            request.setAttribute("student",student);
            //请求跳转到修改页面
            request.getRequestDispatcher("/updateStudent.jsp").forward(request,response);
        }
    }
}
