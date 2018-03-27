package nts.uk.ctx.at.shared.dom.adapter.dailyperformance;

import nts.arc.time.GeneralDate;

public interface DailyPerformanceAdapter {
  PresenceDataApprovedImport findByIdDateAndType (String employeeID , GeneralDate startDate , GeneralDate endDate , int rootType);
}
