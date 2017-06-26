/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.accumulatedpayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.basic.infra.entity.organization.employment.CmnmtEmp;
import nts.uk.ctx.basic.infra.entity.report.PbsmtPersonBase;
import nts.uk.ctx.basic.infra.entity.report.PcpmtPersonCom;
import nts.uk.ctx.basic.infra.entity.report.QyedtYearendDetail;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentDetail;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.employmentcontract.PclmtPersonEmpContract;
import nts.uk.file.pr.app.export.accumulatedpayment.AccPaymentRepository;
import nts.uk.file.pr.app.export.accumulatedpayment.data.AccPaymentItemData;
import nts.uk.file.pr.app.export.accumulatedpayment.query.AccPaymentReportQuery;

/**
 * The Class JpaAccPaymentReportRepository.
 */
@Stateless
public class JpaAccPaymentReportRepository extends JpaRepository implements AccPaymentRepository {

	/** The Constant DATE_FORMAT. */

	private static final String DATE_FORMAT = "yyyyMMdd";

	/** The Constant START_DATE_OF_YEAR. */
	private static final String START_DATE_OF_YEAR = "0101";

	/** The Constant END_DATE_OF_YEAR. */
	private static final String END_DATE_OF_YEAR = "1231";

	/** The Constant PAY_BONUS_ATR. */
	private static final int PAY_BONUS_ATR = 1;

	/** The Constant SPARE_PAY_ATR. */
	private static final int SPARE_PAY_ATR = 0;

	/** The Constant PAYMENT_CATEGORY. */
	private static final int PAYMENT_CATEGORY = 0;

	/** The Constant DEDUCTION_CATEGORY. */
	private static final int DEDUCTION_CATEGORY = 1;

	/** The Constant ITEM_CD_F001. */
	private static final String ITEM_CD_F001 = "F001";

	/** The Constant ITEM_CD_F005. */
	private static final String ITEM_CD_F005 = "F005";

	/** The Constant ITEM_CD_F007. */
	private static final String ITEM_CD_F007 = "F007";

	/** The Constant REGULAR_COM. */
	private static final Short REGULAR_COM = 0;

	/** The Constant YEAR_ADJUSTMENT_ITEM_046. */
	private static final int YEAR_ADJUSTMENT_ITEM_046 = 46;

	/** The Constant YEAR_ADJUSTMENT_ITEM_047. */
	private static final int YEAR_ADJUSTMENT_ITEM_047 = 47;

	/** The Constant YEAR_ADJUSTMENT_ITEM_048. */
	private static final int YEAR_ADJUSTMENT_ITEM_048 = 48;

	/** The Constant YEAR_ADJUSTMENT_ITEM_049. */
	private static final int YEAR_ADJUSTMENT_ITEM_049 = 49;

	/** The Constant YEAR_ADJUSTMENT_ITEM_050. */
	private static final int YEAR_ADJUSTMENT_ITEM_050 = 50;

	/** The Constant YEAR_ADJUSTMENT_ITEM_051. */
	private static final int YEAR_ADJUSTMENT_ITEM_051 = 51;

	/** The Constant RETIRED. */
	private static final String RETIRED = "退職者";

	/** The Constant ENROLMENT. */
	private static final String ENROLMENT = "在籍者";

	/** The Constant SECONDMENT. */
	private static final String SECONDMENT = "出向中";

	/** The Constant BLANK. */
	private static final String BLANK = "";
	
	/** The Constant PERSON_BASE_TBL_INDEX. */
	private static final int PERSON_BASE_TBL_INDEX = 0;
	
	/** The Constant PERSON_COM_TBL_INDEX. */
	private static final int PERSON_COM_TBL_INDEX = 1;
	
	/** The Constant PERSON_EMP_CONTRACT_TBL_INDEX. */
	private static final int PERSON_EMP_CONTRACT_TBL_INDEX = 2;
	
	/** The Constant EMP_TBL_INDEX. */
	private static final int EMP_TBL_INDEX = 3;
	
	/** The Constant PAYMENT_DETAIL_TBL_INDEX. */
	private static final int PAYMENT_DETAIL_TBL_INDEX = 5;
	
	/** The Constant YEAR_END_DETAIL_TBL_INDEX. */
	private static final int YEAR_END_DETAIL_TBL_INDEX = 6;
	
	/** The Constant THOUSAND_NUMBER. */
	private static final int ONE_THOUSAND = 1000;

	/** The Constant QUERY_STRING. */
	private static final String QUERY_STRING = "SELECT p,  pc, ec, e, pd, pdt, yd "
			// a
			+ "FROM PbsmtPersonBase p, "
			// + "PcpmtPersonTempAssign a, "//To get the loan information of
			// individuals who meet the conditions // a demo
			+ "PcpmtPersonCom pc, " 
			+ "PclmtPersonEmpContract ec, " 
			+ "CmnmtEmp e, " 
			+ "QpdmtPayday pd, "
			+ "QstdtPaymentDetail pdt, "
			 + "QyedtYearendDetail yd "
			+ "WHERE p.pid in :PIDs "
			// + "AND a.pcpmtPersonTempAssignPK.ccd = :CCD "
			// + "AND a.pcpmtPersonTempAssignPK.pid = p.pid "
			+ "AND pc.pcpmtPersonComPK.ccd = :CCD " 
			+ "AND pc.pcpmtPersonComPK.pid = p.pid "
			+ "AND pc.regularCom = :REGULAR_COM "
			+ "AND ec.pclmtPersonEmpContractPK.ccd = :CCD "
			+ "AND ec.pclmtPersonEmpContractPK.pId = pc.pcpmtPersonComPK.pid "
			+ "AND ec.pclmtPersonEmpContractPK.strD <= :BASE_YMD " 
			+ "AND ec.endD >= :BASE_YMD "
			+ "AND e.cmnmtEmpPk.companyCode = ec.pclmtPersonEmpContractPK.ccd "
			+ "AND e.cmnmtEmpPk.employmentCode = ec.empCd " 
			+ "AND pd.qpdmtPaydayPK.ccd = :CCD "
			+ "AND pd.qpdmtPaydayPK.payBonusAtr = :PAY_BONUS_ATR " // 1
			+ "AND pd.qpdmtPaydayPK.processingNo = e.processingNo " 
			+ "AND pd.payDate >= :STR_YMD "
			+ "AND pd.payDate <= :END_YMD " 
			+ "AND pdt.qstdtPaymentDetailPK.companyCode = pd.qpdmtPaydayPK.ccd "
			+ "AND pdt.qstdtPaymentDetailPK.personId = p.pid "
			+ "AND pdt.qstdtPaymentDetailPK.processingNo = pd.qpdmtPaydayPK.processingNo "
			+ "AND pdt.qstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR "//1
			+ "AND pdt.qstdtPaymentDetailPK.processingYM = pd.qpdmtPaydayPK.processingYm "//0
			+ "AND pdt.qstdtPaymentDetailPK.sparePayAttribute = :SPARE_PAY_ATR "// 0
			+ "AND yd.qyedtYearendDetailPK.ccd = pdt.qstdtPaymentDetailPK.companyCode "
			+ "AND yd.qyedtYearendDetailPK.pid = p.pid " 
			+ "AND yd.qyedtYearendDetailPK.yearK = :YEAR_k ";

	/** The Constant CHECK_AT_PRINTING_QUERY. */
	private static final String CHECK_AT_PRINTING_QUERY = "SELECT pdt.qstdtPaymentDetailPK.personId, "
			+ "sum(pdt.value) "
			+ "FROM QstdtPaymentDetail pdt," 
			+ "CmnmtEmp e, " 
			+ "QpdmtPayday pd, " 
			+ "PclmtPersonEmpContract ec " 
			+ "WHERE ec.pclmtPersonEmpContractPK.pId in :PIDs " 
			+ "AND ec.pclmtPersonEmpContractPK.ccd = :CCD "
			+ "AND ec.pclmtPersonEmpContractPK.strD <= :BASE_YMD " 
			+ "AND ec.endD >= :BASE_YMD "
			+ "AND e.cmnmtEmpPk.companyCode = ec.pclmtPersonEmpContractPK.ccd "
			+ "AND e.cmnmtEmpPk.employmentCode = ec.empCd " 
			+ "AND pd.qpdmtPaydayPK.ccd = e.cmnmtEmpPk.companyCode "
			+ "AND pd.qpdmtPaydayPK.payBonusAtr = :PAY_BONUS_ATR "
			+ "AND pd.qpdmtPaydayPK.processingNo = e.processingNo " 
			+ "AND pd.payDate >= :STR_YMD "
			+ "AND pd.payDate <= :END_YMD " 
			+ "AND pdt.qstdtPaymentDetailPK.companyCode = pd.qpdmtPaydayPK.ccd "
			+ "AND pdt.qstdtPaymentDetailPK.personId = ec.pclmtPersonEmpContractPK.pId "
			+ "AND pdt.qstdtPaymentDetailPK.processingNo = pd.qpdmtPaydayPK.processingNo "
			+ "AND pdt.qstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR "
			+ "AND pdt.qstdtPaymentDetailPK.processingYM = pd.qpdmtPaydayPK.processingYm "
			+ "AND pdt.qstdtPaymentDetailPK.sparePayAttribute = :SPARE_PAY_ATR "
			+ "AND pdt.qstdtPaymentDetailPK.categoryATR = :CTG_ATR_0 "
			+ "AND pdt.qstdtPaymentDetailPK.itemCode = :ITEM_CD_F001 "
			+ "GROUP BY pdt.qstdtPaymentDetailPK.personId ";
	
	/*
	 * (non-Javadoc)
	 * @see nts.uk.file.pr.app.export.accumulatedpayment.AccPaymentRepository#
	 * getItems(java.lang.String,
	 * nts.uk.file.pr.app.export.accumulatedpayment.query.AccPaymentReportQuery)
	 */
	

	@SuppressWarnings("unchecked")
	@Override
	public List<AccPaymentItemData> getItems(String companyCode, AccPaymentReportQuery query) {
		// Initial General Variables
		GeneralVars general = new GeneralVars();
		general.companyCode = companyCode;
		general.query = query;
		
		// Check Having Conditions
		String havingQuery = "";
		List<String> havingCondition = new ArrayList<>();
		if (query.getIsLowerLimit()) {
			havingCondition.add("SUM(pdt.value) >= :LOWER_LIMIT_VALUE");
		}
		if (query.getIsUpperLimit()) {
			havingCondition.add("SUM(pdt.value) <= :UPPER_LIMIT_VALUE");
		}
		if (!CollectionUtil.isEmpty(havingCondition)) {
			havingQuery += " Having " + havingCondition.stream().collect(Collectors.joining(" AND "));
		}
		// Create Query and Set Parameters
		EntityManager em = this.getEntityManager();
		List<Object[]> masterResultList;
		String sqlQuery = CHECK_AT_PRINTING_QUERY + havingQuery;
		general.typedQuery = em.createQuery(sqlQuery)
				.setParameter("CTG_ATR_0", PAYMENT_CATEGORY)
				.setParameter("ITEM_CD_F001", ITEM_CD_F001);
		this.setGeneralParams(general);

		if (general.query.getIsLowerLimit()) {
			general.typedQuery.setParameter("LOWER_LIMIT_VALUE", general.query.getLowerLimitValue());
		}
		if (general.query.getIsUpperLimit()) {
			general.typedQuery.setParameter("UPPER_LIMIT_VALUE", general.query.getUpperLimitValue());
		}
		// Get Result List
		List<Object[]> resultList = new ArrayList<>();
		CollectionUtil.split(query.getPIdList(), ONE_THOUSAND, subList ->
			resultList.addAll(general.typedQuery.setParameter("PIDs",subList).getResultList())
		);
		List<String> pIdList = new ArrayList<>();
		resultList.stream().forEach(record -> {
			pIdList.add((String) record[0]);
		});
		
		// Check if Result List is Empty
		if (CollectionUtil.isEmpty(resultList)) {
			throw new BusinessException(new RawErrorMessage("対象データがありません。"));
		} 
		else {
			// Get Master Result List
			masterResultList = this.getMasterResultList(general, pIdList);
			if (CollectionUtil.isEmpty(masterResultList)) {
				throw new BusinessException(new RawErrorMessage("対象データがありません。"));
			}
		}
		return this.filterData(masterResultList, general);
	}

	/**
	 * Filter data.
	 *
	 * @param itemList the item list
	 * @param general the general
	 * @return the list
	 */
	private List<AccPaymentItemData> filterData(List<Object[]> itemList, GeneralVars general) {
		List<AccPaymentItemData> resultDataList = new ArrayList<>();
		// Group by EMP.
		Map<String, List<Object[]>> userMap = itemList.stream().collect(Collectors.groupingBy(item -> 
		((PbsmtPersonBase) item[PERSON_BASE_TBL_INDEX]).getPid()));
		for (Map.Entry<String, List<Object[]>> entry : userMap.entrySet()) {
			List<Object[]> detailData = entry.getValue();
			// Taxable Amount
			ItemDetails taxAmountDetails = this.setDetailsTaxAmount();
			Double taxAmount = this.sumValues(detailData, taxAmountDetails);

			// Social Insurance Total Amount
			ItemDetails socialInsDetails = this.setDetailsSocialIns();
			Double socialInsAmount = this.sumValues(detailData, socialInsDetails);

			// Withholding tax amount
			ItemDetails withholTaxDetails = this.setDetailsWithholdTax();
			Double withHoldingTax = this.sumValues(detailData, withholTaxDetails);
			
			String empCode = ((PclmtPersonEmpContract) detailData.get(0)
					[PERSON_EMP_CONTRACT_TBL_INDEX]).empCd;
			String empName = ((CmnmtEmp) detailData.get(0)[EMP_TBL_INDEX]).employmentName;
			PcpmtPersonCom person = (PcpmtPersonCom) detailData.get(0)[PERSON_COM_TBL_INDEX];
			GeneralDate endDatePersonTem = person.getEndD();
			String enrollmentStatus;
			if(endDatePersonTem == null){
				enrollmentStatus = ENROLMENT;
			}
			else if(endDatePersonTem.year() < general.query.getTargetYear().intValue()){
				enrollmentStatus = RETIRED;
			} 
			else {
				enrollmentStatus = ENROLMENT;
			}

			GeneralDate endDatePersonTempAsign = GeneralDate.today();
			// ((PcpmtPersonTempAssign) detailData.get(0)[1]).getEndD();
			String directionalStatus = endDatePersonTempAsign.year() > general.query.getTargetYear()
					? SECONDMENT : BLANK;
			// AccPaymentItemData
			AccPaymentItemData itemData = AccPaymentItemData.builder()
					.taxAmount(taxAmount)
					.empCode(empCode)
					.empName(empName)
					.taxAmount(taxAmount)
					.socialInsuranceAmount(socialInsAmount)
					.widthHoldingTaxAmount(withHoldingTax)
					.directionalStatus(directionalStatus)
					.enrollmentStatus(enrollmentStatus)
					.build();
			resultDataList.add(itemData);
		}
		return resultDataList;
	}
	
	/**
	 * Sets the details tax amount.
	 *
	 * @return the item details
	 */
	private ItemDetails setDetailsTaxAmount() {
		ItemDetails itemDetails = new ItemDetails();
		itemDetails.category = PAYMENT_CATEGORY;
		itemDetails.itemCode = ITEM_CD_F001;
		itemDetails.yearAdjusmentOne = YEAR_ADJUSTMENT_ITEM_046;
		itemDetails.yearAdjusmentTwo = YEAR_ADJUSTMENT_ITEM_049;
		return itemDetails;
	}
	
	/**
	 * Sets the details social ins.
	 *
	 * @return the item details
	 */
	private ItemDetails setDetailsSocialIns() {
		ItemDetails itemDetails = new ItemDetails();
		itemDetails.category = DEDUCTION_CATEGORY;
		itemDetails.itemCode = ITEM_CD_F005;
		itemDetails.yearAdjusmentOne = YEAR_ADJUSTMENT_ITEM_048;
		itemDetails.yearAdjusmentTwo = YEAR_ADJUSTMENT_ITEM_051;
		return itemDetails;
	}
	
	/**
	 * Sets the details withhold tax.
	 *
	 * @return the item details
	 */
	private ItemDetails setDetailsWithholdTax() {
		ItemDetails itemDetails = new ItemDetails();
		itemDetails.category = DEDUCTION_CATEGORY;
		itemDetails.itemCode = ITEM_CD_F007;
		itemDetails.yearAdjusmentOne = YEAR_ADJUSTMENT_ITEM_047;
		itemDetails.yearAdjusmentTwo = YEAR_ADJUSTMENT_ITEM_050;
		return itemDetails;
	}

	/**
	 * Sum values.
	 *
	 * @param detailData the detail data
	 * @param general the general
	 * @return the double
	 */
	private Double sumValues(List<Object[]> detailData, ItemDetails general) {
		// Payment Detail Value
		Double paymentDetailVal = detailData.stream().filter(data -> {
			QstdtPaymentDetail pdt = (QstdtPaymentDetail) data[PAYMENT_DETAIL_TBL_INDEX];
			return pdt.qstdtPaymentDetailPK.categoryATR == general.category && 
					pdt.qstdtPaymentDetailPK.itemCode.equals(general.itemCode);
		}).mapToDouble(result -> {
			if (result[PAYMENT_DETAIL_TBL_INDEX] instanceof QstdtPaymentDetail) {
				QstdtPaymentDetail pdt = (QstdtPaymentDetail) result[PAYMENT_DETAIL_TBL_INDEX];
				return pdt.value.doubleValue();
			}
			return 0;
		}).sum();

		// Value Number of Year End Detail
		Double yearEndNumVal = detailData.stream().filter(data -> {
			QyedtYearendDetail yd = (QyedtYearendDetail) data[YEAR_END_DETAIL_TBL_INDEX]; 
			return yd.getQyedtYearendDetailPK().getAdjItemNo() == general.yearAdjusmentOne ||
			yd.getQyedtYearendDetailPK().getAdjItemNo() == general.yearAdjusmentTwo;
		}).mapToDouble(result -> {
			QyedtYearendDetail yd = (QyedtYearendDetail) result[YEAR_END_DETAIL_TBL_INDEX]; 
			return yd.getValNumber().doubleValue();
		}).sum();
		// Sum above values
		return paymentDetailVal + yearEndNumVal;
	}

	/**
	 * Gets the master result list.
	 *
	 * @param general the general
	 * @param pIdList the id list
	 * @return the master result list
	 */
	@SuppressWarnings("unchecked")
	private List<Object[]> getMasterResultList(GeneralVars general, List<String> pIdList) {
		List<Object[]> masterResultList = new ArrayList<>();
		EntityManager em = this.getEntityManager();
		general.typedQuery = em.createQuery(QUERY_STRING, Object[].class)
				.setParameter("YEAR_k", general.query.getTargetYear())
				.setParameter("REGULAR_COM", REGULAR_COM);// REGULAR_COM
		this.setGeneralParams(general);
		CollectionUtil.split(pIdList, ONE_THOUSAND, subList ->
			masterResultList.addAll(general.typedQuery.setParameter("PIDs", subList).getResultList())
		);
		return masterResultList;
	}
	
	/**
	 * Sets the general params.
	 *
	 * @param general the new general params
	 */
	private void setGeneralParams(GeneralVars general){
		// Set general Parameters for query
		String startDate = general.query.getTargetYear().toString() + START_DATE_OF_YEAR;
		String endDate = general.query.getTargetYear().toString() + END_DATE_OF_YEAR;
		GeneralDate strYMD = GeneralDate.fromString(startDate, DATE_FORMAT);
		GeneralDate endYMD = GeneralDate.fromString(endDate, DATE_FORMAT);
		general.typedQuery.setParameter("CCD", general.companyCode)
		.setParameter("BASE_YMD", GeneralDate.today())
		.setParameter("PAY_BONUS_ATR", PAY_BONUS_ATR)
		.setParameter("STR_YMD", strYMD)
		.setParameter("END_YMD", endYMD)
		.setParameter("SPARE_PAY_ATR", SPARE_PAY_ATR)
		.setParameter("PAY_BONUS_ATR", PAY_BONUS_ATR);
	}

	/**
	 * The Class GeneralVars.
	 */
	class GeneralVars {

		/** The company code. */
		public String companyCode;

		/** The query. */
		public AccPaymentReportQuery query;

		/** The typed query. */
		public Query typedQuery;

	}

	/**
	 * The Class ItemDetails.
	 */
	class ItemDetails {
		
		/** The category. */
		public int category;
		
		/** The item code. */
		public String itemCode;
		
		/** The year adjusment one. */
		public int yearAdjusmentOne;
		
		/** The year adjusment two. */
		public int yearAdjusmentTwo;
	}
}
