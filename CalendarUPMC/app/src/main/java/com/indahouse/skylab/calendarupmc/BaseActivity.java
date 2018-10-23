package com.indahouse.skylab.calendarupmc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils.AsyncResponse;
import com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils.AsyncTaskGetEventsEntries;
import com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils.CheckboxAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.blurry.Blurry;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener,AsyncResponse {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private String url;
    private CheckboxAdapter checkboxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        this.url = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M2_STL");

        //Calendar initialization
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.goToHour(8);

        setupDateTimeInterpreter(false);

        //Settings list
        final ListView list_ue = (ListView) findViewById(R.id.list_ue);
        ArrayList<String> ues = new ArrayList<String>();
        String[] values = new String[] { "M1 STL", "M2 STL", "M1 DAC",
                "M2 DAC", "M1 ANDROIDE", "M2 ANDROIDE", "M1 BIM", "M2 BIM",
                "M1 IMA", "M2 IMA", "M1 RES", "M2 RES", "M1 SAR", "M2 SAR",
                "M1 SESI", "M2 SESI", "M1 SFPN", "M2 SFPN", "M1", "M2"};

        for (int i = 0; i < values.length; ++i) {
            ues.add(values[i]);
        }
        checkboxAdapter = new CheckboxAdapter(this,ues);

        checkboxAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                Toast.makeText(getBaseContext(), String.valueOf("data changed"), Toast.LENGTH_LONG);
            }
        });


        list_ue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUE = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),selectedUE, Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton buttonSaveSettings  = findViewById(R.id.buttonSaveSettings);
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    checkboxAdapter.printSharedPref();
                            }
        });

        // Refresh action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar progressBar = findViewById(R.id.progress);

                if(progressBar.getVisibility() == view.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                    Blurry.with(BaseActivity.this).radius(1)
                            .sampling(2)
                            .async()
                            .color(android.R.color.darker_gray)
                            .animate(500)
                            .onto((ViewGroup) findViewById(R.id.content));
                    new AsyncTaskGetEventsEntries(BaseActivity.this,BaseActivity.this, url).execute("");
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //General layout initialization
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
           NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
           navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);

        if (id == R.id.action_settings) {
            ListView list_ue = findViewById(R.id.list_ue);
            list_ue.setAdapter(checkboxAdapter);
            FloatingActionButton fab = findViewById(R.id.fab);
            FloatingActionButton buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
            TextView textView = findViewById(R.id.textView1);
            CheckBox checkBox = findViewById(R.id.checkBox1);
            WeekView weekView = findViewById(R.id.weekView);

            list_ue.setBackgroundColor(Color.WHITE);

            if(list_ue.getVisibility() == View.GONE){
                list_ue.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.VISIBLE);
                buttonSaveSettings.show();

                weekView.setVisibility(View.GONE);
                fab.hide();
            }
            else{
                list_ue.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                checkBox.setVisibility(View.GONE);
                buttonSaveSettings.hide();
                weekView.setVisibility(View.VISIBLE);
                fab.show();
            }
            return true;
        }
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                mWeekView.goToHour(8);
                return true;

            case R.id.action_day_view:

                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.goToHour(8);
                }

                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.goToHour(8);
                }
                mWeekView.goToHour(8);
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.goToHour(8);
                }
                mWeekView.goToHour(8);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d/M", Locale.getDefault());

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override public String interpretTime(int hour) {
                if(hour<10){
                    return "0"+hour;
                }
                else {
                    return String.valueOf(hour);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_camera) {
            Toast.makeText(this,"Camera",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"Gallery",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"Slideshow",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this,"Manage",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"Send",Toast.LENGTH_SHORT).show();
        }

        try {
            if(fragmentClass!=null) {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        String sHour = String.valueOf(event.getStartTime().get(Calendar.HOUR_OF_DAY));
        String sMinute = String.valueOf(event.getStartTime().get(Calendar.MINUTE));
        String eHour = String.valueOf(event.getEndTime().get(Calendar.HOUR_OF_DAY));
        String eMinute = String.valueOf(event.getEndTime().get(Calendar.MINUTE));
        String location = event.getLocation();
        String name = event.getName();

        if(sMinute.equals("0")){
            sMinute="00";
        }

        if(eMinute.equals("0")){
            eMinute="00";
        }

        createDialog(name, sHour + ":" + sMinute + " - " + eHour + ":" + eMinute + " \n " + location).show();
    }

    public Dialog createDialog(String title, String text){
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        // Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        //  Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public void processFinish(ArrayList<WeekViewEvent> eventz){
        this.events.clear();
        //Yes l'effet de bohr tavu

        for (WeekViewEvent event: eventz) {
            switch(event.getName().toUpperCase().substring(0,3)){
                case "TAS":
                    // Log.e("XXX"," Je suis TAS");
                    event.setColor(getResources().getColor(R.color.event_color_01));
                    break;
                case "DAR":
                    event.setColor(getResources().getColor(R.color.event_color_02));
                    break;
                case "SVP":
                    event.setColor(getResources().getColor(R.color.event_color_03));
                    break;
                case "PPC":
                    event.setColor(getResources().getColor(R.color.event_color_04));
                    break;
                default:
                    // Log.e("XXX"," Je suis " +  event.getName().toUpperCase().substring(0,3));
                    break;
            }

        }

        this.events=eventz;

        updateDisplay();


    }

    public void updateDisplay(){
        WeekViewLoader wk = new WeekViewLoader() {
            int i=0;
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<? extends WeekViewEvent> onLoad(int periodIndex) {
                i++;
                if(i==3) {
                    i=0;
                    return events;
                }
                else{
                    return new ArrayList<WeekViewEvent>();
                }
            }

        };

        mWeekView.setWeekViewLoader(wk);
        mWeekView.notifyDatasetChanged();
        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        Blurry.delete((ViewGroup) findViewById(R.id.content));
    }




}
