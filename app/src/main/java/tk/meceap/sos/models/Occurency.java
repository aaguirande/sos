package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import tk.meceap.sos.constants.Core;

public class Occurency {
    String id,
            agents_address,
            agents_follow_up,
            agents_uid,
            allocated_to,
            completed,
            department_id,
            details,
            flag,
            occurrency_type_id,
            reminder,
            victim_address,
            victim_name,
            victim_uid;
    Department department;
    OccurencyType occurencyType;

    private List<Call> callList = new LinkedList<>();;
    private boolean response = false;

    public Occurency(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.agents_address = data.getString("agents_address");
            this.agents_follow_up = data.getString("agents_follow_up");
            this.agents_uid = data.getString("agents_uid");
            this.allocated_to = data.getJSONObject("allocatedTo").getJSONObject("person").getString("name") + " " +data.getJSONObject("allocatedTo").getJSONObject("person").getString("appellative");
            this.completed = data.getString("completed");
            this.details = data.getString("details");
            this.flag = data.getString("flag");
            this.reminder = data.getString("reminder");
            this.victim_address = data.getString("victim_address");
            this.victim_name = data.getString("victim_name");
            this.victim_uid = data.getString("victim_uid");
            this.occurrency_type_id = data.getString("occurrency_type_id");
            this.occurencyType = getOccurencyTypeById(Core.getInstance().getOccurencyTypes(),data.getString("occurrency_type_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Occurency(String department_id, String occurrency_type_id, String victim_address, String victim_name, String victim_uid) {
        this.department_id = department_id;
        this.occurrency_type_id = occurrency_type_id;
        this.victim_address = victim_address;
        this.victim_name = victim_name;
        this.victim_uid = victim_uid;
    }

    public Occurency(String id, String agents_address, String agents_follow_up, String agents_uid, String allocated_to, String completed, String department_id, String details, String flag, String occurrency_type_id, String reminder, String victim_address, String victim_name, String victim_uid) {
        this.id = id;
        this.agents_address = agents_address;
        this.agents_follow_up = agents_follow_up;
        this.agents_uid = agents_uid;
        this.allocated_to = allocated_to;
        this.completed = completed;
        this.department_id = department_id;
        this.details = details;
        this.flag = flag;
        this.occurrency_type_id = occurrency_type_id;
        this.reminder = reminder;
        this.victim_address = victim_address;
        this.victim_name = victim_name;
        this.victim_uid = victim_uid;
    }

    public Occurency() {

    }

    public Department getDepartmentById(List<Department> data, String id) {
        for (Department d : data) {
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }
    public OccurencyType getOccurencyTypeById(List<OccurencyType> data, String id) {
        for (OccurencyType d : data) {
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }

    public OccurencyType getOccurencyType() {
        return occurencyType;
    }

    public void initCall(String department_id, String occurrency_type_id,
                         String victim_address, String victim_uid) {
        this.victim_uid = victim_uid;
        this.occurrency_type_id = occurrency_type_id;
        this.victim_address = victim_address;
        this.department_id = department_id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getAgents_address() {
        return agents_address;
    }

    public void setAgents_address(String agents_address) {
        this.agents_address = agents_address;
    }

    public String getAgents_follow_up() {
        return agents_follow_up;
    }

    public void setAgents_follow_up(String agents_follow_up) {
        this.agents_follow_up = agents_follow_up;
    }

    public String getAgents_uid() {
        return agents_uid;
    }

    public void setAgents_uid(String agents_uid) {
        this.agents_uid = agents_uid;
    }

    public String getAllocated_to() {
        return allocated_to;
    }

    public void setAllocated_to(String allocated_to) {
        this.allocated_to = allocated_to;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
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

    public String getOccurrency_type_id() {
        return occurrency_type_id;
    }

    public void setOccurrency_type_id(String occurrency_type_id) {
        this.occurrency_type_id = occurrency_type_id;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getVictim_address() {
        return victim_address;
    }

    public void setVictim_address(String victim_address) {
        this.victim_address = victim_address;
    }

    public String getVictim_name() {
        return victim_name;
    }

    public void setVictim_name(String victim_name) {
        this.victim_name = victim_name;
    }

    public String getVictim_uid() {
        return victim_uid;
    }

    public void setVictim_uid(String victim_uid) {
        this.victim_uid = victim_uid;
    }

    public JSONObject getParams() {
        JSONObject params = new JSONObject();
        try {
            params
                    .put("victim_name", victim_name)
                    .put("victim_uid", victim_uid)
                    .put("victim_address", victim_address)
                    .put("occurrency_type_id", occurrency_type_id)
                    .put("department_id", department_id)
                    .put("reminder", "")
                    .put("allocated_to", Core.getInstance().getUserLogged().getUserId())
                    .put("completed", false)
                    .put("agents_address", agents_address)
                    .put("agents_follow_up", agents_follow_up)
                    .put("agents_uid", agents_uid)
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
        return "\nOccurency{" +
                "id='" + id + '\'' +
                ", agents_address='" + agents_address + '\'' +
                ", agents_follow_up='" + agents_follow_up + '\'' +
                ", agents_uid='" + agents_uid + '\'' +
                ", allocated_to='" + allocated_to + '\'' +
                ", completed='" + completed + '\'' +
                ", department_id='" + department_id + '\'' +
                ", details='" + details + '\'' +
                ", flag='" + flag + '\'' +
                ", occurrency_type_id='" + occurrency_type_id + '\'' +
                ", reminder='" + reminder + '\'' +
                ", victim_address='" + victim_address + '\'' +
                ", victim_name='" + victim_name + '\'' +
                ", victim_uid='" + victim_uid + '\'' +
                ", callList=" + callList +
                ", response=" + response +
                '}';
    }
}
