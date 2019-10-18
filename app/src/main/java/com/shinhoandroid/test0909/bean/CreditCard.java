package com.shinhoandroid.test0909.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Liupengfei
 * @describe 信用卡（一对多关系）
 * @date on 2019/10/17 13:53
 */
@Entity
public class CreditCard {

    Long id;  //身份证号码

    String userName;//持有者名字

    String cardNum;//卡号

    @Generated(hash = 85321780)
    public CreditCard(Long id, String userName, String cardNum) {
        this.id = id;
        this.userName = userName;
        this.cardNum = cardNum;
    }

    @Generated(hash = 1860989810)
    public CreditCard() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }



}
