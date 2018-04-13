package com.example.zhishaoju.myapplication.model;

import java.io.Serializable;

/**
 * Created by zhishaoju on 2018/2/13.
 */

public class Person implements Serializable{
    public String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge(){
        return 10;
    }

}
