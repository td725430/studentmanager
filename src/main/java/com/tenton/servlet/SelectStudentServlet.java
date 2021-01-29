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
import java.util.*;

/**
 * @Date: 2021/1/26
 * @Author: Tenton
 * @Description: 增加学生信息
 */
@WebServlet(name = "SelectStudentServlet",urlPatterns = "/selectStudentServlet")
public class SelectStudentServlet extends HttpServlet {
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

        //学号
        String name = request.getParameter("name");
        Student student = null;
        //日期格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //创建一个ArrayList用于存储Student对象，便于传递给前端
        ArrayList<Student> listName = new ArrayList<>();
        //判断此学号是否已经存在
        boolean flagI = false;
        if (name == null || name == ""){
            response.sendRedirect("/studentmanager/listStudentServlet");
        }else {
            //用于将json数据装换成实体类对象
            ObjectMapper mapper = new ObjectMapper();
            //连接redis
            Jedis jedis = RedisConnectionUtil.getJedis();
            //从redis中获取所有学生数据
            Set<String> students = jedis.zrange("student", 0, -1);
            A:for (String str : students) {
                //将json数据装换成实体类对象
                student = mapper.readValue(str, Student.class);
                if (name.equals(student.getId()) == true){
                    flagI = true;
                    break A;
                }else if (student.getName().contains(name) == true){
                    String format = sdf.format(student.getBirthday());
                    Date birthday = java.sql.Date.valueOf(format);
                    student.setBirthday(birthday);
                    listName.add(student);
                }
            }
            //关闭jedis
            jedis.close();
        }
        //根据Id查询学生存在
        if (flagI){
            //出生日期格式转换
            String format = sdf.format(student.getBirthday());
            Date birthday = java.sql.Date.valueOf(format);
            student.setBirthday(birthday);
            request.setAttribute("studentInfo",student);
            request.getRequestDispatcher("/studentInformation.jsp").forward(request,response);
        }else if (listName.size() > 0){
            //倒叙排列
            Collections.sort(listName, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o2.getAvgscore().compareTo(o1.getAvgscore());
                }
            });
            //Redis中数据总条数
            int count = listName.size();
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
                    studentsList = listName.subList(0,count);
                }else {
                    //数据库数据大于10条
                    studentsList = listName.subList(0,10);
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
                    studentsList = listName.subList(begin,begin + 10);
                    //当页数为1，且Redis中数据总数小于10条时
                }else if (pg == 1 && count <= 10){
                    studentsList = listName.subList(0,count);
                    //当页数为1，且Redis中数据总数大于10条时
                }else if (pg == 1 && count >10){
                    studentsList = listName.subList(0,10);
                    //当前页数小于总页数，说明还有多余的数据未显示，还有下一页
                }else if (pg < size){
                    studentsList = listName.subList(begin,begin + 10);
                    //当前页数等于总页数，此时为尾页
                }else if (pg == size){
                    studentsList = listName.subList(begin,begin + remainder);
                }
            }
            request.setAttribute("pageNum",pages);
            request.setAttribute("listName",studentsList);
            request.getRequestDispatcher("/selectByName.jsp").forward(request,response);
        }else {
            response.sendRedirect("/studentmanager/listStudentServlet");
        }
    }
}
