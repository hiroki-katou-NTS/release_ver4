package nts.uk.ctx.pr.core.dom.rule.employment.averagepay;

import java.util.Optional;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AveragePayRepository {

	/**
	 * register new average pay
	 * 
	 * @param averagePay
	 *            average pay
	 */
	void register(AveragePay averagePay);

	/**
	 * find average pay by company code
	 * 
	 * @param companyCode
	 *            company code
	 * @return average pay if exist
	 */
	Optional<AveragePay> findByCompanyCode(String companyCode);

	/**
	 * update average pay
	 * 
	 * @param averagePay
	 *            average pay
	 */
	void update(AveragePay averagePay);

}
