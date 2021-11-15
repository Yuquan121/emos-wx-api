package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbCheckin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Component
public interface TbCheckinDao {
    Integer haveCheckin(HashMap params);

    void insert(TbCheckin entity);

    HashMap searchTodayCheckin(int userId);

    long searchCheckinDays(int userId);

    ArrayList<HashMap> searchWeekCheckin(HashMap params);
}