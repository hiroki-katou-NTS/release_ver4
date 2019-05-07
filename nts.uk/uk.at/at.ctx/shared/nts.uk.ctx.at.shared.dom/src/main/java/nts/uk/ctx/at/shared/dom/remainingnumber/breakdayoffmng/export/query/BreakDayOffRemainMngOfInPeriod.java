package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BreakDayOffRemainMngOfInPeriod {
	/**	List休出代休明細 */
	private List<BreakDayOffDetail> lstDetailData;
	/**	残日数 */
	private double remainDays;
	/**	残時間数 */
	private int remainTimes;
	/**	未消化日数 */
	private double unDigestedDays;
	/**	未消化時間 */
	private int unDigestedTimes;
	/**	発生日数 */
	private double occurrenceDays;
	/**	発生時間 */
	private int occurrenceTimes;
	/**	使用日数 */
	private double useDays;
	/**	使用時間 */
	private int useTimes;
	/**	繰越日数 */
	private double carryForwardDays;
	/**	繰越時間 */
	private int carryForwardTimes;
	/**
	 * 代休エラー
	 */
	private List<DayOffError> lstError;
	/**前回集計期間の翌日 */
	private Finally<GeneralDate> nextDay;
}
