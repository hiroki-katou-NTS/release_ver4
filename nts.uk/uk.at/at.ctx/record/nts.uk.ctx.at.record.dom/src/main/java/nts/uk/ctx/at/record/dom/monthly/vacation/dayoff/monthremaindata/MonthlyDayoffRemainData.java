package nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata;


//import javax.persistence.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
/**
 * 代休月別残数データ
 * @author do_dt
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyDayoffRemainData extends AggregateRoot{
	/**	社員ID */
	private String sId;
	/**年月	 */
	private YearMonth ym;
	/**締めID	 */
	private int closureId;
	/**	締め日 */
	private int closureDay;
	/**	末日とする */
	private boolean lastDayis;
	/**	締め処理状態 */
	private ClosureStatus closureStatus;
	/**	開始年月日 */
	private GeneralDate startDate;
	/**	終了年月日 */
	private GeneralDate endDate;
	/**	発生  1*/
	private DayOffDayAndTimes occurrenceDayTimes;	
	/**	使用 2 */
	private DayOffDayAndTimes useDayTimes;	
	/**	残日数, 残時間 */
	private DayOffRemainDayAndTimes remainingDayTimes;
	/**	繰越日数, 	繰越時間 */
	private DayOffRemainDayAndTimes carryForWardDayTimes;	
	/**	未消化日数, 未消化時間 */
	private DayOffDayAndTimes unUsedDayTimes;

}
