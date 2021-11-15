package com.example.emos.wx.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.wx.db.dao.TbDeptDao;
import com.example.emos.wx.db.dao.TbUserDao;
import com.example.emos.wx.db.pojo.MessageEntity;
import com.example.emos.wx.db.pojo.TbDept;
import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.UserService;
import com.example.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description:
 * @author: lyy
 */

@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {
    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Autowired
    private TbUserDao userDao;

    @Autowired
    private MessageTask messageTask;

    @Autowired
    private TbDeptDao deptDao;

    /**
     * 获取openID
     *
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("临时登录异常");
        }
        return openId;
    }

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> searchUserPermissions(int userId) {
        return userDao.searchUserPermissions(userId);
    }

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    @Override
    public TbUser searchById(int userId) {
        return userDao.searchById(userId);
    }

    /**
     * 查询员工入职日期
     *
     * @param userId
     * @return
     */
    @Override
    public String searchUserHiredate(int userId) {
        return userDao.searchUserHiredate(userId);
    }

    /**
     * 查询员工概要信息
     *
     * @param userId
     * @return
     */
    @Override
    public HashMap searchUserSummary(int userId) {
        return userDao.searchUserSummary(userId);
    }


    @Override
    public ArrayList<HashMap> searchUserGroupByDept(String keyword) {
        ArrayList<HashMap> list_1 = deptDao.searchDeptMembers(keyword);
        ArrayList<HashMap> list_2 = userDao.searchUserGroupByDept(keyword);
        for (HashMap map_1 : list_1) {
            long deptId = (Long) map_1.get("id");
            ArrayList members = new ArrayList();
            for (HashMap map_2 : list_2) {
                long id = (Long) map_2.get("deptId");
                if (deptId == id) {
                    members.add(map_2);
                }
            }
            map_1.put("members", members);
        }
        return list_1;
    }

    @Override
    public ArrayList<HashMap> searchMembers(List param) {
        ArrayList<HashMap> list = userDao.searchMembers(param);
        return list;
    }

    @Override
    public HashMap searchUserInfo(int userId) {
        return null;
    }

    @Override
    public int searchDeptManagerId(int id) {
        return 0;
    }

    @Override
    public int searchGmId() {
        return 0;
    }

    @Override
    public List<HashMap> selectUserPhotoAndName(List param) {
        List<HashMap> list=userDao.selectUserPhotoAndName(param);
        return list;
    }

    @Override
    public String searchMemberEmail(int id) {
        String email=userDao.searchMemberEmail(id);
        return email;
    }

    /**
     * 注册用户
     *
     * @param registerCode
     * @param code
     * @param nickname
     * @param photo
     * @return
     */
    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        //如果邀请码是000000，代表是超级管理员
        if (registerCode.equals("000000")) {
            //查询超级管理员帐户是否已经绑定
            boolean bool = userDao.haveRootUser();
            if (!bool) {
                //把当前用户绑定到ROOT帐户
                String openId = getOpenId(code);
                HashMap param = new HashMap();
                param.put("openId", openId);
                param.put("nickname", nickname);
                param.put("photo", photo);
                param.put("role", "[0]");
                param.put("status", 1);
                param.put("createTime", new Date());
                param.put("root", true);
                userDao.insert(param);
                int id = userDao.searchIdByOpenId(openId);

                MessageEntity entity = new MessageEntity();
                entity.setSenderId(0);
                entity.setSenderName("系统消息");
                entity.setUuid(IdUtil.simpleUUID());
                entity.setMsg("欢迎您注册成为超级管理员，请及时完善您员工个人信息");
                entity.setSendTime(new Date());
                messageTask.sendAsync(id + "", entity);
                return id;
            } else {
                //如果root已经绑定了，就抛出异常
                throw new EmosException("无法绑定超级管理员账号");
            }
        }
        //TODO 此处还有其他判断内容
        else {
            return 0;
        }
    }


    /**
     * 用户登录
     *
     * @param code
     * @return
     */
    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = userDao.searchIdByOpenId(openId);
        if (id == null) {
            throw new EmosException("用户不存在");
        }
        //从消息队列中接收信息，转移到消息表
        messageTask.receiveAsync(id + "");
        return id;
    }
}
