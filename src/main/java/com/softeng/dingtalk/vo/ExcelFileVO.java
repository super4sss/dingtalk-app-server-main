package com.softeng.dingtalk.vo;


import lombok.*;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor  // 序列化用
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ExcelFileVO {
    private String department;
    private File[] files;
}
