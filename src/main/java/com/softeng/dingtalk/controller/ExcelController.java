package com.softeng.dingtalk.controller;

import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.softeng.dingtalk.kit.ExportFileUtil;
import com.softeng.dingtalk.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @PostMapping("/excel/ac_data")
    public void downloadAcData(@RequestBody LocalDate date, HttpServletResponse response) throws IOException {
        excelService.writeAcDataByDate(date, response.getOutputStream());
    }

    /**
     * 审核人打印已审核申请
     * @param uid
     * @param yearmonth
     * @return
     */

    @PostMapping("/excel/print")
    public void printExcel(@RequestParam("uid") int uid ,@RequestParam("yearmonth") int  yearmonth, HttpServletResponse response) throws IOException {
//    public void printExcel(@RequestAttribute int uid,@RequestAttribute int yearmonth, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info(String.valueOf(uid));
        log.info(String.valueOf(yearmonth));
//        log.info(request.getParameterNames().toString());

        String fileName = excelService.excelFill(1,202203,response);

//        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile(fileName)));
//        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());


    }

    @RequestMapping("/excel/look")
    public void toHtmlOf07Base(HttpServletResponse response) throws IOException {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile("C://Users//38106//Desktop//test.xlsx")));
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());

    }


    @PostMapping("/excel/dc_summary_data")
    public void downloadDcSummaryData(@RequestBody LocalDate date, HttpServletResponse response) throws IOException {
        excelService.writeDcSummaryByDate(date, response.getOutputStream());
    }

    /**
     * pdf 预览公共
     *
     * @param response
     * @param fileUrl
     */
    @GetMapping("/excel/readPdfFile")
    public void previewPdf(HttpServletResponse response , String fileUrl){
        log.info(fileUrl);
        ExportFileUtil.preview(response, fileUrl);
    }
}
