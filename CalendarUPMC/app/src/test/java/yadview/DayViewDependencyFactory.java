package yadview;

import com.google.code.yadview.DayViewRenderer;
import com.google.code.yadview.DayViewScrollingController;
import com.google.code.yadview.EventRenderer;
import com.google.code.yadview.util.CalendarDateUtils;
import com.google.code.yadview.util.EventRenderingUtils;
import com.google.code.yadview.util.PreferencesUtils;
import com.google.code.yadview.util.TimeZoneUtils;
import com.google.common.eventbus.EventBus;

public interface DayViewDependencyFactory {
    
    public DayViewRenderer buildDayViewRenderer();
    
    public EventRenderer buildEventRenderer();
    
    public DayViewScrollingController buildScrollingController(EventBus eventBus);
    
    public TimeZoneUtils buildTimezoneUtils();

    public PreferencesUtils buildPreferencesUtils();

    public CalendarDateUtils buildDateUtils();

    public EventRenderingUtils buildRenderingUtils();


}
