package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tk.meceap.sos.constants.Core;

public class Calendar {
    private String id, agent, agentLocation,
            groupCalendar, eventCalendar, detailsCalendar,
            eventDate, eventStartTime, eventEndTime, eventStatus,
            createdBy, updatedAt, createdAt;
    private List<CalendarEventLog> eventLog = new LinkedList<>();

    public Calendar() {
    }

    public Calendar(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.agent = data.getString("name");
            this.agentLocation = data.getString("location");
            this.groupCalendar = data.getString("calendar");
            this.eventCalendar = data.getString("title");
            this.detailsCalendar = data.getString("body");
            this.eventDate = data.getString("start_date");
            this.eventStartTime = data.getString("start_time");
            this.eventEndTime = data.getString("end_date");
            this.eventStatus = data.getString("emergency_type");
            this.createdBy = data.getString("created_by");
            this.updatedAt = data.getString("updated_at");
            this.createdAt = data.getString("created_at");
            this.setEventLog(data.getString("event_log"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getAgent() {
        return agent;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public String getGroupCalendar() {
        return groupCalendar;
    }

    public String getEventCalendar() {
        return eventCalendar;
    }

    public String getDetailsCalendar() {
        return detailsCalendar;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventLog(String s) {
        String[] logs = s.split("[|]");
        int i = 0;
        for (String log : logs) {
            i++;
            if (i != 1) {
                String[] l = log.split(","); //1,06:04:39
                this.eventLog.add(new CalendarEventLog(l[1], getCalendarEventStatusById(Core.getInstance().getEventStatuses(), l[0])));
            }
        }
    }

    public List<CalendarEventLog> getEventLog() {
        return eventLog;
    }

    public CalendarEventStatus getCalendarEventStatusById(List<CalendarEventStatus> eventStatuses, String id) {
        for (CalendarEventStatus ev : eventStatuses) {
            if (ev.getId().equals(id))
                return ev;
        }
        return null;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "\nCalendar{" +
                "id='" + id + '\'' +
                ", agent='" + agent + '\'' +
                ", agentLocation='" + agentLocation + '\'' +
                ", groupCalendar='" + groupCalendar + '\'' +
                ", eventCalendar='" + eventCalendar + '\'' +
                ", detailsCalendar='" + detailsCalendar + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventStartTime='" + eventStartTime + '\'' +
                ", eventEndTime='" + eventEndTime + '\'' +
                ", eventLog='" + eventLog + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdAt='" + createdAt + "\'\n" +
                ", eventStatus='" + eventStatus + "\'" +
                '}';
    }
}
