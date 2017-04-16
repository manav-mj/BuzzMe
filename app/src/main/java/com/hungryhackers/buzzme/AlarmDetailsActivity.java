package com.hungryhackers.buzzme;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmDetailsActivity extends AppCompatActivity {

    EditText title, date, time, desc;

    int mYear, mMonth, mDay, mHour, mMinute;

    Reminder reminder;

    boolean isChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);

        Intent i = getIntent();
        reminder = new Select().from(Reminder.class).where("id = ?",i.getLongExtra("ID", -1)).executeSingle();

        title = (EditText) findViewById(R.id.alarm_title);
        date = (EditText) findViewById(R.id.alarm_date);
        time = (EditText) findViewById(R.id.alarm_time);
        desc = (EditText) findViewById(R.id.alarm_desc);

        title.setText(reminder.title);

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String dateString = dateFormatter.format(reminder.calendar.getTime());
        date.setText(dateString);

        String am_pm = "AM";
        int hour = reminder.calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 12) {
            hour = hour - 12;
            am_pm = "PM";
        }

        time.setText(hour + " : " + reminder.calendar.get(Calendar.MINUTE) + " " + am_pm);

        desc.setText(reminder.description);

        mYear = reminder.calendar.get(Calendar.YEAR);
        mMonth = reminder.calendar.get(Calendar.MONTH);
        mDay = reminder.calendar.get(Calendar.DAY_OF_MONTH);
        mHour = reminder.calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = reminder.calendar.get(Calendar.MINUTE);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    date.callOnClick();
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChanged = true;
                date.setShowSoftInputOnFocus(false);
                final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                Calendar newCalendar = Calendar.getInstance();
                mYear = newCalendar.get(Calendar.YEAR);
                mMonth = newCalendar.get(Calendar.MONTH);
                mDay = newCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        String dateString = dateFormatter.format(newDate.getTime());
                        date.setText(dateString);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChanged = true;
                time.setShowSoftInputOnFocus(false);
                Calendar newCalendar = Calendar.getInstance();
                mHour = newCalendar.get(Calendar.HOUR_OF_DAY);
                mMinute = newCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am_pm = " AM";
                        int currentHour = hourOfDay;
                        if (currentHour > 12) {
                            currentHour = currentHour - 12;
                            am_pm = " PM";
                        }
                        mHour = hourOfDay;
                        mMinute = minute;
                        String timeString = currentHour + ":" + minute + am_pm;
                        time.setText(timeString);
                    }
                }, mHour, mMinute, false);

                timePickerDialog.show();
            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    time.callOnClick();
                }
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        title.addTextChangedListener(textWatcher);
        desc.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.alarm_save){

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(this, AlarmReceiver.class);
            i.putExtra("ID", reminder.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,reminder.getId().intValue(),i, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);

            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDay, mHour, mMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            reminder.set(title.getText().toString(), calendar, desc.getText().toString());
            reminder.save();
            i.putExtra("ID", reminder.getId());
            pendingIntent = PendingIntent.getBroadcast(this, reminder.getId().intValue(), i, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Reminder Saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Quit without saving?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }else {
            finish();
        }
    }
}
