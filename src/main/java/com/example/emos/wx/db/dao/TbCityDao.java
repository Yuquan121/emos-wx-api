package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbCity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TbCityDao {

    String searchCode(String city);

}