package com.softeng.dingtalk.vo;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 07/07/2020 11:29 AM
 */
@Data
public class AcItemVO {
    private double ac;
    private String reason;
    private String beginDate;
    private String endDate;
    private String comTimEva;
    private String resDifEva;
    private String resQuaEva;

}
