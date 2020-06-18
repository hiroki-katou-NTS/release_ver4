package nts.uk.ctx.at.shared.pub.workrule.closure;

import lombok.Getter;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Getter
public class ClosureDateExport {
	/** The closure day. */
	// 日
	private Integer closureDay;

	/** The last day of month. */
	// 末日とする
	private Boolean lastDayOfMonth;
	
	public ClosureDateExport(Integer closureDay, Boolean lastDayOfMonth) {
		this.closureDay = closureDay;
		this.lastDayOfMonth = lastDayOfMonth;
	}
	
	public ClosureDate toClosureDate() {
		return new ClosureDate(this.closureDay, this.lastDayOfMonth);
	}

	public static ClosureDateExport from(ClosureDate closureDate) {
		return new ClosureDateExport(closureDate.getClosureDay().v(), closureDate.getLastDayOfMonth());
	}
}
