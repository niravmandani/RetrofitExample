package com.example.av004.retrofitexample.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by AV004 on 12/28/2016.
 */
@Database(name = MyDatabase.DATABASE_NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String DATABASE_NAME = "usersManager";

    public static final int VERSION = 1;

}
