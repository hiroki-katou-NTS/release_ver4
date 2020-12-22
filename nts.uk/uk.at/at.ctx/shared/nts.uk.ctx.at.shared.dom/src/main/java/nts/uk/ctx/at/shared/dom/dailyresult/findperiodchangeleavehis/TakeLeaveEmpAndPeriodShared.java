package nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
@Setter
@NoArgsConstructor
public class TakeLeaveEmpAndPeriodShared {
	private String hisId;
	
	private DatePeriod period;
	
	private TempAbsenceHisItemShared tempAbsenceHisItemShared;

	public TakeLeaveEmpAndPeriodShared(String hisId, DatePeriod period,
			TempAbsenceHisItemShared tempAbsenceHisItemShared) {
		super();
		this.hisId = hisId;
		this.period = period;
		this.tempAbsenceHisItemShared = tempAbsenceHisItemShared;
	}
	
}
