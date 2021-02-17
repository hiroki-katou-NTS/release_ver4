package nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.overtimeholiday.reflectbreak;

import java.util.List;

import nts.uk.ctx.at.shared.dom.application.reflectprocess.DailyRecordOfApplication;
import nts.uk.ctx.at.shared.dom.common.TimeZoneWithWorkNo;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.BreakApplication;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author thanh_nx
 *
 *         休憩の申請反映
 */
public class ReflectBreakApplication {

	public static void process(List<TimeZoneWithWorkNo> breakTimeOp, DailyRecordOfApplication dailyApp,
			BreakApplication breakLeaveApplication) {

		// [休憩を反映する]をチェック
		if (breakLeaveApplication.getBreakReflectAtr() == NotUseAtr.NOT_USE) {
			return;
		}

		// 休憩の反映 
		ReflectionOfBreak.process(breakTimeOp, dailyApp);
	}

}
