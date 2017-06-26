/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.detailpayment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingRepository;
import nts.uk.file.pr.app.export.detailpayment.data.HeaderReportData;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentConstant;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentSalaryReportData;
import nts.uk.file.pr.app.export.detailpayment.data.SalaryPrintSettingDto;
import nts.uk.file.pr.app.export.detailpayment.query.PaymentSalaryQuery;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.japanese.JapaneseDate;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;

/**
 * The Class PaymentSalaryReportService.
 */
@Stateless
public class PaymentSalaryReportService extends ExportService<PaymentSalaryQuery> {

    /** The generator. */
    @Inject
    private PaySalaryInsuGenerator generator;

    /** The repository. */
    @Inject
    private PaySalaryReportRepository repository;

    /** The salary print repo. */
    @Inject
    private SalaryPrintSettingRepository salaryPrintRepo;
    
    /** The japanese provider. */
    @Inject
    private JapaneseErasProvider japaneseProvider;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.arc.layer.app.file.export.ExportService#handle(nts.arc.layer.app.file
     * .export.ExportServiceContext)
     */
    @Override
    protected void handle(ExportServiceContext<PaymentSalaryQuery> context) {
        PaymentSalaryQuery query = context.getQuery();
        
        // TODO: FAKE EMPLOYEE
        List<String> personIds = Arrays.asList("99900000-0000-0000-0000-000000000001",
                "99900000-0000-0000-0000-000000000002", "99900000-0000-0000-0000-000000000003",
                "99900000-0000-0000-0000-000000000004", "99900000-0000-0000-0000-000000000005");
        query.setPersonIds(personIds);
        
        String companyCode = AppContexts.user().companyCode();
        
        if (!this.repository.isAvailableData(companyCode, query)) {
            throw new BusinessException(new RawErrorMessage("対象データがありません。"));
        }
        // ========= FIND REPORT DATA =========
        PaymentSalaryReportData reportData = this.repository.findReportData(companyCode, query);
        
        // ========= FIND OUTPUT SETTING =========
        SalaryPrintSettingDto configure = findSalarySetting(query);
        reportData.setConfigure(configure);
        
        // ========= FIND HEADER REPORT =========
        HeaderReportData header = findReportHeader(query);
        reportData.setHeaderData(header);

        this.generator.generate(context.getGeneratorContext(), reportData);
    }
    
    /**
     * Find salary setting.
     *
     * @param query the query
     * @return the salary print setting dto
     */
    private SalaryPrintSettingDto findSalarySetting(PaymentSalaryQuery query) {
        SalaryPrintSettingDto dto = new SalaryPrintSettingDto();
        String companyCode = AppContexts.user().companyCode();
        SalaryPrintSetting salary = salaryPrintRepo.find(companyCode);
        
        dto.setShowPayment(salary.getShowPayment());
        dto.setSumPersonSet(salary.getSumPersonSet());
        dto.setSumMonthPersonSet(salary.getSumMonthPersonSet());
        dto.setSumEachDeprtSet(salary.getSumEachDeprtSet());
        dto.setSumMonthDeprtSet(salary.getSumMonthDeprtSet());
        dto.setSumDepHrchyIndexSet(salary.getSumDepHrchyIndexSet());
        dto.setSumMonthDepHrchySet(salary.getSumMonthDepHrchySet());
        List<Integer> listHierarchy = findHierarchy(salary);
        dto.setSelectedLevels(listHierarchy);
        dto.setTotalSet(salary.getTotalSet());
        dto.setMonthTotalSet(salary.getMonthTotalSet());
        
        // ===== SET OPTION REPORT =====
        dto.setOutputFormatType(query.getOutputFormatType());
        dto.setIsVerticalLine(query.getIsVerticalLine());
        dto.setIsHorizontalLine(query.getIsHorizontalLine());
        dto.setPageBreakSetting(query.getPageBreakSetting());
        dto.setHierarchy(query.getHierarchy());
        dto.setOutputLanguage(query.getOutputLanguage());
        
        return dto;
    }
    
    /**
     * Find hierarchy.
     *
     * @param salarySetting the salary setting
     * @return the list
     */
    private List<Integer> findHierarchy(SalaryPrintSetting salarySetting) {
        List<Integer> hierarchies = new ArrayList<>();
        if (salarySetting.getHrchyIndex1()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_1);
        }
        if (salarySetting.getHrchyIndex2()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_2);
        }
        if (salarySetting.getHrchyIndex3()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_3);
        }
        if (salarySetting.getHrchyIndex4()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_4);
        }
        if (salarySetting.getHrchyIndex5()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_5);
        }
        if (salarySetting.getHrchyIndex6()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_6);
        }
        if (salarySetting.getHrchyIndex7()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_7);
        }
        if (salarySetting.getHrchyIndex8()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_8);
        }
        if (salarySetting.getHrchyIndex9()) {
            hierarchies.add(PaymentConstant.HIERARCHY_INDEX_9);
        }
        return hierarchies;
    }

    /**
     * Find report header.
     *
     * @param query the query
     * @return the header report data
     */
    private HeaderReportData findReportHeader(PaymentSalaryQuery query) {
        HeaderReportData header = new HeaderReportData();
        header.setNameCompany("【日通システム株式会社】");
        String titleReport = "明細累計表（給与）";
        if (query.getOutputFormatType() == PaymentConstant.OUTPUT_FORMAT_TYPE_DETAIL) {
            titleReport = "明細一覧表（給与）";
        }
        header.setTitleReport(titleReport);
        header.setDepartment("【部門　：役員　販売促進1課　役員～製造部　製造課　製造　(31部門)】");
        header.setCategory("【分類　：正社員～アルバイト　(5分類)】");
        header.setPosition("【職位　：参事～主任　(10職位)】");
        
        // ========= CONVERT YEARMONTH JAPANESE =========
        StringBuilder yearMonthJP = new StringBuilder("【処理年月：");
        yearMonthJP.append(convertYearMonthJP(query.getStartDate()));
        yearMonthJP.append("～");
        yearMonthJP.append(convertYearMonthJP(query.getEndDate()));
        yearMonthJP.append("】");
        header.setTargetYearMonth(yearMonthJP.toString());
        return header;
    }
    
    /**
     * Convert year month JP.
     *
     * @param yearMonth the year month
     * @return the string
     */
    private String convertYearMonthJP(Integer yearMonth) {
        String firstDay = "01";
        String tmpDate = yearMonth.toString().concat(firstDay);
        String dateFormat = "yyyyMMdd";
        GeneralDate generalDate = GeneralDate.fromString(tmpDate, dateFormat);
        JapaneseDate japaneseDate = this.japaneseProvider.toJapaneseDate(generalDate);
        return japaneseDate.era() + japaneseDate.year() + "年 " + japaneseDate.month() + "月度"; 
    }
}
