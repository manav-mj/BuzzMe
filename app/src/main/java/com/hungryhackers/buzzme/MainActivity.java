package com.hungryhackers.buzzme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.hungryhackers.buzzme.fragments.ToolbarDateFragment;
import com.hungryhackers.buzzme.fragments.ToolbarDescFragment;
import com.hungryhackers.buzzme.fragments.ToolbarTimeFragment;
import com.hungryhackers.buzzme.fragments.ToolbarTitleFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView nextArrow, prevArrow;

    private final int NUM_ITEMS = 4;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;

    FloatingToolbar floatingToolbar;

    String title, description;
    Calendar alarmDateTime;

    ArrayList<Reminder> reminders;
    ArrayList<String> reminderTitles;

    ListView reminderListView;
    ArrayAdapter adapter;

    int currentPosition = 0;

    ToolbarTitleFragment titleFragment;
    ToolbarDateFragment dateFragment;
    ToolbarDescFragment descFragment;
    ToolbarTimeFragment timeFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        reminders = new ArrayList<>();
        reminderTitles = new ArrayList<>();

        reminderListView = (ListView) findViewById(R.id.reminder_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderTitles);
        reminderListView.setAdapter(adapter);


        titleFragment = new ToolbarTitleFragment();
        dateFragment = new ToolbarDateFragment();
        timeFragment = new ToolbarTimeFragment();
        descFragment = new ToolbarDescFragment();


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        floatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
        floatingToolbar.attachFab(fab);

        nextArrow = (ImageView) findViewById(R.id.next_arrow);
        prevArrow = (ImageView) findViewById(R.id.prev_arrow);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (position == 0){
                    prevArrow.setImageResource(R.drawable.ic_close_white_18dp);
                }else if (position == NUM_ITEMS - 1){
                    nextArrow.setImageResource(R.drawable.ic_check_white_18dp);
                }else {
                    nextArrow.setImageResource(R.drawable.ic_chevron_right_white_18dp);
                    prevArrow.setImageResource(R.drawable.ic_chevron_left_white_18dp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        title = "";
        description = "";
        alarmDateTime = Calendar.getInstance();
        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, AlarmDetailsActivity.class);
                i.putExtra("ID", reminders.get(position).getId());
                startActivity(i);
            }
        });
        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final int p = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setItems(new CharSequence[]{"View", "Delete"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent intent = new Intent(MainActivity.this, AlarmDetailsActivity.class);
                                        intent.putExtra("ID", reminders.get(position).getId());
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        Log.d("manav", "onClick: 2");
                                        Reminder reminder = reminders.get(p);
                                        //cancel alarm
                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                        Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
                                        i.putExtra("ID", reminder.getId());
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,reminder.getId().intValue(),i, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.cancel(pendingIntent);

                                        //delete entry from database and list
                                        reminder.delete();
                                        reminders.remove(p);
                                        reminderTitles.remove(p);
                                        adapter.notifyDataSetChanged();
                                        break;
                                }
                            }
                        });

                builder.create().show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        setUpViews();
        floatingToolbar.hide();
        super.onResume();
    }

    private void setUpViews() {
        reminders.clear();
        reminderTitles.clear();
        List<Reminder> reminderList = new Select().from(Reminder.class).execute();
        reminders.addAll(reminderList);
        for (Reminder r :
                reminders) {
            reminderTitles.add(r.title);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (floatingToolbar.isShowing()){
            floatingToolbar.hide();
            clearInput();
        }else {
            super.onBackPressed();
        }
    }

    private  void clearInput(){
        title = "";
        alarmDateTime = Calendar.getInstance();
        description = "";
        viewPager.setCurrentItem(0);
        titleFragment.setTitle(title);
        dateFragment.setDate("Select Date");
        timeFragment.setTime("Select Time");
        descFragment.setDescription(description);
        nextArrow.setImageResource(R.drawable.ic_chevron_right_white_18dp);
    }

    public void prevFragment(View v){
        int current = viewPager.getCurrentItem() - 1;
        if (current >= 0){
            viewPager.setCurrentItem(current);
        }else {
            floatingToolbar.hide();
            clearInput();
        }
    }

    public void nextFragment (View v){
        int current = viewPager.getCurrentItem() + 1;
        if (current < NUM_ITEMS){
            viewPager.setCurrentItem(current);
        }else {
            getDetailsFromFragments();
            if (title.isEmpty()){
                viewPager.setCurrentItem(0);
                nextArrow.setImageResource(R.drawable.ic_chevron_right_white_18dp);
                Toast.makeText(this, "Please fill the title", Toast.LENGTH_SHORT).show();
                return;
            }
            submitReminder();
        }
    }

    private void submitReminder() {
        Reminder reminder = new Reminder(title, alarmDateTime, description);
        reminder.save();
        reminders.add(reminder);
        reminderTitles.add(title);
        adapter.notifyDataSetChanged();
        setAlarm(reminder.getId());
        Log.i("manav", title + ":" + alarmDateTime.getTimeInMillis());
        clearInput();
        floatingToolbar.hide();
    }

    private void setAlarm(Long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("ID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id.intValue(),i, 0);
        alarmManager.set(AlarmManager.RTC, alarmDateTime.getTimeInMillis(), pendingIntent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter{


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return titleFragment;
                case 1:
                    return dateFragment;
                case 2:
                    return timeFragment;
                case 3:
                    return descFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    private void getDetailsFromFragments(){
        title = titleFragment.getTitle();

        Calendar date = dateFragment.getDate();
        Calendar time = timeFragment.getTime();
        alarmDateTime.set(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DATE),
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), 0);
        alarmDateTime.set(Calendar.MILLISECOND, 0);

        description = descFragment.getDescription();
    }
}