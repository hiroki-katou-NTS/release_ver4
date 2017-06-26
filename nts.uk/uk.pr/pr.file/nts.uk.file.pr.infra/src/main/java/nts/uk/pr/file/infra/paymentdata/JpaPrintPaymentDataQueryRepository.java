package nts.uk.pr.file.infra.paymentdata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.pr.file.infra.paymentdata.result.DetailItemDto;

@Stateless
public class JpaPrintPaymentDataQueryRepository extends JpaRepository implements PaymentDataQueryRepository {
	
	private String SELECT_ALL =  " SELECT d.qstdtPaymentDetailPK.categoryATR, d.itemAtr, d.qstdtPaymentDetailPK.itemCode, d.value, "
										+ "d.printLinePosition, d.columnPosition, d.deductAttribute, d.taxATR, d.limitAmount,"
										+ "d.commuteAllowTaxImpose, d.commuteAllowMonth, d.commuteAllowFraction, d.correctFlag" +
										" FROM QstdtPaymentDetail d " +
										" WHERE d.qstdtPaymentDetailPK.companyCode = :CCD" +
												" AND d.qstdtPaymentDetailPK.personId = :PID" +
												" AND d.qstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR" +
												" AND d.qstdtPaymentDetailPK.processingYM = :PROCESSING_YM";
	
	
	private String SELECT_ITEM_BY_CATEGORY = " SELECT d, i.itemName" +
											 " FROM QstdtPaymentDetail d JOIN QcamtItem i ON d.QstdtPaymentDetailPK.companyCode = i.QcamtItemPK.ccd" +
																			" AND d.QstdtPaymentDetailPK.categoryATR = i.QcamtItemPK.ctgAtr" +
																			" AND d.QstdtPaymentDetailPK.itemCode = i.QcamtItemPK.itemCd" +
											 " WHERE d.QstdtPaymentDetailPK.companyCode = :CCD"+
													" AND d.QstdtPaymentDetailPK.personId = :PID" +
													" AND d.QstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR" +
													" AND d.QstdtPaymentDetailPK.processingYM = :PROCESSING_YM" +
													" AND d.QstdtPaymentDetailPK.categoryATR = :CTG_ATR";

	
	private String SELECT_DEDUCTION_ITEMS = " SELECT d, i.itemName" +
											" FROM QstdtPaymentDetail d JOIN QcamtItem i ON d.QstdtPaymentDetailPK.companyCode = i.QcamtItemPK.ccd" +
																			" AND d.QstdtPaymentDetailPK.categoryATR = i.QcamtItemPK.ctgAtr" +
																			" AND d.QstdtPaymentDetailPK.itemCode = i.QcamtItemPK.itemCd" +
											" WHERE d.QstdtPaymentDetailPK.companyCode = :CCD"+
													" AND d.QstdtPaymentDetailPK.personId = :PID" +
													" AND d.QstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR" +
													" AND d.QstdtPaymentDetailPK.processingYM = :PROCESSING_YM" +
													" AND d.QstdtPaymentDetailPK.categoryATR = :CTG_ATR" +
													" AND d.deductAttribute = :DEDUCT_ATR";
	
	
	private String SELECT_ITEM = " SELECT d, i.itemName" +
								 " FROM QstdtPaymentDetail d JOIN QcamtItem i ON d.QstdtPaymentDetailPK.companyCode = i.QcamtItemPK.ccd" +
																 " AND d.QstdtPaymentDetailPK.categoryATR = i.QcamtItemPK.ctgAtr" +
																 " AND d.QstdtPaymentDetailPK.itemCode = i.QcamtItemPK.itemCd" +
								 " WHERE d.QstdtPaymentDetailPK.companyCode = :CCD"+
										" AND d.QstdtPaymentDetailPK.personId = :PID" +
										" AND d.QstdtPaymentDetailPK.payBonusAttribute = :PAY_BONUS_ATR" +
										" AND d.QstdtPaymentDetailPK.processingYM = :PROCESSING_YM" +
										" AND d.QstdtPaymentDetailPK.categoryATR = :CTG_ATR" +
										" AND d.QstdtPaymentDetailPK.itemCode = :ITEM_CD";
	
	@Override
	public List<DetailItemDto> findAll(String companyCode, String personId, int payBonusAtr, int processingYm) {
		return this.queryProxy()
						.query(SELECT_ALL, Object[].class)
							.setParameter("CCD", companyCode)
							.setParameter("PID", personId)
							.setParameter("PAY_BONUS_ATR", payBonusAtr)
							.setParameter("PROCESSING_YM", processingYm)
						.getList(x -> toDomain(x));
	}
	
	private DetailItemDto toDomain(Object[] x) {
		int categoryAtr = (int)x[0]; 
		int itemAtr = (int)x[1];
		String itemCode = (String)x[2];
		BigDecimal value = (BigDecimal)x[3];
		int linePosition = (int)x[4];
		int colPosition = (int)x[5];
		int deductAtr = (int)x[6];
		int taxATR = (int)x[7];
		int limitAmount =(int) x[8];
		BigDecimal commuteAllowTaxImpose = (BigDecimal) x[9];
		BigDecimal commuteAllowMonth = (BigDecimal) x[10];
		BigDecimal commuteAllowFraction = (BigDecimal) x[11];
		int correctFlag = (int) x[12];
		int displayAtr  = 0;
		return DetailItemDto.toData(categoryAtr, itemAtr, itemCode, value.doubleValue(), correctFlag, linePosition, colPosition,
										deductAtr, displayAtr, taxATR, limitAmount, commuteAllowTaxImpose.doubleValue(),
										commuteAllowMonth.doubleValue(), commuteAllowFraction.doubleValue());
	}

	@Override
	public List<DetailItemDto> findItemByCategory(String companyCode, String personId, int payBonusAtr,int processingYm, int categoryAtr) {
		return this.queryProxy()
				.query(SELECT_ITEM_BY_CATEGORY, DetailItemDto.class)
					.setParameter("CCD", companyCode)
					.setParameter("PID", personId)
					.setParameter("PAY_BONUS_ATR", payBonusAtr)
					.setParameter("PROCESSING_YM", processingYm)
					.setParameter("CTG_ATR", categoryAtr)
				.getList();
	}

	@Override
	public List<DetailItemDto> findDeductionItem(String companyCode, String personId, int payBonusAtr,
			int processingYm, int categoryAtr, int deductionAtr) {
		
		return this.queryProxy()
				.query(SELECT_DEDUCTION_ITEMS, DetailItemDto.class)
					.setParameter("CCD", companyCode)
					.setParameter("PID", personId)
					.setParameter("PAY_BONUS_ATR", payBonusAtr)
					.setParameter("PROCESSING_YM", processingYm)
					.setParameter("CTG_ATR", categoryAtr)
					.setParameter("DEDUCT_ATR", deductionAtr)
				.getList();
	}

	@Override
	public Optional<DetailItemDto> findItem(String companyCode, String personId, int payBonusAtr, int processingYm,
			int categoryAtr, int itemCode) {
		
		return this.queryProxy()
				.query(SELECT_ITEM, DetailItemDto.class)
					.setParameter("CCD", companyCode)
					.setParameter("PID", personId)
					.setParameter("PAY_BONUS_ATR", payBonusAtr)
					.setParameter("PROCESSING_YM", processingYm)
					.setParameter("CTG_ATR", categoryAtr)
					.setParameter("ITEM_CD", itemCode)
				.getSingle();
	}
	
	
	
}
