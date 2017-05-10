/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.detailpayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItem;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemRepository;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItem;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingRepository;
import nts.uk.file.pr.app.export.detailpayment.PaySalaryReportRepository;
import nts.uk.file.pr.app.export.detailpayment.data.EmployeeDto;
import nts.uk.file.pr.app.export.detailpayment.data.EmployeeKey;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentConstant;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentSalaryReportData;
import nts.uk.file.pr.app.export.detailpayment.query.PaymentSalaryQuery;

/**
 * The Class JpaPaymentSalaryReportRepository.
 */
@Stateless
public class JpaPaySalaryReportRepository extends JpaRepository implements PaySalaryReportRepository {
    
    /** The Constant QUERY_CHECK_DATA. */
    private static final String QUERY_CHECK_DATA = "SELECT header, detail "
            + "FROM QstdtPaymentHeader header, "
            + "QstdtPaymentDetail detail "
            + "WHERE header.qstdtPaymentHeaderPK.companyCode = :companyCode "
            + "AND header.qstdtPaymentHeaderPK.personId IN :personIds "
            + "AND header.qstdtPaymentHeaderPK.payBonusAtr = 0 "
            + "AND header.qstdtPaymentHeaderPK.processingYM >= :startYM "
            + "AND header.qstdtPaymentHeaderPK.processingYM <= :endYM "
            + "AND detail.qstdtPaymentDetailPK.companyCode = header.qstdtPaymentHeaderPK.companyCode "
            + "AND detail.qstdtPaymentDetailPK.personId = header.qstdtPaymentHeaderPK.personId "
            + "AND detail.qstdtPaymentDetailPK.payBonusAttribute = 0 "
            + "AND detail.qstdtPaymentDetailPK.processingYM >= :startYM "
            + "AND detail.qstdtPaymentDetailPK.processingYM <= :endYM ";
    
    /** The Constant QUERY_PAYMENT_DETAIL. */
    private static final String QUERY_PAYMENT_DETAIL = "SELECT pCom.scd, detail.qstdtPaymentDetailPK.categoryATR,"
            + "detail.qstdtPaymentDetailPK.itemCode, item.itemName,"
            + "detail.qstdtPaymentDetailPK.processingYM, detail.value "
            + "FROM PbsmtPersonBase pBase "
            + "LEFT JOIN PcpmtPersonCom pCom ON "
            + "pCom.pcpmtPersonComPK.pid = pBase.pid "
            + "LEFT JOIN QstdtPaymentDetail detail ON "
            + "detail.qstdtPaymentDetailPK.personId = pCom.pcpmtPersonComPK.pid "
            + "LEFT JOIN QcamtItem item ON "
            + "item.qcamtItemPK.ccd = detail.qstdtPaymentDetailPK.companyCode "
            + "AND item.qcamtItemPK.itemCd = detail.qstdtPaymentDetailPK.itemCode "
            + "AND item.qcamtItemPK.ctgAtr = detail.qstdtPaymentDetailPK.categoryATR "
            + "WHERE pBase.pid IN :personIds "
            + "AND pCom.pcpmtPersonComPK.ccd = :companyCode "
            + "AND detail.qstdtPaymentDetailPK.companyCode = pCom.pcpmtPersonComPK.ccd "
            + "AND detail.qstdtPaymentDetailPK.payBonusAttribute = 0 "
            + "AND detail.qstdtPaymentDetailPK.processingYM >= :startYM "
            + "AND detail.qstdtPaymentDetailPK.processingYM <= :endYM "
            + "AND detail.qstdtPaymentDetailPK.sparePayAttribute IN :sparePayAttributes "
            + "AND detail.qstdtPaymentDetailPK.itemCode IN :itemCodes";
    
    /** The Constant QUERY_NUMER_EMPLOYEE_LIMIT. */
    private static final int QUERY_NUMER_EMPLOYEE_LIMIT = 1000;
    
    /** The output setting repo. */
    @Inject
    private SalaryOutputSettingRepository outputSettingRepo;
    
    /** The master item repo. */
    @Inject
    private ItemMasterRepository masterItemRepo;
    
    /** The aggr item repo. */
    @Inject
    private SalaryAggregateItemRepository aggrItemRepo;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.file.pr.app.export.detailpaymentsalary.PaySalaryReportRepository#
     * isAvailableData(java.lang.String,
     * nts.uk.file.pr.app.export.detailpaymentsalary.query.PaymentSalaryQuery)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isAvailableData(String companyCode, PaymentSalaryQuery paySalaryQuery) {
        EntityManager em = this.getEntityManager();
        Query query = em.createQuery(QUERY_CHECK_DATA);
        query.setParameter("companyCode", companyCode);
        query.setParameter("startYM", paySalaryQuery.getStartDate());
        query.setParameter("endYM", paySalaryQuery.getEndDate());
        
        List<Object[]> dataResult = new ArrayList<>();
        CollectionUtil.split(paySalaryQuery.getPersonIds(), QUERY_NUMER_EMPLOYEE_LIMIT, (subList) -> {
            dataResult.addAll(query.setParameter("personIds", subList).getResultList());
        });
        return !dataResult.isEmpty();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.file.pr.app.export.detailpaymentsalary.PaySalaryReportRepository#
     * findReportData(java.lang.String,
     * nts.uk.file.pr.app.export.detailpaymentsalary.query.PaymentSalaryQuery)
     */
    public PaymentSalaryReportData findReportData(String companyCode, PaymentSalaryQuery paySalaryQuery) {
        // ======================== FIND INFORMATION EMPLOYEE ========================
        // TODO: find list department which is sorted department level.
        List<EmployeeDto> emps = new ArrayList<>();
        
        // ======================== FIND MAP AMOUNT EMPLOYEE ========================
        SalaryOutputSetting outputSetting = outputSettingRepo.findByCode(companyCode,
                paySalaryQuery.getOutputSettingCode());
        
        List<ItemMaster> masterItems = masterItemRepo.findAll(companyCode);
        
        List<SalaryAggregateItem> aggrItems = aggrItemRepo.findAll(companyCode);
        
        // ========= FIND MAP ITEM =========
        Map<CategoryItem, List<String>> mapItem = findMapItem(outputSetting, masterItems, aggrItems);
        
        // ========= FIND ITEM CODE =========
        List<String> itemCodes = mapItem.entrySet()
                .stream()
                .flatMap(item -> item.getValue().stream())
                .collect(Collectors.toList());
        
        // ========= FIND PAYMENT DETAIl =========
        List<Object[]> listObject = findPaymentDetail(companyCode, paySalaryQuery, itemCodes);
        Map<String, List<Object[]>> mapEmp = listObject.stream()
                .collect(Collectors.groupingBy(item -> (String)item[0]));
        
        Map<EmployeeKey, Double> mapEmpAmount = new HashMap<>();
        
        // ========= FIND MAP AMOUNT OF EMPLOYEE =========
        for (Map.Entry<String, List<Object[]>> entry : mapEmp.entrySet()) {
            String codeEmp = entry.getKey();
            List<Object[]> listDetail = entry.getValue();
            
            Map<Integer, List<Object[]>> mapAmountByYm = listDetail.stream()
                    .collect(Collectors.groupingBy(item -> (int) item[4]));
            
            filterData(codeEmp, mapAmountByYm, mapItem, mapEmpAmount);
        }
        // ========= SET REPORT DATA =========
        PaymentSalaryReportData reportData = new PaymentSalaryReportData();
        reportData.setEmployees(emps);
        reportData.setMapEmployeeAmount(mapEmpAmount);
        return reportData;
    }
    
    /**
     * Filter data.
     *
     * @param codeEmp the code emp
     * @param mapAmountByYm the map amount by ym
     * @param mapItem the map item
     * @param mapAmount the map amount
     */
    private void filterData(String codeEmp, Map<Integer, List<Object[]>> mapAmountByYm,
            Map<CategoryItem, List<String>> mapItem, Map<EmployeeKey, Double> mapAmount) {
        
        mapAmountByYm.forEach((yearMonth, objectList) -> {
            mapItem.forEach((categoryItem, subItemCodes) -> {
                
                EmployeeKey key = new EmployeeKey();
                key.setYearMonth(yearMonth);
                key.setEmployeeCode(codeEmp);
                key.setItemName(categoryItem.itemName);
                key.setSalaryCategory(SalaryCategory.valueOf(categoryItem.category.value));
                
                List<Object[]> objectFiltereds = objectList.stream()
                        .filter(ob -> yearMonth == (int) ob[4] && (int) ob[1] == categoryItem.category.value 
                                    && subItemCodes.contains((String) ob[2]))
                        .collect(Collectors.toList());
                
                double amount = 0;
                
                if (!CollectionUtil.isEmpty(objectFiltereds)) {
                    Object[] detail = objectFiltereds.get(0);
                    // item master
                    if (objectFiltereds.size() == 1) {
                        amount = ((BigDecimal) detail[5]).doubleValue();
                    } else {
                        amount = objectFiltereds.stream()
                                .mapToDouble(ob -> ((BigDecimal) ob[5]).doubleValue())
                                .sum();
                    }
                }
                mapAmount.put(key, amount);
            });
        });
    }
    
    /**
     * Find map item.
     *
     * @param outputSetting the output setting
     * @param masterItems the master items
     * @param aggrItems the aggr items
     * @return the map
     */
    private Map<CategoryItem, List<String>> findMapItem(SalaryOutputSetting outputSetting, List<ItemMaster> masterItems,
            List<SalaryAggregateItem> aggrItems) {
        Map<CategoryItem, List<String>> mapItem = new HashMap<>();
        
        for (SalaryCategorySetting category : outputSetting.getCategorySettings()) {
            for (SalaryOutputItem item : category.getItems()) {
                String itemCode = item.getLinkageCode();
                
                CategoryItem categoryItem = new CategoryItem();
                categoryItem.itemCode = itemCode;
                categoryItem.category = category.getCategory();
                
                // ========= MASTER ITEM =========
                if (item.getType() == SalaryItemType.Master) {
                    categoryItem.itemName = masterItems.stream()
                            .filter(masterItem -> masterItem.getCategoryAtr().value == categoryItem.category.value
                                        && masterItem.getItemCode().v().equals(categoryItem.itemCode))
                            .map(masterItem -> masterItem.getItemName().v())
                            .findFirst()
                            .get();
                    mapItem.put(categoryItem, Arrays.asList(itemCode));
                }
                // ========= AGGREGATE ITEM =========
                else {
                    List<String> subMasterItemCodes = aggrItems.stream()
                            .filter(aggr -> aggr.getHeader().getTaxDivision().value == category.getCategory().value
                                        && aggr.getHeader().getAggregateItemCode().v().equals(itemCode))
                            .flatMap(aggr -> aggr.getSubItemCodes().stream()
                                        .map(aggrItem -> aggrItem.getSalaryItemCode()))
                            .collect(Collectors.toList());
                    categoryItem.itemName = aggrItems.stream()
                            .filter(aggr -> aggr.getHeader().getTaxDivision().value == category.getCategory().value
                                        && aggr.getHeader().getAggregateItemCode().v().equals(itemCode))
                            .map(aggr -> aggr.getName().v())
                            .findFirst()
                            .get();
                    mapItem.put(categoryItem, subMasterItemCodes);
                }
            }
        }
        return mapItem;
    }
    
    /**
     * Find payment detail.
     *
     * @param companyCode the company code
     * @param payQuery the pay query
     * @param itemCodes the item codes
     * @return the list
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Object[]> findPaymentDetail(String companyCode, PaymentSalaryQuery payQuery,
            List<String> itemCodes) {
        EntityManager em = this.getEntityManager();
        Query query = em.createQuery(QUERY_PAYMENT_DETAIL);
        query.setParameter("personIds", payQuery.getPersonIds());
        query.setParameter("companyCode", companyCode);
        query.setParameter("startYM", payQuery.getStartDate());
        query.setParameter("endYM", payQuery.getEndDate());
        query.setParameter("itemCodes", itemCodes);
        List<Integer> payAttrs = new ArrayList<>();
        if (payQuery.getIsNormalMonth()) {
            payAttrs.add(PaymentConstant.ZERO);
        }
        if (payQuery.getIsPreliminaryMonth()) {
            payAttrs.add(PaymentConstant.ONE);
        }
        if (payAttrs.isEmpty()) {
            throw new BusinessException(new RawErrorMessage("通常月と予備月が指定されていません。"));
        }
        query.setParameter("sparePayAttributes", payAttrs);
        List select = query.getResultList();
        if (select.isEmpty()) {
            throw new BusinessException(new RawErrorMessage("対象データがありません。"));
        }
        return select;
    }
    
    /**
     * The Class CategoryItem.
     */
    class CategoryItem {
        
        /** The item code. */
        private String itemCode;
        
        /** The item name. */
        private String itemName;
        
        /** The category. */
        private SalaryCategory category;
    }
}
