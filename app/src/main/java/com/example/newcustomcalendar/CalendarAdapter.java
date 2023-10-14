package com.example.newcustomcalendar;

import android.graphics.Color;
import android.icu.util.LocaleData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter <CalendarAdapter.CalendarViewHolder>{//CalendarAdapter.CalendarViewHolder를 참조
    //CalendarAdapter는 RecyclerView.Adapter를 상속받는다.
    ArrayList<Date> dayList; //선언


    //OnItemListener onItemListener; //선언

    public CalendarAdapter(ArrayList<Date> dayList){
        this.dayList = dayList;  //MainActivity 에 있는 달력 리스트를 어뎁터 리스트에 적용
        //this.onItemListener = onItemListener; // MainActivity.this를 onItemListener에 넣음
    }

    @NonNull //null을 허용하지 않을 경우                    @Nullable = null을 허용할 경우
    @Override //부모 클래스로부터 오버라이딩 됐는지 확인
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){ //ViewHolder를 새로 만들어야 할때 호출되는 메소드
        //View Group은  하위에 여러 VIewGroup, View들을 포함하고 ViewGroup, View 객체들의 위치를 결정한다.

        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); //XML구현화 준비


        View view = inflater.inflate(R.layout.calendar_cell,parent, false);
        //view에 parent라는 ViewGroup를 삽입
        //1.R.layout.calendar_cell파일 경로 2. parent객체화한 뷰를 넣을 부모 레이아웃컨테이너 3. 바로인플레이션 하고자하는지

        return new CalendarViewHolder(view); //return CalendarViewHolder메소드에 view를 리턴
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position){ //ViewHolder를 어떠한 데이터와 연결할 때 호출되는 메소드
        //이를 통해 뷰 홀더 객체들의 데이터를 채우게 된다.


        Date monthDate = dayList.get(position); //position == ItemCount수와 이거 ㅈㄴ 이해 않감
        //dayList.배열에 position을 넣는다.

        //System.out.println("position"+position+"date"+dayList.get(position));

        Calendar dateCalendar = Calendar.getInstance(); //객체 생성

        dateCalendar.setTime(monthDate); //날짜 값을 dateCalendar로 옮김

        //현재 년 월
        int currentDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = CalendarUtil.selectedDate.get(Calendar.MONTH)+1;
        int currentYear = CalendarUtil.selectedDate.get(Calendar.YEAR);

        //넘어온 데이터
        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        if((position +1) % 7 == 0){ //토요일 파랑
            holder.dayText.setTextColor(Color.parseColor("#400000FF"));
        }else if(position == 0 || position % 7 ==0){ //일요일 빨강
            holder.dayText.setTextColor(Color.parseColor("#40FF0000"));
        }else {
            holder.dayText.setTextColor(Color.parseColor("#40000000"));
        }

        //비교해서 년, 월 같으면 진한색 아니면 연한색으로 변경
        if(displayMonth == currentMonth && displayYear == currentYear){

            if((position +1) % 7 == 0){ //토요일 파랑
                holder.dayText.setTextColor(Color.parseColor("#FF0000FF"));
            }else if(position == 0 || position % 7 ==0){ //일요일 빨강
                holder.dayText.setTextColor(Color.parseColor("#FFFF0000"));
            }else {
                holder.dayText.setTextColor(Color.parseColor("#FF000000"));
            }
            holder.parentView.setBackgroundColor(Color.parseColor("#D5D5D5"));

            if(displayDay == currentDay)
                holder.itemView.setBackgroundColor(Color.parseColor("#AAFBC9"));
        }
        else{
            holder.parentView.setBackgroundColor(Color.parseColor("#F6F6F6"));
            //holder.dayText.setTextColor(Color.LTGRAY);
        }

        //날짜 변수에 담기
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);// dateCalendar에 요일값을 dayNo에 입력

        holder.dayText.setText(String.valueOf(dayNo));// dayNo를 문자열로 바꿔서 TextView에 적용시킴

        /*if(day == null){
            holder.dayText.setText("");
        }else {
            //해당 일자를 넣는다.
            holder.dayText.setText(String.valueOf(day.getDayOfMonth())); //CalendarViewHolder안에 있는 dayText(TextView)에 day를 적용
            //onBindViewHolder가 데이터를 배열에 순서대로 채우는 기능이니깐 holder는 배열이구나 position
            //아니면 넣을때마다 계속 사용되는건가
            //getDayOfMonth  Day Of Month 월중 일 값

            if(day.equals(CalendarUtil.selectedDate)){ //만약 day와 selectedDate가 같으면
                holder.parentVeiw.setBackgroundColor(Color.LTGRAY); //뒷배경을 그레이로
            }
        }*/

        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                /*int iYear = day.getYear();// 년
                int iMonth =day.getMonthValue(); //월
                int iDay = day.getDayOfMonth(); //일

                String yearMonDay = iYear + "년" + iMonth + "월" + iDay +"일";

                Toast.makeText(holder.itemView.getContext(), yearMonDay,Toast.LENGTH_SHORT).show();
                //메세지*/
            }
        });
    }

    @Override
    public int getItemCount(){
        return dayList.size();
    } //가장먼저 실행되는 함수
    //받아온 날짜 배열 사이즈를 리턴함


    class CalendarViewHolder extends RecyclerView.ViewHolder{ //RecyclerView.ViewHolder상속

        //초기화
        TextView dayText;

        View parentView;

        public CalendarViewHolder(@NonNull View itemView){
            super(itemView); //부모 변수에 접근

            dayText = itemView.findViewById(R.id.dayText);
            //TextView에 물려받은 ViewGroup(calendar cell)에서 R.id.dayText를 넣는다.

            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}