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
import java.util.*;

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
    //先入先出
    @Test
    public void lpushAndrpopTest(){
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //从上往下依次往list队列中存储数据，上面的先入
        jedis.lpush("list","100");
        jedis.lpush("list","200");
        jedis.lpush("list","300");
        jedis.lpush("list","400");
        //遍历取值，从队列的右侧开始取值
        for (int i = 1;i <= jedis.llen("list");i++){
            System.out.println(jedis.rpop("list"));
        }
    }
    //后入先出
    @Test
    public void lpushAndlpopTest(){
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //从上往下依次往queue队列中存储数据，上面的先入
        jedis.lpush("queue","10");
        jedis.lpush("queue","20");
        jedis.lpush("queue","30");
        jedis.lpush("queue","40");
        //遍历取值，从队列的左侧开始取值
        for (int i = 1;i <= jedis.llen("queue");i++){
            System.out.println(jedis.lpop("queue"));
        }
    }
    //使用BitMap存储大数据
    @Test
    public void insertNum(){
        //连接redis
        Jedis jedis = RedisConnectionUtil.getJedis();
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取当天时间
        Date date = new Date();
        String format = sdf.format(date);
        //根据题意要求，用户Id在0~400000000之间，
        //所以这儿直接使用BitMap存储一条400000000的数据来创建一个具有400000000空间大小的位图，
        //每一个位对应一个用户
        jedis.setbit(format,400000000,true);
        //关闭jedis
        jedis.close();
    }
}
