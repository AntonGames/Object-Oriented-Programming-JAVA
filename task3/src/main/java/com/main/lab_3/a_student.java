package com.main.lab_3;

import java.util.Arrays;

public class a_student {
    int index;
    String name;
    String year;
    int[] attendance;
    int attendance_i=0;

    public String name2;
    public String surname;
    public String attendance_string;

    public a_student(String name, String year, int index){
        this.name = name;
        this.year = year;
        this.index = index;
        this.attendance = new int[31];
        attendance_string = Arrays.toString(attendance);
        attendance_string = attendance_string.replace("[", "");
        attendance_string = attendance_string.replace("]", "");
        attendance_string = attendance_string.replace(",", " ");
    }

    public void fill_attendance(){
        StringBuilder day = new StringBuilder();
        attendance = new int[31];
        attendance_i = 0;
        for (int i=0; i<attendance_string.length(); i++){
            if (attendance_string.charAt(i) != ' ') {
                day.append(attendance_string.charAt(i));
            } else{
                attendance[attendance_i] = Integer.parseInt(String.valueOf(day));
                day.setLength(0);
                attendance_i++;
            }
        }
    }

    public int getIndex() { return index; }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getName2() { return name2; }

    public String getSurname() { return surname; }

    public String getAttendance_string() { return attendance_string; }
}
