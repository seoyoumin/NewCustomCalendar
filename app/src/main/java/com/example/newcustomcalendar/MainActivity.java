package com.example.newcustomcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;

public class MainActivity extends AppCompatActivity { //implements 검색

    TextView monthYearText; //년월 텍스트뷰

    //LocalDate selectedDate; //년월 변수
    //LocalDate는 날짜를 표현하는데 사용
    //LocalTime은 시간을 표현하는데 사용

    RecyclerView recyclerView; //리싸이클 뷰 선언
    RecyclerView inputRecyclerView;

    InputAdapter propertyAdapter;
    InputAdapter sortAdapter;
    InputAdapter numberAdapter;



    TextView dateText;
    TextView property;
    TextView sort;
    TextView money;
    EditText content;

    Button save;
    Button conti;

    InputMethodManager imm;


    Calendar calendar; //선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //초기화
        monthYearText = findViewById(R.id.MonthYearText);
        ImageButton prevBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView = findViewById(R.id.recyclerView);

        //main View
        View mainview = findViewById(R.id.activity_custom_calendar);

        inputRecyclerView = findViewById(R.id.inputRecyclerView);

        //TextView dateText = findViewById(R.id.date);
        //TextView property = findViewById(R.id.property);
        //TextView sort = findViewById(R.id.sort);
        //TextView money = findViewById(R.id.money);
        //EditText content= findViewById(R.id.content);

        //Button save = findViewById(R.id.save);
        //Button conti = findViewById(R.id.conti);


        //현재 날짜
        CalendarUtil.selectedDate = Calendar.getInstance(); //CalendarUtil에 있는 값에 캘린더 객채 생성함  자동으로 오늘날짜 입력됨.

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //화면 설정
        setMonthView();

        //이전달 버튼이벤트
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //현재 월-1 변수에 담기
                CalendarUtil.selectedDate.add(Calendar.MONTH, -1); //minusMonths는 월단위로 뺄수 있다.
                setMonthView();

            }
        });

        //다음달 버튼 이벤트
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //현재 월+1 변수에 담기
                CalendarUtil.selectedDate.add(Calendar.MONTH, 1); //plusMonths는 월단위로 더할수 있다.
                setMonthView();
            }
        });

        //dateText.setHint(dateTime(CalendarUtil.selectedDate));
        initializeView();
        recordMenu();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }//onCreate


    private String yearMonthFromDate(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String yearMonth = year + "년" + month + "월";


        return yearMonth;
    }

    private String dateTime(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR);
        int minuth = calendar.get(Calendar.MINUTE);

        String dateTimeMinuth = year + "." + month + "." + day + "  " + hour + ":" + minuth;

        return dateTimeMinuth;
    }


    private void setMonthView() { //화면 설정

        //년월 텍스트뷰 셋팅
        monthYearText.setText(yearMonthFromDate(CalendarUtil.selectedDate)); //날짜 표기를 로컬 데이터를 받아서 날짜를 나열후 텍스트에 붙여넣음

        ArrayList<Date> dayList = daysInMonthArray();//요일에 맞는 일자

        CalendarAdapter adapter = new CalendarAdapter(dayList); //요일에 맞춘 리스트를 CalendarAdapter리스트에 넣고 CalendarAdapter 클래스 객체를 만듬


        //레이아웃 설정( 열 7개)
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);


        recyclerView.setLayoutManager(manager);//레이아웃 적용

        recyclerView.setAdapter(adapter);//어뎁터 적용
    }

    //날짜 생성
    private ArrayList<Date> daysInMonthArray() {

        ArrayList<Date> dayList = new ArrayList<>(); //객체 생성

        //YearMonth yearMonth = YearMonth.from(date); //년월 만 나타내는 클래스에 LocalDate를 입력함
        //from 해당 클래스 인스턴스로 형변환할때 사용

        //날짜 복사해서 변수 생성
        Calendar monthCalendar = (Calendar) CalendarUtil.selectedDate.clone();

        //1일로 셋팅(4월1일)
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //요일 가져와서 -1 일요일:1 월요일:2
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        //날짜 셋팅(-5일전)
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);// 1일 -현재요일.int            1  -5 금요일 = -4

        //42전까지 반복
        while (dayList.size() < 42) {

            //리스트에 날짜 등록
            dayList.add(monthCalendar.getTime()); //getTime = 시간을 숫자로 반환함.

            //1일씩 늘린 날짜로 변경 1->2일 -> 3일
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1); // 1씩 더함.
            //monthCalendar를 1더하고  적용하고 while문 으로 42번을 1번씩 더함과 적용을 반복함.
        }
        return dayList; //리스트 리턴
    }

    public void initializeView(){
        dateText = findViewById(R.id.date);
        property = findViewById(R.id.property);
        sort = findViewById(R.id.sort);
        money = findViewById(R.id.money);
        content= findViewById(R.id.content);

        save = findViewById(R.id.save);
        conti = findViewById(R.id.conti);

    }

    public void recordMenu(){


        property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputRecyclerView.setAdapter(propertyAdapter);
                inputRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputRecyclerView.setAdapter(sortAdapter);

            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputRecyclerView.setAdapter(numberAdapter);
            }
        });

        ArrayList<String>propertyInputBox = new ArrayList<String>
                (Arrays.asList("현금","카드"));

        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 4);

        inputRecyclerView.setLayoutManager(manager);

        propertyAdapter = new InputAdapter(propertyInputBox);

        propertyAdapter.setOnItemClickListener(new InputAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int pos) {
                property.setText(propertyInputBox.get(pos));
                inputRecyclerView.setAdapter(sortAdapter);
            }
        });

        ArrayList<String>sortInputBox = new ArrayList<String>
                (Arrays.asList("식비","교통/차량","문화생활","마트/편의점","패션/미용","생활용품","주거/통신","건강","교육","경조사/회비","부모님","기타"));

        sortAdapter = new InputAdapter(sortInputBox);

        sortAdapter.setOnItemClickListener(new InputAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int pos) {
                sort.setText(sortInputBox.get(pos));
                inputRecyclerView.setAdapter(numberAdapter);
            }
        });

        ArrayList<String>numberInputBox= new ArrayList<String>
                (Arrays.asList("1","2","3","마이너스","4","5","6","-","7","8","9","계산기","","0","","완료"));

        numberAdapter = new InputAdapter(numberInputBox);

        numberAdapter.setOnItemClickListener(new InputAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if(numberInputBox.get(pos) == "마이너스"){
                    int size = money.getText().length();

                    if(size != 0)
                        money.setText(money.getText().toString().substring(0, size -1));
                }else if(numberInputBox.get(pos) == "계산기"){

                }else if(numberInputBox.get(pos)== "완료"){ // 앱에서는 엔터가 아니라 완료였음.
                    content.requestFocus();
                    imm.showSoftInput(content,0);
                    inputRecyclerView.setVisibility(View.INVISIBLE);
                }else if(numberInputBox.get(pos) == "0"){
                    money.append("0");
                }else if(numberInputBox.get(pos) == "1"){
                    money.append("1");
                }else if(numberInputBox.get(pos) == "2"){
                    money.append("2");
                }else if(numberInputBox.get(pos) == "3"){
                    money.append("3");
                }else if(numberInputBox.get(pos) == "4"){
                    money.append("4");
                }else if(numberInputBox.get(pos) == "5"){
                    money.append("5");
                }else if(numberInputBox.get(pos) == "6"){
                    money.append("6");
                }else if(numberInputBox.get(pos) == "7"){
                    money.append("7");
                }else if(numberInputBox.get(pos) == "8"){
                    money.append("8");
                }else if(numberInputBox.get(pos) == "9"){
                    money.append("9");
                }
            }
        });

    }




}//MainActivity
