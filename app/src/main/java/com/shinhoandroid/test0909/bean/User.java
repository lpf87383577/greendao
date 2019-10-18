package com.shinhoandroid.test0909.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class User {

    String userName;
    //@Id：主键 (autoincrement = true)表示主键会自增，如果false就会使用旧值
    //使用Long类型，不能使用long类型否则会报错，
    @Id(autoincrement = true)
    Long userId;

    String gender;

    String telphone;

    @Generated(hash = 2146744915)
    public User(String userName, Long userId, String gender, String telphone) {
        this.userName = userName;
        this.userId = userId;
        this.gender = gender;
        this.telphone = telphone;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelphone() {
        return this.telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }


}
