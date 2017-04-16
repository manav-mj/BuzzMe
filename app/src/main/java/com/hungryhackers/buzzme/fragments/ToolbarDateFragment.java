package com.hungryhackers.buzzme.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.hungryhackers.buzzme.MainActivity;
import com.hungryhackers.buzzme.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YourFather on 26-03-2017.
 */

public class ToolbarDateFragment extends android.support.v4.app.Fragment {
    int mYear, mMonth, mDay;
    String date;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fab_toolbar_date, container, false);
        date = "";
        textView = (TextView) v.findViewById(R.id.toolbar_date);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDate(v);
            }
        });
        return v;
    }

    public void recordDate(final View v){
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        mYear = newCalendar.get(Calendar.YEAR);
        mMonth = newCalendar.get(Calendar.MONTH);
        mDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                date = dateFormatter.format(newDate.getTime());
                textView.setText(date);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public Calendar getDate(){
        if (textView!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDay);
            return calendar;
        }
        return null;
    }
    public void setDate(String s){
        if (textView!=null){
            textView.setText(s);
            date = s;
        }
    }
}
