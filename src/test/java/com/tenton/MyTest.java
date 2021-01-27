package com.tenton;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenton.pojo.Student;
import com.tenton.utils.RedisConnectionUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Date: 2021/1/25
 * @Author: Tenton
 * @Description:
 */
public class MyTest {
    @Test
    public void redisTest(){
        Jedis jedis = RedisConnectionUtil.getJedis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birth = "1998-12-22";
        try {
            Date birthday = sdf.parse(birth);
            Student student = new Student("0001","Tenton",birthday,"学习很努力，乐观向上",99);
//            Student student1 = new Student("0002","李四",birthday,"学习很努力，乐观向上",90);
//            Student student2 = new Student("0003","王五",birthday,"学习很努力，乐观向上",88);
//            Student student3 = new Student("0004","赵六",birthday,"学习很努力，乐观向上",95);
            jedis.zadd("student",99,JSON.toJSONString(student));
//            jedis.zadd("student",90,JSON.toJSONString(student1));
//            jedis.zadd("student",88,JSON.toJSONString(student2));
//            jedis.zadd("student",95,JSON.toJSONString(student3));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("字符串转换为Date异常");
        }
    }
    @Test
    public void redisGetTest() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Jedis jedis = RedisConnectionUtil.getJedis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> students = jedis.zrange("student", 0, -1);
        for (String str : students) {
            Student student = mapper.readValue(str, Student.class);
            System.out.println(sdf.format(student.getBirthday()));
        }
    }
    @Test
    public void mapTest() {
        Map<String,String> map = new HashMap<>();
        map.put("0001","张三");
        map.put("0002","李四");
        System.out.println(map.values());
    }
    @Test
    public void sizeTest(){
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //从redis中获取所有学生数据
        Set<String> students = jedis.zrange("student", 0, -1);
        System.out.println(students.size());
    }
    @Test
    public void idExistTest() throws JsonProcessingException {
        //用于将json数据装换成实体类对象
        ObjectMapper mapper = new ObjectMapper();
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //从redis中获取所有学生数据
        Set<String> students = jedis.zrange("student", 0, -1);
        //学号
        String id = "1004";
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
            System.out.println("1111");
        }else {
            System.out.println("0000");
        }
    }
}
