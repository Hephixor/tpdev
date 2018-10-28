package com.indahouse.skylab.calendarupmc;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jp.wasabeef.blurry.Blurry;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener,AsyncResponse {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private final String baseUE = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/");
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private ArrayList<String> ues = new ArrayList<String>();
    private CheckboxAdapter checkboxAdapter;
    private int tasks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
       initCalendar();
       initSettings();
       initButtons();
       initLayout();

    }

    private void initCalendar(){
        //Calendar initialization
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.goToHour(8);
        setupDateTimeInterpreter(false);
    }

    private void initSettings(){
        //Settings list
        final ListView list_ue = (ListView) findViewById(R.id.list_ue);

        String[] values = new String[] { "STL/M1_STL", "STL/M2_STL", "DAC/M1_DAC",
                "DAC/M2_DAC", "ANDROIDE/M1_ANDROIDE", "ANDROIDE/M2_ANDROIDE", "BIM/M1_BIM", "BIM/M2_BIM",
                "IMA/M1_IMA", "IMA/M2_IMA", "RES/M1_RES", "RES/M2_RES", "SAR/M1_SAR", "SAR/M2_SAR",
                "SESI/M1_SESI", "SESI/M2_SESI", "SFPN/M1_SFPN", "SFPN/M2_SFPN", "M1", "M2"};

        for (int i = 0; i < values.length; ++i) {
            ues.add(values[i]);
        }

        checkboxAdapter = new CheckboxAdapter(this,ues);
    }

    public void initButtons(){
        // Save settings button
        FloatingActionButton buttonSaveSettings  = findViewById(R.id.buttonSaveSettings);
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSaveSettings();
            }
        });

        // Refresh action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCalendars();
            }
        });
    }

    public void initLayout(){
        //General layout initialization
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initDrawer(navigationView);
    }

    public void initDrawer(NavigationView navigationView){
        //Informations
        MenuItem drawer_item_one = navigationView.getMenu().findItem(R.id.infoTextView);
        TextView drawer_text_one = new TextView(this);

        drawer_text_one.setText(getResources().getString(R.string.informations));
        drawer_text_one.setTextColor(Color.BLACK);
        drawer_item_one.setActionView(drawer_text_one);

        //Filter
        MenuItem drawer_item_two = navigationView.getMenu().findItem(R.id.filterTextView);
        TextView drawer_text_two = new TextView(this);

        drawer_text_two.setText(getResources().getString(R.string.filter));
        drawer_text_two.setTextColor(Color.BLACK);
        drawer_item_two.setActionView(drawer_text_two);

        //Refresh
        MenuItem drawer_item_three = navigationView.getMenu().findItem(R.id.refreshTextView);
        TextView drawer_text_three = new TextView(this);

        drawer_text_three.setText(getResources().getString(R.string.refresh));
        drawer_text_three.setTextColor(Color.BLACK);
        drawer_item_three.setActionView(drawer_text_three);
    }

    public void actionSettings(){
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
    }

    public void actionToday(){
        mWeekView.goToToday();
        mWeekView.goToHour(8);
    }

    public void actionDayView(MenuItem item){
        Double hour = mWeekView.getFirstVisibleHour();
        if (mWeekViewType != TYPE_DAY_VIEW) {
            Calendar caltmp = Calendar.getInstance();
            caltmp = mWeekView.getFirstVisibleDay();
            item.setChecked(!item.isChecked());
            mWeekViewType = TYPE_DAY_VIEW;
            mWeekView.setNumberOfVisibleDays(1);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            mWeekView.goToDate(caltmp);
        }
        mWeekView.goToHour(hour);
    }

    public void actionThreeDayView(MenuItem item){
        Double hour = mWeekView.getFirstVisibleHour();
        if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
            Calendar caltmp = Calendar.getInstance();
            caltmp = mWeekView.getFirstVisibleDay();
            item.setChecked(!item.isChecked());
            mWeekViewType = TYPE_THREE_DAY_VIEW;
            mWeekView.setNumberOfVisibleDays(3);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            mWeekView.goToDate(caltmp);
        }
        mWeekView.goToHour(hour);
    }

    public void actionWeekView(MenuItem item){
        Double hour = mWeekView.getFirstVisibleHour();
        if (mWeekViewType != TYPE_WEEK_VIEW) {
            Calendar caltmp = Calendar.getInstance();
            caltmp = mWeekView.getFirstVisibleDay();
            item.setChecked(!item.isChecked());
            mWeekViewType = TYPE_WEEK_VIEW;
            mWeekView.setNumberOfVisibleDays(7);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
            mWeekView.goToDate(caltmp);
        }
        mWeekView.goToHour(hour);
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
        setupDateTimeInterpreter(id == R.id.action_week_view);

        if (id == R.id.action_settings) {
            actionSettings();
            return true;
        }
        switch (id){
            case R.id.action_today:
                actionToday();
                return true;

            case R.id.action_day_view:
                actionDayView(item);
                return true;

            case R.id.action_three_day_view:
                actionThreeDayView(item);
                return true;

            case R.id.action_week_view:
                actionWeekView(item);
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
        ListView list_ue = (ListView) findViewById(R.id.list_ue);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if(list_ue.getVisibility() == View.VISIBLE){
            toggleView();
        }

        else {
            super.onBackPressed();
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
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

        for (WeekViewEvent wve : eventz) {
            this.events.add(wve);
        }

        tasks --;
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

        if(tasks==0){
            mWeekView.notifyDatasetChanged();
            ProgressBar progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.GONE);
            Blurry.delete((ViewGroup) findViewById(R.id.content));

          /*  Set<WeekViewEvent> wveSet = new HashSet<WeekViewEvent>();
            wveSet.addAll(events);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("cachedCalendar",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("calendar",wveSet);
            editor.commit(); */

        }
    }

    public void downloadCalendars(){
        events.clear();
        ProgressBar progressBar = findViewById(R.id.progress);
        if(checkboxAdapter.hasCheck()) {
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
                Blurry.with(BaseActivity.this).radius(1)
                        .sampling(2)
                        .async()
                        .color(android.R.color.darker_gray)
                        .animate(500)
                        .onto((ViewGroup) findViewById(R.id.content));

                ArrayList<String> urlsToDownload = new ArrayList<String>();
                ArrayList<Boolean> urlsCheck = new ArrayList<>(checkboxAdapter.getBools());

                //wtf les index sur les lists

                int i = 0;
                for (Boolean b : urlsCheck) {
                    if (b.equals(true)) {
                        urlsToDownload.add(ues.get(i));
                    }
                    i++;
                }

                tasks = urlsToDownload.size();
                downloadUrls(urlsToDownload);
                updateDisplay();


            } else {

            }
        }
        else{
            events.clear();
            updateDisplay();
        }
    }

    public void downloadUrls(ArrayList<String> urlsToDownload){
        for (String strUrl: urlsToDownload) {
            String completeUrl = baseUE + strUrl;
            // Log.e("XXX ", completeUrl);
            new AsyncTaskGetEventsEntries(BaseActivity.this,BaseActivity.this, completeUrl).execute("");
        }
    }

    public void actionSaveSettings(){
       toggleView();
    }

    public void toggleView(){
        ListView list_ue = findViewById(R.id.list_ue);
        list_ue.setAdapter(checkboxAdapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        TextView textView = findViewById(R.id.textView1);
        CheckBox checkBox = findViewById(R.id.checkBox1);
        WeekView weekView = findViewById(R.id.weekView);


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
    }

}
