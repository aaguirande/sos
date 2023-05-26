package tk.meceap.sos.models;

import org.json.JSONException;
import org.json.JSONObject;

public class District {
    String id, name, description, province_id;
    Province province;

    public District(JSONObject data) {
        try {
            this.id = data.getString("id");
            this.name = data.getString("name");
            this.description = data.getString("description");
            this.province_id = data.getString("province_id");
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

    public String getProvince_id() {
        return province_id;
    }
}
