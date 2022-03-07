//package com.softeng.dingtalk.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
///**
// * @author zhanyeye
// * @description 每周绩效申请中的AC值申请  （一个 DcRecord 可能有多个 taskItem: 一个申请可能包含多个task申请）
// * @date 12/5/2019
// */
//@Getter
//@Setter
//@Entity
//@NoArgsConstructor
//@ToString
//@Deprecated
//public class TaskItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//
//    private String userId;
//
//    private String describe;
//
//    private LocalDate beginTime;
//
//    private LocalDate endTime;
//
//    private boolean status;
//
//    /**
//     * task申请属于的周绩效申请
//     * 设置many端对one端延时加载，仅需要其ID
//     */
//    @JsonIgnore
//    @JsonIgnoreProperties("taskItems")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private DcRecord dcRecord;
//    @JsonIgnore
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private TaskRecord taskRecord;
//
//    public TaskItem(String reason, double task) {
//        this.reason = reason;
//        this.task = task;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
