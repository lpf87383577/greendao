package com.shinhoandroid.test0909;

import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shinhoandroid.test0909.bean.CreditCard;
import com.shinhoandroid.test0909.bean.IdCard;
import com.shinhoandroid.test0909.bean.Student;
import com.shinhoandroid.test0909.bean.StudentAndTeacherBean;
import com.shinhoandroid.test0909.bean.Teacher;
import com.shinhoandroid.test0909.bean.User;
import com.shinhoandroid.test0909.db.CreditCardDao;
import com.shinhoandroid.test0909.db.DaoSession;
import com.shinhoandroid.test0909.db.IdCardDao;
import com.shinhoandroid.test0909.db.StudentAndTeacherBeanDao;
import com.shinhoandroid.test0909.db.StudentDao;
import com.shinhoandroid.test0909.db.TeacherDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    TextView tv;

    Long onetwoone = 1L;

    Long onetwoMany = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tv = findViewById(R.id.tv);


    }

    //1对1关系
    public void bt1(View v){

        addStudent();

    }

    //1对1关系
    public void bt4(View v){

        tv.setText(new Gson().toJson(getOne2One()));

    }

    //1对多关系添加
    public void bt2(View v){

        addOne2Many();

    }

    //1对多关系查询
    public void bt5(View v){

        tv.setText(new Gson().toJson(getOne2Many()));

    }
    //多对多关系添加
    public void bt3(View v){

        addMany2Many();

        QueryBuilder<Student> queryBuilder1 = MyApplication.getmDaoSession().queryBuilder(Student.class);
        QueryBuilder<Teacher> queryBuilder2 = MyApplication.getmDaoSession().queryBuilder(Teacher.class);
        QueryBuilder<StudentAndTeacherBean> queryBuilder3 = MyApplication.getmDaoSession().queryBuilder(StudentAndTeacherBean.class);

        tv.setText(new Gson().toJson(queryBuilder1.list())+"\n"+new Gson().toJson(queryBuilder2.list())+"\n"+new Gson().toJson(queryBuilder3.list()));

    }

    //多对多关系查询
    public void bt6(View v){

        tv.setText(new Gson().toJson(getMany2Many()));

    }

    //一对一插入数据
    public void addStudent(){

        DaoSession daoSession = MyApplication.getmDaoSession();

        Student student = new Student();

        student.setStudentNo(onetwoone);

        student.setAge(12);

        student.setName("小"+onetwoone);

        daoSession.insert(student);

        //插入对应的IdCard数据
        IdCard idCard = new IdCard();
        idCard.setUserName("小"+onetwoone);
        idCard.setIdNo(onetwoone);
        daoSession.insert(idCard);

        onetwoone++;
    }

    //一对多添加数据
    public void addOne2Many(){

        DaoSession daoSession = MyApplication.getmDaoSession();
        Student student = new Student();
        student.setId(onetwoMany);

        student.setAge(1);
        student.setName("小明"+onetwoMany);
        daoSession.insert(student);

        //插入对应的CreditCard数据
        for (int j = 0; j<5 ; j++) {

            CreditCard creditCard = new CreditCard();

            creditCard.setId(onetwoMany);

            creditCard.setCardNum("111"+onetwoMany+""+j);

            daoSession.insert(creditCard);
        }
        onetwoMany++;
    }

    //查询一对一数据,查询学生卡身份证是111的学生
    public List<Student> getOne2One(){

        QueryBuilder<Student> queryBuilder = MyApplication.getmDaoSession().queryBuilder(Student.class);

        queryBuilder.join(IdCard.class, IdCardDao.Properties.IdNo)
                .where(IdCardDao.Properties.IdNo.gt(1));

        return queryBuilder.list();

    }

    public List<CreditCard> getOne2Many(){

        QueryBuilder<Student> queryBuilder = MyApplication.getmDaoSession().queryBuilder(Student.class);

        List<CreditCard> list = new ArrayList<>();

        for(Student student:queryBuilder.list()){

            list.addAll(student.getMCreditCards());

        }

        return list;

    }

    public void addMany2Many(){

        DaoSession daoSession = MyApplication.getmDaoSession();

        Student student1 = new Student();
        student1.setStudentNo(1L);
        student1.setAge(9);
        student1.setName("小明");

        Student student2 = new Student();
        student2.setStudentNo(2L);
        student2.setAge(10);
        student2.setName("小花");

        Student student3 = new Student();
        student3.setStudentNo(3L);
        student3.setAge(9);
        student3.setName("小李");
        //存储学生
        daoSession.insert(student1);
        daoSession.insert(student2);
        daoSession.insert(student3);

        Teacher teacher1 = new Teacher();
        teacher1.setTeacherId(1L);
        teacher1.setName("李老师");

        Teacher teacher2 = new Teacher();
        teacher2.setTeacherId(2L);
        teacher2.setName("王老师");

        //存储老师
        daoSession.insert(teacher1);
        daoSession.insert(teacher2);

        //存储老师和学生关系
        StudentAndTeacherBean teacherBean1 = new StudentAndTeacherBean(student1.getId(), teacher1.getTeacherId());
        daoSession.insert(teacherBean1);
        StudentAndTeacherBean teacherBean2 = new StudentAndTeacherBean(student2.getId(), teacher1.getTeacherId());
        daoSession.insert(teacherBean2);
        StudentAndTeacherBean teacherBean3 = new StudentAndTeacherBean(student2.getId(), teacher2.getTeacherId());
        daoSession.insert(teacherBean3);
        StudentAndTeacherBean teacherBean4 = new StudentAndTeacherBean(student3.getId(), teacher2.getTeacherId());
        daoSession.insert(teacherBean4);



}

    //获取李老师和王老师共同的学生
    public List<Student> getMany2Many(){

        QueryBuilder<StudentAndTeacherBean> queryBuilder = MyApplication.getmDaoSession().queryBuilder(StudentAndTeacherBean.class);

        List<StudentAndTeacherBean> studentAndTeacherBeans = queryBuilder.where(StudentAndTeacherBeanDao.Properties.TeacherId.eq(1)).list();

        List<Long> l = new ArrayList<>();

        for (StudentAndTeacherBean s: studentAndTeacherBeans ) {

            l.add(s.getStudentNo());
        }

        Log.e("lpf--",new Gson().toJson(studentAndTeacherBeans));

        QueryBuilder<Student> queryBuilder2 = MyApplication.getmDaoSession().queryBuilder(Student.class);

        List<Student> students = queryBuilder2.where(StudentDao.Properties.StudentNo.in(l)).list();

        return students;

    }

}
