package com.hungryhackers.buzzme;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;

/**
 * Created by YourFather on 22-03-2017.
 */
@Table(name = "Reminders")
public class Reminder extends Model {
    @Column(name = "title")
    public String title;

    @Column(name = "calendar")
    public Calendar calendar;

    @Column(name = "description")
    public String description;


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Column(name = "completed")
    public boolean completed;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean selected;

    public Reminder(){
        super();
    }

    public Reminder(String title, Calendar calendar, String description) {
        super();
        this.title = title;
        this.calendar = calendar;
        this.description = description;
    }

    public void set(String title, Calendar calendar, String description){
        this.title = title;
        this.calendar = calendar;
        this.description = description;
    }
}
