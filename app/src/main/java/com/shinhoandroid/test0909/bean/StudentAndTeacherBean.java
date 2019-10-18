package com.shinhoandroid.test0909.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Liupengfei
 * @describe 学生老师管理器
 * @date on 2019/10/17 17:33
 */
@Entity
public class StudentAndTeacherBean {

    Long studentNo; //学生ID
    Long teacherId; //老师ID
    @Generated(hash = 1523230813)
    public StudentAndTeacherBean(Long studentNo, Long teacherId) {
        this.studentNo = studentNo;
        this.teacherId = teacherId;
    }
    @Generated(hash = 207804266)
    public StudentAndTeacherBean() {
    }
    public Long getStudentNo() {
        return this.studentNo;
    }
    public void setStudentNo(Long studentNo) {
        this.studentNo = studentNo;
    }
    public Long getTeacherId() {
        return this.teacherId;
    }
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
 

}
