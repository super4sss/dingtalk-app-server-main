package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author zhanyeye
 * @description 周绩效审核业务逻辑
 * @create 12/26/2019 3:34 PM
 */
@Service
@Transactional
@Slf4j
public class AuditService {
    @Autowired
    AcItemRepository acItemRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    UserService userService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    NotifyService notifyService;

    // 自己注入自己防止事务失效
    @Autowired
    AuditService auditService;


    /**
     * 持久化被审核通过的 acItems
     * @param acItems
     * @param dc
     */
    public void saveCheckedAcRecord(List<AcItem> acItems, DcRecord dc) {
        acItems.forEach(acItem -> {
            // 前端传来的没有dcRecord属性, 手动添加
            acItem.setDcRecord(dc);
            if (acItem.isStatus()) {
                // ac申请被同意
                AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, acItem, dc.getInsertTime()));
                acItem.setAcRecord(acRecord);
            }
        });
        acItemRepository.saveAll(acItems);
    }


    /**
     * 更新审核结果
     * @param checkVO 审核人提交的审核结果
     * @return
     */
    public DcRecord updateAuditResult(CheckVO checkVO) {
        DcRecord dc = dcRecordRepository.findById(checkVO.getId()).get();
        if (dc.isStatus()) {
            // status为真，表示之前审核过，此次提交为更新, 删除旧的AcItems， 同时级联删除相关AcRecord
            acItemRepository.deleteByDcRecord(dc);
        }
        // 更新 cvalue, dc, ac
        dc.update(checkVO.getCvalue(), checkVO.getDc(), checkVO.getAc(), checkVO.getLoadEva(), checkVO.getLoadEvaNum(),checkVO.getObeEva(), checkVO.getIniEva(), checkVO.getTeamEva(), checkVO.getAtteEva(), checkVO.getClotEva(), checkVO.getRepEva(), checkVO.getPerfEva(),checkVO.getPerfEvaNum());
        auditService.saveCheckedAcRecord(checkVO.getAcItems(), dc);
        return dc;
    }


    /**
     * 更新用户指定周的dc值
     * @param uid 用户id
     * @param yearmonth 所在年月
     * @param week 所在周
     */
//    public void updateDcSummary(int uid, int yearmonth, int week) {
//        DcSummary dcSummary = Optional.ofNullable(dcSummaryRepository.getDcSummary(uid, yearmonth))
//                .orElse(new DcSummary(uid, yearmonth));
//        dcSummary.updateWeek(week, dcRecordRepository.getUserWeekTotalDc(uid, yearmonth, week));
//        dcSummaryRepository.save(dcSummary);
//        performanceService.computeSalary(uid, yearmonth);
//    }
    /**
     * 更新用户指定周的dc值
     * @param uid 用户id
     * @param yearmonth 所在年月
     * @param month 所在月
     */
    public void updateDcSummary(int uid, int yearmonth, int month) {
        DcSummary dcSummary = Optional.ofNullable(dcSummaryRepository.getDcSummary(uid, yearmonth))
                .orElse(new DcSummary(uid, yearmonth));
        dcSummary.updateMonth(month, dcRecordRepository.getUserWeekTotalDc(uid, yearmonth, month));
        dcSummaryRepository.save(dcSummary);
        performanceService.computeSalary(uid, yearmonth);
    }


    /**
     * 审核人分页获取已审核申请
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public Map listCheckedVO(int uid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CheckedVO> pages = dcRecordRepository.listChecked(uid, pageable);
        List<CheckedVO> contents = pages.getContent();
        contents.forEach(checked -> {
            checked.setAcItems(acItemRepository.findAllByDcRecordID(checked.getId()));
        });
        return Map.of(
                "content", contents,
                "total", pages.getTotalElements()
        );
    }



    /**
     * 审核人根据时间筛选已审核申请
     * @param uid
     * @param yearmonth
     * @param week
     * @return
     */
//    public List<CheckedVO> listCheckedByDate(int uid, int yearmonth, int week) {
//        List<CheckedVO> checkedVOS = dcRecordRepository.listCheckedByDate(uid, yearmonth, week);
//        checkedVOS.forEach(vo -> {
//            vo.setAcItems(acItemRepository.findAllByDcRecordID(vo.getId()));
//        });
//        return checkedVOS;
//    }

    /**
     * 审核人根据时间筛选已审核申请
     * @param uid
     * @param yearmonth
     * @param week
     * @return
     */
    public List<CheckedVO> listCheckedByDate(int uid, int yearmonth, int week) {
        List<CheckedVO> checkedVOS = dcRecordRepository.listCheckedByDate(uid, yearmonth);
        log.info(checkedVOS.toString());
        checkedVOS.forEach(vo -> {
            vo.setAcItems(acItemRepository.findAllByDcRecordID(vo.getId()));
        });
        return checkedVOS;
    }


    /**
     * 审核人查看待审核的申请
     * @param uid 审核人的id
     * @return
     */
    public List<ToCheckVO> listPendingApplication(int uid) {
        List<ToCheckVO> toCheckVOList = dcRecordRepository.listToCheckVO(uid);
        toCheckVOList.forEach(toCheckVO -> {
            toCheckVO.setAcItems(acItemRepository.findAllByDcRecordID(toCheckVO.getId()));
        });
        toCheckVOList.forEach(toCheckVO -> addEvaItems(toCheckVO));
        log.info(toCheckVOList.toString());
        return toCheckVOList;

    }

    //向toCheckVO添加EvaVOList
    public ToCheckVO addEvaItems(ToCheckVO dcRecordVO){
        List<EvaVO> evaItems = new ArrayList<EvaVO>();
        EvaVO evaItem =new EvaVO(null,null);

//        evaItem.setEvaNam("工作量");
//        evaItem.setEva(dcRecordVO.getLoadEva());
//        evaItems.add(evaItem);
//
//        evaItem.setEvaNam("工作服从性");
//        evaItem.setEva(dcRecordVO.getObeEva());
//
//        evaItem.setEvaNam("工作主动性");
//        evaItem.setEva(dcRecordVO.getIniEva());
//        evaItems.add(evaItem);
//        evaItem.setEvaNam("团队互助性");
//        evaItem.setEva(dcRecordVO.getTeamEva());
//        evaItems.add(evaItem);
//        evaItem.setEvaNam("考勤情况");
//        evaItem.setEva(dcRecordVO.getAtteEva());
//        evaItems.add(evaItem);
//        evaItem.setEvaNam("穿戴情况");
//        evaItem.setEva(dcRecordVO.getClotEva());
//        evaItems.add(evaItem);
//        evaItem.setEvaNam("周报质量");
//        evaItem.setEva(dcRecordVO.getRepEva());
//        evaItems.add(evaItem);
//        evaItem.setEvaNam("绩效奖励");
//        evaItem.setEva(dcRecordVO.getPerfEva());
//        evaItems.add(evaItem);
        evaItems.add(new EvaVO("工作量",dcRecordVO.getLoadEva()));
        evaItems.add(new EvaVO("工作服从性",dcRecordVO.getObeEva()));
        evaItems.add(new EvaVO("工作主动性",dcRecordVO.getIniEva()));
        evaItems.add(new EvaVO("团队互助性",dcRecordVO.getTeamEva()));
        evaItems.add(new EvaVO("考勤情况",dcRecordVO.getAtteEva()));
        evaItems.add(new EvaVO("穿戴情况",dcRecordVO.getClotEva()));
        evaItems.add(new EvaVO("周报质量",dcRecordVO.getRepEva()));
        evaItems.add(new EvaVO("绩效奖励",dcRecordVO.getPerfEva()));


        dcRecordVO.setEvaItems(evaItems);

        return dcRecordVO;
    }


    /**
     * 查询审核人未审核数
     * @param aid 审核人id
     * @return
     */
    public int getUnCheckCnt(int aid) {
        return dcRecordRepository.getUnCheckCntByAid(aid);
    }


}
