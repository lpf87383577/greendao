package com.shinhoandroid.test0909;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shinhoandroid.test0909.bean.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    List<User> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);



    }
    //增加单一数据
    public void bt1(View v){

        User user = new User();

        user.setGender("女");

        user.setUserName("bt1");

        user.setTelphone("1888888");

        DbUtils.insertData(user);
    }

    //增加多条数据
    public void bt12(View v){

        for (int i =0;i<4;i++){

            User user = new User();

            user.setGender("男");

            user.setUserName("小"+i);

            DbUtils.insertData(user);
        }
    }


    //删除单一数据
    public void bt2(View v){

        User user = new User();

        user.setGender("女");

        user.setUserName("bt1");

        user.setUserId(0L);

        DbUtils.deleteData(user);

    }

    //删除整张表数据
    public void bt23(View v){

        DbUtils.deleteAll(User.class);
    }

    public void bt3(View v){

        List list = DbUtils.queryAll(User.class);

        tv.setText(new Gson().toJson(list));


    }
    //查询数据
    public void bt4(View v){

        List list = DbUtils.queryData(User.class,"1");

        tv.setText(new Gson().toJson(list));

    }
    public void bt5(View v){
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);

        startActivity(intent);

    }
}
