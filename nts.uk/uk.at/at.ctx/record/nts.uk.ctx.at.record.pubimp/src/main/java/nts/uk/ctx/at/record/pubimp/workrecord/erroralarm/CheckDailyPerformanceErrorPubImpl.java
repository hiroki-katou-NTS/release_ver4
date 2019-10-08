package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.CheckDailyPerformanceErrorPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.DailyPerformanceErrorExport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CheckDailyPerformanceErrorPubImpl implements CheckDailyPerformanceErrorPub {

	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepo;

	@Override
	public List<DailyPerformanceErrorExport> check(String employeeID, GeneralDate strDate, GeneralDate endDate) {
		if(strDate.after(endDate)){
			return new ArrayList<>();
		}
		String companyID = AppContexts.user().companyId();
		List<DailyPerformanceErrorExport> result = new ArrayList<>();
		while (strDate.beforeOrEquals(endDate)) {
			result.add(new DailyPerformanceErrorExport(strDate, employeeDailyPerErrorRepo.checkExistErrorByDate(companyID, employeeID, strDate)));
			strDate = strDate.addDays(1);
		}
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean checksDailyPerformanceError(String employeeID, GeneralDate strDate, GeneralDate endDate) {
		String companyID = AppContexts.user().companyId();
		return employeeDailyPerErrorRepo.checkErrorByPeriodDate(companyID, employeeID, strDate, endDate);
	}
	
	

}
