package nts.uk.ctx.at.record.app.command.dailyperform.checkdata;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RCDailyCorrectionResult {

	private List<IntegrationOfDaily> lstDailyDomain;
	
	private List<IntegrationOfMonthly> lstMonthDomain;
	
	private List<DailyRecordWorkCommand> commandNew;
	
	private List<DailyRecordWorkCommand> commandOld;
	
	private List<DailyItemValue> dailyItems;
	
	private boolean update;
	
	public RCDailyCorrectionResult filterDataError(Collection<String> errorRelease, Collection<String> lstEmployeeId) {
		
		lstDailyDomain = lstDailyDomain.stream()
				                       .filter(x -> !errorRelease.contains(x.getWorkInformation().getEmployeeId()+"|"+x.getWorkInformation().getYmd()) 
				                    		     && !lstEmployeeId.contains(x.getWorkInformation().getEmployeeId()+"|"+x.getWorkInformation().getYmd())).collect(Collectors.toList());
		
//		lstMonthDomain = lstMonthDomain.stream()
//                .filter(x -> x.getAttendanceTime().isPresent() && !lstEmployeeId.contains(x.getAttendanceTime().get().getEmployeeId())).collect(Collectors.toList());
		
		commandNew = commandNew.stream()
                .filter(x -> !errorRelease.contains(x.getWorkInfo().getEmployeeId()+"|"+x.getWorkInfo().getWorkDate()) 
             		     && !lstEmployeeId.contains(x.getWorkInfo().getEmployeeId()+"|"+x.getWorkInfo().getWorkDate())).collect(Collectors.toList());
		
		commandOld = commandOld.stream()
                .filter(x -> !errorRelease.contains(x.getWorkInfo().getEmployeeId()+"|"+x.getWorkInfo().getWorkDate()) 
             		     && !lstEmployeeId.contains(x.getWorkInfo().getEmployeeId()+"|"+x.getWorkInfo().getWorkDate())).collect(Collectors.toList());
		
		dailyItems = dailyItems.stream()
                .filter(x -> !errorRelease.contains(x.getEmployeeId()+"|"+x.getDate()) 
             		     && !lstEmployeeId.contains(x.getEmployeeId()+"|"+x.getDate())).collect(Collectors.toList());
		return this;
		
	};
}
