package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 向前端传送审核人已经审核的申请,向后端传送审核人修改的申请
 * @create 1/28/2020 8:11 PM
 */
@AllArgsConstructor
@NoArgsConstructor  // 序列化用
@Getter
@Setter
@ToString
public class CheckedVO {
    /**
     * dcRecord id
     */
    private int id;
    /**
     * 申请人姓名
     */
    private String name;
    /**
     * 申请人id
     */
    private int uid;
    /**
     * Dedication Value
     */
    private double dvalue;
    /**
     * Contribution Value
     */
    private double cvalue;
    private double dc;
    private double ac;
    /**
     * 表示申请所属 年、月
     */
    private int yearmonth;
    private int week;
    /**
     * 审核人id，用于通知消息中，显示审核人
     */
    private int aid;
    private LocalDateTime insertTime;
    private LocalDate weekdate;
    private List<AcItem> acItems;


    private String loadEva;
    private String loadEvaNum;
    private String obeEva;
    private String iniEva;
    private String teamEva;
    private String atteEva;
    private String clotEva;
    private String repEva;
    private String perfEva;
    private String perfEvaNum;




    public CheckedVO(int id, String name, int uid, double dvalue, double cvalue, double dc, double ac, int yearmonth, int week, LocalDateTime insertTime, LocalDate weekdate,
                     String loadEva,String loadEvaNum,String obeEva,String iniEva,String teamEva,String atteEva,String clotEva,String repEva,String perfEva,String perfEvaNum
    ) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.dvalue = dvalue;
        this.cvalue = cvalue;
        this.dc = dc;
        this.ac = ac;
        this.yearmonth = yearmonth;
        this.week = week;
        this.insertTime = insertTime;
        this.weekdate = weekdate;
        this.loadEva = loadEva;
        this.loadEvaNum = loadEvaNum;
        this.obeEva = obeEva;
        this.iniEva = iniEva;
        this.teamEva = teamEva;
        this.atteEva = atteEva;
        this.clotEva = clotEva;
        this.repEva = repEva;
        this.perfEva = perfEva;
        this.perfEvaNum = perfEvaNum;
    }


}
