package nts.uk.ctx.pr.core.dom.payment.banktransfer;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface BankTransferRepository {
	/**
	 * Add new bank transfer
	 * 
	 * @param root
	 *            Bank Transfer domain
	 */
	void add(BankTransfer root);

	/**
	 * Update new bank transfer
	 * 
	 * @param root
	 *            Bank Transfer domain
	 */
	void update(BankTransfer root);

	/**
	 * 
	 * @param companyCode
	 * @param payBonusAtr
	 * @param processingNo
	 * @param payDate
	 * @param sparePayAtr
	 */
	void remove(String companyCode, int payBonusAtr, int processingNo, GeneralDate payDate, int sparePayAtr);

	/**
	 * 
	 * @param companyCode
	 * @param processingNo
	 * @param payDate
	 */
	void removeAll(String companyCode, int processingNo, GeneralDate payDate);

	/**
	 * Find list bank transfer (SEL_1)
	 * 
	 * @param param
	 *            parameter information
	 * @return BankTransfer
	 */
	List<BankTransfer> findBySEL1(BankTransferParam param);

	/**
	 * Added by sonnh1
	 * 
	 * Find list bank transfer (SEL_1_1: not include property "sparePatAtr")
	 * 
	 * @param param
	 * @return
	 */
	List<BankTransfer> findBySEL1_1(BankTransferParam param);

	/**
	 * Find list bank transfer (SEL_2)
	 * 
	 * @param param
	 *            parameter information
	 * @return BankTransfer
	 */
	List<BankTransfer> findBySEL2(BankTransferParam param);

	/**
	 * Find a bank transfer
	 * 
	 * @param param
	 *            parameter information
	 * @return BankTransfer
	 */
	Optional<BankTransfer> find(BankTransferParam param);
}
