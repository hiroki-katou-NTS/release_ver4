package nts.uk.ctx.at.function.dom.adapter.fixedcheckitem;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
public interface FixedCheckItemAdapter {
	
		public Optional<ValueExtractAlarm>  checkWorkTypeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTypeCD);
		
		public Optional<ValueExtractAlarm>  checkWorkTimeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTimeCD);
		
		public List<ValueExtractAlarm> checkPrincipalUnconfirm(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
		
		public List<ValueExtractAlarm> checkAdminUnverified(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
		
		public List<ValueExtractAlarm> checkingData(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
		public List<ValueExtractAlarm> checkAdminUnverified(String workplaceID,String employeeID,DatePeriod datePeriod);
		
		public List<ValueExtractAlarm> checkContinuousVacation(String employeeID,DatePeriod datePeriod);
}
