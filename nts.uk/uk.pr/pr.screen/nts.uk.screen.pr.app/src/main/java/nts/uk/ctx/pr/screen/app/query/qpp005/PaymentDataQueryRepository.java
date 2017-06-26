package nts.uk.ctx.pr.screen.app.query.qpp005;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.screen.app.query.qpp005.result.DetailItemDto;

public interface PaymentDataQueryRepository {
	
	/**
	 * Get all detail item
	 * @param companyCode
	 * @param personId
	 * @param payBonusAtr
	 * @param processingYM
	 * @return
	 */
	List<DetailItemDto> findAll(String companyCode, String personId, int payBonusAtr, int processingYM);

	/**
	 * Get item list of each category
	 * @param companyCode
	 * @param personId
	 * @param payBonusAtr
	 * @param processingYm
	 * @param categoryAtr
	 * @return
	 */
	List<DetailItemDto> findItemByCategory(String companyCode, String personId, int payBonusAtr, int processingYm, int categoryAtr);
	
	/**
	 * Get item list of deduction category
	 * @param companyCode
	 * @param personId
	 * @param payBonusAtr
	 * @param processingYm
	 * @param categoryAtr
	 * @param deductionAtr
	 * @return
	 */
	List<DetailItemDto> findDeductionItem(String companyCode, String personId, int payBonusAtr, int processingYm, int categoryAtr, int deductionAtr);

	/**
	 * Get each item
	 * @param companyCode
	 * @param personId
	 * @param payBonusAtr
	 * @param processingYm
	 * @param categoryAtr
	 * @param itemCode
	 * @return
	 */
	Optional<DetailItemDto> findItem(String companyCode, String personId, int payBonusAtr, int processingYm,int categoryAtr, int itemCode);
}
