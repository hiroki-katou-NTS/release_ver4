package nts.uk.ctx.bs.employee.dom.temporaryabsence;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.history.strategic.UnduplicatableHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * Domain Name: 休職休業履歴
 * 
 * @author xuan vinh
 * @author danpv
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TempAbsenceHistory extends AggregateRoot
		implements UnduplicatableHistory<DateHistoryItem, DatePeriod, GeneralDate> {

	/**
	 * 会社ID
	 */
	private String companyId;
	
	/**
	 * 社員ID
	 */
	private String employeeId;

	/**
	 * 期間
	 */
	private List<DateHistoryItem> dateHistoryItems;

	@Override
	public List<DateHistoryItem> items() {
		return this.dateHistoryItems;
	}
	
}
