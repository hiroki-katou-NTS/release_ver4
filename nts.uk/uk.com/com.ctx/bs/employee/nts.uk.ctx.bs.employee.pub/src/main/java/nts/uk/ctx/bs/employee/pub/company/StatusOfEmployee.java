package nts.uk.ctx.bs.employee.pub.company;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusOfEmployee {
	
	private String employeeId;
	
	private List<DatePeriod> listPeriod;

}
