package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

public class EntityType {
    String id, name, description, level;

    public EntityType(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.name = data.getString("name");
            this.description = data.getString("description");
            this.level = data.getString("level");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getLevel() {
        return level;
    }
}
