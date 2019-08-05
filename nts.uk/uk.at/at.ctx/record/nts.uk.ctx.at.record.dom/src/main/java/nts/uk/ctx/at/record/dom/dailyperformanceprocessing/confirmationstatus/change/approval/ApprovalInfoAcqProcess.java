package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 *
 */
@Stateless
public class ApprovalInfoAcqProcess {
	@Inject
	private ApprovalStatusInfoEmp approvalStatusInfoEmp;
	
	public List<ConfirmInfoResult> getApprovalInfoAcp(String companyId, String empTarget, List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		if (periodOpt.isPresent()) {
			return processModeAll(companyId, empTarget, employeeIds, periodOpt, yearMonthOpt);
		} else {
			return processModeAll(companyId, empTarget, employeeIds, periodOpt, yearMonthOpt);
		}

	}

	private List<ConfirmInfoResult> processModeAll(String companyId, String empTarget, List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		if (employeeIds.size() == 1) {
			return approvalStatusInfoEmp.approvalStatusInfoOneEmp(companyId, empTarget, employeeIds.get(0), periodOpt, yearMonthOpt, false);
		} else {
			return approvalStatusInfoEmp.approvalStatusInfoMulEmp(companyId, empTarget, employeeIds, periodOpt, yearMonthOpt, false);
		}
	}

}
