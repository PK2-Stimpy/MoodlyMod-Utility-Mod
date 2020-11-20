package us.np.moodlymod.util;

import org.json.JSONArray;

import java.util.ArrayList;

public class FriendsUtil {
    public ConfigUtils configUtils;
    public JSONArray friends;

    public FriendsUtil() {
        configUtils = new ConfigUtils("friends", "");
        if(configUtils.get("friends") == null) {
            configUtils.set("friends", new JSONArray().put("a"));
            configUtils.save();
        }
        friends = configUtils.getJSON().getJSONArray("friends");
    }

    public void save() {
        configUtils.set("friends", friends);
        configUtils.save();
    }

    public void addFriend(String user) { if(!isFriend(user)) friends.put(user); }
    public void remFriend(String user) { if(isFriend(user)) friends.remove(friends.toList().indexOf(user)); }
    public boolean isFriend(String user) { return friends.toList().contains(user); }

}