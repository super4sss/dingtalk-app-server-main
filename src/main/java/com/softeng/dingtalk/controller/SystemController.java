package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.PaperLevel;
import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.SystemService;
import com.softeng.dingtalk.vo.QueryUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 5/27/2020 4:49 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class SystemController {
    @Autowired
    SystemService systemService;


    /**
     * 根据条件分页筛选用户
     * @param vo
     * @param page
     * @return
     */
    @PostMapping("/system/user/{page}")
    public Map queryUser(@RequestBody QueryUserVO vo, @PathVariable int page) {
        Page<User> pages = systemService.multiQueryUser(page, 10, vo.getName(), vo.getPosition());
        return Map.of("content", pages.getContent(), "total", pages.getTotalElements());
    }


    /**
     * 从钉钉同步用户
     */
    @GetMapping("/system/fetchuser")
    public void fetchUser() {
        systemService.fetchUsers();
    }


    /**
     * 查询所有的绩效标准
     * @return List<SubsidyLevel>
     */
    @GetMapping("/system/subsidy")
    public List<SubsidyLevel> listSubsidy() {
        return systemService.listSubsidy();
    }


    /**
     * 更新绩效标准
     * @param subsidyLevels
     */
    @PostMapping("/system/subsidy")
    public void updateSubsidy(@RequestBody List<SubsidyLevel> subsidyLevels) {
        systemService.setSubsidy(subsidyLevels);
    }


    /**
     * 查询所有的论文AC标准
     * @return
     */
    @GetMapping("/system/paperlevel")
    public List<PaperLevel> listPaperLevel() {
        return systemService.listPaperLevel();
    }


    /**
     * 更新论文绩效标准
     * @param paperLevels
     */
    @PostMapping("/system/paperlevel")
    public void updatePaperLevel(@RequestBody List<PaperLevel> paperLevels) {
        systemService.updatePaperLevel(paperLevels);
    }


    /**
     * 更新用户信息
     * @param user
     */
    @PostMapping("/system/userinfo")
    public void updateUserInfo(@RequestBody User user) {
        systemService.updateUserInfo(user);
    }


    /**
     * 禁用用户
     * @param uid
     */
    @GetMapping("/system/disable/user/{uid}")
    public void disableUser(@PathVariable int uid) {

        systemService.disableUser(uid);
    }


    /**
     * 取消禁用用户
     * @param uid
     */
    @GetMapping("/system/enable/user/{uid}")
    public void enableUser(@PathVariable int uid) {
        systemService.enableUser(uid);
    }


    /**
     * 查询禁用的用户
     * @return
     */
    @GetMapping("/system/query/disableuser")
    public List<User> queryDisableUser() {
        return systemService.queryDisableUser();
    }

    /**
     * 手动更新指定月份的 DcSummary
     * @param yearmonth 要更新绩效的月份
     */
    @GetMapping("/system/updateperf/{yearmonth}")
    public void manulUpdatePerformance(@PathVariable int yearmonth) {
        systemService.manulUpdatePerformance(yearmonth);
    }
}
