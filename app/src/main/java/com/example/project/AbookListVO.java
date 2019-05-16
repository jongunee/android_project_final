package com.example.project;

/**
 * 리스트형 달력 데이터
 **/
public class AbookListVO {
    String listDay;
    String listMoney;
    String listDayWeek;
    String listYear;
    String listMonth;

    AbookListVO(String listDay, String listMoney, String listDayWeek, String listYear, String listMonth) {
        this.listDay = listDay;
        this.listMoney = listMoney;
        this.listDayWeek = listDayWeek;
        this.listYear = listYear;
        this.listMonth = listMonth;
    }

    @Override
    public String toString() {
        return "AbookListVO{" +
                "listDay='" + listDay + '\'' +
                ", listMoney='" + listMoney + '\'' +
                ", listDayWeek='" + listDayWeek + '\'' +
                ", listYear='" + listYear + '\'' +
                ", listMonth='" + listMonth + '\'' +
                '}';
    }
}