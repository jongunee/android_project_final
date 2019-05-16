package com.example.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.project.MainActivity.D_CONTENT;
import static com.example.project.MainActivity.TABLE_DNAME;
import static com.example.project.MainActivity.D_PK;
import static com.example.project.MainActivity.D_TITLE;
import static com.example.project.MainActivity.D_DATE;


/**
 * Created by 1hk_s on 2018-06-07.
 */

class MyDBHelper2 extends SQLiteOpenHelper {
    public MyDBHelper2(Context context) {
        super(context, "myDB.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DBHelper", "onCreate");
        db.execSQL("DROP TABLE IF EXISTS diary;");
        db.execSQL("CREATE TABLE diary (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "content TEXT," +
                "w_date TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DBHelper", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS diary;");
    }
    public void insert(String title, String content) {
        Log.e("helper", "insert");
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO diary (id, title, content, w_date) VALUES " +
                " (NULL, ?, ?, datetime('now','localtime'))", new Object[] {title, content});
        db.close();
    }

    public List<Map<String, Object>> select() {
        Log.e("helper", "select");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, title, content, w_date FROM diary", null);
        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", cursor.getInt(0));
            map.put("title", cursor.getString(1));
            map.put("content", cursor.getString(2));
            map.put("w_date", cursor.getString(3));
            list.add(map);
        }
        db.close();
        return list;
    }
}