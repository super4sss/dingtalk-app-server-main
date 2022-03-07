package com.softeng.dingtalk.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


/**
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class TaskVO {

    private String taskId;

    private String describe;

    private LocalDate beginTime;

    private LocalDate endTime;

    private boolean status;


}
