package nts.uk.ctx.at.record.dom.workrecord.actualsituation;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckShortage {
	
	private boolean checkShortage;

	private boolean retiredFlag;
	
	private Optional<DatePeriod> periodCheckLock;

	public static CheckShortage defaultShortage(boolean checkShortage) {
		return new CheckShortage(checkShortage, false, Optional.empty());
	}
    
	public CheckShortage createRetiredFlag(boolean retiredFlag) {
		this.retiredFlag = retiredFlag;
		return this;
	}
	
	public CheckShortage createCheckShortage(boolean checkShortage) {
		this.checkShortage = checkShortage;
		return this;
	}
	
	public CheckShortage createPeriodCheck(DatePeriod period) {
		this.periodCheckLock = period == null ?  Optional.empty() : Optional.of(period);
		return this;
	}
	
}
