package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;
/**
 * 特別休暇の集計結果情報
 * @author do_dt
 *
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InPeriodOfSpecialLeaveResultInfor {
	/**
	 * 特別休暇情報
	 */
	private InPeriodOfSpecialLeave aggSpecialLeaveResult;
	/**
	 * 特別休暇情報(期間終了日の翌日開始時点)
	 */
	private Finally<InPeriodOfSpecialLeave> aggSpecialLeaveNextResult;
	/**
	 * 前回集計期間の翌日
	 */
	private Finally<GeneralDate> nextDate;
}
