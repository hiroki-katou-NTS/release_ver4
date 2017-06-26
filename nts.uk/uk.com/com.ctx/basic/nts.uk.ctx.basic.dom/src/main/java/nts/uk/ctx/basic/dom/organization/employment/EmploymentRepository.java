package nts.uk.ctx.basic.dom.organization.employment;

import java.util.List;
import java.util.Optional;

public interface EmploymentRepository {
	/**
	 * insert Employment
	 * @param employment
	 */
	void add(Employment employment);
	/**
	 * update Employment
	 * @param employment
	 */
	void update(Employment employment);
	/**
	 * remove employment by company code and employment code
	 * @param companyCode
	 * @param employmentCode
	 */
	void remove(String companyCode, String employmentCode);
	/**
	 * find employment by company code and employment code
	 * @param companyCode
	 * @param employmentCode
	 * @return
	 */
	Optional<Employment> findEmployment(String companyCode, String employmentCode);
	/**
	 * find all employments by company code
	 * @param companyCode
	 * @return
	 */
	List<Employment> findAllEmployment(String companyCode);
	/**
	 * find employment by company code and display flag =1
	 * @param companyCode
	 * @param displayFlg
	 * @return
	 */
	Optional<Employment> findEmploymnetByDisplayFlg(String companyCode);
}
