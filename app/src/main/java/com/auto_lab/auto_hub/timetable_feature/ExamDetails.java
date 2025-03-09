package com.auto_lab.auto_hub.timetable_feature;

import androidx.annotation.NonNull;

public class ExamDetails {
    private String moduleCode;
    private String time;
    private String venue;
    private String duration;
    private String description;
    private String paper;
    private String date;

    // Default Constructor
    public ExamDetails() {
    }

    // Parameterized Constructor
    public ExamDetails(String moduleCode, String description,String paper,String date,String time, String duration,String venue) {
        this.moduleCode = moduleCode;
        this.time = time;
        this.venue = venue;
        this.duration = duration;
        this.description = description;
        this.paper = paper;
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    // Getters and Setters
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    @NonNull
    @Override
    //String moduleCode, String description,String paper,String date,String time, String duration,String venue
    public String toString() {
        return moduleCode + ";" + description + ";" + paper + ";" + date + ";" + time + ";" + duration +";"+ venue;
    }
}

