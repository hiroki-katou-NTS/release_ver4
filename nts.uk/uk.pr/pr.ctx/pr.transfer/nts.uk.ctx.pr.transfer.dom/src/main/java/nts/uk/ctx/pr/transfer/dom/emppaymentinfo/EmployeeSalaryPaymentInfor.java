package nts.uk.ctx.pr.transfer.dom.emppaymentinfo;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.history.YearMonthHistoryItem;
import nts.uk.shr.com.history.strategic.PersistentResidentHistory;
import nts.arc.time.calendar.period.YearMonthPeriod;

/**
 * 
 * @author HungTT - 社員給与支払情報
 *
 */

@Getter
public class EmployeeSalaryPaymentInfor extends AggregateRoot
		implements PersistentResidentHistory<YearMonthHistoryItem, YearMonthPeriod, YearMonth> {

	/**
	 * 社員ID
	 */
	private String employeeId;
	/**
	 * 期間
	 */
	private List<YearMonthHistoryItem> period;

	@Override
	public List<YearMonthHistoryItem> items() {
		return this.period;
	}

	public EmployeeSalaryPaymentInfor(String employeeId, List<YearMonthHistoryItem> period) {
		super();
		this.employeeId = employeeId;
		this.period = period;
	}

}
