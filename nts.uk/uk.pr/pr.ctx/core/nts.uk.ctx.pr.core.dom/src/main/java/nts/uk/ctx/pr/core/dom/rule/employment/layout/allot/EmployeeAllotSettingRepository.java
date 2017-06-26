package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.paymentdata.insure.EmploymentInsuranceAtr;

public interface EmployeeAllotSettingRepository {
	Optional<EmployeeAllotSetting> find(String companyCode, String historyID, String employeeCd);
	
	// Return List
	List<EmployeeAllotSetting> findAll(String companyCode,String historyID);
	List<EmployeeAllSetting> findAllEm(String companyCode,String historyID);
//	List<Employment> findAllEmName();
	List<EmployeeAllotSetting> findEmpDetail(String companyCode,String historyID);
}
