package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Province {
    String id, name, description;

    public Province(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.name = data.getString("name");
            this.description = data.getString("description");
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
}
