package com.example.emos.wx.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @description:
 * @author: lyy
 */

public interface CheckinService {
    public String validCanCheckIn(int userId,String date);

    void checkin(HashMap params);

    void createFaceModel(int userId,String path);

    HashMap searchTodayCheckin(int userId);

    long searchCheckinDays(int userId);

    ArrayList<HashMap> searchWeekCheckin(HashMap param);

    ArrayList<HashMap> searchMonthCheckin(HashMap param);



}
