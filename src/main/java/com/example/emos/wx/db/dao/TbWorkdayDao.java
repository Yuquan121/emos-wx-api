package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbWorkday;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Component
public interface TbWorkdayDao {

    /**
     *  查询是否为特殊工作日
     * @return 行
     */
     Integer searchTodayIsWorkdate();

     ArrayList<String> searchWorkdayInRange(HashMap params);


}