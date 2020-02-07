package nts.uk.ctx.at.record.pub.dailyperformanceformat.businesstype;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.history.strategic.PersistentHistory;
import nts.arc.time.calendar.period.DatePeriod;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessTypeOfEmployeeHistoryEx implements PersistentHistory<DateHistoryItem, DatePeriod, GeneralDate> {
	/** 会社ID */
	String companyId;

	/** 履歴 */
	List<DateHistoryItem> history;

	/** 社員ID */
	String employeeId;

	@Override
	public List<DateHistoryItem> items() {
		return this.history;
	}

}
