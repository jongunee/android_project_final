package com.example.project;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 그리드뷰 어댑터
 */
public class GridAdapter extends BaseAdapter {

    private Calendar mCal;
    private final List<String> list;
    private final LayoutInflater inflater;
    private Context appCompatActivity = null;
    Map<String, Map<String, Object>> list3;
    int layout;

    /**
     * 생성자
     */

    public GridAdapter(Context context, List<String> list, Map<String, Map<String, Object>> list3, int view) {
        appCompatActivity = context;

        this.list = list;
        this.list3 = list3;

        Log.e("adapter", "constructor " + list3.toString());
        this.layout = view;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override

    public int getCount() {

        return list.size();

    }

    @Override

    public String getItem(int position) {

        return list.get(position);

    }

    @Override

    public long getItemId(int position) {

        return position;

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        //int a = position - 9;

        //Log.e("data!",  String.valueOf(a));
        Log.e("data5", String.valueOf(position));

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);

            holder = new ViewHolder();


            holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);

            holder.totalGridview = (TextView) convertView.findViewById(R.id.total_gridview);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        mCal = Calendar.getInstance();

        holder.tvItemGridView.setText("" + getItem(position));

        String year = mCal.get(Calendar.YEAR) + "";
        int m = mCal.get(Calendar.MONTH) + 1;
        String month = m < 10 ? "0" + m : "" + m;

        String item = getItem(position);

        String key = year + "/" + month + "/" + item;
        Log.e("adapter", "position " + position);
        Log.e("adapter", "item " + item);

        Map<String, Object> dbData = list3.get(key);
        if(dbData != null) {
            holder.totalGridview.setText((String) dbData.get("sum"));
        }

//        list3.get(item);
//        holder.totalGridview.setText((String) list3.get(item).get("sum"));


        //money.setText(data.get(position).listMoney);

        //Log.e("data5",  String.valueOf(a));
        Log.e("data5", String.valueOf(position));

        //해당 날짜 텍스트 컬러,배경 변경
        holder.totalGridview.setTextColor(appCompatActivity.getResources().getColor(R.color.black));


        //오늘 day 가져옴

        Integer today = mCal.get(Calendar.DAY_OF_MONTH);

        String sToday = String.valueOf(today);


        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            String Today = String.valueOf(i);
            if (Today.equals(getItem(position))) {
                holder.tvItemGridView.setTextColor(appCompatActivity.getResources().getColor(R.color.black));
            }
        }
        if ("일".equals(getItem(0))) {
            holder.tvItemGridView.setTextColor(appCompatActivity.getResources().getColor(R.color.gray));
        }

        sToday = String.valueOf(today);
        if (sToday.equals(getItem(position))) {
            holder.tvItemGridView.setTextColor(appCompatActivity.getResources().getColor(R.color.black));
        }

        return convertView;

    }

}