package nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 社員雇用保険履歴
 */
public interface EmpInsHistRepository {
    List<EmpInsHist> getByEmpIdsAndStartDate(List<String> empIds, GeneralDate startDate);
    List<EmpInsHist> getEmpInsHistById(String cid, List<String> sid, GeneralDate baseDate);
	List<EmpInsHist> getAllEmpInsHist();

	Optional<EmpInsHist> getByEmpIdAndEndDate(String sId, GeneralDate endDate);
	
	List<EmpInsHist> getByEmpIdsAndStartDateInPeriod(String companyId, List<String> empIds, GeneralDate startDate, GeneralDate endDate);
	
	List<EmpInsHist> getByEmpIdsAndEndDateInPeriod(String companyId, List<String> empIds, GeneralDate startDate, GeneralDate endDate);
}
