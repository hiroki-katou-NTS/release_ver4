package nts.uk.file.at.app.export.bento;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;


/**
 * 月間予約台帳
 * @author Doan Duy Hung
 *
 */
@NoArgsConstructor
@Data
public class ReservationMonthDataSource {
	
	/**
	 * 会社名
	 */
	private String companyName;
	
	private String title;
	
	/**
	 * 対象期間
	 */
	private DatePeriod period;
	
	private List<ReservationWkpLedger> wkpLedgerLst;
}
