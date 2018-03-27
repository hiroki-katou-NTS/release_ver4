package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ScheTimeReflect;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ScheWorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectParameter;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.AttendanceOfficeAtr;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;

@Stateless
public class StartEndTimeOffReflectImpl implements StartEndTimeOffReflect{

	@Inject
	private WorkTypeIsClosedService worktypeService;
	@Inject 
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDaily;
	@Inject
	private ScheWorkUpdateService scheWorkUpdate;
	@Inject
	private ScheStartEndTimeReflect scheTimereflect;
	@Inject
	private ScheTimeReflect scheTime;
	
	@Override
	public void startEndTimeOffReflect(OvertimeParameter param, WorkInfoOfDailyPerformance workInfo) {
		//INPUT．勤種反映フラグ(実績)をチェックする
		if(!param.isActualReflectFlg()) {
			return;
		}
		//自動打刻をクリアする
		this.clearAutomaticEmbossing(param.getEmployeeId(),
				param.getDateInfo(),
				workInfo.getRecordWorkInformation().getWorkTypeCode().v(),
				param.isAutoClearStampFlg(),
				0);
		//開始終了時刻の反映(事前)
		this.startEndTimeOutput(param, workInfo);
	}

	@Override
	public void clearAutomaticEmbossing(String employeeId, GeneralDate dateData, String worktypeCode,
			boolean isClearAuto, Integer timeData) {
		// INPUT．自動セット打刻をクリアフラグをチェックする
		if(!isClearAuto) {
			return;
		}
		//打刻自動セット区分を取得する
		if(!worktypeService.checkStampAutoSet(worktypeCode, AttendanceOfficeAtr.ATTENDANCE)) {
			//打刻元情報を取得する
			Optional<TimeLeavingOfDailyPerformance> optTimeLeaving = timeLeavingOfDaily.findByKey(employeeId, dateData);
			if(!optTimeLeaving.isPresent()) {
				return;
			}
			TimeLeavingOfDailyPerformance timeLeaving = optTimeLeaving.get();
			List<TimeLeavingWork> leavingStamp = timeLeaving.getTimeLeavingWorks();
			if(leavingStamp.isEmpty()) {
				return;
			}
			List<TimeLeavingWork> lstLeavingStamp1 = leavingStamp.stream()
					.filter(x -> x.getWorkNo().v() == 1).collect(Collectors.toList());
			if(lstLeavingStamp1.isEmpty()) {
				return;
			}
			TimeLeavingWork leavingStamp1 = lstLeavingStamp1.get(0);
			WorkStamp workStamp = leavingStamp1.getLeaveStamp().get().getStamp().get();
			//出勤の打刻元情報が「打刻自動セット(個人情報)、直行直帰申請」
			if(workStamp.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
					|| workStamp.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT_APPLICATION) {
				//開始時刻の反映
				TimeReflectParameter timeReflect = new TimeReflectParameter(employeeId, dateData, timeData, 1, true);
				scheWorkUpdate.updateReflectStartEndTime(timeReflect);
			}
		}
		//打刻自動セット区分を取得する
		if(!worktypeService.checkStampAutoSet(worktypeCode, AttendanceOfficeAtr.OFFICEWORK)) {
			//打刻元情報を取得する
			Optional<TimeLeavingOfDailyPerformance> optTimeLeaving = timeLeavingOfDaily.findByKey(employeeId, dateData);
			if(!optTimeLeaving.isPresent()) {
				return;
			}
			TimeLeavingOfDailyPerformance timeLeaving = optTimeLeaving.get();
			List<TimeLeavingWork> leavingStamp = timeLeaving.getTimeLeavingWorks();
			if(leavingStamp.isEmpty()) {
				return;
			}
			List<TimeLeavingWork> lstLeavingStamp1 = leavingStamp.stream()
					.filter(x -> x.getWorkNo().v() == 1).collect(Collectors.toList());
			if(lstLeavingStamp1.isEmpty()) {
				return;
			}
			TimeLeavingWork leavingStamp1 = lstLeavingStamp1.get(0);
			WorkStamp workStamp = leavingStamp1.getLeaveStamp().get().getStamp().get();
			//出勤の打刻元情報が「打刻自動セット(個人情報)、直行直帰申請」
			if(workStamp.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
					|| workStamp.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT_APPLICATION) {
				//開始時刻の反映
				TimeReflectParameter timeReflect = new TimeReflectParameter(employeeId, dateData, timeData, 1, false);
				scheWorkUpdate.updateReflectStartEndTime(timeReflect);
			}
		}
	}

	@Override
	public void startEndTimeOutput(OvertimeParameter param,
			WorkInfoOfDailyPerformance workInfo) {
		//反映する開始終了時刻を求める
		WorkTimeTypeOutput workInfor = new WorkTimeTypeOutput(workInfo.getRecordWorkInformation().getWorkTimeCode() == null ? null : workInfo.getRecordWorkInformation().getWorkTimeCode().v(), 
				workInfo.getRecordWorkInformation().getWorkTypeCode() == null ? null : workInfo.getRecordWorkInformation().getWorkTypeCode().v());
		ScheStartEndTimeReflectOutput findStartEndTime = scheTimereflect.findStartEndTime(param, workInfor);
		//ジャスト遅刻早退により時刻を編集する
		StartEndTimeOutput justLateEarly = this.justLateEarly(workInfor.getWorktimeCode(), findStartEndTime);
		//１回勤務反映区分(output)をチェックする
		if(findStartEndTime.isCountReflect1Atr()) {
			//開始時刻を反映できるかチェックする
			if(scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getDateInfo(), 1, workInfor.getWorkTypeCode(), true)) {
				//開始時刻の反映
				TimeReflectParameter timeReflect = new TimeReflectParameter(param.getEmployeeId(), param.getDateInfo(), justLateEarly.getStart1(), 1, true);
				scheWorkUpdate.updateReflectStartEndTime(timeReflect);
			}
			//終了時刻を反映できるかチェックする
			if(scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getDateInfo(), 1, workInfor.getWorkTypeCode(), false)) {
				//終了時刻の反映
				TimeReflectParameter timeReflect = new TimeReflectParameter(param.getEmployeeId(), param.getDateInfo(), justLateEarly.getEnd1(), 1, false);
				scheWorkUpdate.updateReflectStartEndTime(timeReflect);
			}
		}
		//２回勤務反映区分(output)をチェックする
		if(findStartEndTime.isCountReflect2Atr()) {
			//開始時刻2を反映できるかチェックする
			if(scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getDateInfo(), 2, workInfor.getWorkTypeCode(), true)) {
				//開始時刻2の反映
				TimeReflectParameter timeReflect = new TimeReflectParameter(param.getEmployeeId(), param.getDateInfo(), justLateEarly.getStart2(), 2, true);
				scheWorkUpdate.updateReflectStartEndTime(timeReflect);
			}
			//終了時刻2を反映できるかチェックする
			if(scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getDateInfo(), 2, workInfor.getWorkTypeCode(), false)) {
				//終了時刻2の反映
				TimeReflectParameter timeReflect = new TimeReflectParameter(param.getEmployeeId(), param.getDateInfo(), justLateEarly.getEnd2(), 2, false);
				scheWorkUpdate.updateReflectStartEndTime(timeReflect);
			}
		}
		
	}

	@Override
	public StartEndTimeOutput justLateEarly(String worktimeCode, ScheStartEndTimeReflectOutput startEndTime) {
		StartEndTimeOutput startEnd = new StartEndTimeOutput(null, null, null, null);
		// INPUT．１回勤務反映区分をチェックする
		if(startEndTime.isCountReflect1Atr()) {
			//ジャスト遅刻により時刻を編集する
			Integer timeStart = scheTime.justTimeLateLeave(worktimeCode, startEndTime.getStartTime1(), 1, true);
			startEnd.setStart1(timeStart);
			//ジャスト早退により時刻を編集する
			Integer timeEnd = scheTime.justTimeLateLeave(worktimeCode, startEndTime.getEndTime1(), 1, false);
			startEnd.setEnd1(timeEnd);
		}
		//INPUT．２回勤務反映区分をチェックする
		if(startEndTime.isCountReflect2Atr()) {
			//ジャスト遅刻により時刻を編集する
			Integer timeStart2 = scheTime.justTimeLateLeave(worktimeCode, startEndTime.getStartTime2(), 2, true);
			startEnd.setStart2(timeStart2);
			//ジャスト早退により時刻を編集する
			Integer timeEnd2 = scheTime.justTimeLateLeave(worktimeCode, startEndTime.getEndTime2(), 2, false);
			startEnd.setEnd2(timeEnd2);
		}
		
		return startEnd;
	}
}
