package tk.meceap.sos.constants;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    String token, userId, personId, email, name, category;

    public LocalStorage(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("STORAGE_MECEAP_API", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getToken() {
        token = sharedPreferences.getString("TOKEN", "");
        return token;
    }

    public void setToken(String token) {
        editor.putString("TOKEN", token);
        editor.commit();
        this.token = token;
    }

    public String getUserId() {
        userId = sharedPreferences.getString("USER_ID", "");
        return userId;
    }

    public void setUserId(String userId) {
        editor.putString("USER_ID", userId);
        editor.commit();
        this.userId = userId;
    }

    public String getPersonId() {
        personId = sharedPreferences.getString("PERSON_ID", "");
        return personId;
    }

    public void setPersonId(String personId) {
        editor.putString("PERSON_ID", personId);
        editor.commit();
        this.personId = personId;
    }

    public String getEmail() {
        email = sharedPreferences.getString("EMAIL", "");
        return email;
    }

    public void setEmail(String email) {
        editor.putString("EMAIL", email);
        editor.commit();
        this.email = email;
    }

    public String getName() {
        name = sharedPreferences.getString("NAME", "");
        return name;
    }

    public void setName(String name) {
        editor.putString("NAME", name);
        editor.commit();
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        editor.putString("CATEGORY", category);
        editor.commit();
        this.category = category;
    }
}
