package nts.uk.ctx.at.record.dom.adapter.company;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusOfEmployeeExport {
	
	private String employeeId;

	private List<DatePeriod> listPeriod;
}
