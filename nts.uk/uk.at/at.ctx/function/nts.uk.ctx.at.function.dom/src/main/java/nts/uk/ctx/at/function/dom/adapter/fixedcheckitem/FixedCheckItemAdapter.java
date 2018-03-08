package nts.uk.ctx.at.function.dom.adapter.fixedcheckitem;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
public interface FixedCheckItemAdapter {
	
		public Optional<ValueExtractAlarm>  checkWorkTypeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTypeCD);
		
		public Optional<ValueExtractAlarm>  checkWorkTimeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTimeCD);
		
		public List<ValueExtractAlarm> checkPrincipalUnconfirm(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
		
		public List<ValueExtractAlarm> checkAdminUnverified(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
		
		public List<ValueExtractAlarm> checkingData(List<ValueExtractAlarm> listValue,String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
}
