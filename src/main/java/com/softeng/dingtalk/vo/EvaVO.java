package com.softeng.dingtalk.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@AllArgsConstructor
@Getter
@Setter
public class EvaVO {

//    private static final String loadEvaNam ="工作量";
//    private static final String obeEvaNam ="工作服从性";
//    private static final String iniEvaNam ="工作主动性";
//    private static final String teamEvaNam ="团队互助性";
//    private static final String atteEvaNam ="考勤情况";
//    private static final String clotEvaNam ="穿戴情况";
//    private static final String repEvaNam ="周报质量";
//    private static final String perfEvaNam ="绩效奖励";

    private String evaNam ="";
    private String eva = "";

    public EvaVO(String evaNam, String eva){
        this.evaNam = evaNam;
        this.eva = eva;
    }



}
