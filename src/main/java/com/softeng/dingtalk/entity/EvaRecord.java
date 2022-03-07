package com.softeng.dingtalk.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class EvaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = true)
    private LocalDateTime createTime;
    @Column
    private String evaNam;
    private String evaOpt;
    private String evaVal;
    private String eva;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;

    /**
     * ac申请属于的周绩效申请
     * 设置many端对one端延时加载，仅需要其ID
     */
    @JsonIgnore
    @JsonIgnoreProperties("acItems")
    @ManyToOne(fetch = FetchType.LAZY)
    private DcRecord dcRecord;

}
