package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 加载评价excel数据
 * @create 1/28/2020 8:11 PM
 */
@AllArgsConstructor
@NoArgsConstructor  // 序列化用
@Getter
@Setter
@ToString
public class EvaExcelVO {
    /**
     * dcRecord id
     */
    private int id;
    /**
     * 申请人姓名
     */
    private String name;
    /**
     * 申请人职位
     */
    private String position;
    /**
     * 申请人id
     */
//    private int auditorid;
    /**
     * 表示申请所属 年、月
     */
    private int yearmonth;
    private int week;
    /**
     * 审核人id，用于通知消息中，显示审核人
     */
//    private int aid;

    /**
     * ac值申请列表
     */
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




    public EvaExcelVO(int id, String name, String position,int auditorid, double dvalue, double cvalue, double dc, double ac, int yearmonth, int week, LocalDateTime insertTime, LocalDate weekdate,
                      String loadEva,String loadEvaNum,String obeEva,String iniEva,String teamEva,String atteEva,String clotEva,String repEva,String perfEva,String perfEvaNum
    ) {
        this.id = id;
        this.name = name;
        this.position =position;
//        this.auditorid = auditorid;

        this.yearmonth = yearmonth;
        this.week = week;
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
    public EvaExcelVO(int id, String name,int auditorid, double dvalue, double cvalue, double dc, double ac, int yearmonth, int week, LocalDateTime insertTime, LocalDate weekdate,
                      String loadEva,String loadEvaNum,String obeEva,String iniEva,String teamEva,String atteEva,String clotEva,String repEva,String perfEva,String perfEvaNum
    ) {
        this.id = id;
        this.name = name;
//        this.auditorid = auditorid;

        this.yearmonth = yearmonth;
        this.week = week;

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

