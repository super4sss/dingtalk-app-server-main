package com.softeng.dingtalk.vo;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor  // 序列化用
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ExcelVO1 {
    //发起审批人
    private Long[] employeeField_l4f605yk;
    //发起审批人
    private Long[] employeeField_l3vcbbkd;
    //月份
    private Long dateField_l4nwl6av;
    //综合评价系数
    private double numberField_l1x027hj;
    //工作业绩综合系数
    private double numberField_l1od4dy8;
    //工作态度
    private double numberField_l1x027hl;
   //日常行为
    private String selectField_l1zrhsb9;
   //周报质量
    private double numberField_l1yqul9o;

}
