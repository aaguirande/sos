package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Agent {
    String token, userId, personId, email, name, eventId,
            actionLogCount, daysSinceMember, rating, loginCount, group, contact,
            entityId, agentPatentId, graduatedAt, isActive, agentPatent;
    Entity entity;

    public Agent(String token, String userId, String personId, String email, String eventId) {
        this.token = token;
        this.userId = userId;
        this.personId = personId;
        this.email = email;
        this.eventId = eventId;
    }

    public Agent() {

    }

    public void setUserAgent(JSONObject data) {
        try {
            this.actionLogCount = data.getString("actionLogCount");
            this.daysSinceMember = data.getString("daysSinceMember");
            this.rating = data.getString("rating");
            this.loginCount = data.getString("loginCount");
            this.group = data.getJSONObject("group").getString("name");
            this.contact = data.getJSONObject("person").getString("phone");
            this.name = data.getJSONObject("person").getString("name")+ " "+data.getJSONObject("person").getString("appellative");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAgent(JSONObject data) {
        try {
            this.entityId = data.getString("entity_id");
            this.agentPatentId = data.getString("agent_patent_id");
            this.graduatedAt = data.getString("graduated_at");
            this.isActive = data.getString("isActive");
            this.entity = new Entity(data.getJSONObject("entity"));
            this.agentPatent = data.getJSONObject("agent_patent").getString("name");
            this.name = data.getJSONObject("person").getString("name") + " " +data.getJSONObject("person").getString("appellative");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActionLogCount() {
        return actionLogCount;
    }

    public void setActionLogCount(String actionLogCount) {
        this.actionLogCount = actionLogCount;
    }

    public String getDaysSinceMember() {
        return daysSinceMember;
    }

    public void setDaysSinceMember(String daysSinceMember) {
        this.daysSinceMember = daysSinceMember;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(String loginCount) {
        this.loginCount = loginCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getContact() {
        return contact;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getAgentPatentId() {
        return agentPatentId;
    }

    public String getGraduatedAt() {
        return graduatedAt;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getAgentPatent() {
        return agentPatent;
    }

    public Entity getEntity() {
        return entity;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "\nAgent{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", personId='" + personId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", actionLogCount='" + actionLogCount + '\'' +
                ", daysSinceMember='" + daysSinceMember + '\'' +
                ", rating='" + rating + '\'' +
                ", loginCount='" + loginCount + '\'' +
                ", group='" + group + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
