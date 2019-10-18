package com.shinhoandroid.test0909.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Liupengfei
 * @describe 学生卡（一对一关系）
 * @date on 2019/10/15 10:57
 */
@Entity
public class IdCard {

    @Id
    Long idNo;//学生号

    String userName;//用户名

    @Generated(hash = 1946426852)
    public IdCard(Long idNo, String userName) {
        this.idNo = idNo;
        this.userName = userName;
    }

    @Generated(hash = 1500073048)
    public IdCard() {
    }

    public Long getIdNo() {
        return this.idNo;
    }

    public void setIdNo(Long idNo) {
        this.idNo = idNo;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    
}
