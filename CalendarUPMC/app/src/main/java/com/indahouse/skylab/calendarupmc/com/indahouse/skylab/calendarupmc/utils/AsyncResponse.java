package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<WeekViewEvent> events);
}