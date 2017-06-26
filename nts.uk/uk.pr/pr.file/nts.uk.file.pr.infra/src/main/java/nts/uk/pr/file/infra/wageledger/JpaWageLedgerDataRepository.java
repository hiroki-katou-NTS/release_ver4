/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wageledger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDep;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobTitle;
import nts.uk.ctx.basic.infra.entity.report.PbsmtPersonBase;
import nts.uk.ctx.basic.infra.entity.report.PclmtPersonTitleRgl;
import nts.uk.ctx.basic.infra.entity.report.PcpmtPersonCom;
import nts.uk.ctx.basic.infra.entity.report.PogmtPersonDepRgl;
import nts.uk.ctx.basic.infra.entity.report.QyedtYearendDetail;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItem;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemPK;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentDetail;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentDetailPK;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentHeader;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPayday;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItem;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemRepository;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLItemType;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingRepository;
import nts.uk.ctx.pr.report.infra.util.JpaUtil;
import nts.uk.file.pr.app.export.wageledger.WageLedgerDataRepository;
import nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery;
import nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery.OutputType;
import nts.uk.file.pr.app.export.wageledger.data.WLNewLayoutReportData;
import nts.uk.file.pr.app.export.wageledger.data.WLOldLayoutReportData;
import nts.uk.file.pr.app.export.wageledger.data.newlayout.BeforeEndYearData;
import nts.uk.file.pr.app.export.wageledger.data.newlayout.TotalData;
import nts.uk.file.pr.app.export.wageledger.data.oldlayout.DeductionData;
import nts.uk.file.pr.app.export.wageledger.data.oldlayout.PaymentData;
import nts.uk.file.pr.app.export.wageledger.data.share.HeaderReportData;
import nts.uk.file.pr.app.export.wageledger.data.share.MonthlyData;
import nts.uk.file.pr.app.export.wageledger.data.share.ReportItemDto;

/**
 * The Class JpaWageLedgerDataRepository.
 */
@Stateless
public class JpaWageLedgerDataRepository extends JpaRepository implements WageLedgerDataRepository {

	/** The output setting repository. */
	@Inject
	private WLOutputSettingRepository outputSettingRepository;

	/** The aggregate item repository. */
	@Inject
	private WLAggregateItemRepository aggregateItemRepository;

	/** The Constant CHECK_HAS_DATA_QUERY_STRING. */
	private static final String CHECK_HAS_DATA_QUERY_STRING = "SELECT COUNT(h.qstdtPaymentHeaderPK.personId) "
			+ "FROM QstdtPaymentHeader h "
			+ "WHERE h.qstdtPaymentHeaderPK.companyCode = :companyCode "
			+ "AND h.qstdtPaymentHeaderPK.personId IN :personIds "
			+ "AND h.qstdtPaymentHeaderPK.sparePayAtr = :sparePayAtr "
			+ "AND h.qstdtPaymentHeaderPK.processingYM >= :startProcessingYM "
			+ "AND h.qstdtPaymentHeaderPK.processingYM <= :endProcessingYM ";

	/** The Constant HEADER_QUERY_STRING. */
	private static final String HEADER_QUERY_STRING = "SELECT p, pc, pd, cd, pt "
			+ "FROM PbsmtPersonBase p "
			+ "LEFT JOIN PcpmtPersonCom pc"
			+ " ON pc.pcpmtPersonComPK.pid = p.pid "
			+ "LEFT JOIN PogmtPersonDepRgl pd"
			+ " ON pd.pogmtPersonDepRglPK.pid = p.pid "
			+ "LEFT JOIN CmnmtDep cd"
			+ " ON cd.cmnmtDepPK.departmentCode = pd.depcd "
			+ "LEFT JOIN PclmtPersonTitleRgl pt"
			+ " ON pt.pclmtPersonTitleRglPK.pid = p.pid "
			+ "WHERE p.pid IN :personIds "
			+ "AND pc.pcpmtPersonComPK.ccd = :companyCode "
			+ "AND pd.strD <= :baseDate "
			+ "AND pd.endD >= :baseDate ";

	/** The Constant ALL_DETAIL_DATA_QUERY_STRING. */
	private static final String ALL_DETAIL_DATA_QUERY_STRING = "SELECT d, m "
			+ "FROM QcamtItem m "
			+ "LEFT JOIN QstdtPaymentDetail d"
			+ " ON d.qstdtPaymentDetailPK.itemCode = m.qcamtItemPK.itemCd"
			+ " AND d.qstdtPaymentDetailPK.companyCode = m.qcamtItemPK.ccd "
			+ "WHERE m.qcamtItemPK.ccd = :companyCode "
			+ "AND d.qstdtPaymentDetailPK.personId in :personIds "
			+ "AND d.qstdtPaymentDetailPK.processingYM >= :startProcessingYM "
			+ "AND d.qstdtPaymentDetailPK.processingYM <= :endProcessingYM "
			+ "AND d.qstdtPaymentDetailPK.sparePayAttribute = :sparePayAtr ";

	/** The Constant MASTER_ITEM_QUERY_STRING. */
	private static final String MASTER_ITEM_QUERY_STRING = "SELECT mi FROM QcamtItem mi "
			+ "WHERE mi.qcamtItemPK.ccd = :companyCode";

	/** The Constant POSITION_QUERY_STRING. */
	private static final String POSITION_QUERY_STRING = "SELECT jt "
			+ "FROM CmnmtJobHist jh "
			+ "LEFT JOIN CmnmtJobTitle jt"
			+ " ON jt.cmnmtJobTitlePK.historyId = jh.cmnmtJobHistPK.historyId "
			+ "WHERE jt.cmnmtJobTitlePK.companyCode = :companyCode "
			+ "AND jt.cmnmtJobTitlePK.jobCode IN :jobCode "
			+ "AND jh.startDate <= :baseDate "
			+ "AND jh.endDate >= :baseDate";

	/** The Constant PAYMENT_DATE_QUERY_STRING. */
	private static final String PAYMENT_DATE_QUERY_STRING = "SELECT ph, pd FROM QpdmtPayday pd "
					+ "LEFT JOIN QstdtPaymentHeader ph ON"
					+ " ph.qstdtPaymentHeaderPK.companyCode = pd.qpdmtPaydayPK.ccd"
					+ " AND ph.qstdtPaymentHeaderPK.processingNo = pd.qpdmtPaydayPK.processingNo"
					+ " AND ph.qstdtPaymentHeaderPK.payBonusAtr = pd.qpdmtPaydayPK.payBonusAtr"
					+ " AND ph.qstdtPaymentHeaderPK.processingYM = pd.qpdmtPaydayPK.processingYm"
					+ " AND ph.qstdtPaymentHeaderPK.sparePayAtr = pd.qpdmtPaydayPK.sparePayAtr "
					+ "WHERE ph.qstdtPaymentHeaderPK.companyCode = :companyCode "
					+ "AND ph.qstdtPaymentHeaderPK.personId IN :personIds "
					+ "AND ph.qstdtPaymentHeaderPK.sparePayAtr = 0 "
					+ "AND ph.qstdtPaymentHeaderPK.processingYM >= :startProcessingYM "
					+ "AND ph.qstdtPaymentHeaderPK.processingYM <= :endProcessingYM "
					+ "AND ph.qstdtPaymentHeaderPK.payBonusAtr = :paymentType";

	/** The Constant BEFORE_END_YEAR_DATA_QUERY_STRING. */
	private static final String BEFORE_END_YEAR_DATA_QUERY_STRING = "SELECT ph FROM QyedtYearendDetail ph "
			+ "WHERE ph.qyedtYearendDetailPK.ccd = :companyCode "
			+ "AND ph.qyedtYearendDetailPK.pid IN :personIds "
			+ "AND ph.qyedtYearendDetailPK.yearK = :year";

	/** The Constant TOTAL_TAX_ITEM_CODE. */
	private static final String TOTAL_TAX_ITEM_CODE = "F001";

	/** The Constant TOTAL_TAX_EXEMPTION_ITEM_CODE. */
	private static final String TOTAL_TAX_EXEMPTION_ITEM_CODE = "F002";

	/** The Constant TOTAL_PAYMENT_ITEM_CODE. */
	private static final String TOTAL_PAYMENT_ITEM_CODE = "F003";

	/** The Constant TOTAL_DEDUCTION_ITEM_CODE. */
	private static final String TOTAL_DEDUCTION_ITEM_CODE = "F114";

	/** The Constant TOTAL_REAL_ITEM_CODE. */
	private static final String TOTAL_REAL_ITEM_CODE = "F309";

	/** The Constant TOTAL_SOCIAL_INSURANCE_ITEM_CODE. */
	private static final String TOTAL_SOCIAL_INSURANCE_ITEM_CODE = "F105";

	/** The Constant TOTAL_TAXABLE_ITEM_CODE. */
	private static final String TOTAL_TAXABLE_ITEM_CODE = "F106";

	/** The Constant TOTAL_INCOME_TAX_ITEM_CODE. */
	private static final String TOTAL_INCOME_TAX_ITEM_CODE = "F107";

	/** The Constant TOTAL_INBAHATANT_TAX_ITEM_CODE. */
	private static final String TOTAL_INBAHATANT_TAX_ITEM_CODE = "F108";

	/** The Constant ARTICLE_CATEGORY. */
	private static final int ARTICLE_CATEGORY = 3;

	/*
	 * (non-Javadoc)
	 * @see nts.uk.file.pr.app.export.wageledger.WageLedgerDataRepository
	 * #findReportDatas(java.lang.String,
	 * nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findReportDatas(String companyCode, WageLedgerReportQuery query, Class<T> returnType) {
		ResultData resultData = this.queryDataList(companyCode, query);

		// Covert to Data model.
		if (returnType.getName().equals(WLOldLayoutReportData.class.getName())) {
			return (List<T>) this.convertToOldLayoutDataList(resultData, query, companyCode);
		}
		if (returnType.getName().equals(WLNewLayoutReportData.class.getName())) {
			query.isAggreatePreliminaryMonth = false;
			return (List<T>) this.covertToNewLayoutDataList(resultData, query, companyCode);
		}

		// Not support return type.
		throw new RuntimeException("Cannot return object instance of " + returnType.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.file.pr.app.export.wageledger.WageLedgerDataRepository
	 * #checkHasReportData(java.lang.String,
	 * nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery)
	 */
	@Override
	public boolean hasReportData(String companyCode, WageLedgerReportQuery query) {
		EntityManager em = this.getEntityManager();

		// Create Year Month.
		YearMonth startYearMonth = YearMonth.of(query.targetYear, 1);
		YearMonth endYearMonth = YearMonth.of(query.targetYear, 12);
		TypedQuery<Long> typedQuery = em.createQuery(CHECK_HAS_DATA_QUERY_STRING, Long.class)
				.setParameter("companyCode", companyCode)
				.setParameter("sparePayAtr", JpaUtil.boolean2Short(query.isAggreatePreliminaryMonth))
				.setParameter("startProcessingYM", startYearMonth.v())
				.setParameter("endProcessingYM", endYearMonth.v());

		List<Long> resultList = new ArrayList<>();
		CollectionUtil.split(query.employeeIds, 1000, (subList) -> {
			resultList.add((long) typedQuery.setParameter("personIds", subList).getSingleResult());
		});
		return resultList.stream().mapToLong(Long::longValue).sum() > 0;
	}

	/**
	 * Query data list.
	 *
	 * @param companyCode the company code
	 * @param query the query
	 * @return the result data
	 */
	@SuppressWarnings("unchecked")
	private ResultData queryDataList(String companyCode, WageLedgerReportQuery query) {
		
		EntityManager em = this.getEntityManager();
		// Create Year Month.
		YearMonth startYearMonth = YearMonth.of(query.targetYear, 1);
		YearMonth endYearMonth = YearMonth.of(query.targetYear, 12);
		
		// ===================== Query all master item.=====================
		// Create Query Master item only.
		Query typeQuery = em.createQuery(ALL_DETAIL_DATA_QUERY_STRING)
				.setParameter("companyCode", companyCode)
				.setParameter("sparePayAtr", query.isAggreatePreliminaryMonth ? 1 : 0)
				.setParameter("startProcessingYM", startYearMonth.v())
				.setParameter("endProcessingYM", endYearMonth.v());

		// Query data.
		List<Object[]> masterResultList = new ArrayList<>();
		CollectionUtil.split(query.employeeIds, 1000, (subList) -> {
			masterResultList.addAll(typeQuery.setParameter("personIds", subList).getResultList());
		});
		if (query.outputType == OutputType.MasterItems) {
			return new ResultData(masterResultList);
		}
		
		// ===================== Query by output setting.=====================
		// Find Output setting.
		Optional<WLOutputSetting> optOutputSetting = this.outputSettingRepository.findByCode(companyCode,
				new WLOutputSettingCode(query.outputSettingCode));
		if (!optOutputSetting.isPresent()) {
			throw new BusinessException("Entity not found.");
		}
		WLOutputSetting outputSetting = optOutputSetting.get();
		
		// Find Aggregate item in output setting.
		List<String> aggregateItemCode = outputSetting.getCategorySettings().stream()
				.map(cate -> cate.getOutputItems().stream().filter(item -> item.getType() == WLItemType.Aggregate)
						.map(item -> item.getLinkageCode()).collect(Collectors.toList()))
				.flatMap(items -> items.stream()).collect(Collectors.toList());
		List<WLAggregateItem> aggregateItems = this.aggregateItemRepository.findByCodes(companyCode, aggregateItemCode);
		
		// Aggregate master item and aggregate item in output setting.
		Map<String, List<Object[]>> userMap = masterResultList.stream()
				.collect(Collectors.groupingBy(item -> ((QstdtPaymentDetail) item[0]).qstdtPaymentDetailPK.personId));
		List<Object[]> outputSettingDataList = new ArrayList<>();
		userMap.forEach((userId, userData) -> {
			Map<QcamtItem, List<Object[]>> dataMaps = userData.stream()
					.collect(Collectors.groupingBy(data -> (QcamtItem) data[1]));
			outputSettingDataList.addAll(this.filterDataByOutputSetting(companyCode, dataMaps, outputSetting,
					aggregateItems, userId));
		});
		
		return new ResultData(masterResultList, outputSettingDataList);
	}
	
	/**
	 * Filter data by output setting.
	 *
	 * @param companyCode the company code
	 * @param dataMaps the data maps
	 * @param outputSetting the output setting
	 * @param aggregateItems the aggregate items
	 * @param persionId the persion id
	 * @return the list
	 */
	private List<Object[]> filterDataByOutputSetting(String companyCode, Map<QcamtItem, List<Object[]>> dataMaps,
			WLOutputSetting outputSetting, List<WLAggregateItem> aggregateItems, String persionId) {
		List<Object[]> results = new ArrayList<>();

		outputSetting.getCategorySettings().forEach(category -> {
			category.getOutputItems().forEach(outputItem -> {
				// Master item.
				if (outputItem.getType() == WLItemType.Master) {
					QcamtItem masterItem = new QcamtItem();
					masterItem.qcamtItemPK = new QcamtItemPK();
					masterItem.qcamtItemPK.ccd = companyCode;
					masterItem.qcamtItemPK.ctgAtr = category.getCategory().value;
					masterItem.qcamtItemPK.itemCd = outputItem.getLinkageCode();
					results.addAll(dataMaps.get(masterItem).stream().filter(detail -> {
						return ((QstdtPaymentDetail) detail[0]).qstdtPaymentDetailPK.payBonusAttribute == category
								.getPaymentType().value;
					}).collect(Collectors.toList()));
					return;
				}
				
				// Aggregate item.
				WLAggregateItem aggregateItem = aggregateItems.stream()
						.filter(item -> item.getSubject().getCategory() == category.getCategory()
								&& item.getSubject().getPaymentType() == category.getPaymentType()
								&& item.getSubject().getCode().v().equals(outputItem.getLinkageCode()))
						.findFirst().get();
				if (CollectionUtil.isEmpty(aggregateItem.getSubItems())) {
					return;
				}
				List<QcamtItem> masterItemList = aggregateItem.getSubItems().stream()
						.map(subItem -> {
							QcamtItem masterItem = new QcamtItem();
							masterItem.qcamtItemPK = new QcamtItemPK();
							masterItem.qcamtItemPK.ccd = companyCode;
							masterItem.qcamtItemPK.ctgAtr = category.getCategory().value;
							masterItem.qcamtItemPK.itemCd = subItem;
							return masterItem;
						}).collect(Collectors.toList());
				
				// Calculate aggregate item.
				Map<Integer, Double> aggregateValueMap = new HashMap<>();
				masterItemList.forEach(item -> {
					List<Object[]> data = dataMaps.get(item);
					if (CollectionUtil.isEmpty(data)) {
						return;
					}
					// Group data by year month.
					Map<Integer, List<Object[]>> monthMap = data.stream().collect(Collectors.groupingBy(
							monthData -> ((QstdtPaymentDetail) monthData[0]).qstdtPaymentDetailPK.processingYM));
					
					monthMap.forEach((yearMonth, monthData) -> {
						
						// Calculate aggregate item value.
						double aggregateValue = monthData.stream()
								.filter(dataItem -> ((QstdtPaymentDetail) dataItem[0])
										.qstdtPaymentDetailPK.payBonusAttribute == category.getPaymentType().value)
								.mapToDouble(dataItem -> ((QstdtPaymentDetail) dataItem[0]).value.doubleValue())
								.sum();
						aggregateValueMap.put(yearMonth, aggregateValue);
					});
				});
				
				// Create data object.
				Object[] aggreateItemData = new Object[4];
				QcamtItem masterItem = masterItemList.get(0);
				masterItem.qcamtItemPK.itemCd = aggregateItem.getSubject().getCode().v() + "_A";
				masterItem.itemName = aggregateItem.getName().v();
				aggreateItemData[1] = masterItem;
				QstdtPaymentDetail paymentDetail = new QstdtPaymentDetail();
				paymentDetail.qstdtPaymentDetailPK = new QstdtPaymentDetailPK();
				paymentDetail.qstdtPaymentDetailPK.categoryATR = category.getCategory().value;
				paymentDetail.qstdtPaymentDetailPK.companyCode = companyCode;
				paymentDetail.qstdtPaymentDetailPK.personId = persionId;
				paymentDetail.qstdtPaymentDetailPK.payBonusAttribute = category.getPaymentType().value;
				paymentDetail.qstdtPaymentDetailPK.itemCode = aggregateItem.getSubject().getCode().v();
				aggreateItemData[2] = aggregateItem.getShowNameZeroValue();
				aggreateItemData[3] = aggregateItem.getShowValueZeroValue();
				
				// Check if none data for aggregate item.
				if (aggregateValueMap.isEmpty()) {
					paymentDetail.value = new BigDecimal(0);
					aggreateItemData[0] = paymentDetail;
					results.add(aggreateItemData);
					return;
				}
				
				// Add item to result list.
				aggregateValueMap.forEach((yearMonth, value) -> {
					paymentDetail.qstdtPaymentDetailPK.processingYM = yearMonth;
					paymentDetail.value = new BigDecimal(value);
					aggreateItemData[0] = paymentDetail;
					results.add(aggreateItemData);
				});
			});
		});
		return results;
	}

	/**
	 * Covert to new layout data list.
	 *
	 * @param resultData the result data
	 * @param query the query
	 * @param companyCode the company code
	 * @return the list
	 */
	private List<WLNewLayoutReportData> covertToNewLayoutDataList(ResultData resultData, WageLedgerReportQuery query,
			String companyCode) {
		List<WLNewLayoutReportData> reportList = new ArrayList<>();
		MonthData monthData = new MonthData();
		// Query Header data.
		Map<String, HeaderReportData> headerDataMap = this.findHeaderDatas(companyCode, query);

		// Group by user.
		Map<String, List<Object[]>> userMap = resultData.reportItemData.stream()
				.collect(Collectors.groupingBy(item -> ((QstdtPaymentDetail) item[0]).qstdtPaymentDetailPK.personId));
		Map<String, List<Object[]>> userAllMasterDataMap = resultData.allMasterItemData.stream()
				.collect(Collectors.groupingBy(item -> ((QstdtPaymentDetail) item[0]).qstdtPaymentDetailPK.personId));
		Map<String, BeforeEndYearData> userEndYearDataMap = this.findBeforeEndYearData(companyCode, query);
		Map<String, Map<Integer, GeneralDate>> userSalaryPaymentDateMap = this.findPaymentDate(companyCode, query,
				PaymentType.Salary);
		Map<String, Map<Integer, GeneralDate>> userBonusPaymentDateMap = this.findPaymentDate(companyCode, query,
				PaymentType.Bonus);
		List<QcamtItem> masterItems = this.getEntityManager().createQuery(MASTER_ITEM_QUERY_STRING, QcamtItem.class)
				.setParameter("companyCode", companyCode).getResultList();
		
		// Convert to report data model.
		headerDataMap.forEach((personId, headerData)-> {
			List<Object[]> detailData = userMap.get(personId);
			List<Object[]> allMasterItemData = userAllMasterDataMap.get(personId);
			if (CollectionUtil.isEmpty(detailData)) {
				throw new BusinessException(new RawErrorMessage("対象データがありません。"));
			}
			// =========================== Payment Date. ===========================
			Map<Integer, GeneralDate> salaryPayDateMap = userSalaryPaymentDateMap.get(personId);
			if (salaryPayDateMap == null) {
				salaryPayDateMap = new HashMap<>();
			}
			Map<Integer, GeneralDate> bonusPayDateMap = userBonusPaymentDateMap.get(personId);
			if (bonusPayDateMap == null) {
				bonusPayDateMap = new HashMap<>();
			}
			
			// =========================== Total Data. ===========================
			ResultData userResultData = new ResultData(allMasterItemData, detailData);
			TotalData salaryTotalData = this.findTotalDataItems(userResultData, PaymentType.Salary, monthData,
					masterItems);
			TotalData bonusTotalData = this.findTotalDataItems(userResultData, PaymentType.Bonus, monthData,
					masterItems);
			
			// =========================== Salary payment Data. ===========================
			List<ReportItemDto> salaryPaymentItems = new ArrayList<>(
					this.convertMasterResultDatasToItemMap(userResultData.reportItemData,
							monthData, PaymentType.Salary, WLCategory.Payment).values());
			
			// =========================== Salary Deduction Data. ===========================
			List<ReportItemDto> salaryDeductionItems = new ArrayList<>(
					this.convertMasterResultDatasToItemMap(userResultData.reportItemData,
							monthData, PaymentType.Salary, WLCategory.Deduction).values());
			
			// =========================== Salary Attendance Data. ===========================
			List<ReportItemDto> salaryAttendanceItems = new ArrayList<>(
					this.convertMasterResultDatasToItemMap(userResultData.reportItemData,
							monthData, PaymentType.Salary, WLCategory.Attendance).values());
			
			// =========================== Bonus Payment Data. ===========================
			List<ReportItemDto> bonusPaymentItems = new ArrayList<>(
					this.convertMasterResultDatasToItemMap(userResultData.reportItemData,
							monthData, PaymentType.Bonus, WLCategory.Payment).values());
			
			// =========================== Bonus Deduction Data. ===========================
			List<ReportItemDto> bonusDeductionItems = new ArrayList<>(
					this.convertMasterResultDatasToItemMap(userResultData.reportItemData,
							monthData, PaymentType.Bonus, WLCategory.Deduction).values());
			
			// =========================== Bonus Attendance Data. ===========================
			List<ReportItemDto> bonusAttendanceItems = new ArrayList<>(
					this.convertMasterResultDatasToItemMap(userResultData.reportItemData,
							monthData, PaymentType.Bonus, WLCategory.Payment).values());
			
			// Create report data model.
			WLNewLayoutReportData data = WLNewLayoutReportData.builder()
					.headerData(headerData)
					.salaryTotalData(salaryTotalData)
					.bonusTotalData(bonusTotalData)
					.salaryPaymentItems(salaryPaymentItems)
					.salaryDeductionItems(salaryDeductionItems)
					.salaryAttendanceItems(salaryAttendanceItems)
					.bonusPaymentItems(bonusPaymentItems)
					.bonusDeductionItems(bonusDeductionItems)
					.bonusAttendanceItems(bonusAttendanceItems)
					.beforeEndYearData(userEndYearDataMap.get(personId))
					.salaryPaymentDateMap(salaryPayDateMap)
					.bonusPaymentDateMap(bonusPayDateMap)
					.build();
			
			reportList.add(data);
		});
		return reportList;
	}
	
	/**
	 * Find before end year data.
	 *
	 * @param companyCode the company code
	 * @param queryData the query data
	 * @return the map
	 */
	private Map<String, BeforeEndYearData> findBeforeEndYearData(String companyCode, WageLedgerReportQuery queryData) {
		EntityManager em = this.getEntityManager();
		
		// Create Query.
		TypedQuery<QyedtYearendDetail> query = em
				.createQuery(BEFORE_END_YEAR_DATA_QUERY_STRING, QyedtYearendDetail.class)
				.setParameter("companyCode", companyCode)
				.setParameter("year", queryData.targetYear);
		// Query data.
		List<QyedtYearendDetail> datasResult = new ArrayList<>();
		CollectionUtil.split(queryData.employeeIds, 1000, (subList) -> {
			datasResult.addAll(query.setParameter("personIds", subList).getResultList());
		});
		
		// Convert to Dto.
		Map<String, BeforeEndYearData> resultMap = new HashMap<>();
		datasResult.stream().collect(Collectors.groupingBy(data -> data.getQyedtYearendDetailPK().getPid()))
				.forEach((id, data) -> {
					Long totalTaxPreviousPosition = 0l;
					Long totalTaxOtherMoney = 0l;
					Long totalSocialInsurancePreviousPosition = 0l;
					Long totalSocialInsuranceOtherMoney = 0l;
					Long acquisitionTaxPreviousPosition = 0l;
					Long acquisitionTaxOtherMoney = 0l;
					for (QyedtYearendDetail yearendDetail : data) {
						switch (yearendDetail.getQyedtYearendDetailPK().getAdjItemNo()) {
						case 47:
							totalTaxPreviousPosition = yearendDetail.getValNumber().longValue();
							break;
						case 50:
							totalTaxOtherMoney = yearendDetail.getValNumber().longValue();
							break;
						case 49:
							totalSocialInsurancePreviousPosition = yearendDetail.getValNumber().longValue();
							break;
						case 52:
							totalSocialInsuranceOtherMoney = yearendDetail.getValNumber().longValue();
							break;
						case 48:
							acquisitionTaxPreviousPosition = yearendDetail.getValNumber().longValue();
							break;
						case 51:
							acquisitionTaxOtherMoney = yearendDetail.getValNumber().longValue();
							break;
						default:
							break;
						}
					}
					resultMap.put(id, BeforeEndYearData.builder()
						.totalSocialInsuranceOtherMoney(totalSocialInsuranceOtherMoney)
						.totalSocialInsurancePreviousPosition(totalSocialInsurancePreviousPosition)
						.totalTaxOtherMoney(totalTaxOtherMoney)
						.totalTaxPreviousPosition(totalTaxPreviousPosition)
						.acquisitionTaxOtherMoney(acquisitionTaxOtherMoney)
						.acquisitionTaxPreviousPosition(acquisitionTaxPreviousPosition)
						.build());
				});
		return resultMap;
	}
	
	/**
	 * Find total data items.
	 *
	 * @param resultData the result data
	 * @param paymentType the payment type
	 * @param monthData the month data
	 * @param masterItems the master items
	 * @return the total data
	 */
	private TotalData findTotalDataItems(ResultData resultData, PaymentType paymentType, MonthData monthData,
			List<QcamtItem> masterItems) {
		// Total tax.
		ItemData totalTaxItemData = new ItemData(paymentType.value,
				WLCategory.Payment.value, TOTAL_TAX_ITEM_CODE);
		ReportItemDto totalTaxItem = this.findItem(resultData.allMasterItemData, masterItems, totalTaxItemData,
				monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_TAX_ITEM_CODE, paymentType);

		// Total Tax Exemption.
		ItemData totalTaxExemptionItemData = new ItemData(paymentType.value, WLCategory.Payment.value,
				TOTAL_TAX_EXEMPTION_ITEM_CODE);
		ReportItemDto totalTaxExemptionItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalTaxExemptionItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_TAX_EXEMPTION_ITEM_CODE, paymentType);

		// Total payment.
		ItemData totalPaymentItemData = new ItemData(paymentType.value, WLCategory.Payment.value,
				TOTAL_PAYMENT_ITEM_CODE);
		ReportItemDto totalPaymentItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalPaymentItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_PAYMENT_ITEM_CODE, paymentType);

		// Total Social Insurance.
		ItemData totalSocialInsuranceItemData = new ItemData(paymentType.value, WLCategory.Deduction.value,
				TOTAL_SOCIAL_INSURANCE_ITEM_CODE);
		ReportItemDto totalSocialInsuranceItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalSocialInsuranceItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_SOCIAL_INSURANCE_ITEM_CODE, paymentType);

		// Total Taxable.
		ItemData totalTaxableItemData = new ItemData(paymentType.value, WLCategory.Deduction.value,
				TOTAL_TAXABLE_ITEM_CODE);
		ReportItemDto totalTaxableItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalTaxableItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_TAXABLE_ITEM_CODE, paymentType);

		// Total Income Tax.
		ItemData totalIncomeTaxItemData = new ItemData(paymentType.value, WLCategory.Deduction.value,
				TOTAL_INCOME_TAX_ITEM_CODE);
		ReportItemDto totalIncomeTax = this.findItem(resultData.allMasterItemData, masterItems,
				totalIncomeTaxItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_INCOME_TAX_ITEM_CODE, paymentType);

		// Total Inhabitant Tax.
		ItemData totalInhabitantTaxItemData = new ItemData(paymentType.value, WLCategory.Deduction.value,
				TOTAL_INBAHATANT_TAX_ITEM_CODE);
		ReportItemDto totalInhabitantTaxItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalInhabitantTaxItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData,
				TOTAL_INBAHATANT_TAX_ITEM_CODE, paymentType);

		// Total Deduction Item.
		ItemData totalDeductionItemData = new ItemData(paymentType.value, WLCategory.Deduction.value,
				TOTAL_DEDUCTION_ITEM_CODE);
		ReportItemDto totalDeductionItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalDeductionItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData, TOTAL_DEDUCTION_ITEM_CODE, paymentType);

		// Total Real item (real salary or real bonus).
		ItemData totalRealItemData = new ItemData(paymentType.value,
				ARTICLE_CATEGORY, TOTAL_REAL_ITEM_CODE);
		ReportItemDto totalRealItem = this.findItem(resultData.allMasterItemData, masterItems,
				totalRealItemData, monthData);
		this.removeItemsInDataList(resultData.reportItemData, 
				TOTAL_REAL_ITEM_CODE, paymentType);
		
		// Return.
		return TotalData.builder()
				.totalTax(totalTaxItem)
				.totalTaxExemption(totalTaxExemptionItem)
				.totalDeduction(totalDeductionItem)
				.totalIncomeTax(totalIncomeTax)
				.totalInhabitantTax(totalInhabitantTaxItem)
				.totalPayment(totalPaymentItem)
				.totalReal(totalRealItem)
				.totalSocialInsurance(totalSocialInsuranceItem)
				.totalTaxable(totalTaxableItem)
				.build();
	}
	
	/**
	 * Removes the items in data list.
	 *
	 * @param dataList the data list
	 * @param itemCode the item code
	 * @param paymentType the payment type
	 */
	private void removeItemsInDataList(List<Object[]> dataList, String itemCode, PaymentType paymentType) {
		// Find item in data list.
		List<Object[]> foundItems = dataList.stream().filter(data -> {
			QcamtItem masterItem = (QcamtItem) data[1];
			return masterItem.qcamtItemPK.ctgAtr == paymentType.value
					&& masterItem.qcamtItemPK.itemCd.equals(itemCode);
		}).collect(Collectors.toList());
		dataList.removeAll(foundItems);
	}

	/**
	 * Convert to old layout data list.
	 *
	 * @param resultData the result data
	 * @param query the query
	 * @param companyCode the company code
	 * @return the list
	 */
	private List<WLOldLayoutReportData> convertToOldLayoutDataList(ResultData resultData, WageLedgerReportQuery query,
			String companyCode) {
		List<WLOldLayoutReportData> dataList = new ArrayList<>();
		MonthData monthData = new MonthData();
		// Query Header data.
		Map<String, HeaderReportData> headerDataMap = this.findHeaderDatas(companyCode, query);

		// Group by user.
		Map<String, List<Object[]>> userMap = resultData.reportItemData.stream()
				.collect(Collectors.groupingBy(item -> ((QstdtPaymentDetail) item[0]).qstdtPaymentDetailPK.personId));
		Map<String, List<Object[]>> userAllMasterDataMap = resultData.allMasterItemData.stream()
				.collect(Collectors.groupingBy(item -> ((QstdtPaymentDetail) item[0]).qstdtPaymentDetailPK.personId));
		List<QcamtItem> masterItems = this.getEntityManager().createQuery(MASTER_ITEM_QUERY_STRING, QcamtItem.class)
				.setParameter("companyCode", companyCode)
				.getResultList();
		
		// Convert to report data model.
		headerDataMap.forEach((personId, headerData) -> {
			List<Object[]> detailData = userMap.get(personId);
			List<Object[]> allMasterItemData = userAllMasterDataMap.get(personId);
			if (CollectionUtil.isEmpty(detailData)) {
				throw new BusinessException(new RawErrorMessage("対象データがありません。"));
			}

			// =============== Salary payment data ===========================
			Map<QcamtItem, ReportItemDto> salaryPaymentItemsMap = this
					.convertMasterResultDatasToItemMap(detailData, monthData, PaymentType.Salary, WLCategory.Payment);
			PaymentData salaryPaymentData = this.convertToPaymentData(salaryPaymentItemsMap, allMasterItemData,
					PaymentType.Salary, masterItems, monthData);

			// =============== Bonus payment data ===========================
			Map<QcamtItem, ReportItemDto> bonusPaymentItemsMap = this.convertMasterResultDatasToItemMap(detailData,
					monthData, PaymentType.Bonus, WLCategory.Payment);
			PaymentData bonusPaymentData = this.convertToPaymentData(bonusPaymentItemsMap, allMasterItemData,
					PaymentType.Bonus, masterItems, monthData);

			// =============== Salary Deduction data ===========================
			// Convert result list to item map.
			Map<QcamtItem, ReportItemDto> salaryDeductionItemsMap = this
					.convertMasterResultDatasToItemMap(detailData, monthData, PaymentType.Salary, WLCategory.Deduction);
			DeductionData salaryDeductionData = this.convertToDeductionData(salaryDeductionItemsMap, allMasterItemData,
					PaymentType.Salary, masterItems, monthData);

			// =============== Bonus Deduction data ===========================
			Map<QcamtItem, ReportItemDto> bonusDeductionItemsMap = this
					.convertMasterResultDatasToItemMap(detailData, monthData, PaymentType.Bonus, WLCategory.Deduction);
			DeductionData bonusDeductionData = this.convertToDeductionData(bonusDeductionItemsMap, allMasterItemData,
					PaymentType.Bonus, masterItems, monthData);

			// =============== Net salary item ===========================
			ItemData netSalaryItemData = new ItemData(PaymentType.Salary.value, ARTICLE_CATEGORY, TOTAL_REAL_ITEM_CODE);
			ReportItemDto netSalaryItem = this.findItem(allMasterItemData, masterItems, netSalaryItemData, monthData);

			// =============== Total bonus item ===========================
			ItemData totalBonusItemData = new ItemData(PaymentType.Bonus.value, ARTICLE_CATEGORY, TOTAL_REAL_ITEM_CODE);
			ReportItemDto totalBonusItem = this.findItem(allMasterItemData, masterItems, totalBonusItemData, monthData);

			// =============== Salary Attendance Data. ===========================
			Map<QcamtItem, ReportItemDto> salaryAttendanceItemsMap = this.convertMasterResultDatasToItemMap(
					detailData, monthData, PaymentType.Salary, WLCategory.Attendance);

			// =============== Bonus Attendance Data ===========================
			Map<QcamtItem, ReportItemDto> bonusAttendanceItemsMap = this
					.convertMasterResultDatasToItemMap(detailData, monthData, PaymentType.Bonus, WLCategory.Attendance);
			
			// Create report data model.
			WLOldLayoutReportData data = WLOldLayoutReportData.builder()
					.headerData(headerData)
					.salaryPaymentData(salaryPaymentData)
					.bonusPaymentData(bonusPaymentData)
					.salaryDeductionData(salaryDeductionData)
					.bonusDeductionData(bonusDeductionData)
					.netSalaryData(netSalaryItem)
					.totalBonusData(totalBonusItem)
					.salaryAttendanceDatas(new ArrayList<>(salaryAttendanceItemsMap.values()))
					.bonusAttendanceDatas(new ArrayList<>(bonusAttendanceItemsMap.values()))
					.salaryMonthList(monthData.salaryMonths)
					.bonusMonthList(monthData.bonusMonths)
					.build();

			// Add to report data list.
			dataList.add(data);
		});
		return dataList;
	}

	/**
	 * Find item.
	 *
	 * @param results the results
	 * @param masterItems the master items
	 * @param itemData the item data
	 * @param monthData the month data
	 * @return the report item dto
	 */
	private ReportItemDto findItem(List<Object[]> results, List<QcamtItem> masterItems,
			ItemData itemData, MonthData monthData) {
		boolean isShowName = true;
		boolean isShowValue = true;
		
		// Find master item
		QcamtItem masterItem = masterItems.stream()
				.filter(item -> {
					return item.qcamtItemPK.itemCd.equals(itemData.itemCode);
				}).findFirst().get();

		// Filter by conditions
		List<Object[]> itemDataResultList = results.stream().filter(res -> {
			QstdtPaymentDetail paymentDetail = (QstdtPaymentDetail) res[0];
			return paymentDetail.qstdtPaymentDetailPK.categoryATR == itemData.category
					&& paymentDetail.qstdtPaymentDetailPK.payBonusAttribute == itemData.paymentType
					&& paymentDetail.qstdtPaymentDetailPK.itemCode.equals(itemData.itemCode);
		}).collect(Collectors.toList());
		if (!itemDataResultList.isEmpty() && itemDataResultList.get(0).length > 2) {
			isShowName = (Boolean) itemDataResultList.get(0)[2];
			isShowValue = (Boolean) itemDataResultList.get(0)[3];
		}

		// Convert data.
		List<MonthlyData> monthlyDatas = itemDataResultList.stream().map(data -> {
			QstdtPaymentDetail paymentDetail = (QstdtPaymentDetail) data[0];
			YearMonth processYM = YearMonth.of(paymentDetail.qstdtPaymentDetailPK.processingYM);
			int month = processYM.month();

			// Add month to salary month List.
			if (itemData.paymentType == PaymentType.Salary.value && !monthData.salaryMonths.contains(month)) {
				monthData.salaryMonths.add(month);
			}
			// Add month to bonus month List.
			if (itemData.paymentType == PaymentType.Bonus.value && !monthData.bonusMonths.contains(month)) {
				monthData.bonusMonths.add(month);
			}

			return MonthlyData.builder()
					.month(processYM.month())
					.amount(paymentDetail.value.longValue())
					.build();
		}).collect(Collectors.toList());
		
		// Return.
		return ReportItemDto.builder()
				.name(masterItem.itemName)
				.monthlyDatas(monthlyDatas)
				.isShowName(isShowName)
				.isShowValue(isShowValue)
				.build();
	}

	/**
	 * Convert to payment data.
	 *
	 * @param resultsMap the results map
	 * @param allData the all data
	 * @param paymentType the payment type
	 * @param masterItems the master items
	 * @param monthData the month data
	 * @return the payment data
	 */
	private PaymentData convertToPaymentData(Map<QcamtItem, ReportItemDto> resultsMap, List<Object[]> allData,
			PaymentType paymentType, List<QcamtItem> masterItems, MonthData monthData) {
		List<ReportItemDto> items = new ArrayList<>(resultsMap.values());
		
		// Create total tax item.
		ItemData totalTaxResultItemData = new ItemData(paymentType.value, WLCategory.Payment.value,
				TOTAL_TAX_ITEM_CODE);
		ReportItemDto totalTaxReportItem = this.findItem(allData, masterItems, totalTaxResultItemData, monthData);
		items.remove(totalTaxReportItem);

		// Create total tax exemption item.
		ItemData totalTaxExemptionResultItemData = new ItemData(paymentType.value, WLCategory.Payment.value,
				TOTAL_TAX_EXEMPTION_ITEM_CODE);
		ReportItemDto totalTaxExemptionReportItem = this.findItem(allData, masterItems, totalTaxExemptionResultItemData,
				monthData);
		items.remove(totalTaxExemptionReportItem);

		// Create total salary payment item.
		ItemData totalSalaryPaymentResultItemData = new ItemData(paymentType.value, WLCategory.Payment.value,
				TOTAL_PAYMENT_ITEM_CODE);
		ReportItemDto totalSalaryPaymentReportItem = this.findItem(allData, masterItems,
				totalSalaryPaymentResultItemData, monthData);
		items.remove(totalSalaryPaymentReportItem);
		
		return PaymentData.builder()
				.aggregateItemList(items)
				.totalTax(totalTaxReportItem)
				.totalTaxExemption(totalTaxExemptionReportItem)
				.totalSalaryPayment(totalSalaryPaymentReportItem)
				.build();
	}

	/**
	 * Convert to deduction data.
	 *
	 * @param resultsMap the results map
	 * @param allData the all data
	 * @param paymentType the payment type
	 * @param masterItems the master items
	 * @param monthData the month data
	 * @return the deduction data
	 */
	private DeductionData convertToDeductionData(Map<QcamtItem, ReportItemDto> resultsMap, List<Object[]> allData,
			PaymentType paymentType, List<QcamtItem> masterItems, MonthData monthData) {
		List<ReportItemDto> items = new ArrayList<>(resultsMap.values());

		// Create total deduction item.
		ItemData totalDeductionItemData = new ItemData(paymentType.value, WLCategory.Deduction.value,
				TOTAL_DEDUCTION_ITEM_CODE);
		ReportItemDto totalDeductionReportItem = this.findItem(allData, masterItems, totalDeductionItemData, monthData);
		items.remove(totalDeductionReportItem);

		return DeductionData.builder()
				.aggregateItemList(items)
				.totalDeduction(totalDeductionReportItem)
				.build();
	}

	/**
	 * Convert master result datas to item map.
	 *
	 * @param results the results
	 * @param monthData the month data
	 * @param paymentType the payment type
	 * @param category the category
	 * @return the map
	 */
	private Map<QcamtItem, ReportItemDto> convertMasterResultDatasToItemMap(List<Object[]> results,
			MonthData monthData, PaymentType paymentType, WLCategory category) {
		// Filter result list by payment type and category.
		List<Object[]> filteredResultList = results.stream().filter(res -> {
			QstdtPaymentDetail paymentDetail = (QstdtPaymentDetail) res[0];
			return paymentDetail.qstdtPaymentDetailPK.categoryATR == category.value
					&& paymentDetail.qstdtPaymentDetailPK.payBonusAttribute == paymentType.value;
		}).collect(Collectors.toList());

		Map<QcamtItem, ReportItemDto> items = new HashMap<>();
		// Group result list by master item.
		Map<QcamtItem, List<Object[]>> itemListMap = filteredResultList.stream()
				.collect(Collectors.groupingBy(res -> (QcamtItem) res[1]));
		itemListMap.forEach((masterItem, datas) -> {
			boolean isShowName = true;
			boolean isShowValue = true;
			if (!datas.isEmpty() && datas.get(0).length > 2) {
				isShowName = (Boolean) datas.get(0)[2];
				isShowValue = (Boolean) datas.get(0)[3];
			}
			List<MonthlyData> monthlyDatas = datas.stream().map(data -> {
				QstdtPaymentDetail paymentDetail = (QstdtPaymentDetail) data[0];
				
				// Check have data in payment detail.
				if (paymentDetail.qstdtPaymentDetailPK.processingYM == 0) {
					return MonthlyData.builder().month(0).amount(paymentDetail.value.longValue()).build();
				}
				YearMonth processYM = YearMonth.of(paymentDetail.qstdtPaymentDetailPK.processingYM);
				int month = processYM.month();

				// Add month to salary month List.
				if (paymentDetail.qstdtPaymentDetailPK.payBonusAttribute == PaymentType.Salary.value
						&& !monthData.salaryMonths.contains(month)) {
					monthData.salaryMonths.add(month);
				}
				// Add month to bonus month List.
				if (paymentDetail.qstdtPaymentDetailPK.payBonusAttribute == PaymentType.Bonus.value
						&& !monthData.bonusMonths.contains(month)) {
					monthData.bonusMonths.add(month);
				}

				return MonthlyData.builder().month(month).amount(paymentDetail.value.longValue()).build();
			}).collect(Collectors.toList());
			items.put(masterItem, ReportItemDto.builder()
					.code(masterItem.qcamtItemPK.itemCd)
					.name(masterItem.itemName)
					.monthlyDatas(monthlyDatas)
					.isShowName(isShowName)
					.isShowValue(isShowValue)
					.build());
		});

		return items;
	}

	/**
	 * Find header datas.
	 *
	 * @param companyCode the company code
	 * @param queryData the query data
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, HeaderReportData> findHeaderDatas(String companyCode, WageLedgerReportQuery queryData) {
		EntityManager em = this.getEntityManager();

		// Create Query.
		Query query = em.createQuery(HEADER_QUERY_STRING)
				.setParameter("companyCode", companyCode)
				.setParameter("baseDate", queryData.baseDate);

		// Query data.
		List<Object[]> headerDatasResult = new ArrayList<>();
		CollectionUtil.split(queryData.employeeIds, 1000, (subList) -> {
			headerDatasResult.addAll(query.setParameter("personIds", subList).getResultList());
		});
		
		// Query position of employee.
		List<String> jobCodes = headerDatasResult.stream().map(data -> {
			
			PclmtPersonTitleRgl personTitle = (PclmtPersonTitleRgl) data[4];
			return personTitle.getJobcd();
		}).distinct().collect(Collectors.toList());

		List<CmnmtJobTitle> jobList = em.createQuery(POSITION_QUERY_STRING, CmnmtJobTitle.class)
				.setParameter("companyCode", companyCode)
				.setParameter("jobCode", jobCodes)
				.setParameter("baseDate", queryData.baseDate)
				.getResultList();
		Map<String, CmnmtJobTitle> jobMap = jobList.stream()
				.collect(Collectors.toMap(job -> job.cmnmtJobTitlePK.jobCode, Function.identity()));

		// Convert to header data model.
		return headerDatasResult.stream().collect(Collectors.toMap(key -> {
			PbsmtPersonBase personBase = (PbsmtPersonBase) key[0];
			return personBase.getPid();
		}, data -> {
			PbsmtPersonBase personBase = (PbsmtPersonBase) data[0];
			PcpmtPersonCom personCompany = (PcpmtPersonCom) data[1];
			PogmtPersonDepRgl personDepartment = (PogmtPersonDepRgl) data[2];
			CmnmtDep departmentMaster = (CmnmtDep) data[3];
			PclmtPersonTitleRgl personTitle = (PclmtPersonTitleRgl) data[4];
			CmnmtJobTitle jobTitle = jobMap.get(personTitle.getJobcd());
			
			// Build Header Data.
			HeaderReportData headerReportData = HeaderReportData.builder().employeeCode(personCompany.getScd())
					.employeeName(personBase.getNameOfficial()).sex(personBase.getGendar() == 0 ? "男性" : "女性")
					.departmentCode(personDepartment.getDepcd()).departmentName(departmentMaster.getDepName())
					.targetYear(queryData.targetYear).position(jobTitle.jobName).build();
			return headerReportData;
		}));
	}
	
	/**
	 * Find payment date.
	 *
	 * @param companyCode the company code
	 * @param queryData the query data
	 * @param paymentType the payment type
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<Integer, GeneralDate>> findPaymentDate(String companyCode, WageLedgerReportQuery queryData,
			PaymentType paymentType) {
		EntityManager em = this.getEntityManager();
		// Create Year Month.
		YearMonth startYearMonth = YearMonth.of(queryData.targetYear, 1);
		YearMonth endYearMonth = YearMonth.of(queryData.targetYear, 12);
		
		// Create Query.
		Query query = em.createQuery(PAYMENT_DATE_QUERY_STRING)
				.setParameter("companyCode", companyCode)
				.setParameter("startProcessingYM", startYearMonth.v())
				.setParameter("endProcessingYM", endYearMonth.v())
				.setParameter("paymentType", paymentType.value);
		// Query data.
		List<Object[]> datasResult = new ArrayList<>();
		CollectionUtil.split(queryData.employeeIds, 1000, (subList) -> {
			datasResult.addAll(query.setParameter("personIds", subList).getResultList());
		});
		
		// Convert data to dto.
		return datasResult.stream().collect(Collectors.groupingBy(
				data -> ((QstdtPaymentHeader) data[0]).qstdtPaymentHeaderPK.personId, Collectors.toMap(data -> {
					YearMonth ym = YearMonth.of(((QpdmtPayday) data[1]).qpdmtPaydayPK.processingYm);
					return ym.month();
				}, data -> ((QpdmtPayday) data[1]).payDate)));
	}

	/**
	 * The Class MonthData.
	 */
	public class MonthData {
		
		/** The salary months. */
		public List<Integer> salaryMonths;
		
		/** The bonus months. */
		public List<Integer> bonusMonths;

		/**
		 * Instantiates a new month data.
		 */
		public MonthData() {
			this.salaryMonths = new ArrayList<>();
			this.bonusMonths = new ArrayList<>();
		}
	}

	/**
	 * The Class ItemData.
	 */
	public class ItemData {
		
		/** The payment type. */
		public int paymentType;

		/** The category. */
		public int category;

		/** The item code. */
		public String itemCode;

		/**
		 * Instantiates a new item data.
		 *
		 * @param paymentType the payment type
		 * @param category the category
		 * @param itemCode the item code
		 */
		public ItemData(int paymentType, int category, String itemCode) {
			super();
			this.paymentType = paymentType;
			this.category = category;
			this.itemCode = itemCode;
		}
	}
	
	/**
	 * The Class ResultData.
	 */
	public class ResultData {
		
		/** The all master item data. */
		public List<Object[]> allMasterItemData;
		
		/** The report item data. */
		public List<Object[]> reportItemData;
		
		/**
		 * Instantiates a new result data.
		 *
		 * @param allMasterItemData the all master item data
		 */
		public ResultData(List<Object[]> allMasterItemData) {
			this.allMasterItemData = allMasterItemData;
			this.reportItemData = allMasterItemData;
		}

		/**
		 * Instantiates a new result data.
		 *
		 * @param allMasterItemData the all master item data
		 * @param reportItemData the report item data
		 */
		public ResultData(List<Object[]> allMasterItemData, List<Object[]> reportItemData) {
			super();
			this.allMasterItemData = allMasterItemData;
			this.reportItemData = reportItemData;
		}
	}
}
