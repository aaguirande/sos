package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

import tk.meceap.sos.constants.Core;

public class OccurrenceFilter {
    String occurrenceType, dateType, completed, flag, dateFrom, dateTo;
    int total = 0, perPage = 10, lastPage = 0, currentPage = 1;

    public OccurrenceFilter() {
    }

    public void setOccurrenceFilterPagination(int total, int perPage, int lastPage, int currentPage) {
        this.total = total;
        this.perPage = perPage;
        this.lastPage = lastPage;
        this.currentPage = currentPage;
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

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public int getCurrentPage() {
        return currentPage;
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
                    .put("per_page", perPage)
                    .put("current_page", currentPage)
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
