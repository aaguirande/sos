package tk.meceap.sos.models;

public class Agent {
    String token, userId, personId, email, name, category;

    public Agent(String token, String userId, String personId, String email, String name, String category) {
        this.token = token;
        this.userId = userId;
        this.personId = personId;
        this.email = email;
        this.name = name;
        this.category = category;
    }

    public Agent() {

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "\nAgent{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", personId='" + personId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

}
