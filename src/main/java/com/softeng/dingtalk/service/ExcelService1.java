package com.softeng.dingtalk.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.log.StaticLog;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkyida_1_0.models.BatchSaveFormDataHeaders;
import com.aliyun.dingtalkyida_1_0.models.BatchSaveFormDataRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zhangchunsheng.excel2pdf.EPFactory;
import com.github.zhangchunsheng.excel2pdf.IExcel2PDF;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.excel.AcData;
import com.softeng.dingtalk.excel.DcSummaryData;
import com.softeng.dingtalk.kit.ObjKit;
import com.softeng.dingtalk.kit.TimeExchange;
import com.softeng.dingtalk.mapper.AcRecordMapper;
import com.softeng.dingtalk.mapper.DcSummaryMapper;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.test.Sample;
import com.softeng.dingtalk.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional
@Slf4j
public class ExcelService1 {

    @Autowired
    AcRecordMapper acRecordMapper;
    @Autowired
    DcSummaryMapper dcSummaryMapper;
    @Autowired
    DcRecordRepository dcRecordRepository;
    /**
     * 根据模板excel，填充指定月份指定员工绩效报表
     *
     */
    @Autowired
    AcItemRepository acItemRepository ;

    @Autowired
    UserService userService;

    public void simpleRead() {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        String fileName = "D://月度绩效考核评分//评估一部//绩效评价标准-员工层（张贞）.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        try (ExcelReader excelReader = EasyExcel.read(fileName, EvaExcelVO1.class, new EvaExcelVO1Listener()).build()) {
            // 构建一个sheet 这里可以指定名字或者no
            ReadSheet readSheet = EasyExcel.readSheet(1).build();
            // 读取一个sheet
            excelReader.read(readSheet);
        }
    }

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkyida_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkyida_1_0.Client(config);
    }

    Workbook workbook;



    Sheet sheet;

    //根据单元格查找数据
    public String getCellByRef(String ref)throws Exception{
//        String path = "E://月度绩效考核评分//评估一部//绩效评价标准-员工层（陈佩雯）.xlsx";
//        FileInputStream fileInputStream = new FileInputStream(path);
        //获取工作簿，这里使用的07版
//        Workbook workbook = new XSSFWorkbook(fileInputStream);
//        //获取工作表
//        Sheet sheet = workbook.getSheetAt(1);
        FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);

        String val ="";

        for (Row row : sheet) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                if (cellRef.formatAsString().equals(ref)) {
                    switch (cell.getCellType()) {
                        case STRING:
                            val=cell.getStringCellValue();
                            System.out.println(cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.println(cell.getDateCellValue());
                                val=cell.getDateCellValue().toString();
                            } else {
                                System.out.println(cell.getNumericCellValue());
                                val=String.valueOf(cell.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            System.out.println(cell.getCellFormula());
                            CellValue evaluate = formulaEvaluator.evaluate(cell);
                            System.out.println(evaluate.formatAsString());
                            val=evaluate.formatAsString();
                            break;
//                            switch (cell.getCachedFormulaResultType()){
//                                case NUMERIC:
//                                    if(DateUtil.isCellDateFormatted(cell)){
//                                        Date date = cell.getDateCellValue();
//                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                                        val = sdf.format(date);
//                                    }else{
//                                        BigDecimal n = new BigDecimal(cell.getNumericCellValue());
//                                        DecimalFormat decimalFormat = new DecimalFormat("0");
//                                        decimalFormat.setMaximumFractionDigits(18);
//                                        val = decimalFormat.format(n.doubleValue());
//                                    }
//                                    break;
//                                case STRING:
//                                    val = String.valueOf(cell.getStringCellValue());
//                                    if(val != null){
//                                        val = val.trim();
//                                    }
//                                    break;
//                                case BOOLEAN:
//                                    val = String.valueOf(cell.getBooleanCellValue());
//                                    break;
//                                case ERROR: val = "";
//                                    break;
//                                default:
//                                    val = cell.getRichStringCellValue().getString();
//                                    break;
//                            }
//                            break;
                        case BLANK:
                            System.out.println();
                            break;
                        default:
                            System.out.println();
                    }
                }
            }
        }
        return val;
    }

    //根据文件路径返回ExcelVO1对象列表
    public  List<ExcelVO1> createExcelVO1ByFileName(String path,String department) throws Exception {
//        DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost:3306/dingtalk?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC", "root", "87890315a");
//        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
//        _MappingKit.mapping(arp);
//        // 与 jfinal web 环境唯一的不同是要手动调用一次相关插件的start()方法
//        dp.start();
//        arp.start();

        ExcelService1 excelService1 = new ExcelService1();
        List<ExcelVO1> excelVO1List = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(path);
//        获取工作簿，这里使用的07版
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        //获取工作表
        int sheetNum = 1;
        for (int sheetNO = 1; sheetNO < workbook.getNumberOfSheets()-1; sheetNO++) {
            if (workbook.getSheetName(sheetNO).contains("月")){
                sheetNum++;
            }

        }
        System.out.println(sheetNum+"sheetnum");
        for (int sheetNO = 1; sheetNO < sheetNum; sheetNO++) {
            Sheet sheet = workbook.getSheetAt(sheetNO);
            excelService1.workbook = workbook;
            excelService1.sheet = sheet;
            System.out.println(sheetNO+"sheetno");

            ExcelVO1 ExcelVO1 = new ExcelVO1();

            int i = 7;

            while (i < 100) {
                String ref = "C" + i;
                if ("小计".equals(excelService1.getCellByRef(ref))) {
                    break;
                }
                i++;
            }
            //发起审批人
           log.info(department+"++++");

//            String userid1 = userService.getUserId(department);
            String userid1 = userService.getUserId(department);

                ExcelVO1.setEmployeeField_l4f605yk(new Long[]{Long.valueOf(userid1)});
            //成员
            if(StringUtils.isNotEmpty(excelService1.getCellByRef("B" + 2))) {

                String name = excelService1.getCellByRef("B" + 2);
                System.out.println(name+"----");
//                String userid = userService.getUserId(name);
                String userid = userService.getUserId(name);

                ExcelVO1.setEmployeeField_l3vcbbkd(new Long[]{Long.valueOf(userid)});
            }


            //月份
            String month = excelService1.getCellByRef("B1");

            String time = TimeExchange.timeToDate(month, "yyyy/MM/dd");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            try {
                date = dateFormat.parse(time);
                System.out.println(date.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(date.getTime() + "+++++++++++++++++++++++++++++++");
            // 注意format的格式要与日期String的格式相匹配
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        try {
//            date = dateFormat.parse(time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            ExcelVO1.setDateField_l4nwl6av(date.getTime());
            //工作业绩综合系数
            if(StringUtils.isNotEmpty(excelService1.getCellByRef("Q" + i))&& StringUtils.isNumeric(excelService1.getCellByRef("Q" + i))) {
                ExcelVO1.setNumberField_l1od4dy8(Double.parseDouble(excelService1.getCellByRef("Q" + i)));
            }
            //综合评价系数
            if(StringUtils.isNotEmpty(excelService1.getCellByRef("Q" + (i + 29)))&& StringUtils.isNumeric(excelService1.getCellByRef("Q" + (i+29)))) {
            ExcelVO1.setNumberField_l1x027hj(Double.parseDouble(excelService1.getCellByRef("Q" + (i + 29))));
            }
            //工作态度
                if(!excelService1.getCellByRef("Q" + (i + 13)).isEmpty()&& StringUtils.isNumeric(excelService1.getCellByRef("Q" + (i+13)))) {
            ExcelVO1.setNumberField_l1x027hl(Double.parseDouble(excelService1.getCellByRef("Q" + (i + 13))));
                }
            //周报质量
                    if(!excelService1.getCellByRef("Q" + (i + 21)).isEmpty()&& StringUtils.isNumeric(excelService1.getCellByRef("Q" + (i + 21)))) {
            ExcelVO1.setNumberField_l1yqul9o(Double.parseDouble(excelService1.getCellByRef("Q" + (i + 21))));
                    }
            //日常行为
                        if(!excelService1.getCellByRef("Q" + (i + 17)).isEmpty()&& StringUtils.isNumeric(excelService1.getCellByRef("Q" + (i + 17)))) {
            ExcelVO1.setSelectField_l1zrhsb9(excelService1.getCellByRef("Q" + (i + 17)));
                        }

            excelVO1List.add(ExcelVO1);


        }
        return excelVO1List;
    }

//讲json导入宜搭
    public void yidaImport(String json) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dingtalkyida_1_0.Client client = ExcelService1.createClient();
        BatchSaveFormDataHeaders batchSaveFormDataHeaders = new BatchSaveFormDataHeaders();
        batchSaveFormDataHeaders.xAcsDingtalkAccessToken = "0e55704b7a5e3b46b23bfad67e3d0639";
        BatchSaveFormDataRequest batchSaveFormDataRequest = new BatchSaveFormDataRequest()
                .setSystemToken("VL966A71SXKZ2FKJ0J939BZFPJXB2V1WXTZ1LF6")
                .setFormUuid("FORM-9O666M71JNB1AJPPDMTT98EBENLB2PTHP6F4L21")
                .setUserId("1601330027791038")
                .setAppType("APP_DXIO4L354UUD9OYOY815")
                .setFormDataJsonList(java.util.Arrays.asList(
                        json
                ));

        try {
            client.batchSaveFormDataWithOptions(batchSaveFormDataRequest, batchSaveFormDataHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                System.out.println(err.message);
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                System.out.println(err.message);
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }

    }

    //读取文件夹为ExcelFileVO列表
    public List<ExcelFileVO>  traverseFile(String path){

        List<ExcelFileVO> excelFileVOList= new ArrayList<>();
        File file=new File(path);
        if(file.exists()){
            File[] f=file.listFiles();
            if (f != null) {
                for(File f2:f){
                    if(f2.isDirectory()&&f2.getName().contains("（")){
                        ExcelFileVO excelFileVO = new ExcelFileVO();
                        String substring = f2.getName().substring(f2.getName().indexOf("（")+1, f2.getName().indexOf("）"));
                        switch (substring){
                            case "杜晓峰":
                                excelFileVO.setDepartment("杜晓峰");
                                excelFileVO.setFiles(f2.listFiles());
                                break;
                            case "李微":
                                excelFileVO.setDepartment("李微");
                                excelFileVO.setFiles(f2.listFiles());
                                break;
                            case "任凤斌":
                                excelFileVO.setDepartment("任凤斌");
                                excelFileVO.setFiles(f2.listFiles());
                                break;

                            case "潘一伦":
                                excelFileVO.setDepartment("潘一伦");
                                excelFileVO.setFiles(f2.listFiles());
                                break;

                            case "杨银辉":
                                excelFileVO.setDepartment("杨银辉");
                                excelFileVO.setFiles(f2.listFiles());
                                break;
                            case "林晨":
                                excelFileVO.setDepartment("林晨");
                                excelFileVO.setFiles(f2.listFiles());
                                break;
                            default:

                        }



                        excelFileVOList.add(excelFileVO);
                        System.out.println(excelFileVOList);
                        System.out.println("文件夹："+f2.getAbsolutePath());
                        traverseFile(f2.getAbsolutePath());
                    }else{
                        System.out.println("文件："+f2.getAbsolutePath());
                    }
                }
            }
        }else{
            System.out.println("文件不存在！");
        }
        return excelFileVOList;
    }



    public void testEvaluator() throws Exception{
//        String path = "C://Users//38106//Documents//月度绩效考核评分//评估一部//绩效评价标准-员工层（张贞）.xlsx";
        String path = "E://月度绩效考核评分//评估一部//绩效评价标准-员工层（陈佩雯）.xlsx";
        FileInputStream fileInputStream = new FileInputStream(path);
        //获取工作簿，这里使用的07版
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        //获取工作表
        Sheet sheet = workbook.getSheetAt(1);
        //获取公式所在单元格

        DataFormatter formatter = new DataFormatter();
        //对应字典
        Map<String, String> map = new HashMap<String, String>();
        map.put("C8","");
        map.put("","");
        map.put("","");
        map.put("","");

        //后面使用它来执行计算公式
        FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        for (Row row : sheet) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                System.out.print(cellRef.formatAsString());



                System.out.print(" - ");
                // get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
                String text = formatter.formatCellValue(cell);
                System.out.println(text);
                // Alternatively, get the value and format it yourself
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.println(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            System.out.println(cell.getDateCellValue());
                        } else {
                            System.out.println(cell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
//                        System.out.println(cell.getCellFormula());
                        CellValue evaluate = formulaEvaluator.evaluate(cell);
                        System.out.println(evaluate.formatAsString());
                        break;
                    case BLANK:
                        System.out.println();
                        break;
                    default:
                        System.out.println();
                }
            }
        }


    }
//    public void repeatedRead() {
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
//        // 读取全部sheet
//        // 这里需要注意 EvaExcelVO1Listener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个EvaExcelVO1Listener里面写
//        EasyExcel.read(fileName, EvaExcelVO1.class, new EvaExcelVO1Listener()).doReadAll();
//
//        // 读取部分sheet
//        fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
//
//
//        // 写法2： 不使用 try-with-resources
//        ExcelReader excelReader = null;
//        try {
//            excelReader = EasyExcel.read(fileName).build();
//
//            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
//            ReadSheet readSheet1 =
//                    EasyExcel.readSheet(0).head(EvaExcelVO1.class).registerReadListener(new DemoDataListener()).build();
//            ReadSheet readSheet2 =
//                    EasyExcel.readSheet(1).head(EvaExcelVO1.class).registerReadListener(new DemoDataListener()).build();
//            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
//            excelReader.read(readSheet1, readSheet2);
//        } finally {
//            if (excelReader != null) {
//                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
//                excelReader.finish();
//            }
//        }
//    }

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

     //从excel复数sheets中读取evaexcel对象map集合
    public void repeatRead(){
        String filename ="";
        EasyExcel.read(filename);
    }



    }

