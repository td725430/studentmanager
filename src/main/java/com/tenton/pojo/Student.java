package com.tenton.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Date: 2021/1/26
 * @Author: Tenton
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    /**
     * 学号
     */
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 备注
     */
    private String description;
    /**
     * 平均分
     */
    private Integer avgscore;
}
