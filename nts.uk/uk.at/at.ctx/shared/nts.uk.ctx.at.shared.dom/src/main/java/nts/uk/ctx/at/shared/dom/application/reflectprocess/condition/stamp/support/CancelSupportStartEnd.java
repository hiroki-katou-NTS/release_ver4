package nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.stamp.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.DailyRecordOfApplication;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.UpdateEditSttCreateBeforeAppReflect;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.stamp.CancelAppStamp;
import nts.uk.ctx.at.shared.dom.application.stamp.DestinationTimeAppShare;
import nts.uk.ctx.at.shared.dom.application.stamp.StartEndClassificationShare;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.ReasonTimeChange;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkTimeInformation;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.temporarytime.WorkNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.OuenWorkTimeSheetOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.TimeSheetOfAttendanceEachOuenSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.WorkContent;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.record.WorkplaceOfWorkEachOuen;

/**
 * @author thanh_nx
 *
 *         応援開始・終了打刻の取消
 */
public class CancelSupportStartEnd {

	public static List<Integer> process(DailyRecordOfApplication dailyApp,
			List<DestinationTimeAppShare> listDestinationTimeApp) {

		List<Integer> lstItemId = new ArrayList<>();
		// [input. 打刻取消(List)]でループ
		listDestinationTimeApp.stream().forEach(data -> {
			Optional<OuenWorkTimeSheetOfDailyAttendance> ouenOpt = dailyApp.getOuenTimeSheet().stream()
					.filter(x -> x.getWorkNo() == data.getSupportWork().orElse(Integer.MAX_VALUE)).findFirst();

			if (ouenOpt.isPresent()) {
				// 処理中の応援枠NOがキーとなる[応援]をクリアする
				val result = update(ouenOpt.get(), data);
				OuenWorkTimeSheetOfDailyAttendance update = result.getLeft();

				dailyApp.getOuenTimeSheet().remove(ouenOpt.get());
				dailyApp.getOuenTimeSheet().add(update);
				lstItemId.addAll(result.getRight());
			}
		});

		// 申請反映状態にする
		UpdateEditSttCreateBeforeAppReflect.update(dailyApp, lstItemId);
		return lstItemId;
	}

	private static Pair<OuenWorkTimeSheetOfDailyAttendance, List<Integer>> update(
			OuenWorkTimeSheetOfDailyAttendance old, DestinationTimeAppShare data) {
		List<Integer> lstItemId = new ArrayList<>();
		TimeSheetOfAttendanceEachOuenSheet sheet = null;
		if (data.getStartEndClassification() == StartEndClassificationShare.START) {
			sheet = TimeSheetOfAttendanceEachOuenSheet.create(old.getTimeSheet().getWorkNo(),
					Optional.of(new WorkTimeInformation(
							new ReasonTimeChange(TimeChangeMeans.APPLICATION, old.getTimeSheet().getStart()
									.map(x -> x.getReasonTimeChange().getEngravingMethod()).orElse(null)),
							null)),
					old.getTimeSheet().getEnd());
			lstItemId.add(CancelAppStamp.createItemId(929, data.getEngraveFrameNo(), 10));
		} else {
			sheet = TimeSheetOfAttendanceEachOuenSheet.create(new WorkNo(data.getEngraveFrameNo()),
					old.getTimeSheet().getStart(),
					Optional.of(new WorkTimeInformation(
							new ReasonTimeChange(TimeChangeMeans.APPLICATION, old.getTimeSheet().getEnd()
									.map(x -> x.getReasonTimeChange().getEngravingMethod()).orElse(null)),
							null)));
			lstItemId.add(CancelAppStamp.createItemId(930, data.getEngraveFrameNo(), 10));
		}

		WorkplaceOfWorkEachOuen workplace = WorkplaceOfWorkEachOuen
				.create(old.getWorkContent().getWorkplace().getWorkplaceId(), null);
		lstItemId.add(CancelAppStamp.createItemId(921, data.getEngraveFrameNo(), 10));

		WorkContent workContent = WorkContent.create(old.getWorkContent().getCompanyId(), workplace,
				old.getWorkContent().getWork());
		return Pair.of(OuenWorkTimeSheetOfDailyAttendance.create(old.getWorkNo(), workContent, sheet), lstItemId);

	}
}
