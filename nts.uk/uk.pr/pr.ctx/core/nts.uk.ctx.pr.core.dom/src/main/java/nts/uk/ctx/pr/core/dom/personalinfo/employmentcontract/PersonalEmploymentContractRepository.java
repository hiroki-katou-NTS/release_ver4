package nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface PersonalEmploymentContractRepository {
	
	Optional<PersonalEmploymentContract> find(String companyCode, String personId, GeneralDate baseYmd);
	
	/**
	 * Find employment contract
	 * @param companyCode company code
	 * @param personId person id
	 * @param baseYmd base year month date
	 * @return
	 */
	Optional<PersonalEmploymentContract> findActive(String companyCode, String personId, GeneralDate baseYmd);
	
	/**
	 * 
	 * @param companyCode
	 * @param personIdList
	 * @param baseYmd
	 * @return list employment contract of list person with base times.
	 */
	List<PersonalEmploymentContract> findAll(String companyCode, List<String> personIdList, GeneralDate baseYmd);
}
