package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import tk.meceap.sos.constants.Core;

public class Occurrence {
    String id,
            agentsAddress,
            agentsFollowUp,
            agentsUid,
            allocatedTo,
            completed,
            departmentId,
            details,
            flag,
            occurrencyTypeId,
            reminder,
            victimAddress,
            victimName,
            date,
            victimUid;
    Department department;
    OccurrenceType occurrenceType;

    private List<Call> callList = new LinkedList<>();

    private boolean response = false;

    public Occurrence(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.agentsAddress = data.getString("agents_address");
            this.agentsFollowUp = data.getString("agents_follow_up");
            this.agentsUid = data.getString("agents_uid");
            this.completed = data.getString("completed");
            this.details = data.getString("details");
            this.flag = data.getString("flag");
            this.victimAddress = data.getString("victim_address");
            this.victimName = data.getString("victim_name");
            this.victimUid = data.getString("victim_uid");
            this.date = data.getString("created_at");
            this.occurrencyTypeId = data.getString("occurrency_type_id");
            this.occurrenceType = getOccurencyTypeById(Core.getInstance().getOccurencyTypes(), data.getString("occurrency_type_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Occurrence(String department_id, String occurrency_type_id, String victim_address, String victim_name, String victim_uid) {
        this.departmentId = department_id;
        this.occurrencyTypeId = occurrency_type_id;
        this.victimAddress = victim_address;
        this.victimName = victim_name;
        this.victimUid = victim_uid;
    }

    public Occurrence(String id, String agents_address, String agents_follow_up, String agents_uid, String allocated_to, String completed, String department_id, String details, String flag, String occurrency_type_id, String reminder, String victim_address, String victim_name, String victim_uid) {
        this.id = id;
        this.agentsAddress = agents_address;
        this.agentsFollowUp = agents_follow_up;
        this.agentsUid = agents_uid;
        this.allocatedTo = allocated_to;
        this.completed = completed;
        this.departmentId = department_id;
        this.details = details;
        this.flag = flag;
        this.occurrencyTypeId = occurrency_type_id;
        this.reminder = reminder;
        this.victimAddress = victim_address;
        this.victimName = victim_name;
        this.victimUid = victim_uid;
    }

    public Occurrence() {

    }

    public Department getDepartmentById(List<Department> data, String id) {
        for (Department d : data) {
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }

    public OccurrenceType getOccurencyTypeById(List<OccurrenceType> data, String id) {
        for (OccurrenceType d : data) {
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }

    public OccurrenceType getOccurencyType() {
        return occurrenceType;
    }

    public void initCall(String department_id, String occurrency_type_id,
                         String victim_address, String victim_uid) {
        this.victimUid = victim_uid;
        this.occurrencyTypeId = occurrency_type_id;
        this.victimAddress = victim_address;
        this.departmentId = department_id;
    }

    public List<Call> getCallList() {
        return callList;
    }

    public void setCallList(Call callList) {
        this.callList.add(callList);
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Department getDepartment() {
        return department;
    }

    public OccurrenceType getOccurrenceType() {
        return occurrenceType;
    }

    public void setOccurrenceType(OccurrenceType occurrenceType) {
        this.occurrenceType = occurrenceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgentsAddress() {
        return agentsAddress;
    }

    public void setAgentsAddress(String agentsAddress) {
        this.agentsAddress = agentsAddress;
    }

    public String getAgentsFollowUp() {
        return agentsFollowUp;
    }

    public void setAgentsFollowUp(String agentsFollowUp) {
        this.agentsFollowUp = agentsFollowUp;
    }

    public String getAgentsUid() {
        return agentsUid;
    }

    public void setAgentsUid(String agentsUid) {
        this.agentsUid = agentsUid;
    }

    public String getAllocatedTo() {
        return allocatedTo;
    }

    public void setAllocatedTo(String allocatedTo) {
        this.allocatedTo = allocatedTo;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOccurrencyTypeId() {
        return occurrencyTypeId;
    }

    public void setOccurrencyTypeId(String occurrencyTypeId) {
        this.occurrencyTypeId = occurrencyTypeId;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getVictimAddress() {
        return victimAddress;
    }

    public void setVictimAddress(String victimAddress) {
        this.victimAddress = victimAddress;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getVictimUid() {
        return victimUid;
    }

    public void setVictimUid(String victimUid) {
        this.victimUid = victimUid;
    }

    public JSONObject getParams() {
        JSONObject params = new JSONObject();
        try {
            params
                    .put("victim_name", victimName)
                    .put("victim_uid", victimUid)
                    .put("victim_address", victimAddress)
                    .put("occurrency_type_id", occurrencyTypeId)
                    .put("department_id", departmentId)
                    .put("reminder", "")
                    .put("allocated_to", Core.getInstance().getUserLogged().getUserId())
                    .put("completed", false)
                    .put("agents_address", agentsAddress)
                    .put("agents_follow_up", agentsFollowUp)
                    .put("agents_uid", agentsUid)
                    .put("details", details)
                    .put("flag", flag)
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public JSONObject getUpdateParams() {
        JSONObject params = new JSONObject();
        try {
            params
                    .put("victim_name", victimName)
                    .put("occurrency_type_id", occurrenceType.getId())
                    .put("allocated_to", Core.getInstance().getUserLogged().getUserId())
                    .put("completed", completed)
                    .put("details", details)
                    .put("flag", flag)
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    @Override
    public String toString() {
        return "\n\nOccurrence{" +
                "id='" + id + '\'' +
                ", agentsAddress='" + agentsAddress + '\'' +
                ", agentsFollowUp='" + agentsFollowUp + '\'' +
                ", agentsUid='" + agentsUid + '\'' +
                ", allocatedTo='" + allocatedTo + '\'' +
                ", completed='" + completed + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", details='" + details + '\'' +
                ", flag='" + flag + '\'' +
                ", occurrencyTypeId='" + occurrencyTypeId + '\'' +
                ", reminder='" + reminder + '\'' +
                ", victimAddress='" + victimAddress + '\'' +
                ", victimName='" + victimName + '\'' +
                ", date='" + date + '\'' +
                ", victimUid='" + victimUid + '\'' +
                ", department=" + department +
                ", occurrenceType=" + occurrenceType +
                ", callList=" + callList +
                ", response=" + response +
                "}\n\n";
    }
}
