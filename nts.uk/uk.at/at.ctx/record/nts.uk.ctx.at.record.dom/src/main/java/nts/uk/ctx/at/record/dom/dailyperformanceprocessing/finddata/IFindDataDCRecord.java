package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface IFindDataDCRecord {

	Optional<IdentityProcessUseSet> findIdentityByKey(String companyId);
	
	Optional<ApprovalProcessingUseSetting> findApprovalByCompanyId(String companyId);
	
	List<StatusOfEmployeeExport> getListAffComHistByListSidAndPeriod(Optional<String> keyRandom, List<String> sid, DatePeriod datePeriod);
	
	void clearAllStateless();
}
