package nts.uk.screen.at.app.dailyperformance.correction.dto.month;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DPMonthParent {
	private String employeeId;
	private int yearMonth;
	private int closureId;
	private ClosureDateDto closureDate;
	
	@Setter
	private long version;
	
	public DPMonthParent(String employeeId, int yearMonth, int closureId, ClosureDateDto closureDate) {
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
	}
}
