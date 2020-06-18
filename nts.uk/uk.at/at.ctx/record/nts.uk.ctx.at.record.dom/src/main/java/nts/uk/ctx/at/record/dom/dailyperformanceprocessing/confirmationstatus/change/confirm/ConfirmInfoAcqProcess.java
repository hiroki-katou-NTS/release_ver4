package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriodCacheKey;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx 確認情報取得処理
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ConfirmInfoAcqProcess {

	@Inject
	private ConfirmStatusInfoEmp confirmStatusInfoEmp;

	public List<ConfirmInfoResult> getConfirmInfoAcp(String companyId, List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cachedClosurePeriod = new HashMap<>();
		return getConfirmInfoAcp(companyId, employeeIds, periodOpt, yearMonthOpt, cachedClosurePeriod) ;
	}

	public List<ConfirmInfoResult> getConfirmInfoAcp(String companyId, List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt, Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cachedClosurePeriod) {
		if (employeeIds.size() == 1) {
			return confirmStatusInfoEmp.confirmStatusInfoOneEmp(companyId, employeeIds.get(0), periodOpt, yearMonthOpt, cachedClosurePeriod);
		} else {
			return confirmStatusInfoEmp.confirmStatusInfoMulEmp(companyId, employeeIds, periodOpt, yearMonthOpt, cachedClosurePeriod);
		}
	}

}
