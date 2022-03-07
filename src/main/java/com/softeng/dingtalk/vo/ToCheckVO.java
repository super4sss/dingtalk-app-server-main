package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 审核人获取待审核申请数据
 * @create 1/19/2020 8:33 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class ToCheckVO {
    private int id;
    private int uid;
    private String name;
    /**
     * Dedication Value
     */
    private double dvalue;
    /**
     * 表示申请所属 年、月
     */
    private int yearmonth;
    /**
     * 申请所属周
     */
    private int week;
    private LocalDateTime insertTime;
    List<AcItem> acItems;
    List<EvaVO> evaItems;
    private LocalDate weekdate;
    private String loadEva;
    private String loadEvaNum;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String obeEva;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String iniEva;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String teamEva;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String atteEva;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String clotEva;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String repEva;
    //    @Column(columnDefinition="DECIMAL(10,3)")
    private String perfEva;
    private String perfEvaNum;

    public ToCheckVO(
            int id, int uid, String name, double dvalue, int yearmonth, int week, LocalDateTime insertTime, LocalDate weekdate
//            ,String loadEva,String obeEva,String iniEva,String teamEva,String atteEva,String clotEva,String repEva,String perfEva
    ) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.dvalue = dvalue;
        this.yearmonth = yearmonth;
        this.week = week;
        this.insertTime = insertTime;
        this.weekdate = weekdate;
//        this.loadEva = loadEva;
//        this.obeEva = obeEva;
//        this.iniEva = iniEva;
//        this.teamEva = teamEva;
//        this.atteEva = atteEva;
//        this.clotEva = clotEva;
//        this.repEva = repEva;
//        this.perfEva = perfEva;
    }
    public ToCheckVO(
            int id, int uid, String name, double dvalue, int yearmonth, int week, LocalDateTime insertTime, LocalDate weekdate
            ,String loadEva,String loadEvaNum,String obeEva,String iniEva,String teamEva,String atteEva,String clotEva,String repEva,String perfEva,String perfEvaNum
    ) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.dvalue = dvalue;
        this.yearmonth = yearmonth;
        this.week = week;
        this.insertTime = insertTime;
        this.weekdate = weekdate;
        this.loadEva = loadEva;
        this.loadEva = loadEvaNum;
        this.obeEva = obeEva;
        this.iniEva = iniEva;
        this.teamEva = teamEva;
        this.atteEva = atteEva;
        this.clotEva = clotEva;
        this.repEva = repEva;
        this.perfEva = perfEva;
        this.perfEva = perfEvaNum;
    }
}
