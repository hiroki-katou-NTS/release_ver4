package nts.uk.ctx.pr.core.dom.paymentdata.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.paymentdata.Payment;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetail;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;

public interface PaymentDataRepository {
	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param processingNo
	 * @param payBonusAttribute
	 * @param processingYM
	 * @param sparePayAttribute
	 * @return Payment
	 */
	Optional<Payment> find(String companyCode, String personId, int processingNo, int payBonusAttribute,
			int processingYM, int sparePayAttribute);

	/**
	 * Added by sonnh1
	 * 
	 * @param companyCode
	 * @param personId
	 * @param processingNo
	 * @param processingYm
	 * @param payBonusAtr
	 * @param sparePayAtr
	 * @param categoryAtr
	 * @param itemCode
	 * @return
	 */
	Optional<PaymentDetail> findItemWith9Property(String companyCode, String personId, int processingNo,
			int processingYm, int payBonusAtr, int sparePayAtr, int categoryAtr, String itemCode, BigDecimal value);

	/**
	 * Added by sonnh1
	 * 
	 * @param companyCode
	 * @param personId
	 * @param processingNo
	 * @param processingYm
	 * @param payBonusAtr
	 * @param categoryAtr
	 * @param itemCode
	 * @param value
	 * @return
	 */
	List<PaymentDetail> findItemWith8Property(String companyCode, String personId, int processingNo, int processingYm,
			int payBonusAtr, int categoryAtr, String itemCode, BigDecimal value);

	/**
	 * Added by sonnh1
	 * 
	 * @param companyCode
	 * @param personId
	 * @param processingNo
	 * @param payBonusAttribute
	 * @param processingYM
	 * @return
	 */
	List<Payment> findItemWith5Property(String companyCode, String personId, int processingNo, int payBonusAttribute,
			int processingYM);

	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param payBonusAttribute
	 * @param processingYM
	 * @return payment header
	 */
	List<Payment> findPaymentHeader(String companyCode, String personId, int payBonusAttribute, int processingYM);

	/**
	 * check is exist payment header of personid in processing year month
	 * 
	 * @param personId
	 * @param processingYM
	 * @return
	 */
	boolean isExistHeader(String companyCode, String personId, int payBonusAttribute, int processingYM);

	/**
	 * check detail item is exsit
	 * 
	 * @param companyCode
	 * @param personId
	 * @param baseYM
	 * @param categoryAtr
	 * @param itemCode
	 * @return
	 */
	boolean isExistDetail(String companyCode, String personId, int processingNo, int baseYM, int categoryAtr,
			String itemCode, int payBonusAtr, int sparePayAtr);

	/**
	 * import new data payment detail
	 * 
	 * @param payment
	 *            (contains detail) /** Import data for payment include payment
	 *            head and payment detail
	 * @param payment
	 */
	void add(Payment payment);

	/**
	 * Update header
	 * 
	 * @param payment
	 */
	void updateHeader(Payment payment);

	/**
	 * Insert header
	 * 
	 * @param payment
	 */
	void insertHeader(Payment payment);

	/**
	 * Insert detail
	 * 
	 * @param detailItem
	 */
	void insertDetail(Payment payment, DetailItem detailItem);

	/**
	 * Update detail
	 * 
	 * @param detailItem
	 */
	void updateDetail(Payment payment, DetailItem detailItem);

	/**
	 * Remove payment details
	 * 
	 * @param personId
	 * @param processingYM
	 * @param companyCode
	 */
	void removeDetails(String personId, Integer processingYM, String companyCode);

	/**
	 * Remove payment header
	 * 
	 * @param personId
	 * @param processingYM
	 * @param companyCode
	 */
	void removeHeader(String personId, Integer processingYM, String companyCode);

}
