package nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.stamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.application.reflectprocess.DailyRecordOfApplication;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.UpdateEditSttCreateBeforeAppReflect;
import nts.uk.ctx.at.shared.dom.application.stamp.TimeStampAppOtherShare;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;

/**
 * @author thanh_nx
 *
 *         休憩時間帯の反映
 */
public class ReflectBreakTime {

	public static List<Integer> reflect(DailyRecordOfApplication dailyApp,
			List<TimeStampAppOtherShare> listTimeStampAppOther) {

		List<Integer> lstItemId = new ArrayList<Integer>();

		listTimeStampAppOther.stream().forEach(data -> {

//			if (dailyApp.getBreakTime().isPresent()) {

					Optional<BreakTimeSheet> brs = dailyApp.getBreakTime()
							.getBreakTimeSheets().stream().filter(y ->
								y.getBreakFrameNo().v() == data.getDestinationTimeZoneApp().getEngraveFrameNo())
							.findFirst();
					if (brs.isPresent()) {
						brs.get().setStartTime(data.getTimeZone().getStartTime());
						brs.get().setEndTime(data.getTimeZone().getEndTime());
					} else {
						dailyApp.getBreakTime().getBreakTimeSheets().add(create(data));
					}

//			} else {
//				List<BreakTimeSheet> lstBreak = new ArrayList<>();
//				lstBreak.add(create(data));
//				dailyApp.setBreakTime(Optional.of(new BreakTimeOfDailyAttd(lstBreak)));
//			}
			lstItemId.addAll(createId(data.getDestinationTimeZoneApp().getEngraveFrameNo()));
		});

		// 申請反映状態にする
		UpdateEditSttCreateBeforeAppReflect.update(dailyApp, lstItemId);
		return lstItemId;
	}

	private static BreakTimeSheet create(TimeStampAppOtherShare data) {
		return new BreakTimeSheet(new BreakFrameNo(data.getDestinationTimeZoneApp().getEngraveFrameNo()),
				data.getTimeZone().getStartTime(), data.getTimeZone().getEndTime());
	}

	private static List<Integer> createId(int no) {
		return Arrays.asList(CancelAppStamp.createItemId(157, no, 6), CancelAppStamp.createItemId(159, no, 6));
	}
}
