package com.example.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.project.Detail.KEY_CONTEXT;
import static com.example.project.Detail.KEY_DATE;
import static com.example.project.Detail.KEY_ID;
import static com.example.project.Detail.KEY_PAY;
import static com.example.project.Detail.KEY_PRICE;
import static com.example.project.Detail.TABLE_NAME;
/**
 * Created by 1hk_s on 2018-06-07.
 */

class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {

        super(context, "My_Account_Data.db", null, 5);
    }

    public void onCreate(SQLiteDatabase db) {
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String query = String.format("CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "%s TEXT, "
                + "%s INTEGER, "
                + "%s TEXT, "
                + "%s TEXT);", TABLE_NAME, KEY_ID, KEY_CONTEXT, KEY_PRICE, KEY_PAY, KEY_DATE);
        db.execSQL(query);


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public Map<String, Map<String, Object>> getData() {
        SQLiteDatabase db = getWritableDatabase();

        Map<String, Map<String, Object>> list = new HashMap<>();

        // 총합 가격 표시
        //String queryPriceSum = String.format( " SELECT SUM(price) FROM %s WHERE date = '%s'", TABLE_NAME, View_DATE);
        String queryPriceSum = String.format( " SELECT DATE, SUM(PRICE) FROM %s GROUP BY DATE", TABLE_NAME);

        System.out.println("res1 : " + queryPriceSum);

        Cursor cursor = db.rawQuery(queryPriceSum, null);
        //cursor.moveToNext();

        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", cursor.getString(0));
            map.put("sum", String.valueOf(cursor.getInt(1)));
            list.put(cursor.getString(0), map);
        }

        return list;
    }
}