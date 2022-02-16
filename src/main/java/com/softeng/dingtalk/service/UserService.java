package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.BaseApi;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.UserInfoVO;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @author zhanyeye
 * @description User业务逻辑组件
 * @date 12/7/2019
 */
@Service
@Transactional
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcRecordRepository acRecordRepository;

    @Autowired
    BaseApi baseApi;

    /**
     * 判断用户权限是否为审核人
     * @param uid 用户id
     * @return
     */
    public boolean isAuditor(int uid) {
        return userRepository.getUserAuthority(uid) == User.AUDITOR_AUTHORITY;
    }


    /**
     * 查询系统可用用户
     * @return
     */
    public List<UserVO> listUserVO() {
        return userRepository.listUserVOS();
    }


    /**
     * 获取用户的userID （获取周报）
     * @param id
     * @return
     */
    public String getUserid(int id) {
        return userRepository.findById(id).get().getUserid();
    }


    /**
     * 通过useID获取用户 -> 通过用户进入系统时调用API获得的userID查询用户，判断用户是否在系统中，还是新用户
     * @param userid
     * @return
     */
    public User getUser(String userid) {
        return userRepository.findByUserid(userid);
    }


    /**
     * 查询所有的审核员 -> 供用户提交审核申请时选择
     * @return
     */
    public Map getAuditorUser() {
        return Map.of("auditorlist", userRepository.listAuditor());
    }


    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    public Map getUserInfo(int uid) {
        User u = userRepository.findById(uid).get();
        double ac = acRecordRepository.getUserAcSum(uid);
        baseApi.getJsapiTicket(); // 提前拿到jsapi ticket，避免需要时再去那减少时延
        if (u.getAvatar() != null) {
            return Map.of("name", u.getName(), "avatar", u.getAvatar(), "ac", ac, "userid", u.getUserid());
        } else {
            return Map.of("name", u.getName(), "ac", ac, "userid", u.getUserid());
        }
    }


    /**
     * 更新用户权限
     * @param uid
     * @param authority
     */
    public void updateRole(int uid, int authority) {
        userRepository.updateUserRole(uid, authority);
    }


    /**
     * 查询用户详情
     * @param uid
     * @return
     */
    public UserInfoVO getUserDetail(int uid) {
        User u = userRepository.findById(uid).get();
        return new UserInfoVO(u.getName(), u.getAvatar(), u.getPosition(), u.getStuNum());
    }


    public void updateUserInfo(UserInfoVO userInfoVO, int uid) {
        User u = userRepository.findById(uid).get();
        u.setStuNum(userInfoVO.getStuNum());
        u.setName(userInfoVO.getName());
        userRepository.save(u);
    }


}
