package com.hungryhackers.buzzme.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hungryhackers.buzzme.MainActivity;
import com.hungryhackers.buzzme.R;

import java.util.Calendar;

/**
 * Created by YourFather on 26-03-2017.
 */

public class ToolbarTimeFragment extends android.support.v4.app.Fragment {
    int mHour, mMinute;
    String time;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fab_toolbar_time, container, false);
        time = "";
        textView = (TextView) v.findViewById(R.id.toolbar_time);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordTime(v);
            }
        });
        return v;
    }

    public void recordTime(View v){
        Calendar newCalendar = Calendar.getInstance();
        mHour = newCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = newCalendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = " AM";
                int currentHour = hourOfDay;
                if (currentHour > 12){
                    currentHour = currentHour - 12;
                    am_pm = " PM";
                }
                mHour = hourOfDay;
                mMinute = minute;
                time = Integer.toString(currentHour) + ":" + Integer.toString(minute) + am_pm;
                textView.setText(time);
            }
        }, mHour, mMinute,false);

        timePickerDialog.show();
    }

    public Calendar getTime(){
        if (textView!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, mHour);
            calendar.set(Calendar.MINUTE, mMinute);
            return calendar;
        }
        return null;
    }
    public void setTime(String s){
        if (textView!=null){
            textView.setText(s);
            time = s;
        }
    }
}
