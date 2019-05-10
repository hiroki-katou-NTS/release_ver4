package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;

/**
 * 「期間内の振出振休残数を取得する」のOutputData
 * @author do_dt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AbsRecRemainMngOfInPeriod {
	/**
	 * 振出振休明細
	 */
	private List<AbsRecDetailPara> lstAbsRecMng;
	/**
	 * 残日数
	 */
	private double remainDays;
	/**
	 * 未消化日数
	 */
	private double unDigestedDays;
	/**
	 * 発生日数
	 */
	private double occurrenceDays;
	/**
	 * 使用日数
	 */
	private double useDays;
	/**
	 * 繰越日数
	 */
	private double carryForwardDays;
	/**
	 * 振休エラー
	 */
	private List<PauseError> pError;
	/**
	 * 前回の集計期間の翌日
	 */
	private Finally<GeneralDate> nextDay;
}
