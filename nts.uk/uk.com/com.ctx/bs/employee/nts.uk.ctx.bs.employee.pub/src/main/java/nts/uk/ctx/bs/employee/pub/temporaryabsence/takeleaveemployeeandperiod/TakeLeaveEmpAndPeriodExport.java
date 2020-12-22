package nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
@Setter
@NoArgsConstructor
public class TakeLeaveEmpAndPeriodExport {
	private String hisId;
	
	private DatePeriod period;
	
	private TempAbsenceHisItemExport tempAbsenceHisItemExport;

	public TakeLeaveEmpAndPeriodExport(String hisId, DatePeriod period,
			TempAbsenceHisItemExport tempAbsenceHisItemExport) {
		super();
		this.hisId = hisId;
		this.period = period;
		this.tempAbsenceHisItemExport = tempAbsenceHisItemExport;
	}
	
}
