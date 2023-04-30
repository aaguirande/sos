package tk.meceap.sos.models;

public class CalendarEventLog {
    String time;
    CalendarEventStatus eventStatus;

    public CalendarEventLog(String time, CalendarEventStatus eventStatus) {
        this.time = time;
        this.eventStatus = eventStatus;
    }

    public String getTime() {
        return time;
    }

    public CalendarEventStatus getEventStatus() {
        return eventStatus;
    }

    @Override
    public String toString() {
        return "\nCalendarEventLog{" +
                "time='" + time + '\'' +
                ", eventStatus=" + eventStatus +
                '}';
    }
}
