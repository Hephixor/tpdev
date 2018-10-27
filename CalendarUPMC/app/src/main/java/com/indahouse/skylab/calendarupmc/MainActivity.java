package com.indahouse.skylab.calendarupmc;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity{
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

       //Finalement cette classe servirait à charger le cache de l'emploi du temps ?

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        
        return events;
    }



}
