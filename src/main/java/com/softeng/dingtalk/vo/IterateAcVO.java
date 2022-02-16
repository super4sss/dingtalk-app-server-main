package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.IterationDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/19/2020 5:22 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class IterateAcVO {
    private LocalDate finishdate;
    private List<IterationDetail> iterationDetails;
}
