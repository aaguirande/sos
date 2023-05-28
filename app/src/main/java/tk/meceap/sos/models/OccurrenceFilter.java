package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

import tk.meceap.sos.constants.Core;

public class OccurrenceFilter {
    String occurrenceType, dateType, completed, flag, dateFrom, dateTo;

    public OccurrenceFilter() {
    }

    public String getOccurrenceType() {
        return occurrenceType;
    }

    public void setOccurrenceType(String occurrenceType) {
        this.occurrenceType = occurrenceType;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public JSONObject getFilterParams() {
        JSONObject params = new JSONObject();
        try {
            params
                    .put("dateFilter", dateType)
                    .put("occurrence_type", occurrenceType)
                    .put("allocated_to", Core.getInstance().getUserLogged().getUserId())
                    .put("completed", completed)
                    .put("min", dateFrom)
                    .put("max", dateTo)
                    .put("flag", flag)
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
