package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    LinearLayout calendar, list, diary;
    private TextView tvDate;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;

    private ArrayList<String> diary_list = null;
    private ArrayAdapter<String> diary_adapter;
    private ListView diary_listView = null;
    private Button add;
    private Button remove;
    private EditText editText;

    final static String D_CONTENT = "dContent";
    final static String TABLE_DNAME = "MyDiaryList";
    final static String D_PK = "dPk";
    final static String D_TITLE = "dTitle";
    final static String D_DATE = "dDate";

    ListView d_listview;
    DiaryAdapter d_adapter;
    MyDBHelper2 myDBHelper2;
    List<Map<String, Object>> d_list;


    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    MyCursorAdapter myAdapter;

    final static String KEY_ID = "_id";
    final static String KEY_CONTEXT = "context";
    final static String KEY_PRICE = "price";
    final static String KEY_PAY = "pay";
    final static String TABLE_NAME = "MyAccountList";
    final static String KEY_DATE = "date";
    public static String View_DATE = getToday_date();           // 날짜를 현재날짜로 초기화

    /**
     * 하단 버튼 설정
     **/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    calendar.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                    diary.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    calendar.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    diary.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    calendar.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                    diary.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.abook_calendar);
        list = findViewById(R.id.abook_list);
        diary = findViewById(R.id.abook_diary);

        tvDate = (TextView) findViewById(R.id.tv_date);
        gridView = (GridView) findViewById(R.id.gridview);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /**  달력표 **/

        TextView grid_itemView = findViewById(R.id.total_gridview);
        // 오늘에 날짜를 세팅 해준다.

        long now = System.currentTimeMillis();

        final Date date = new Date(now);

        //연,월,일을 따로 저장

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);

        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);

        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        /** 환경설정 버튼 **/
        final ImageView setButton = findViewById(R.id.setting);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
            }
        });

        /** 달력표 **/
        //현재 날짜 텍스트뷰에 뿌려줌

        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));


        //gridview 요일 표시

        dayList = new ArrayList<String>();

        dayList.add("일");

        dayList.add("월");

        dayList.add("화");

        dayList.add("수");

        dayList.add("목");

        dayList.add("금");

        dayList.add("토");


        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)

        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);

        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add

        for (int i = 1; i < dayNum; i++) {

            dayList.add("");

        }
        String YY = curYearFormat.format(date);
        String MM = curMonthFormat.format(date);

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        //gridAdapter = new GridAdapter(getApplicationContext(), dayList, data);
        ArrayList<AbookListVO> data3 = new ArrayList<>();

        mHelper = new MyDBHelper(this);
        Map<String, Map<String, Object>> list3 = mHelper.getData();

        Log.e("data1", list3.toString());
        Log.e("data1", String.valueOf(mCal.getActualMaximum(Calendar.DAY_OF_MONTH)));
        int max_day = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);


        int[] g_day;
        g_day = new int[max_day];

        String g_daylist;
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            g_day[i] = i + 1;
            Log.e("data1", String.valueOf(g_day[i]));

        }

        GridAdapter g_adapter = new GridAdapter(getApplicationContext(), dayList, list3, R.layout.item_calendar_gridview);
        gridView.setAdapter(g_adapter);

        //gridView.setAdapter(gridAdapter);
        //CustomAdapter adapter = new CustomAdapter(MainActivity.this, R.layout.list_view, data);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //setContentView(R.layout.detail);
                String s_date = curYearFormat.format(date) + "/" + curMonthFormat.format(date) + "/" + String.valueOf(position - 9);


                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra("date", s_date);
                startActivity(intent);
            }
        });


        /** 리스트 달력 **/
        LayoutInflater inflater = getLayoutInflater();   //Layout XML 디자인 모습을 자바의 View 형태로 변환하는 것

        inflater.inflate(R.layout.list_view, null);

        ListView listView = findViewById(R.id.main_list_view);
        TextView mainListMon = findViewById(R.id.main_list_mon);


        mCal = Calendar.getInstance();
//        mCal.set(Calendar.DATE, 3);
        int ListDate = mCal.get(Calendar.DATE);
        int ListDateArray[] = {ListDate, ListDate - 1, ListDate - 2, ListDate - 3, ListDate - 4, ListDate - 5, ListDate - 6, ListDate - 7};

        //System.out.println(ListDateArray[0]);

        String dayLatest;
        String yearMonth = curYearFormat.format(date) + "/" + curMonthFormat.format(date);

        mainListMon.setText(yearMonth);
        //String YY = curYearFormat.format(date);
        //String MM = curMonthFormat.format(date);

        ArrayList<AbookListVO> data = new ArrayList<>();

        mHelper = new MyDBHelper(this);
        Map<String, Map<String, Object>> list2 = mHelper.getData();
        //Log.e("data", list2.toString());


        /** 총액 조회**/

        //String sum = String.valueOf(cursor.getInt(0));
        //System.out.println("resSum : " + sum);

        for (int i = 0; i < 7; i++) {
            try {
                int day = ListDateArray[i];
                //Log.e("data", "day " + day);
                if (day <= 0) {
                    break;
                }
                //Log.e("data", "day2 " + day);
                dayLatest = String.valueOf(ListDateArray[i]);
                String dayWeek = getDateDay(dayLatest, "dd");

                String year = mCal.get(Calendar.YEAR) + "";
                String month = (mCal.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCal.get(Calendar.MONTH) + 1) : (mCal.get(Calendar.MONTH) + 1) + "";
                String ymd = year + "/" + month + "/" + (dayLatest.length() < 2 ? "0" + dayLatest : dayLatest);

                //Log.e("data", "week " + dayWeek);
                //Log.e("data", "ymd " + ymd);
                int price = 0;
                if (list2.get(ymd) != null) {
                    price = Integer.parseInt((String) list2.get(ymd).get("sum"));
                    //Log.e("data", "price " + price);
                }
                AbookListVO vo = new AbookListVO(dayLatest, price + "", dayWeek, YY, MM);
                data.add(vo);
                //Log.e("data", "vo " + vo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Log.e("data", "list000 " + data.toString());
        CustomAdapter adapter = new CustomAdapter(MainActivity.this, R.layout.list_view, data);
        listView.setAdapter(adapter);

        /** 다이어리 **/

        myDBHelper2 = new MyDBHelper2(getApplicationContext());

        d_listview = (ListView) findViewById(R.id.diary_listView);
        d_adapter = new DiaryAdapter(this, R.layout.diary_item);
        d_listview.setAdapter(d_adapter);

        Button add = (Button) findViewById(R.id.diary_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = (EditText) findViewById(R.id.title);
                EditText content = (EditText) findViewById(R.id.content);
                myDBHelper2.insert(title.getText().toString(), content.getText().toString());
                showData();
            }
        });
        showData();
    }

    private void showData() {
        d_adapter.setList(myDBHelper2.select());
        d_adapter.notifyDataSetChanged();
    }




    /**
     * 해당 월에 표시할 일 수 구함
     */

    private void setCalendarDate(int month) {

        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

            dayList.add("" + (i + 1));

        }


    }

    /**
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     *
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public String getDateDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);


        switch (dayNum) {
            case 1:
                day = "(일요일)";
                break;
            case 2:
                day = "(월요일)";
                break;
            case 3:
                day = "(화요일)";
                break;
            case 4:
                day = "(수요일)";
                break;
            case 5:
                day = "(목요일)";
                break;
            case 6:
                day = "(금요일)";
                break;
            case 7:
                day = "(토요일)";
                break;

        }

        return day;
    }

    static public String getToday_date() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/M/d", Locale.KOREA);
        Date currentTime = new Date();
        String Today_day = mSimpleDateFormat.format(currentTime).toString();
        return Today_day;
    }

}
