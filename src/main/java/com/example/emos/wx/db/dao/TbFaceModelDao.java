package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbFaceModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TbFaceModelDao {

    String searchFaceModel(int userId);

    void insert(TbFaceModel faceModel);

    int deleteFaceModel(int userId);

}