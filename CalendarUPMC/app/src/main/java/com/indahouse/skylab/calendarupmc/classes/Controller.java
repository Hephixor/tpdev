package com.indahouse.skylab.calendarupmc.classes;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indahouse.skylab.calendarupmc.BaseActivity;
import com.indahouse.skylab.calendarupmc.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class Controller {
    BaseActivity baseActivity;
    public static final int TYPE_DAY_VIEW = 1;
    public static final int TYPE_THREE_DAY_VIEW = 2;
    public static final int TYPE_WEEK_VIEW = 3;
    public int mWeekViewType = TYPE_THREE_DAY_VIEW;
    public WeekView mWeekView;
    public final String baseUE = new String("https://cal.ufr-info-p6.jussieu.fr/caldav.php/");
    public List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    public LinkedHashMap<String,String> keyToUrls;
    public CheckboxAdapter checkboxAdapter;
    public int tasks;

    public Controller(BaseActivity baseActivity){
        this.baseActivity = baseActivity;
    }

    //download wrapper with design
    public void downloadCalendars(){
        events.clear();
        ProgressBar progressBar = baseActivity.findViewById(R.id.progress);
        if(checkboxAdapter.hasCheck()) {
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
                Blurry.with(baseActivity).radius(1)
                .sampling(2)
                .async()
                .color(android.R.color.darker_gray)
                .animate(500)
                .onto((ViewGroup) baseActivity.findViewById(R.id.content));

                ArrayList<String> urlsToDownload = new ArrayList<String>();
                ArrayList<Boolean> urlsCheck = new ArrayList<>(checkboxAdapter.getBools());

                int i = 0;
                for (Boolean b : urlsCheck) {
                    if (b.equals(true)) {
                        ArrayList<String> urls = new ArrayList<String>(keyToUrls.values());
                        urlsToDownload.add(urls.get(i));
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

    //Get events list from url
    public void downloadUrls(ArrayList<String> urlsToDownload){
        for (String strUrl: urlsToDownload) {
            String completeUrl = baseUE + strUrl;
            new AsyncTaskGetEventsEntries(this,baseActivity, completeUrl).execute("");
        }
    }

    public void actionSaveSettings(){
        toggleView();
    }

    //Switch between calendar and settings
    public void toggleView(){
        ListView list_ue = baseActivity.findViewById(R.id.list_ue);
        list_ue.setAdapter(checkboxAdapter);
        FloatingActionButton fab = baseActivity.findViewById(R.id.fab);
        FloatingActionButton buttonSaveSettings = baseActivity.findViewById(R.id.buttonSaveSettings);
        TextView textView = baseActivity.findViewById(R.id.textView1);
        CheckBox checkBox = baseActivity.findViewById(R.id.checkBox1);
        WeekView weekView = baseActivity.findViewById(R.id.weekView);


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

    //Calendar initialization
    public void initCalendar(){
        mWeekView = (WeekView) baseActivity.findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(baseActivity);
        mWeekView.setMonthChangeListener(baseActivity);
        mWeekView.setEventLongPressListener(baseActivity);
        mWeekView.setEmptyViewLongPressListener(baseActivity);
        mWeekView.goToHour(8);
        setupDateTimeInterpreter(false);
    }

    //Settings list
    public void initSettings(){
        final ListView list_ue = (ListView) baseActivity.findViewById(R.id.list_ue);
        keyToUrls = new LinkedHashMap<>();

        keyToUrls.put("M1 STL", "STL/M1_STL");
        keyToUrls.put("M2 STL", "STL/M2_STL");
        keyToUrls.put("M1 DAC", "DAC/M1_DAC");
        keyToUrls.put("M2 DAC", "DAC/M2_DAC");
        keyToUrls.put("M1 ANDROIDE", "ANDROIDE/M1_ANDROIDE");
        keyToUrls.put("M2 ANDROIDE", "ANDROIDE/M2_ANDROIDE");
        keyToUrls.put("M1 BIM", "BIM/M1_BIM");
        keyToUrls.put("M2 BIM", "BIM/M2_BIM");
        keyToUrls.put("M1 IMA", "IMA/M1_IMA");
        keyToUrls.put("M2 IMA", "IMA/M2_IMA");
        keyToUrls.put("M1 RES", "RES/M1_RES");
        keyToUrls.put("M2 RES", "RES/M2_RES");
        keyToUrls.put("M1 SAR", "RES/M1_SAR");
        keyToUrls.put("M2 SAR", "RES/M2_SAR");
        keyToUrls.put("M1 SESI", "SESI/M1_SESI");
        keyToUrls.put("M2 SESI", "SESI/M2_SESI");
        keyToUrls.put("M1 SFPN", "SFPN/M1_SFPN");
        keyToUrls.put("M2 SFPN", "SFPN/M2_SFPN");
        keyToUrls.put("M1", "");
        keyToUrls.put("M2", "");


        List<String> resource = new ArrayList<String>();
        for (String str: keyToUrls.keySet()) {
            resource.add(str);
        }

        checkboxAdapter = new CheckboxAdapter(baseActivity,keyToUrls, resource);
    }

    //Set buttons listeners
    public void initButtons(){
        // Save settings button
        FloatingActionButton buttonSaveSettings  = baseActivity.findViewById(R.id.buttonSaveSettings);
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSaveSettings();
            }
        });

        // Refresh action button
        FloatingActionButton fab = (FloatingActionButton) baseActivity.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCalendars();
            }
        });
    }

    public void initLayout(){
        //General layout initialization
        Toolbar toolbar = (Toolbar) baseActivity.findViewById(R.id.toolbar);
        baseActivity.setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) baseActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(baseActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) baseActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(baseActivity);
        baseActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        initDrawer(navigationView);
    }

    public void initDrawer(NavigationView navigationView){
        //Informations
        MenuItem drawer_item_one = navigationView.getMenu().findItem(R.id.infoTextView);
        TextView drawer_text_one = new TextView(baseActivity);

        drawer_text_one.setText(baseActivity.getResources().getString(R.string.informations));
        drawer_text_one.setTextColor(Color.BLACK);
        drawer_item_one.setActionView(drawer_text_one);

        //Filter
        MenuItem drawer_item_two = navigationView.getMenu().findItem(R.id.filterTextView);
        TextView drawer_text_two = new TextView(baseActivity);

        drawer_text_two.setText(baseActivity.getResources().getString(R.string.filter));
        drawer_text_two.setTextColor(Color.BLACK);
        drawer_item_two.setActionView(drawer_text_two);

        //Refresh
        MenuItem drawer_item_three = navigationView.getMenu().findItem(R.id.refreshTextView);
        TextView drawer_text_three = new TextView(baseActivity);

        drawer_text_three.setText(baseActivity.getResources().getString(R.string.refresh));
        drawer_text_three.setTextColor(Color.BLACK);
        drawer_item_three.setActionView(drawer_text_three);
    }

    //Display master selection list
    public void actionSettings(){
        ListView list_ue = baseActivity.findViewById(R.id.list_ue);
        list_ue.setAdapter(checkboxAdapter);
        FloatingActionButton fab = baseActivity.findViewById(R.id.fab);
        FloatingActionButton buttonSaveSettings = baseActivity.findViewById(R.id.buttonSaveSettings);
        TextView textView = baseActivity.findViewById(R.id.textView1);
        CheckBox checkBox = baseActivity.findViewById(R.id.checkBox1);
        WeekView weekView = baseActivity.findViewById(R.id.weekView);
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

    //Switch to 1-day appearance
    public void actionDayView(MenuItem item){
        Double hour = mWeekView.getFirstVisibleHour();
        if (mWeekViewType != TYPE_DAY_VIEW) {
            Calendar caltmp = Calendar.getInstance();
            caltmp = mWeekView.getFirstVisibleDay();
            item.setChecked(!item.isChecked());
            mWeekViewType = TYPE_DAY_VIEW;
            mWeekView.setNumberOfVisibleDays(1);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.goToDate(caltmp);
        }
        mWeekView.goToHour(hour);
    }

    //Switch to 3-days appearance
    public void actionThreeDayView(MenuItem item){
        Double hour = mWeekView.getFirstVisibleHour();
        if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
            Calendar caltmp = Calendar.getInstance();
            caltmp = mWeekView.getFirstVisibleDay();
            item.setChecked(!item.isChecked());
            mWeekViewType = TYPE_THREE_DAY_VIEW;
            mWeekView.setNumberOfVisibleDays(3);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.goToDate(caltmp);
        }
        mWeekView.goToHour(hour);
    }

    //Switch to week appearance
    public void actionWeekView(MenuItem item){
        Double hour = mWeekView.getFirstVisibleHour();
        if (mWeekViewType != TYPE_WEEK_VIEW) {
            Calendar caltmp = Calendar.getInstance();
            caltmp = mWeekView.getFirstVisibleDay();
            item.setChecked(!item.isChecked());
            mWeekViewType = TYPE_WEEK_VIEW;
            mWeekView.setNumberOfVisibleDays(7);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, baseActivity.getResources().getDisplayMetrics()));
            mWeekView.goToDate(caltmp);
        }
        mWeekView.goToHour(hour);
    }

    //Modal provider
    public Dialog createDialog(String title, String text){
        return new AlertDialog.Builder(baseActivity)
        .setTitle(title)
        .setMessage(text)
        .setPositiveButton(android.R.string.ok, null)
        .create();
    }

    //Stuff to format date & time
    public void setupDateTimeInterpreter(final boolean shortDate) {
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


    //Notify Calendar to display newly created events
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

    }


    public void processFinish(ArrayList<WeekViewEvent> eventz){
        for (WeekViewEvent event: eventz) {
            switch(event.getName().toUpperCase().substring(0,3)){
                case "TAS":
                    // Log.e("XXX"," Je suis TAS");
                event.setColor(baseActivity.getResources().getColor(R.color.event_color_01));
                break;
                case "DAR":
                event.setColor(baseActivity.getResources().getColor(R.color.event_color_02));
                break;
                case "SVP":
                event.setColor(baseActivity.getResources().getColor(R.color.event_color_03));
                break;
                case "PPC":
                event.setColor(baseActivity.getResources().getColor(R.color.event_color_04));
                break;
                default:
                    // Log.e("XXX"," Je suis " +  event.getName().toUpperCase().substring(0,3));
                break;
            }
        }

        for (WeekViewEvent wve : eventz) {
            events.add(wve);
        }

        updateDisplay();

        //Count until all calendars are downloaded
        tasks --;
        if(tasks==0){
            //Display calendars
            mWeekView.notifyDatasetChanged();
            ProgressBar progressBar = baseActivity.findViewById(R.id.progress);
            progressBar.setVisibility(View.GONE);
            Blurry.delete((ViewGroup) baseActivity.findViewById(R.id.content));

            //Saving calendars in cache
            SharedPreferences sharedPreferences = baseActivity.getSharedPreferences("cachedCalendar",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("cache", toJson((events)));
            editor.commit();
         }
    }

    public void getCachedCalendars(){
        ArrayList<WeekViewEvent> cachedCalendars = new ArrayList<WeekViewEvent>();
        //Get cached calendars
        SharedPreferences sharedPreferences = baseActivity.getSharedPreferences("cachedCalendar",Context.MODE_PRIVATE);
        String strCalendars = sharedPreferences.getString("cache","Defaultvalue");
        if(!(strCalendars.equals("Defaultvalue"))){
            cachedCalendars = (ArrayList<WeekViewEvent>) fromJson(strCalendars, new TypeToken<ArrayList<WeekViewEvent>>() {
            }.getType());
            events.clear();
            for (WeekViewEvent wve : cachedCalendars) {
                events.add(wve);
            }
            updateDisplay();
        }
    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

}
