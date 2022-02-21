package com.softeng.dingtalk.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.softeng.dingtalk.entity.DcSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ContentStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER,
        fillForegroundColor = 42, bottomBorderColor = 42, leftBorderColor = 42, rightBorderColor = 42)
@HeadFontStyle(fontHeightInPoints = 11)
public class DcSummaryData {
    @ColumnWidth(15)
    @ExcelProperty("工号")
    private String stuNum;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("助研金")
    private double salary;
    @ExcelProperty("第一周DC")
    private double week1;
    @ExcelProperty("第二周DC")
    private double week2;
    @ExcelProperty("第三周DC")
    private double week3;
    @ExcelProperty("第四周DC")
    private double week4;
    @ExcelProperty("第五周DC")
    private double week5;
    @ExcelProperty("总DC")
    private double total;
    @ExcelProperty("当前AC")
    private double ac;
    @ExcelProperty("Top-Up")
    private double topUp;

    public DcSummaryData(String stuNum, String name, DcSummary dcSummary) {
        this.stuNum = stuNum;
        this.name = name;
        this.salary = dcSummary.getSalary();
        this.week1 = dcSummary.getWeek1();
        this.week2 = dcSummary.getWeek2();
        this.week3 = dcSummary.getWeek3();
        this.week4 = dcSummary.getWeek4();
        this.week5 = dcSummary.getWeek5();
        this.total = dcSummary.getTotal();
        this.ac = dcSummary.getAc();
        this.topUp = dcSummary.getTopup();
    }

}
