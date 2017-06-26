/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.insurance.salary;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.file.pr.app.export.insurance.data.ChecklistPrintSettingDto;
import nts.uk.file.pr.app.export.insurance.data.DataRowItem;
import nts.uk.file.pr.app.export.insurance.data.HeaderReportData;
import nts.uk.file.pr.app.export.insurance.data.InsuranceOfficeDto;
import nts.uk.file.pr.app.export.insurance.data.MLayoutInsuOfficeDto;
import nts.uk.file.pr.app.export.insurance.data.MLayoutRowItem;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuMLayoutReportData;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuReportData;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SocialInsuReportService.
 */
@Stateless
public class SocialInsuReportService extends ExportService<SocialInsuQuery> {

    /** The generator. */
    @Inject
    private SocialInsuGenerator generator;
    
    /** The generator M layout. */
    @Inject
    private SocialInsuMergeLayoutGenerator generatorMLayout;
    
    /** The query processor. */
    @Inject
    private SocialInsuQueryProcessor queryProcessor;
    
    /** The repository. */
    @Inject
    private SocialInsuRepository repository;
    
    /* (non-Javadoc)
     * @see nts.arc.layer.app.file.export.ExportService#handle(nts.arc.layer.app.file.export.ExportServiceContext)
     */
    @Override
    protected void handle(ExportServiceContext<SocialInsuQuery> context) {
        SocialInsuQuery query = context.getQuery();
        String companyCode = AppContexts.user().companyCode();
        String loginPersonID = "00000000-0000-0000-0000-000000000001"; //"000000000000000000000000000000000001";
        //AppContexts.user().personId();
        
        if (!repository.isAvailableData(companyCode, loginPersonID, query)) {
            throw new BusinessException(new RawErrorMessage("対象データがありません。"));
        }
        
        // find list health insurance average earn
        List<HealthInsuranceAvgearn> listhealInsuAvgearn = queryProcessor
                .findHealInsuAvgearnByOffice(query.getOfficeCodes());
        // find list pension average earn
        List<PensionAvgearn> listPensionAvgearn = queryProcessor.findPensAvgearnByOffice(query.getOfficeCodes());
        // find configure print output.
        ChecklistPrintSettingDto configOutput = queryProcessor.findConfigureOutputSetting();
        
        // ================== 表示する ==================
        if (configOutput.getShowCategoryInsuranceItem()) {
            List<SocialInsuReportData> listReport = repository.findReportData(companyCode, loginPersonID, query,
                    listhealInsuAvgearn, listPensionAvgearn);
            // dataReports.get(1): report data of office.
            SocialInsuReportData reportCompany = listReport.get(1);
            reportCompany.setConfigureOutput(configOutput);
            reportCompany.setIsCompany(true);
            processData(reportCompany, query);
            
            // dataReports.get(0): report data of personal.
            SocialInsuReportData reportPersonal = listReport.get(0);
            reportPersonal.setIsCompany(false);
            reportPersonal.setConfigureOutput(configOutput);
            processData(reportPersonal, query);
            
            // set child raising contribution money of company for personal.
            reportPersonal.setChildRaisingTotalCompany(reportCompany.getTotalAllOffice()
                    .getChildRaisingContributionMoney());
            
            // generator report
            this.generator.generate(context.getGeneratorContext(), listReport);
        }
        // ================== 表示しない ==================
        else {
            SocialInsuMLayoutReportData reportData = this.repository.findReportMLayout(companyCode, loginPersonID,
                    query, listhealInsuAvgearn, listPensionAvgearn);
            reportData.setConfigureOutput(configOutput);
            
            processDataMLayout(reportData, query);
            
            // generator report
            this.generatorMLayout.generate(context.getGeneratorContext(), reportData);
        }
    }
    
    /**
     * Process data.
     *
     * @param reportData the report data
     * @param query the query
     */
    private void processData(SocialInsuReportData reportData, SocialInsuQuery query) {
        // find header of report.
        HeaderReportData header = findReportHeader(query);
        if (reportData.getIsCompany()) {
            header.setTitleReport("会社保険料チェックリスト(事業主)");
        }
        reportData.setHeaderData(header);
        
        List<InsuranceOfficeDto> officeItems = reportData.getOfficeItems();
        List<DataRowItem> totalEachOffices = new ArrayList<>();
        
        // calculate total each office monthly
        for (InsuranceOfficeDto office : officeItems) {
            // calculate each office total.
            DataRowItem totalEachOffice = calculateTotal(office.getEmployeeDtos());
            office.setTotalEachOffice(totalEachOffice);
            totalEachOffices.add(totalEachOffice);
        }
        // calculate all office total.
        DataRowItem totalAllOffice = calculateTotal(totalEachOffices);
        reportData.setTotalAllOffice(totalAllOffice);
        
        // insured collect amount.
        double insuredCollectAmount = calInsuredCollectAmount(totalAllOffice);
        reportData.setInsuredCollectAmount(insuredCollectAmount);
    }
    
    /**
     * Process data M layout.
     *
     * @param reportData the report data
     * @param query the query
     */
    private void processDataMLayout(SocialInsuMLayoutReportData reportData, SocialInsuQuery query) {
        // find header of report.
        HeaderReportData header = findReportHeader(query);
        header.setTitleReport("社会保険チェックリスト");
        reportData.setHeaderData(header);
        
        List<MLayoutInsuOfficeDto> officeItems = reportData.getOfficeItems();
        List<MLayoutRowItem> totalEachOffices = new ArrayList<>();
        
        // calculate total each office monthly
        for (MLayoutInsuOfficeDto office : officeItems) {
            // calculate each office total.
            MLayoutRowItem totalEachOffice = calculateTotalMLayout(office.getEmployees());
            office.setTotalEachOffice(totalEachOffice);
            totalEachOffices.add(totalEachOffice);
        }
        // calculate all office total.
        MLayoutRowItem totalAllOffice = calculateTotalMLayout(totalEachOffices);
        reportData.setTotalAllOffice(totalAllOffice);
        
        // insured collect amount.
        double insuredCollectAmount = calInsuCollectMLayout(totalAllOffice);
        reportData.setInsuredCollectAmount(insuredCollectAmount);
    }
    
    /**
     * Find report header.
     *
     * @param query the query
     * @return the social insu header report data
     */
    private HeaderReportData findReportHeader(SocialInsuQuery query) {
        HeaderReportData headerData = new HeaderReportData();
        headerData.setNameCompany("【日通 システム株式会社】");
        headerData.setTitleReport("会社保険料チェックリスト(被保険者)");
        headerData.setInformationOffice("【事業所:本社 ~ 福岡支社(3事業所)】");
        // TODO: convert to year Japan.
        headerData.setTargetYearMonth("【対象年月:平成25年04月 給与】 ");
        String headerCondition = "【出カ条件:算定月額と対象月控除額が";
        if (query.getIsEqual()) {
            headerCondition += "全て同じ、";
        }
        if (query.getIsDeficient()) {
            headerCondition += "不足、";
        }
        if (query.getIsRedundant()) {
            headerCondition += "超過";
        }
        headerCondition += "】 ";
        headerData.setCondition(headerCondition);
        headerData.setFormalCalculation("【計算方法:全体の保険料より被保険者分を差し引く方法】");
        return headerData;
    }
    
    /**
     * Calculate total.
     *
     * @param rowItems the row items
     * @return the data row item
     */
    private DataRowItem calculateTotal(List<DataRowItem> rowItems) {
        DataRowItem rowItem = new DataRowItem();
        double monthlyHealthInsuranceNormal = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyHealthInsuranceNormal())
                .sum();
        rowItem.setMonthlyHealthInsuranceNormal(monthlyHealthInsuranceNormal);
        
        double monthlyGeneralInsuranceNormal = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyGeneralInsuranceNormal())
                .sum();
        rowItem.setMonthlyGeneralInsuranceNormal(monthlyGeneralInsuranceNormal);
        
        double monthlyLongTermInsuranceNormal = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyLongTermInsuranceNormal())
                .sum();
        rowItem.setMonthlyLongTermInsuranceNormal(monthlyLongTermInsuranceNormal);
        
        double monthlySpecificInsuranceNormal = rowItems.stream()
                .mapToDouble(item -> item.getMonthlySpecificInsuranceNormal())
                .sum();
        rowItem.setMonthlySpecificInsuranceNormal(monthlySpecificInsuranceNormal);
        
        double monthlyBasicInsuranceNormal = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyBasicInsuranceNormal())
                .sum();
        rowItem.setMonthlyBasicInsuranceNormal(monthlyBasicInsuranceNormal);
        
        double monthlyHealthInsuranceDeduction = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyHealthInsuranceDeduction())
                .sum();
        rowItem.setMonthlyHealthInsuranceDeduction(monthlyHealthInsuranceDeduction);
        
        double monthlyGeneralInsuranceDeduction = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyGeneralInsuranceDeduction())
                .sum();
        rowItem.setMonthlyGeneralInsuranceDeduction(monthlyGeneralInsuranceDeduction);
        
        double monthlyLongTermInsuranceDeduction = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyLongTermInsuranceDeduction())
                .sum();
        rowItem.setMonthlyLongTermInsuranceDeduction(monthlyLongTermInsuranceDeduction);
        
        double monthlySpecificInsuranceDeduction = rowItems.stream()
                .mapToDouble(item -> item.getMonthlySpecificInsuranceDeduction())
                .sum();
        rowItem.setMonthlySpecificInsuranceDeduction(monthlySpecificInsuranceDeduction);
        
        double monthlyBasicInsuranceDeduction = rowItems.stream()
                .mapToDouble(item -> item.getMonthlyBasicInsuranceDeduction())
                .sum();
        rowItem.setMonthlyBasicInsuranceDeduction(monthlyBasicInsuranceDeduction);
        
        double welfarePensionInsuranceNormal = rowItems.stream()
                .mapToDouble(item -> item.getWelfarePensionInsuranceNormal())
                .sum();
        rowItem.setWelfarePensionInsuranceNormal(welfarePensionInsuranceNormal);
        
        double welfarePensionInsuranceDeduction = rowItems.stream()
                .mapToDouble(item -> item.getWelfarePensionInsuranceDeduction())
                .sum();
        rowItem.setWelfarePensionInsuranceDeduction(welfarePensionInsuranceDeduction);
        
        double welfarePensionFundNormal = rowItems.stream()
                .mapToDouble(item -> item.getWelfarePensionFundNormal())
                .sum();
        rowItem.setWelfarePensionFundNormal(welfarePensionFundNormal);
        
        double welfarePensionFundDeduction = rowItems.stream()
                .mapToDouble(item -> item.getWelfarePensionFundDeduction())
                .sum();
        rowItem.setWelfarePensionFundDeduction(welfarePensionFundDeduction);
        
        double childRaisingContributionMoney = rowItems.stream()
                .mapToDouble(item -> item.getChildRaisingContributionMoney())
                .sum();
        rowItem.setChildRaisingContributionMoney(childRaisingContributionMoney);
        
        return rowItem;
    }
    
    /**
     * Calculate total M layout.
     *
     * @param rowItems the row items
     * @return the m layout row item
     */
    private MLayoutRowItem calculateTotalMLayout(List<MLayoutRowItem> rowItems) {
        MLayoutRowItem item = new MLayoutRowItem();
        
        double healInsuFeePersonal = rowItems.stream()
                .mapToDouble(p -> p.getHealInsuFeePersonal())
                .sum();
        item.setHealInsuFeePersonal(healInsuFeePersonal);
        
        double healInsuFeeCompany = rowItems.stream()
                .mapToDouble(p -> p.getHealInsuFeeCompany())
                .sum();
        item.setHealInsuFeeCompany(healInsuFeeCompany);
        
        double deductionHealInsuPersonal = rowItems.stream()
                .mapToDouble(p -> p.getDeductionHealInsuPersonal())
                .sum();
        item.setDeductionHealInsuPersonal(deductionHealInsuPersonal);
        
        double deductionHealInsuCompany = rowItems.stream()
                .mapToDouble(p -> p.getDeductionHealInsuCompany())
                .sum();
        item.setDeductionHealInsuCompany(deductionHealInsuCompany);
        
        double welfarePenInsuPersonal = rowItems.stream()
                .mapToDouble(p -> p.getWelfarePenInsuPersonal())
                .sum();
        item.setWelfarePenInsuPersonal(welfarePenInsuPersonal);
        
        double welfarePenInsuCompany = rowItems.stream()
                .mapToDouble(p -> p.getWelfarePenInsuCompany())
                .sum();
        item.setWelfarePenInsuCompany(welfarePenInsuCompany);
        
        double deductionWelfarePenInsuPersonal = rowItems.stream()
                .mapToDouble(p -> p.getDeductionWelfarePenInsuPersonal())
                .sum();
        item.setDeductionWelfarePenInsuPersonal(deductionWelfarePenInsuPersonal);
        
        double deductionWelfarePenInsuCompany = rowItems.stream()
                .mapToDouble(p -> p.getDeductionWelfarePenInsuCompany())
                .sum();
        item.setDeductionWelfarePenInsuCompany(deductionWelfarePenInsuCompany);
        
        double welfarePenFundPersonal = rowItems.stream()
                .mapToDouble(p -> p.getWelfarePenFundPersonal())
                .sum();
        item.setWelfarePenFundPersonal(welfarePenFundPersonal);
        
        double welfarePenFundCompany = rowItems.stream()
                .mapToDouble(p -> p.getWelfarePenFundCompany())
                .sum();
        item.setWelfarePenFundCompany(welfarePenFundCompany);
        
        double deductionWelfarePenFundPersonal = rowItems.stream()
                .mapToDouble(p -> p.getDeductionWelfarePenFundPersonal())
                .sum();
        item.setDeductionWelfarePenFundPersonal(deductionWelfarePenFundPersonal);
        
        double deductionWelfarePenFundCompany = rowItems.stream()
                .mapToDouble(p -> p.getDeductionWelfarePenFundCompany())
                .sum();
        item.setDeductionWelfarePenFundCompany(deductionWelfarePenFundCompany);
        
        double childRaising = rowItems.stream()
                .mapToDouble(p -> p.getChildRaising())
                .sum();
        item.setChildRaising(childRaising);
        
        return item;
    }
    
    /**
     * Cal insured collect amount.
     *
     * @param totalAllOffice the total all office
     * @return the double
     */
    private double calInsuredCollectAmount(DataRowItem totalAllOffice) {
        return (totalAllOffice.getMonthlyHealthInsuranceDeduction()
                + totalAllOffice.getWelfarePensionInsuranceDeduction()
                + totalAllOffice.getWelfarePensionFundDeduction());
    }
    
    /**
     * Cal insu collect M layout.
     *
     * @param item the item
     * @return the double
     */
    private double calInsuCollectMLayout(MLayoutRowItem item) {
        return (item.getHealInsuFeePersonal() + item.getWelfarePenInsuPersonal() + item.getWelfarePenFundPersonal());
    }
}
