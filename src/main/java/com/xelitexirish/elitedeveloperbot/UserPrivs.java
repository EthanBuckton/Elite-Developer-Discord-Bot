package com.xelitexirish.elitedeveloperbot;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JSONReader;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UserPrivs {

    private static ArrayList<User> adminUsers = new ArrayList<>();

    public static void setupUsers() {
        addDefaultUsers();
    }

    public static boolean isUserAdmin(User user) {
        if (adminUsers.contains(user)) {
            return true;
        } else {
            for (Role role : user.getJDA().getGuildById(Constants.DISCORD_SERVER_ID).getMember(user).getRoles()){
                if (role.getId().equalsIgnoreCase(Constants.ROLE_ADMIN_ID)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUserMod(User user){
        for (Role role : user.getJDA().getGuildById(Constants.DISCORD_SERVER_ID).getMember(user).getRoles()){
            if (role.getId().equalsIgnoreCase(Constants.ROLE_MOD_ID)){
                return true;
            }
        }
        return false;
    }

    public static boolean isUserStaff(User user) {
        for (Role role : user.getJDA().getGuildById(Constants.DISCORD_SERVER_ID).getMember(user).getRoles()) {
            if(role.getId().equalsIgnoreCase(Constants.ROLE_STAFF_ID)) {
                return true;
            }
        }
        return false;
    }

    public static void addDefaultUsers() {

        try {
            JSONObject jsonObject = JSONReader.readJsonFromUrl(Constants.ADMIN_USERS_URL);
            JSONArray jsonArray = jsonObject.getJSONArray("adminUsers");
            if (jsonArray != null) {
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(x);

                    String id = String.valueOf(jsonItem.get("id"));

                    adminUsers.add(Main.jda.getUserById(id));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(User user, Permission permission) {
        for (Role role : user.getJDA().getGuildById(Constants.DISCORD_SERVER_ID).getMember(user).getRoles()) {
            return role.hasPermission(permission);
        }
        return false;
    }

    public static ArrayList<User> getAllStaff() {
        ArrayList<User> allStaff = new ArrayList<>();
        for (Member user : Main.jda.getGuildById(Constants.DISCORD_SERVER_ID).getMembers()) {
            if (isUserStaff(user.getUser())) {
                allStaff.add(user.getUser());
            }
        }
        return allStaff;
    }
}
