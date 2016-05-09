package com.pktworld.chatdemo.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public String getUserName() {
        return getString("userName");
    }

    public void setUserName(String userName) {
        put("userName", userName);
    }

    public String getBody() {
        return getString("body");
    }

    public void setBody(String body) {
        put("body", body);
    }

    public String getDateTime() {
        return getString("dateTime");
    }

    public void setDateTime(String dateTime) {
        put("dateTime", dateTime);
    }

    public String getProfileImage() {
        return getString("profileImage");
    }

    public void setProfileImage(String profileImage) {
        put("profileImage", profileImage);
    }
    
    public void setfriendobjectId(String frndobjectId) {
        put("identificationKey", frndobjectId);  
    }
    public String getgetfriendobjectId() {
        return getString("identificationKey");
    }
}