package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;

/**
 * @author ThanhNX
 *
 *         繰越数を計算する
 */
public class CalcCarryForwardNumber {

	private CalcCarryForwardNumber() {
	};

	// 繰越数を計算する
	public static ReserveLeaveRemainingDayNumber calc(List<AccumulationAbsenceDetail> data, GeneralDate date, boolean isMode) {

		// アルゴリズム「6.残数と未消化を集計する」を実行
		return new ReserveLeaveRemainingDayNumber(TotalRemainUndigest.process(data, date.addDays(-1), isMode).getRemainDays());

	}

}
