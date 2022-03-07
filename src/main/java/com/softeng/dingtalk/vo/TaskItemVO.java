package com.softeng.dingtalk.vo;

import lombok.Data;

@Data
public class TaskItemVO {

    private String describe;
    private String[] taskDate;
    private String beginDate;
    private String endDate;


   public TaskItemVO(String describe,String beginDate,String endDate){
this.describe= describe;
this.beginDate =beginDate;
this.endDate = endDate;
this.taskDate = new String[]{beginDate, endDate};


   }
}
