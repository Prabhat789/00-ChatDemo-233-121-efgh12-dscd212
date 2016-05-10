package com.pktworld.chatdemo.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Prabhat on 10/05/16.
 */

@ParseClassName("Group")
public class Group extends ParseObject{

    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public String getGroupName() {
        return getString("groupName");
    }

    public void setGroupName(String userName) {
        put("groupName", userName);
    }
    public String getDateTime() {
        return getString("dateTime");
    }

    public void setDateTime(String dateTime) {
        put("dateTime", dateTime);
    }
}
