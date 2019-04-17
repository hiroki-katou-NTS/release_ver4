package nts.uk.ctx.workflow.dom.adapter.bs.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@Getter
public class StatusOfEmpImport {
	
	private String employeeId;
	
	private List<DatePeriod> listPeriod;
}
