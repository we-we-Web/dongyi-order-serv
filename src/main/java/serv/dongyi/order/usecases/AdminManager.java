package serv.dongyi.order.usecases;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;
import java.util.List;

import java.util.Arrays;
import java.util.List;
import io.github.cdimascio.dotenv.Dotenv;

public class AdminManager {
    private static AdminManager instance;
    private List<String> admins;

    private AdminManager() {
        try {
            Dotenv dotenv = Dotenv.load();
            String adminsEnv = dotenv.get("ADMINS");

            if (adminsEnv != null && !adminsEnv.isEmpty()) {
                admins = Arrays.asList(adminsEnv.split(","));
            } else {
                admins = List.of();
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load .env file. Using default admin list.");
            admins = List.of();
        }
    }

    public static synchronized AdminManager getInstance() {
        if (instance == null) {
            instance = new AdminManager();
        }
        return instance;
    }

    public boolean isAdmin(String adminId) {
        return admins.contains(adminId);
    }
}
