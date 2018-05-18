package nts.uk.ctx.at.shared.pub.workrule.closure;

import lombok.Getter;

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
}
