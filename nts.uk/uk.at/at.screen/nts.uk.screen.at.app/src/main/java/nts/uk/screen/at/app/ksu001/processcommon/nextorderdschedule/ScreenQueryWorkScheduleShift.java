package nts.uk.screen.at.app.ksu001.processcommon.nextorderdschedule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.schedule.workschedule.ScheManaStatuTempo;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.screen.at.app.ksu001.displayinshift.ShiftMasterMapWithWorkStyle;
import nts.uk.screen.at.app.ksu001.processcommon.CreateWorkScheduleShift;
import nts.uk.screen.at.app.ksu001.processcommon.CreateWorkScheduleShiftBase;
import nts.uk.screen.at.app.ksu001.processcommon.WorkScheduleShiftBaseResult;
import nts.uk.screen.at.app.ksu001.processcommon.WorkScheduleShiftResult;

/**
 * 勤務予定（シフト）dtoを作成する
 * @author hoangnd
 * todo
 */
@Stateless
public class ScreenQueryWorkScheduleShift {

	
	
	@Inject
	private CreateWorkScheduleShift createWorkScheduleShift;
	
	@Inject
	private CreateWorkScheduleShiftBase createWorkScheduleShiftBase;
	/**
	 * 
	 * @param listShiftMasterNotNeedGetNew 新たに取得する必要のないシフト一覧：List<シフトマスタ>
	 * @param mngStatusAndWScheMap 管理状態と勤務予定Map：Map<社員の予定管理状態, Optional<勤務予定>>
	 * @param mapDataDaily 管理状態と勤務実績Map：Map<社員の予定管理状態, Optional<日別勤怠(Work)>>
	 * @param isAchievement 実績も取得するか：boolean
	 * @return
	 */
	public WorkScheduleShiftBaseResult create(
			List<ShiftMasterMapWithWorkStyle> listShiftMasterNotNeedGetNew,
			Map<ScheManaStatuTempo, Optional<WorkSchedule>> mngStatusAndWScheMap,
			Map<ScheManaStatuTempo, Optional<IntegrationOfDaily>> mapDataDaily,
			Boolean isAchievement
			) {
		WorkScheduleShiftBaseResult output = new WorkScheduleShiftBaseResult(
				Collections.emptyList(),
				new HashMap()
				);
		// 1: call
		WorkScheduleShiftResult workScheduleShiftResult = 
				createWorkScheduleShift.getWorkScheduleShift(
							mngStatusAndWScheMap,
							listShiftMasterNotNeedGetNew);
		
		// 2 実績も取得するか == true
		if (isAchievement) {
			// 2.1: <call>
			WorkScheduleShiftBaseResult workScheduleShiftBaseResult = 
					createWorkScheduleShiftBase.getWorkScheduleShiftBase(
							mapDataDaily,
							listShiftMasterNotNeedGetNew);
			// 2.2 
			/**
			 * 予定と実績のList<勤務予定（シフト）dto>をMargeする。
				実績が存在する日（社員ID、年月日が一致するデータが存在する場合）は、
				取得した予定を実績で上書きしてListを作る。 
			 */
			
			// todo
			
			
		}
		
		return output;
		
	}
}
