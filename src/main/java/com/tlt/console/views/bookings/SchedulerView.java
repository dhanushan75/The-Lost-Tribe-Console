package com.tlt.console.views.bookings;

import org.vaadin.stefan.fullcalendar.CalendarView;

public enum SchedulerView implements CalendarView {

    /**
     * Day timeline
     **/
    TIMELINE_DAY("timelineDay"),
    /**
     * Week timeline
     **/
    TIMELINE_WEEK("timelineWeek"),
    /**
     * Month timeline
     **/
    TIMELINE_MONTH("timelineMonth"),
    /**
     * Year timeline
     **/
    TIMELINE_YEAR("timelineYear"),

    /**
     * Day timeline showing also resources
     **/
    RESOURCE_TIMELINE_DAY("resourceTimelineDay"),
    /**
     * Week timeline showing also resources
     **/
    RESOURCE_TIMELINE_WEEK("resourceTimelineWeek"),
    /**
     * Month timeline showing also resources
     **/
    RESOURCE_TIMELINE_MONTH("resourceTimelineMonth"),
    /**
     * Year timeline showing also resources
     **/
    RESOURCE_TIMELINE_YEAR("resourceTimelineYear"),

    /**
     * Day timegrid showing also resources
     */
    RESOURCE_TIME_GRID_DAY("resourceTimeGridDay"), // was AGENDA_DAY
    /**
     * Week timegrid showing also resources
     */
    RESOURCE_TIME_GRID_WEEK("resourceTimeGridWeek"), // was AGENDA_WEEK

    ;

    private final String clientSideName;

    SchedulerView(String clientSideName) {
        this.clientSideName = clientSideName;
    }

    @Override
    public String getClientSideValue() {
        return clientSideName;
    }

    @Override
    public String getName() {
        return name() + " (S)";
    }

}
