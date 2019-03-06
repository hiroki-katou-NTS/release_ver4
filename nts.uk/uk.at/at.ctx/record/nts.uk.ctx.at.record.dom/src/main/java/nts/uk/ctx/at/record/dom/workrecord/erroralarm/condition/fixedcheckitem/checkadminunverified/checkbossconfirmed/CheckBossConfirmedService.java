package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified.checkbossconfirmed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.algorithm.AppTargetPersonStatusAdapter;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.StateConfirm;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CheckBossConfirmedService {
	@Inject
	private AppTargetPersonStatusAdapter appTargetPersonStatusAdapter;

	public List<StateConfirm> checkBossConfirmed(String employeeID, GeneralDate startDate, GeneralDate endDate) {
		List<StateConfirm> listState = appTargetPersonStatusAdapter.appTargetPersonStatus(employeeID, startDate,
				endDate, 1);
		if (listState.isEmpty()) {
			return Collections.emptyList();
		}
		return listState;
	}

	public List<StateConfirm> checkBossConfirmed(String employeeID, DatePeriod datePeriod) {
		List<StateConfirm> listState = new ArrayList<>();
		try {
			listState = appTargetPersonStatusAdapter.appTargetPersonStatus(employeeID, datePeriod, 1);
		} catch (BusinessException e) {
			throw new BusinessException("Msg_1430", "承認者");
		}
		// List<StateConfirm> listState =
		// appTargetPersonStatusAdapter.appTargetPersonStatus(employeeID,
		// datePeriod, 1);
		if (listState.isEmpty()) {
			return Collections.emptyList();
		}
		return listState;
	}

	public List<StateConfirm> checkBossConfirmed(List<String> employeeID, DatePeriod datePeriod) {
		try {
			return appTargetPersonStatusAdapter.appTargetPersonStatus(employeeID, datePeriod, 1);
		} catch (BusinessException e) {
			throw new BusinessException("Msg_1430", "承認者");
		}
	}

}
