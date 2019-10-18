package com.shinhoandroid.test0909;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.shinhoandroid.test0909.db.DaoMaster;
import com.shinhoandroid.test0909.db.DaoSession;
import com.shinhoandroid.test0909.db.DbSQLiteOpenHelper;

/**
 * @author Liupengfei
 * @describe
 * @date on 2019/9/11 10:32
 */
public class MyApplication extends Application {

    private static MyApplication mApp;
    private static DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //配置数据库
        initGreenDao();
    }

    private void initGreenDao() {


        //如果你想查看日志信息，请将DEBUG设置为true
        MigrationHelper.DEBUG = true;

        //创建数据库mydb.db
        DbSQLiteOpenHelper helper = new DbSQLiteOpenHelper(this, "mydb.db",
                null);

        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getmDaoSession(){
        return mDaoSession;
    }

}
