package com.softeng.dingtalk.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zhanyeye
 * @description 职位
 * @create 5/28/2020 8:20 AM
 */

public enum Position {
    DOCTOR("老板"),
    POSTGRADUATE("经理"),
    UNDERGRADUATE("员工"),
    OTHER("待定");

    private String title;

    private Position(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

}
