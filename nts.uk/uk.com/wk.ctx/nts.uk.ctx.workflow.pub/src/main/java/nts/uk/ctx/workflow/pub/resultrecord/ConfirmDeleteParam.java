package nts.uk.ctx.workflow.pub.resultrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.closure.ClosureMonth;

@AllArgsConstructor
@Getter
public class ConfirmDeleteParam {

	/**
	 * 年月
	 */
	private YearMonth yearMonth;
	
	/**
	 * 締めID
	 */
	private Integer closureID;
	
	/**
	 * 締め日
	 */
	private ClosureDate closureDate;
		
	
	
	public ClosureMonth toClosureMonth() {
		return new ClosureMonth(this.yearMonth, this.closureID, this.closureDate);
	}
}
