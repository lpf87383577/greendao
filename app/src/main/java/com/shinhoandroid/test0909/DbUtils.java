package com.shinhoandroid.test0909;

import com.shinhoandroid.test0909.db.DaoSession;

import java.util.List;

/**
 * @author Liupengfei
 * @describe 数据库的操作
 * @date on 2019/9/12 9:04
 */
public class DbUtils {

    //插入数据
    public static void insertData(Object s) {
        MyApplication.getmDaoSession().insert(s);
    }

    //数据存在则替换，数据不存在则插入
    public static void insertOrReplace(Object s) {
        MyApplication.getmDaoSession().insertOrReplace(s);
    }

    //删除单一数据（按照主键去删除）
    public static void deleteData(Object s) {
        MyApplication.getmDaoSession().delete(s);
    }

    //删除整个表数据
    public static void deleteAll(Class c) {

        MyApplication.getmDaoSession().deleteAll(c);
    }

    //更新
    public static void updataData(Object s) {

        MyApplication.getmDaoSession().update(s);
    }

    //查找所有数据
    public static List queryAll(Class c){
        List mList = MyApplication.getmDaoSession().loadAll(c);
        return mList;
    }

    //按条件查找数据
    public static List queryData(Class c,String s) {
        List cs = MyApplication.getmDaoSession().queryRaw(c, " where id = ?", s);
        return cs;
    }


}
