package com.foodie.home.util;

import android.util.Log;

import com.foodie.base.enums.VoucherCantUseType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static List<String> getDayList(){
        List<String> dayList = new ArrayList<>();
        dayList.add("今天");
        dayList.add("明天");
        dayList.add("后天");

        return  dayList;
    }
    public static List<String> getMinuteList(){
        List<String> minuteList = new ArrayList<>();
        minuteList.add("00");
        minuteList.add("15");
        minuteList.add("30");
        minuteList.add("45");
        return  minuteList;
    }
    public static List<String> getShowHourList(){
        List<String> hourList = new ArrayList<>();
        for(int i=0;i<24;i++){
            hourList.add(i+"点");
        }
        return  hourList;
    }
    public static List<String> getHourList(){
        List<String> hourList = new ArrayList<>();
        for(int i=0;i<10;i++){
            hourList.add("0"+i);
        }
        for(int i=0;i<10;i++){
            hourList.add("1"+i);
        }
        for(int i=0;i<4;i++){
            hourList.add("2"+i);
        }
        return  hourList;
    }


    public static String transferDate(String day){

        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        if(day.equals("今天")){
            return getDate();
        }else if(day.equals("明天")){
            return getFetureDate(1);
        }else{
            return getFetureDate(2);
        }
    }

    public static String getDate(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //从前端或者自己模拟一个日期格式，转为String即可
        String dateStr = format.format(now);
        return dateStr;
    }
    public static String getTime(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        //从前端或者自己模拟一个日期格式，转为String即可
        String dateStr = format.format(now);
        return dateStr;

    }

    public static boolean compareTime(String time){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //从前端或者自己模拟一个日期格式，转为String即可
        try {

     //       String dateStr = format.format(now);
            Date chooseTime = format.parse(time);
      //      Date nowTime = format.parse(dateStr);
            int compareTo = chooseTime.compareTo(now);

            if(compareTo<0){
                return false;
            }else{
                return true;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
    public static Integer ifExpire(String startDate,String deadLine){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = format.parse(startDate);
            Date dead = format.parse(deadLine);
            int compareTo = now.compareTo(start);
            int compareTo2 = now.compareTo(dead);
            if(compareTo<0 ){
                return VoucherCantUseType.NotTime.getIndex();
            }else if(compareTo2>0){
                return VoucherCantUseType.Expire.getIndex();

            }else{
                return VoucherCantUseType.CanUser.getIndex();

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
