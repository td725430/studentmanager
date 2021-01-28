package com.tenton.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenton.pojo.Student;
import com.tenton.utils.RedisConnectionUtil;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Date: 2021/1/26
 * @Author: Tenton
 * @Description: 查询所有学生信息操作
 */
@WebServlet(name = "ListStudentServlet",urlPatterns = "/listStudentServlet")
public class ListStudentServlet extends HttpServlet {
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
        //日期格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //从redis中获取所有学生数据
        Set<String> students = jedis.zrange("student", 0, -1);
        //创建一个ArrayList用于存储Student对象，便于传递给前端
        ArrayList<Student> list = new ArrayList<>();
        //遍历，默认是升序排列
        for (String str : students) {
            //将json数据装换成实体类对象
            Student student = mapper.readValue(str, Student.class);
            String format = sdf.format(student.getBirthday());
            Date birthday = java.sql.Date.valueOf(format);
            student.setBirthday(birthday);
            list.add(student);
        }
        //倒叙排列
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o2.getAvgscore().compareTo(o1.getAvgscore());
            }
        });
        //Redis中数据总条数
        int count = list.size();
        //设置每页显示10条数据
        int totalPage = count / 10;
        if (count % 10 >0){
            totalPage += 1;
        }
        ArrayList pages = new ArrayList();
        for (int i = 1;i <= totalPage;i++){
            pages.add(i);
        }
        //总页数，最后一页
        int size = pages.size();
        //创建一个List,用于存储分页显示的学生数据
        List<Student> studentsList = null;
        //获取前端当前页数
        String page = request.getParameter("page");
        //判断页数是否为空
        if (page == null){
            //数据库数据小于10条时
            if (count <= 10){
                studentsList = list.subList(0,count);
            }else {
                //数据库数据大于10条
                studentsList = list.subList(0,10);
            }
        }else {
            //将从前端获取到的页数转换成整数
            Integer pg = Integer.parseInt(page);
            //根据页数赋予开始显示数据位置
            int begin = (pg-1) * 10;
            //剩余数据
            int remainder = count % 10;
            //当余数 == 0时，说明后面已没有数据
            if (remainder == 0){
                studentsList = list.subList(begin,begin + 10);
            //当页数为1，且Redis中数据总数小于10条时
            }else if (pg == 1 && count <= 10){
                studentsList = list.subList(0,count);
            //当页数为1，且Redis中数据总数大于10条时
            }else if (pg == 1 && count >10){
                studentsList = list.subList(0,10);
            //当前页数小于总页数，说明还有多余的数据未显示，还有下一页
            }else if (pg < size){
                studentsList = list.subList(begin,begin + 10);
            //当前页数等于总页数，此时为尾页
            }else if (pg == size){
                studentsList = list.subList(begin,begin + remainder);
            }
        }
        request.setAttribute("pages",pages);
        request.setAttribute("list",studentsList);
        request.getRequestDispatcher("/main.jsp").forward(request,response);
    }
}
