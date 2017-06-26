package nts.uk.ctx.pr.core.infra.repository.paymentdata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetail;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.PrintPositionCategory;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDataRepository;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentDetail;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentDetailPK;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentHeader;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstdtPaymentHeaderPK;

@Stateless
public class JpaPaymentDataRepository extends JpaRepository implements PaymentDataRepository {

	private final String SELECT_HEADER = " SELECT c FROM QstdtPaymentHeader c "
			+ " WHERE c.qstdtPaymentHeaderPK.companyCode = :CCD" + " AND c.qstdtPaymentHeaderPK.personId = :PID"
			+ " AND c.qstdtPaymentHeaderPK.payBonusAtr = :PAY_BONUS_ATR"
			+ " AND c.qstdtPaymentHeaderPK.processingYM = :PROCESSING_YM";

	private final String REMOVE_DETAIL = "DELETE FROM QstdtPaymentDetail d "
			+ "WHERE d.qstdtPaymentDetailPK.companyCode = :CCD" + " AND d.qstdtPaymentDetailPK.personId = :PID"
			+ " AND d.qstdtPaymentDetailPK.processingYM = :PROCESSING_YM"
			+ " AND d.qstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR"
			+ " AND d.qstdtPaymentDetailPK.processingNo = :PROCESSING_NO"
			+ " AND d.qstdtPaymentDetailPK.sparePayAttribute = :SPARE_PAY_ATR";

	private String SEL_11 = "SELECT d FROM QstdtPaymentDetail d" + " WHERE d.qstdtPaymentDetailPK.companyCode = :CCD"
			+ " AND d.qstdtPaymentDetailPK.personId = :PID"
			+ " AND d.qstdtPaymentDetailPK.processingNo = :PROCESSING_NO"
			+ " AND d.qstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR"
			+ " AND d.qstdtPaymentDetailPK.processingYM = :PROCESSING_YM"
			+ " AND d.qstdtPaymentDetailPK.sparePayAttribute = :SPARE_PAY_ATR"
			+ " AND d.qstdtPaymentDetailPK.categoryATR = :CTG_ATR" + " AND d.qstdtPaymentDetailPK.itemCode = :ITEM_CD"
			+ " AND d.value > :VAL";

	private String SEL_11_1 = "SELECT d FROM QstdtPaymentDetail d" + " WHERE d.qstdtPaymentDetailPK.companyCode = :CCD"
			+ " AND d.qstdtPaymentDetailPK.personId = :PID"
			+ " AND d.qstdtPaymentDetailPK.processingNo = :PROCESSING_NO"
			+ " AND d.qstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR"
			+ " AND d.qstdtPaymentDetailPK.processingYM = :PROCESSING_YM"
			+ " AND d.qstdtPaymentDetailPK.categoryATR = :CTG_ATR" + " AND d.qstdtPaymentDetailPK.itemCode = :ITEM_CD"
			+ " AND d.value > :VAL";

	private final String SELECT_3_1 = " SELECT c FROM QstdtPaymentHeader c "
			+ " WHERE c.qstdtPaymentHeaderPK.companyCode = :CCD" + " AND c.qstdtPaymentHeaderPK.personId = :PID"
			+ " AND c.qstdtPaymentHeaderPK.processingNo = :PROCESSING_NO"
			+ " AND c.qstdtPaymentHeaderPK.payBonusAtr = :PAY_BONUS_ATR"
			+ " AND c.qstdtPaymentHeaderPK.processingYM = :PROCESSING_YM";

	@Override
	public Optional<PaymentDetail> findItemWith9Property(String companyCode, String personId, int processingNo,
			int processingYm, int payBonusAtr, int sparePayAtr, int categoryAtr, String itemCode, BigDecimal value) {
		return this.queryProxy().query(SEL_11, PaymentDetail.class).setParameter("CCD", companyCode)
				.setParameter("PID", personId).setParameter("PROCESSING_NO", processingNo)
				.setParameter("PROCESSING_YM", processingYm).setParameter("PAY_BONUS_ATR", payBonusAtr)
				.setParameter("SPARE_PAY_ATR", sparePayAtr).setParameter("CTG_ATR", categoryAtr)
				.setParameter("ITEM_CD", itemCode).setParameter("VAL", value).getSingle();
	}

	@Override
	public List<PaymentDetail> findItemWith8Property(String companyCode, String personId, int processingNo,
			int processingYm, int payBonusAtr, int categoryAtr, String itemCode, BigDecimal value) {
		return this.queryProxy().query(SEL_11_1, QstdtPaymentDetail.class).setParameter("CCD", companyCode)
				.setParameter("PID", personId).setParameter("PROCESSING_NO", processingNo)
				.setParameter("PROCESSING_YM", processingYm).setParameter("PAY_BONUS_ATR", payBonusAtr)
				.setParameter("CTG_ATR", categoryAtr).setParameter("ITEM_CD", itemCode).setParameter("VAL", value)
				.getList(c -> paymentDetailToDomain(c));
	}

	@Override
	public List<Payment> findItemWith5Property(String companyCode, String personId, int processingNo,
			int payBonusAttribute, int processingYM) {
		return this.queryProxy().query(SELECT_3_1, QstdtPaymentHeader.class).setParameter("CCD", companyCode)
				.setParameter("PID", personId).setParameter("PROCESSING_NO", processingNo)
				.setParameter("PAY_BONUS_ATR", payBonusAttribute).setParameter("PROCESSING_YM", processingYM)
				.getList(c -> toDomain(c));
	}

	@Override
	public Optional<Payment> find(String companyCode, String personId, int processingNo, int payBonusAttribute,
			int processingYM, int sparePayAttribute) {
		return this.queryProxy().find(new QstdtPaymentHeaderPK(companyCode, personId, processingNo, payBonusAttribute,
				processingYM, sparePayAttribute), QstdtPaymentHeader.class).map(c -> toDomain(c));
	}

	@Override
	public List<Payment> findPaymentHeader(String companyCode, String personId, int payBonusAtr, int processingYm) {
		return this.queryProxy().query(SELECT_HEADER, QstdtPaymentHeader.class).setParameter("CCD", companyCode)
				.setParameter("PID", personId).setParameter("PAY_BONUS_ATR", payBonusAtr)
				.setParameter("PROCESSING_YM", processingYm).getList(c -> toDomain(c));
	}

	@Override
	public boolean isExistHeader(String companyCode, String personId, int payBonusAttribute, int processingYM) {
		List<QstdtPaymentHeader> pHeader = this.queryProxy().query(SELECT_HEADER, QstdtPaymentHeader.class)
				.setParameter("CCD", companyCode).setParameter("PID", personId)
				.setParameter("PAY_BONUS_ATR", payBonusAttribute).setParameter("PROCESSING_YM", processingYM).getList();

		return !pHeader.isEmpty();
	}

	@Override
	public boolean isExistDetail(String companyCode, String personId, int processingNo, int baseYM, int categoryAtr,
			String itemCode, int payBonusAtr, int sparePayAtr) {
		QstdtPaymentDetailPK key = new QstdtPaymentDetailPK(companyCode, personId, processingNo, payBonusAtr, baseYM,
				sparePayAtr, categoryAtr, itemCode);
		Optional<QstdtPaymentDetail> detail = this.queryProxy().find(key, QstdtPaymentDetail.class);
		return detail.isPresent();
	}

	@Override
	public void add(Payment payment) {
		QstdtPaymentHeader paymentHeader = toPaymentHeaderEntity(payment);

		this.commandProxy().update(paymentHeader);

		for (DetailItem item : payment.getDetailPaymentItems()) {
			this.insertDetail(payment, item);
		}
		for (DetailItem item : payment.getDetailDeductionItems()) {
			this.insertDetail(payment, item);
		}
		for (DetailItem item : payment.getDetailPersonalTimeItems()) {
			this.insertDetail(payment, item);
		}
		for (DetailItem item : payment.getDetailArticleItems()) {
			this.insertDetail(payment, item);
		}
	}

	@Override
	public void updateHeader(Payment payment) {
		QstdtPaymentHeader paymentHeader = toPaymentHeaderEntity(payment);
		this.commandProxy().update(paymentHeader);
	}

	@Override
	public void insertHeader(Payment payment) {
		QstdtPaymentHeader paymentHeader = toPaymentHeaderEntity(payment);

		this.commandProxy().insert(paymentHeader);
	}

	@Override
	public void insertDetail(Payment payment, DetailItem item) {
		QstdtPaymentDetail paymentDetail = toPaymentDetailEntity(payment, item);

		this.commandProxy().insert(paymentDetail);
	}

	@Override
	public void updateDetail(Payment payment, DetailItem item) {
		QstdtPaymentDetail paymentDetail = toPaymentDetailEntity(payment, item);

		this.commandProxy().update(paymentDetail);
	}

	/**
	 * convert data to domain
	 * 
	 * @param entity
	 * @return
	 */
	private static Payment toDomain(QstdtPaymentHeader entity) {
		val domain = Payment.createFromJavaType(entity.qstdtPaymentHeaderPK.companyCode,
				entity.qstdtPaymentHeaderPK.personId, entity.employeeName, entity.qstdtPaymentHeaderPK.processingNo,
				entity.qstdtPaymentHeaderPK.payBonusAtr, entity.qstdtPaymentHeaderPK.processingYM,
				entity.qstdtPaymentHeaderPK.sparePayAtr, entity.standardDate, entity.specificationCode,
				entity.specificationName, entity.residenceCode, entity.residenceName, entity.healthInsuranceGrade,
				entity.healthInsuranceAverageEarn, entity.ageContinuationInsureAtr, entity.tenureAtr, entity.taxAtr,
				entity.pensionInsuranceGrade, entity.pensionAverageEarn, entity.employmentInsuranceAtr,
				entity.dependentNumber, entity.workInsuranceCalculateAtr, entity.insuredAtr, entity.bonusTaxRate,
				entity.calcFlag, entity.makeMethodFlag, entity.comment);
		domain.additionInfo(entity.departmentCode, entity.departmentName, entity.companyName);
		// entity.toDomain(domain);
		return domain;
	}

	private static PaymentDetail paymentDetailToDomain(QstdtPaymentDetail entity) {
		val domain = PaymentDetail.createFromJavaType(entity.qstdtPaymentDetailPK.companyCode,
				entity.qstdtPaymentDetailPK.personId, entity.qstdtPaymentDetailPK.processingNo,
				entity.qstdtPaymentDetailPK.payBonusAttribute, entity.qstdtPaymentDetailPK.processingYM,
				entity.qstdtPaymentDetailPK.sparePayAttribute, entity.qstdtPaymentDetailPK.categoryATR,
				entity.qstdtPaymentDetailPK.itemCode, entity.value);
		return domain;
	}

	/**
	 * convert payment header data to entity
	 * 
	 * @param domain
	 * @return
	 */
	private static QstdtPaymentHeader toPaymentHeaderEntity(Payment domain) {
		QstdtPaymentHeader entity = new QstdtPaymentHeader();
		// entity.fromDomain(domain);
		entity.qstdtPaymentHeaderPK = new QstdtPaymentHeaderPK(domain.getCompanyCode().v(), domain.getPersonId().v(),
				domain.getProcessingNo().v().intValue(), domain.getPayBonusAtr().value,
				domain.getProcessingYM().v().intValue(), domain.getSparePayAtr().value);
		entity.employeeName = domain.getPersonName().v();
		entity.standardDate = domain.getStandardDate();
		entity.specificationCode = domain.getSpecificationCode().v();
		entity.specificationName = domain.getSpecificationName().v();
		entity.residenceCode = domain.getResidenceCode().v();
		entity.residenceName = domain.getResidenceName().v();
		entity.healthInsuranceAverageEarn = domain.getHealthInsuranceAverageEarn().v().intValue();
		entity.healthInsuranceGrade = domain.getHealthInsuranceGrade().v().intValue();
		entity.ageContinuationInsureAtr = domain.getAgeContinuationInsureAtr().value;
		entity.tenureAtr = domain.getTenureAtr().value;
		entity.taxAtr = domain.getTaxAtr().value;
		entity.pensionInsuranceGrade = domain.getPensionInsuranceGrade().v();
		entity.pensionAverageEarn = domain.getPensionAverageEarn().v();
		entity.employmentInsuranceAtr = domain.getEmploymentInsuranceAtr().value;
		entity.dependentNumber = domain.getDependentNumber().v();
		entity.workInsuranceCalculateAtr = domain.getWorkInsuranceCalculateAtr().value;
		entity.insuredAtr = domain.getInsuredAtr().value;
		entity.bonusTaxRate = domain.getBonusTaxRate().v();
		entity.calcFlag = domain.getCalcFlag().value;
		entity.makeMethodFlag = domain.getMakeMethodFlag().value;
		entity.comment = domain.getComment().v();

		PrintPositionCategory item0 = domain.getPrintCategories().get(0);
		entity.printPositionCategoryATR1 = item0.getCategoryAtr().value;
		entity.printPositionCategoryLines1 = item0.getLines().v();

		PrintPositionCategory item1 = domain.getPrintCategories().get(1);
		entity.printPositionCategoryATR2 = item1.getCategoryAtr().value;
		entity.printPositionCategoryLines2 = item1.getLines().v();

		if (domain.getPrintCategories().size() >= 3) {
			PrintPositionCategory item2 = domain.getPrintCategories().get(2);
			entity.printPositionCategoryATR3 = item2.getCategoryAtr().value;
			entity.printPositionCategoryLines3 = item2.getLines().v();
		} else {
			entity.printPositionCategoryATR5 = 2;
			entity.printPositionCategoryLines5 = -1;
		}

		if (domain.getPrintCategories().size() >= 4) {
			PrintPositionCategory item3 = domain.getPrintCategories().get(3);
			entity.printPositionCategoryATR4 = item3.getCategoryAtr().value;
			entity.printPositionCategoryLines4 = item3.getLines().v();
		} else {
			entity.printPositionCategoryATR5 = 3;
			entity.printPositionCategoryLines5 = -1;
		}

		if (domain.getPrintCategories().size() >= 5) {
			PrintPositionCategory item4 = domain.getPrintCategories().get(4);
			entity.printPositionCategoryATR5 = item4.getCategoryAtr().value;
			entity.printPositionCategoryLines5 = item4.getLines().v();
		} else {
			entity.printPositionCategoryATR5 = -1;
			entity.printPositionCategoryLines5 = -1;
		}
		// add by EAP 06.データ作成（実行）-登録処理 (update 28.11.2016)
		entity.companyName = "日通システム株式会社";
		entity.employmentCode = "0000000001";
		entity.employmentName = "雇用";
		entity.departmentCode = "0000000001";
		entity.departmentName = "部門";
		entity.externalDepartmentCode = "000000000000001";
		entity.classificationCode = "0000000001";
		entity.classificationName = "分類";
		entity.positionCode = "0000000001";
		entity.positionName = "職位";
		// entity.specificationName = "";

		return entity;
	}

	/**
	 * convert payment detail to entity
	 * 
	 * @param domain
	 * @param detail
	 * @return
	 */
	private QstdtPaymentDetail toPaymentDetailEntity(Payment domain, DetailItem detail) {
		QstdtPaymentDetail entity = new QstdtPaymentDetail();
		// entity.fromDomain(domain);
		entity.qstdtPaymentDetailPK = new QstdtPaymentDetailPK(domain.getCompanyCode().v(), domain.getPersonId().v(),
				domain.getProcessingNo().v().intValue(), domain.getPayBonusAtr().value,
				domain.getProcessingYM().v().intValue(), domain.getSparePayAtr().value, detail.getCategoryAtr().value,
				detail.getItemCode().v());
		entity.value = BigDecimal.valueOf(detail.getValue());
		entity.correctFlag = detail.getCorrectFlag().value;
		entity.taxATR = detail.getTaxAtr();
		entity.socialInsurranceAttribute = detail.getSocialInsuranceAtr().value;
		entity.laborSubjectAttribute = detail.getLaborInsuranceAtr().value;
		entity.columnPosition = detail.getItemPosition().getColumnPosition().v();
		entity.printLinePosition = detail.getItemPosition().getLinePosition().v();
		entity.limitAmount = detail.getLimitAmount(); // QCAM_ITEM.LIMIT_MNY
		entity.fixPayATR = detail.getFixPayAtr(); // FIX_PAY_ATR
		entity.averagePayATR = detail.getAveragePayAtr();//
		entity.deductAttribute = detail.getDeductionAtr().value; //
		entity.itemAtr = detail.getItemAtr().value; //
		entity.commuteAllowTaxImpose = BigDecimal.valueOf(detail.getCommuteAllowTaxImpose());
		entity.commuteAllowMonth = BigDecimal.valueOf(detail.getCommuteAllowMonth());
		entity.commuteAllowFraction = BigDecimal.valueOf(detail.getCommuteAllowFraction());

		return entity;
	}

	@Override
	public void removeDetails(String personId, Integer processingYM, String companyCode) {
		this.getEntityManager().createQuery(REMOVE_DETAIL).setParameter("CCD", companyCode)
				.setParameter("PID", personId).setParameter("PROCESSING_YM", processingYM)
				.setParameter("PAY_BONUS_ATR", 0).setParameter("PROCESSING_NO", 1).setParameter("SPARE_PAY_ATR", 0)
				.executeUpdate();
	}

	@Override
	public void removeHeader(String personId, Integer processingYM, String companyCode) {
		val pk = new QstdtPaymentHeaderPK(companyCode, personId, 1, 0, processingYM, 0);
		this.commandProxy().remove(QstdtPaymentHeader.class, pk);
	}
}