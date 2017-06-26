package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output;

import java.util.List;

/**
 * @author sonnlb
 *
 */
public interface RegalDocCompanyRepository {

	/**
	 * @param companyCode
	 * @return
	 */
	List<RegalDocCompany> findAll(String companyCode);
}
