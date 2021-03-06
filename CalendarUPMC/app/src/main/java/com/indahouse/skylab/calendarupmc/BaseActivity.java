package com.indahouse.skylab.calendarupmc;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.indahouse.skylab.calendarupmc.classes.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener{

    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Set up the controller
        controller = new Controller(this);
        controller.initCalendar();
        controller.initSettings();
        controller.initButtons();
        controller.initLayout();
        controller.getCachedCalendars();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        controller.setupDateTimeInterpreter(id == R.id.action_week_view);

        //Call action settings
        if (id == R.id.action_settings) {
            controller.actionSettings();
            return true;
        }
        //Control the calendar appearance
        switch (id){
            case R.id.action_today:
            controller.actionToday();
            return true;

            case R.id.action_day_view:
            controller.actionDayView(item);
            return true;

            case R.id.action_three_day_view:
            controller.actionThreeDayView(item);
            return true;

            case R.id.action_week_view:
            controller.actionWeekView(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView list_ue = (ListView) findViewById(R.id.list_ue);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if(list_ue.getVisibility() == View.VISIBLE){
            controller.toggleView();
        }

        else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        String sHour = String.valueOf(event.getStartTime().get(Calendar.HOUR_OF_DAY));
        String sMinute = String.valueOf(event.getStartTime().get(Calendar.MINUTE));
        String eHour = String.valueOf(event.getEndTime().get(Calendar.HOUR_OF_DAY));
        String eMinute = String.valueOf(event.getEndTime().get(Calendar.MINUTE));
        String location = event.getLocation();
        String name = event.getName();

        //Fix time
        if(sMinute.equals("0")){
            sMinute="00";
        }

        if(eMinute.equals("0")){
            eMinute="00";
        }

        //Display event infos in a modal
        controller.createDialog(name, sHour + ":" + sMinute + " - " + eHour + ":" + eMinute + " \n " + location).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "You found the secret function !" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "You found the secret function !" , Toast.LENGTH_SHORT).show();
    }

    //Useless listener
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        return events;
    }

}
