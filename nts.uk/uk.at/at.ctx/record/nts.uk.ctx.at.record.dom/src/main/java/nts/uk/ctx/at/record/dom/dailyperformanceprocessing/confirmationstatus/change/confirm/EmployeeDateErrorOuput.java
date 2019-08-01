package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@Getter
public class EmployeeDateErrorOuput {
	
	private String employeeId;
	
	private GeneralDate date;

	private Boolean hasError;

}
