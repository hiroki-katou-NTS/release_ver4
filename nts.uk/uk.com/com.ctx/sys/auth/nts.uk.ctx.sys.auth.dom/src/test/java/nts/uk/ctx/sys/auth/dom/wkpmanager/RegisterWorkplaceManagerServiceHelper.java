package nts.uk.ctx.sys.auth.dom.wkpmanager;

import java.util.List;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.sys.shared.dom.employee.EmpEnrollPeriodImport;
import nts.uk.ctx.sys.shared.dom.employee.SecondSituation;

public class RegisterWorkplaceManagerServiceHelper {
	/**
	 * create list workplaceMananger
	 * @param sid
	 * @param periods
	 * @return
	 */
	public static List<WorkplaceManager> createWorkplaceManagers(String sid, List<DatePeriod> periods) {
		return periods.stream()
				.map(c -> WorkplaceManager.createNew("workplaceId", sid, c))
				.collect(Collectors.toList());

	}
	
	/**
	 * create workplaceManager
	 * @param workplaceManagerId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static WorkplaceManager createWorkplaceManager(String workplaceManagerId, GeneralDate startDate, GeneralDate endDate) {
		return new WorkplaceManager(workplaceManagerId, "workplaceId", "sid", new DatePeriod(startDate, endDate));
	}
	
	/**
	 * create company employee history
	 * @param sid
	 * @param period
	 * @return
	 */
	public static EmpEnrollPeriodImport createEmpEnrollPeriodImport(String sid, DatePeriod period) {
		return new EmpEnrollPeriodImport(sid, period, SecondSituation.ACCEPTING);
	}
	
	/**
	 * create list company employee history
	 * @param sid
	 * @param periods
	 * @return
	 */
	public static List<EmpEnrollPeriodImport> createEmpEnrollPeriodImports(String sid, List<DatePeriod> periods) {
		return periods.stream().map(c -> createEmpEnrollPeriodImport(sid, c)).collect(Collectors.toList());
	}
}
