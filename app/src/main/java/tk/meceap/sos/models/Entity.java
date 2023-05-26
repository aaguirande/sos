package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Entity {
    EntityType entityType;

    String id, name, description, departmentId, districtId, entityTypeId;

    public Entity(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.name = data.getString("name");
            this.description = data.getString("description");
            this.departmentId = data.getString("department_id");
            this.districtId = data.getString("district_id");
            this.entityTypeId = data.getString("entity_type_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public String getEntityTypeId() {
        return entityTypeId;
    }

}
