package com.example.project;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    MyDBHelper mHelper;


    /** dataListView() 변수 저장 **/
    int ListDateArray[];
    String dayLatest;
    String YY;
    String MM;
    ListView listView;
    int price = 0;              // price 초기화

    NotificationManager notificationManager;
    PendingIntent intent;
    public final String PREFERENCE = "com.studio572.samplesharepreference";

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

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);


        /************************ 달력표(캘린더) ************************/

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));
        //gridview 요일 표시
        dayList = new ArrayList<>();
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


        /** 달력 일자 어뎁터 이용하여 getView로 전송 **/
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //setContentView(R.layout.detail);
                String s_date = curYearFormat.format(date) + "/" + curMonthFormat.format(date) + "/" + String.valueOf(position - 9) ;


                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra("date", s_date);
                startActivity(intent);
            }
        });


        /************************ 리스트 달력 ************************/
        LayoutInflater inflater = getLayoutInflater();   //Layout XML 디자인 모습을 자바의 View 형태로 변환하는 것

        inflater.inflate(R.layout.list_view, null);

        listView = findViewById(R.id.main_list_view);
        TextView mainListMon = findViewById(R.id.main_list_mon);


        mCal = Calendar.getInstance();
        //mCal.set(Calendar.DATE, 3);
        int ListDate = mCal.get(Calendar.DATE);
        ListDateArray = new int[] { ListDate, ListDate-1, ListDate-2, ListDate-3, ListDate-4, ListDate-5, ListDate-6, ListDate-7 };

        String yearMonth = curYearFormat.format(date) + "/" + curMonthFormat.format(date);

        mainListMon.setText(yearMonth);
        YY = curYearFormat.format(date);
        MM = curMonthFormat.format(date);
        mHelper = new MyDBHelper(this);

        dataListView();
    }

    /** 합산가격 DB에서 데이터 재조회 **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dataListView();
    }

    /** 리스트 데이터 어뎁터를 통해 화면에서 보여주기 **/
    public void dataListView() {
        ArrayList<AbookListVO> data = new ArrayList<>();

        Map<String, Map<String, Object>> list2 = mHelper.getData();
        //Log.e("data", list2.toString());

        for (int i=0; i<7; i++) {
            try {
                int day = ListDateArray[i];
                Log.e("data", "day " + day);
                if(day <= 0) {
                    break;
                }
                Log.e("data", "day2 " + day);
                dayLatest = String.valueOf(ListDateArray[i]);
                String dayWeek = getDateDay(dayLatest, "dd");

                String year = mCal.get(Calendar.YEAR) + "";
                String month = (mCal.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCal.get(Calendar.MONTH) + 1) : (mCal.get(Calendar.MONTH) + 1) + "";
                String ymd = year + "/" + month + "/" + (dayLatest.length() < 2 ? "0" + dayLatest : dayLatest);

                //Log.e("data", "week " + dayWeek);
                //Log.e("data", "ymd " + ymd);
                //int price = 0;
                if(list2.get(ymd) != null) {
                    price = Integer.parseInt((String) list2.get(ymd).get("sum"));
                    Log.e("data", "price " + price);
                }
                AbookListVO vo = new AbookListVO(dayLatest, price + "", dayWeek, YY, MM);
                data.add(vo);
                //Log.e("data", "vo " + vo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e("data", "list000 " + data.toString());
        CustomAdapter adapter = new CustomAdapter(MainActivity.this, R.layout.list_view, data);
        listView.setAdapter(adapter);


        /********** 환경설정 버튼 **********/
        final ImageView setButton = findViewById(R.id.setting);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        /********** 알림 설정 **********/
        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
        if(pref.equals("")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("limitPay", "0");

            // 메모리에 있는 데이터를 저장장치에 저장한다.
            editor.commit();
            String result = pref.getString("limitPay", "");
            Log.e("data111", "MainRes " + result);
        }

        String year = mCal.get(Calendar.YEAR) + "";
        String month = (mCal.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCal.get(Calendar.MONTH) + 1) : (mCal.get(Calendar.MONTH) + 1) + "";
        String ymd = year + "/" + month + "/" + (mCal.get(Calendar.DATE) < 2 ? "0" + mCal.get(Calendar.DATE) : mCal.get(Calendar.DATE));

        Log.e("data111", "data " + list2.toString());
        Log.e("data111", "ymd " + ymd);

        if(list2.get(ymd) == null) {
            price = 0;
        }
        else {
            int saveMoney = 0;
            if(pref.getString("limitPay", "") != "") {
                saveMoney = Integer.parseInt(pref.getString("limitPay", ""));
            }

            double limitMoney = (saveMoney * 0.9);

            price = Integer.parseInt((String) list2.get(ymd).get("sum"));
            Log.e("data111", "price " + price);

            if(price >= limitMoney && limitMoney > 0) {
                intent = PendingIntent.getActivity(MainActivity.this, 0,
                        new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder builder = new Notification.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.ic_launcher_background) // 아이콘 설정하지 않으면 오류남
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle("알림") // 제목 설정
                        .setContentText("하루 제한금액을 초과하거나 90%이상 소비하였습니다.") // 내용 설정
                        .setTicker("한줄 출력") // 상태바에 표시될 한줄 출력
                        .setAutoCancel(true)
                        .setContentIntent(intent);

                notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());
            }
        }
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
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public String getDateDay(String date, String dateType) throws Exception {

        String day = "" ;
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
        switch(dayNum){
            case 1:
                day = "(일요일)";
                break ;
            case 2:
                day = "(월요일)";
                break ;
            case 3:
                day = "(화요일)";
                break ;
            case 4:
                day = "(수요일)";
                break ;
            case 5:
                day = "(목요일)";
                break ;
            case 6:
                day = "(금요일)";
                break ;
            case 7:
                day = "(토요일)";
                break ;
        }

        return day ;
    }

}
