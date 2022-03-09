package com.softeng.dingtalk.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.excel.AcData;
import com.softeng.dingtalk.excel.DcSummaryData;
import com.softeng.dingtalk.mapper.AcRecordMapper;
import com.softeng.dingtalk.mapper.DcSummaryMapper;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.vo.EvaExcelVO;
import com.softeng.dingtalk.vo.TestFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    AcItemRepository acItemRepository ;

    @Autowired
    UserService userService;
    public String excelFill(int uid,int yearmonth) {
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // {} 代表普通变量 {.} 代表是list的变量
//        String templateFileName =
//                TestFileUtil.getPath() + "demo" + File.separator + "fill" + File.separator + "complex.xlsx";

        log.info(String.valueOf(uid));
        String position = userService.getUserDetail(uid).getPosition().getTitle();
        log.info(position);





        EvaExcelVO evaExcelVO = dcRecordRepository.listEvaExcelVO(uid,yearmonth).get(0);
//        ToCheckVO toCheckVO = dcRecordRepository.listToCheckVO(uid).get(0);
        log.info("1");
//        evaExcelVO.setAcItems(acItemRepository.findAllByDcRecordID(evaExcelVO.getId()));
        log.info("2");
        List<AcItem> acItems= acItemRepository.findAllByDcRecordID(evaExcelVO.getId());

//log.info(acItemRepository.findAllByDcRecordID(38).toString());

        evaExcelVO.setPosition(position);


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
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
//        excelWriter.fill(evaExcelVO.getAcItems(), fillConfig, writeSheet);
        ObjectMapper oMapper = new ObjectMapper();
        //evo转map
        Map<String, Object> map = oMapper.convertValue(evaExcelVO, Map.class);
        List<Map> acItemList=new ArrayList<>();
        acItems.forEach(acItem -> acItemList.add(oMapper.convertValue(acItem,Map.class)));
//测试速度
//        Map map =new HashMap();
//        map.put("loadEva","1");
//        map.put("obeEva","2");
//        map.put("iniEva","3");
//        map.put("teamEva","4");
        log.info(map.toString());
        log.info(acItemList.toString());



//        excelWriter.fill(acItemList, fillConfig, writeSheet);

            excelWriter.fill(map, writeSheet);



//        try {
//            String pdfFileName =TestFileUtil.getPath() + "complexFill" + System.currentTimeMillis() + ".pdf";
////            FileKit.excel2PDF(fileName,pdfFileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

log.info("111");
        return  "success";
    }




}
