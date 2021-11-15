package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbHolidays;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Component
public interface TbHolidaysDao {

    Integer searchTodayIsHoliday();

    ArrayList<String> searchHolidaysInRange(HashMap params);



}