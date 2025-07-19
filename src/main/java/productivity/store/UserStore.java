package main.java.productivity.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserStore {
    private static final String USER_DATA_FILE = "data/users.json";
    private static Map<String, String> users;
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        loadUsers();
        // Default admin user (only added if file doesn't exist)
        if (users.isEmpty()) {
            users.put("admin", "1234");
            saveUsers();
        }
    }

    private static void loadUsers() {
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                users = mapper.readValue(file, new TypeReference<Map<String, String>>() {});
            } else {
                users = new HashMap<>();
                // Create parent directories if they don't exist
                file.getParentFile().mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
            users = new HashMap<>();
        }
    }

    private static void saveUsers() {
        try {
            mapper.writeValue(new File(USER_DATA_FILE), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String username, String password) {
        users.put(username, password);
        saveUsers();
    }

    public static boolean validate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static boolean userExists(String username) {
        return users.containsKey(username);
    }
}