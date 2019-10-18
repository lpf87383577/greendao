package com.shinhoandroid.test0909.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.shinhoandroid.test0909.bean.IdCard;
import com.shinhoandroid.test0909.bean.Teacher;

import org.greenrobot.greendao.database.Database;

/**
 * @author Liupengfei
 * @describe 数据库升级辅助类
 * @date on 2019/10/18 14:34
 */
public class DbSQLiteOpenHelper extends DaoMaster.OpenHelper {

    public DbSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.e("lpf--","数据库版本--"+oldVersion+"---"+newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, UserDao.class, IdCardDao.class, TeacherDao.class,StudentDao.class,
                StudentAndTeacherBeanDao.class,CreditCardDao.class);
    }
}
