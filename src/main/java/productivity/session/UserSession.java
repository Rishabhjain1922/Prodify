package main.java.productivity.model;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String password;  // Add password field

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }  // New getter

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void clear() {
        this.username = null;
        this.password = null;
    }
}