package com.auto_lab.auto_hub.timetable_feature;

import androidx.annotation.NonNull;

public class Module {
    private String moduleCode;
    private String sessionType; //eg lecture, prac, tutorial
    private String sessionTime;
    private String sessionVenue;

    public Module() {

    }

    public Module(String moduleCode, String sessionType, String sessionTime, String sessionVenue) {
        this.sessionType = sessionType;
        this.sessionTime = sessionTime;
        this.sessionVenue = sessionVenue;
        this.moduleCode = moduleCode;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getSessionVenue() {
        return sessionVenue;
    }

    public void setSessionVenue(String sessionVenue) {
        this.sessionVenue = sessionVenue;
    }

    @NonNull
    @Override
    public String toString() {
        return moduleCode+";"+sessionType+";"+sessionTime+";"+sessionVenue;
    }
}
