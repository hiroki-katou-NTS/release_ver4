package nts.uk.ctx.at.shared.ac.dailyperformance;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.DailyPerformanceAdapter;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.PresenceDataApprovedImport;
@Stateless
public class DailyPerformanceAdapterImpl implements DailyPerformanceAdapter {

	@Override
	public PresenceDataApprovedImport findByIdDateAndType(String employeeID, GeneralDate startDate, GeneralDate endDate, int rootType) {
		// TODO Auto-generated method stub
		return null;
	}

}
