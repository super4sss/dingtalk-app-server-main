package com.softeng.dingtalk.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.excel.AcData;
import com.softeng.dingtalk.excel.DcSummaryData;
import com.softeng.dingtalk.kit.ObjKit;
import com.softeng.dingtalk.mapper.AcRecordMapper;
import com.softeng.dingtalk.mapper.DcSummaryMapper;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.EvaExcelVO;
import com.softeng.dingtalk.vo.TestFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class ExcelService {

    @Autowired
    AcRecordMapper acRecordMapper;
    @Autowired
    DcSummaryMapper dcSummaryMapper;
    @Autowired
    DcRecordRepository dcRecordRepository;

    /**
     * 根据日期，下载指定月份所有同学AC变化的情况
     * @param date 需要下载的日期
     * @param outputStream response的输出流
     */
    public void writeAcDataByDate(LocalDate date, OutputStream outputStream) {
        EasyExcel.write(outputStream, AcData.class)
                .sheet(date.toString().substring(0, 7))
                .doWrite(acRecordMapper.listAcDataByYearMonth(date.getYear(), date.getMonth()));
    }


    /**
     * 根据日期，下载指定月份所有同学绩效情况的情况
     * @param date 需要下载的日期
     * @param outputStream response的输出流
     */
    public void writeDcSummaryByDate(LocalDate date, OutputStream outputStream) {
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        EasyExcel.write(outputStream, DcSummaryData.class)
                .sheet(date.toString().substring(0, 7))
                .doWrite(dcSummaryMapper.listDcSummaryDataByYearMonth(yearmonth));
    }



    /**
     * 根据模板excel，填充指定月份指定员工绩效报表
     *
     */
    @Autowired
    UserService userService;
    public String excelFill(int uid,int yearmonth) {
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // {} 代表普通变量 {.} 代表是list的变量
//        String templateFileName =
//                TestFileUtil.getPath() + "demo" + File.separator + "fill" + File.separator + "complex.xlsx";
        EvaExcelVO evaExcelVO = new EvaExcelVO();
        String position = userService.getUserDetail(uid).getPosition().getTitle();
        evaExcelVO =dcRecordRepository.listEvaExcelVO(uid,yearmonth).get(0);
log.info(evaExcelVO.toString());
        if (ObjKit.empty(evaExcelVO)){
           return null;
        };
        evaExcelVO.setPosition(position);




        Map<String, Object> map = new HashMap();
//
//
//
        map.put("month","2");
//        map.put("username",evaExcelVO.getName());
//        map.put("position",evaExcelVO.getPosition());
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
        String templateFileName =
//                TestFileUtil.getPath()+"绩效评价标准-员工层 - 模板.xlsx";
        TestFileUtil.getPath()+"test.xlsx";

        String fileName = TestFileUtil.getPath() + "complexFill" + System.currentTimeMillis() + ".xlsx";

        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
        // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
        // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
        // 如果数据量大 list不是最后一行 参照下一个
//        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
//        excelWriter.fill(evaExcelVO.getAcItems(), fillConfig, writeSheet);
//        excelWriter.fill(data(), fillConfig, writeSheet);

        ObjectMapper mapper = new ObjectMapper();

        try {
//            Map m = mapper.readValue(mapper.writeValueAsString(evaExcelVO), HashMap.class);
//            log.info(m.toString());
            excelWriter.fill(map, writeSheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
//try {

log.info("111");
//}catch (Exception e){
//    e.printStackTrace();
//}
//        excelWriter.fill(evaExcelVO, writeSheet);
//        excelWriter.finish();
//        EasyExcel.write(fileName).withTemplate(templateFileName).sheet(3).toString();
        return  "success";
    }




}
