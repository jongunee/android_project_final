package com.example.project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 리스트형 달력 어뎁터
 **/
public class CustomAdapter extends BaseAdapter {
    MainActivity ma;
    int layout;
    ArrayList<AbookListVO> data;

    CustomAdapter(MainActivity ma, int view, ArrayList<AbookListVO> data) {
        this.ma = ma;
        this.layout = view;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ma.getLayoutInflater();
        View view = inflater.inflate(layout, null);
        TextView day = view.findViewById(R.id.list_day);
        TextView money = view.findViewById(R.id.list_money);
        TextView dayWeek = view.findViewById(R.id.list_day_week);
        Button mainListBtn = view.findViewById(R.id.list_btn);

        day.setText(data.get(position).listDay);

        if(data.get(position).listMoney.equals("0")) {
            money.setText("내역이 없습니다.");
        }
        else {
            money.setText(data.get(position).listMoney);
        }

        dayWeek.setText(data.get(position).listDayWeek);

        String dayParam = day.getText().toString();
        String month = data.get(position).listMonth;
        String year = data.get(position).listYear;

        final String resDay = year + "/" + month + "/" + dayParam;

        mainListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ma, Detail.class);
                intent.putExtra("date", resDay);
                ma.startActivityForResult(intent, 200);
            }
        });

        return view;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}