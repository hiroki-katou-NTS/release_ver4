package dailyattdcal.dailywork.algorithm.aftercorrectatt;

import javax.ejb.Stateless;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;

/**
 * @author ThanhNX
 *
 *         休憩時間帯を補正する
 */
@Stateless
public class CorrectRestTime {

	// 休憩時間帯を補正する
	public void correct(IntegrationOfDaily domainDaily) {

		// TODO: 日別勤怠の休憩時間帯を取得する

		// TODO: 休憩時間帯を取得

		// TODO: 休憩時間帯をマージする
		//MergeBreaksTime.merge(domainDaily.getEditState(), domainDaily.getBreakTime());
	}

}
