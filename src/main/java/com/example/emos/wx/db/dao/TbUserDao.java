package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Mapper
@Component
public interface TbUserDao {

    /**
     * 查询是否存在超级管理员
     * @return true or false
     */
    public boolean haveRootUser();

    /**
     *  插入员工数据
     * @param params 员工信息
     * @return 改变行数
     */
    public int insert(HashMap params);

    /**
     * 查询用户id
     * @param openId
     *
     */
    public Integer searchIdByOpenId(String openId);

    /**
     * 查询用户信息
     * @param userId 用户id
     * @return 用户对象
     */
    public TbUser searchById(int userId);


    /**
     * 查询用户权限
     * @param userId
     *
     */
    public Set<String> searchUserPermissions(int userId);

    /**
     *查询员工的姓名和部门名称
     */
    public HashMap searchNameAndDept(int userId);

    /**
     * 查询员工入职日期
     * @param userId
     * @return
     */
    String searchUserHiredate(int userId);

    HashMap searchUserSummary(int userId);

    ArrayList<HashMap> searchUserGroupByDept(String keyword);

    ArrayList<HashMap> searchMembers(List param);

    HashMap searchUserInfo(int userId);

    int searchDeptManagerId(int id);

    int searchGmId();

    List<HashMap> selectUserPhotoAndName(List param);

    String searchMemberEmail(int id);
}