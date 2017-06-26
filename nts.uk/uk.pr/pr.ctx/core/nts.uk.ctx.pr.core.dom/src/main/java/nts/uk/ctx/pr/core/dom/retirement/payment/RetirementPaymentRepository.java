package nts.uk.ctx.pr.core.dom.retirement.payment;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.shr.com.primitive.PersonId;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface RetirementPaymentRepository {
	
	/**
	 * register new Retirement Payment
	 * @param retirementPayment Retirement Payment
	 */
	void add(RetirementPayment retirementPayment);
	
	/**
	 * get list Retirement Payment by Company Code and Person ID
	 * @param companyCode Company Code
	 * @param personId Person ID
	 * @return list Retirement Payment by COmpant Code and Person ID
	 */
	List<RetirementPayment> findByCompanyCodeAndPersonId(CompanyCode companyCode, PersonId personId);
	
	/**
	 * get single Retirement Payment by Company Code, Person ID and Date Time
	 * @param companyCode Company Code
	 * @param personId Person ID
	 * @param dateTime Date Time
	 * @return single Retirement Payment by Company Code, Person ID and Date Time
	 */
	Optional<RetirementPayment> findRetirementPaymentInfo(CompanyCode companyCode, PersonId personId, GeneralDate dateTime);
	
	/**
	 * update exist Retirement Payment
	 * @param retirementPayment Retirement Payment
	 */
	void update(RetirementPayment retirementPayment);
	
	/**
	 * delete exist Retirement Payment
	 * @param retirementPayment Retirement Payment
	 */
	void remove(RetirementPayment retirementPayment);
}
