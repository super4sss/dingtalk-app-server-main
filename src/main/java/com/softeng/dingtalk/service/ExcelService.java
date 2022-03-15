package com.softeng.dingtalk.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;

import cn.hutool.log.StaticLog;
import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zhangchunsheng.excel2pdf.EPFactory;
import com.github.zhangchunsheng.excel2pdf.IExcel2PDF;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;


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

    public String excelFill(int uid, int yearmonth, HttpServletResponse response) throws IOException {
//        ImportParams params = new ImportParams();
//        params.setTitleRows(2);
//        params.setHeadRows(2);
        //params.setSheetNum(9);
//        params.setNeedSave(true);
//        List<EvaExcelVO> list = ExcelImportUtil.importExcel(new File(
//                "C://Users//38106//Desktop//test.xlsx"), EvaExcelVO.class, params);

        TemplateExportParams exportParams = new TemplateExportParams();
//        exportParams.setHeadingRows(2);
//        exportParams.setHeadingStartRow(2);
        Map<String,Object> value = new HashMap<String, Object>();

                EvaExcelVO evaExcelVO = dcRecordRepository.listEvaExcelVO(uid,yearmonth).get(0);
        List<AcItem> acItems= acItemRepository.findAllByDcRecordID(evaExcelVO.getId());
        String position = userService.getUserDetail(uid).getPosition().getTitle();
        evaExcelVO.setPosition(position);

                ObjectMapper oMapper = new ObjectMapper();
        //evo转map
        Map<String, Object> map = oMapper.convertValue(evaExcelVO, Map.class);

        log.info(map.toString());
        List<Map> acItemList=new ArrayList<>();
        acItems.forEach(acItem -> acItemList.add(oMapper.convertValue(acItem,Map.class)));
        log.info(acItemList.get(0).toString());
        map.put("items",acItemList);
//        value.put("map",map);



//        Map<String,Object> obj = new HashMap<String, Object>();
//        map.put("obj", obj);
//        obj.put("name", list.size());
//        exportParams.setTemplateUrl("C://Users//38106//Desktop//test.xlsx");
        exportParams.setTemplateUrl("D://test.xlsx");
        exportParams.setColForEach(true);
        Workbook book = ExcelExportUtil.exportExcel(exportParams,map);
        String fileName =TestFileUtil.getPath() + "complexFill1" + System.currentTimeMillis() + ".xlsx";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName)) ;
        book.write(fileOutputStream);
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
//        inputStream = this.getClass().getResourceAsStream(fileName);
//        String name = UUID.randomUUID().toString().substring(0, 10);
        String name = TestFileUtil.getPath() + "complexFill1" + System.currentTimeMillis()+ ".pdf" ;
//        FileOutputStream outputStream = new FileOutputStream(new File(fileName));
//        com.github.zhangchunsheng.excel2pdf.excel2007.Excel2PDF excel2PDF = new com.github.zhangchunsheng.excel2pdf.excel2007.Excel2PDF(inputStream, outputStream);
//        excel2PDF.convert();
        IExcel2PDF excel2PdfTool = EPFactory.getEP(fileName, name, TestFileUtil.getPath()  + "SimHei.ttf" );
        if(excel2PdfTool != null) {
            excel2PdfTool.convert();
        }

        ServletOutputStream out;
        BufferedInputStream buf;
        try {
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "inline;fileName=" + name.substring(name.lastIndexOf("/") + 1));
            File file = new File(name);
            //使用输入读取缓冲流读取一个文件输入流
            InputStream is = new FileInputStream(file);
            buf = new BufferedInputStream(is);
            //使用输入读取缓冲流读取一个文件输入流
//            buf = new BufferedInputStream(url.openStream());
            //利用response获取一个字节流输出对象
            out = response.getOutputStream();
            //定义个数组，由于读取缓冲流中的内容
            byte[] buffer = new byte[1024];
            int n;
            //while循环一直读取缓冲流中的内容到输出的对象中
            while ((n = buf.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            //写出到请求的地方
            out.flush();
            buf.close();
            out.close();
        } catch (IOException e) {
            StaticLog.error("ExportFileUtil.download() IOException", e);
        } catch (Exception e) {
            StaticLog.error("Exception", e);
        }


        return null;
    }




    }

