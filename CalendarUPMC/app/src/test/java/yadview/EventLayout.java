/*

Copyright 2013 Chris Pope

Copyright (C) 2007 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 
 */


package yadview;


import com.google.code.yadview.Event;

import java.util.ArrayList;
import java.util.Iterator;

public class EventLayout {
    
    
    private final Event mEvent;

    private int mColumn;
    private int mMaxColumns;
    // The coordinates of the event rectangle drawn on the screen.
    private float left;
    private float right;
    private float top;
    private float bottom;

    // These 4 fields are used for navigating among events within the selected
    // hour in the Day and Week view.
    private com.google.code.yadview.EventLayout nextRight;
    private com.google.code.yadview.EventLayout nextLeft;
    private com.google.code.yadview.EventLayout nextUp;
    private com.google.code.yadview.EventLayout nextDown;
    
    
    
    public EventLayout(Event e){
        mEvent = e;
    }
    
    /**
     * Computes a position for each event.  Each event is displayed
     * as a non-overlapping rectangle.  For normal events, these rectangles
     * are displayed in separate columns in the week view and day view.  For
     * all-day events, these rectangles are displayed in separate rows along
     * the top.  In both cases, each event is assigned two numbers: N, and
     * Max, that specify that this event is the Nth event of Max number of
     * events that are displayed in a group. The width and position of each
     * rectangle depend on the maximum number of rectangles that occur at
     * the same time.
     *
     * @param eventsList the list of events, sorted into increasing time order
     * @param minimumDurationMillis minimum duration acceptable as cell height of each event
     * rectangle in millisecond. Should be 0 when it is not determined.
     */
    public static void computePositions(ArrayList<com.google.code.yadview.EventLayout> eventsList,
            long minimumDurationMillis) {
        if (eventsList == null) {
            return;
        }

        // Compute the column positions separately for the all-day events
        doComputePositions(eventsList, minimumDurationMillis, false);
        doComputePositions(eventsList,  minimumDurationMillis, true);
    }
    
    //TODO: Allow different layout strategies
    private static void doComputePositions(ArrayList<com.google.code.yadview.EventLayout> eventLayouts, long minimumDurationMillis, boolean doAlldayEvents) {
        final ArrayList<com.google.code.yadview.EventLayout> activeList = new ArrayList<com.google.code.yadview.EventLayout>();
        final ArrayList<com.google.code.yadview.EventLayout> groupList = new ArrayList<com.google.code.yadview.EventLayout>();

        if (minimumDurationMillis < 0) {
            minimumDurationMillis = 0;
        }

        long colMask = 0;
        int maxCols = 0;
        for (com.google.code.yadview.EventLayout eventLayout : eventLayouts) {
            // Process all-day events separately
            if (eventLayout.getEvent().drawAsAllday() != doAlldayEvents)
                continue;

           if (!doAlldayEvents) {
                colMask = removeNonAlldayActiveEvents(
                        eventLayout, activeList.iterator(), minimumDurationMillis, colMask);
            } else {
                colMask = removeAlldayActiveEvents(eventLayout, activeList.iterator(), colMask);
            }

            // If the active list is empty, then reset the max columns, clear
            // the column bit mask, and empty the groupList.
            if (activeList.isEmpty()) {
                for (com.google.code.yadview.EventLayout ev : groupList) {
                    ev.setMaxColumns(maxCols);
                }
                maxCols = 0;
                colMask = 0;
                groupList.clear();
            }

            // Find the first empty column.  Empty columns are represented by
            // zero bits in the column mask "colMask".
            int col = findFirstZeroBit(colMask);
            if (col == 64)
                col = 63;
            colMask |= (1L << col);
            eventLayout.setColumn(col);
            activeList.add(eventLayout);
            groupList.add(eventLayout);
            int len = activeList.size();
            if (maxCols < len)
                maxCols = len;
        }
        for (com.google.code.yadview.EventLayout ev : groupList) {
            ev.setMaxColumns(maxCols);
        }
    }

    private static long removeAlldayActiveEvents(com.google.code.yadview.EventLayout event, Iterator<com.google.code.yadview.EventLayout> iter, long colMask) {
        // Remove the inactive allday events. An event on the active list
        // becomes inactive when the end day is less than the current event's
        // start day.
        while (iter.hasNext()) {
            final com.google.code.yadview.EventLayout active = iter.next();
            if (active.getEvent().getEndDay() < event.getEvent().getStartDay()) {
                colMask &= ~(1L << active.getColumn());
                iter.remove();
            }
        }
        return colMask;
    }

    private static long removeNonAlldayActiveEvents(
            com.google.code.yadview.EventLayout eventLayout, Iterator<com.google.code.yadview.EventLayout> iterator, long minDurationMillis, long colMask) {
        long start = eventLayout.getEvent().getStartMillis();
        // Remove the inactive events. An event on the active list
        // becomes inactive when its end time is less than or equal to
        // the current event's start time.
        while (iterator.hasNext()) {
            final com.google.code.yadview.EventLayout active = iterator.next();

            final long duration = Math.max(
                    active.getEvent().getEndMillis() - active.getEvent().getStartMillis(), minDurationMillis);
            if ((active.getEvent().getStartMillis() + duration) <= start) {
                colMask &= ~(1L << active.getColumn());
                iterator.remove();
            }
        }
        return colMask;
    }

    public static int findFirstZeroBit(long val) {
        for (int ii = 0; ii < 64; ++ii) {
            if ((val & (1L << ii)) == 0)
                return ii;
        }
        return 64;
    }

    public Event getEvent() {
        return mEvent;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int column) {
        mColumn = column;
    }

    public int getMaxColumns() {
        return mMaxColumns;
    }

    public void setMaxColumns(int maxColumns) {
        mMaxColumns = maxColumns;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public com.google.code.yadview.EventLayout getNextRight() {
        return nextRight;
    }

    public void setNextRight(com.google.code.yadview.EventLayout nextRight) {
        this.nextRight = nextRight;
    }

    public com.google.code.yadview.EventLayout getNextLeft() {
        return nextLeft;
    }

    public void setNextLeft(com.google.code.yadview.EventLayout nextLeft) {
        this.nextLeft = nextLeft;
    }

    public com.google.code.yadview.EventLayout getNextUp() {
        return nextUp;
    }

    public void setNextUp(com.google.code.yadview.EventLayout nextUp) {
        this.nextUp = nextUp;
    }

    public com.google.code.yadview.EventLayout getNextDown() {
        return nextDown;
    }

    public void setNextDown(com.google.code.yadview.EventLayout nextDown) {
        this.nextDown = nextDown;
    }
    

}
