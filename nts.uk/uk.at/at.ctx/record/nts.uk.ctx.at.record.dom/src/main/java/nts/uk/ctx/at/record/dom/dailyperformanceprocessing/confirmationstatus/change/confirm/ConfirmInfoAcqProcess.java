package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx 確認情報取得処理
 */
@Stateless
public class ConfirmInfoAcqProcess {

	@Inject
	private ConfirmStatusInfoEmp confirmStatusInfoEmp;

	public List<ConfirmInfoResult> getConfirmInfoAcp(String companyId, List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		if (periodOpt.isPresent()) {
			return processModeAll(companyId, employeeIds, periodOpt, yearMonthOpt);
		} else {
			return processModeAll(companyId, employeeIds, periodOpt, yearMonthOpt);
		}

	}

	private List<ConfirmInfoResult> processModeAll(String companyId, List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		if (employeeIds.size() == 1) {
			return confirmStatusInfoEmp.confirmStatusInfoOneEmp(companyId, employeeIds.get(0), periodOpt, yearMonthOpt);
		} else {
			return confirmStatusInfoEmp.confirmStatusInfoMulEmp(companyId, employeeIds, periodOpt,
					yearMonthOpt);
		}
	}

}
