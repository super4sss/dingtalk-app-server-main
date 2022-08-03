package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSON;
import com.softeng.dingtalk.api.ReportApi;
import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.ExcelService1;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.ExcelFileVO;
import com.softeng.dingtalk.vo.ExcelVO1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api")
public class YidaController {

    @Autowired
    AuditService auditService;
    @Autowired
    UserService userService;
    @Autowired
    DateUtils dateUtils;
    @Autowired
    NotifyService notifyService;

    @Autowired
    ExcelService1 excelService1;

    /**
     * 查询所有审核人
     * @return
     */
    @GetMapping("/yida/insert")
    public String getAuditors() throws Exception {


        List<ExcelFileVO> excelFileVOList = excelService1.traverseFile("E://月度绩效考核评分");
        List<ExcelVO1> excelVO1List1 = new ArrayList<ExcelVO1>();
        for (ExcelFileVO excelFileVO : excelFileVOList) {
            String department = excelFileVO.getDepartment();
            for (File file : excelFileVO.getFiles()) {
                String path = file.getPath();
                System.out.println(path);
                List<ExcelVO1> excelVO1List = excelService1.createExcelVO1ByFileName(path, department);
                excelVO1List1.addAll(excelVO1List);

            }
        }
        String json = JSON.toJSONString(excelVO1List1);
//        excelService1.yidaImport(json);
        return "222";

    }
}
