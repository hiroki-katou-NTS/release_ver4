package nts.uk.screen.at.app.query.kdp.kdps01.s;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * 
 * @author sonnlb
 *
 */
public class DisplayHistorySmartPhoneStampQuery {
	// 打刻日時(日付)
	private String startDate;
	// 打刻日時(日付)
	private String endDate;

	public DatePeriod getPeriod() {

		return new DatePeriod(GeneralDate.fromString(this.startDate, "yyyy/MM/dd"),
				GeneralDate.fromString(this.endDate, "yyyy/MM/dd"));
	}
}
