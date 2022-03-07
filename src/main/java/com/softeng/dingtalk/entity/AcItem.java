package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 每周绩效申请中的AC值申请  （一个 DcRecord 可能有多个 acItem: 一个申请可能包含多个ac申请）
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Deprecated
/**
 * 任务
 */
public class AcItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    //任务描述
    private String reason;
    private boolean status;
    @Column
    private String beginDate;
    private String endDate;
    @Column
    private String comTimEva;
    private String resDifEva;
    private String resQuaEva;


    /**
     * ac申请属于的周绩效申请
     * 设置many端对one端延时加载，仅需要其ID
     */
    @JsonIgnore
    @JsonIgnoreProperties("acItems")
    @ManyToOne(fetch = FetchType.LAZY)
    private DcRecord dcRecord;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

    public AcItem(String reason, double ac) {
        this.reason = reason;
        this.ac = ac;
    }
}
