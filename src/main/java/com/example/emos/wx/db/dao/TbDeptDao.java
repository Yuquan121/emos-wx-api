package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbDept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;


@Mapper
@Component
public interface TbDeptDao {

    public ArrayList<HashMap> searchDeptMembers(String keyword);

}