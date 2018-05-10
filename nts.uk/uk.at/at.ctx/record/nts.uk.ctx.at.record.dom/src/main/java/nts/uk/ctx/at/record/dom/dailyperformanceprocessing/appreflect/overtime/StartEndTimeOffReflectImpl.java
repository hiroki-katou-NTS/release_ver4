package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ScheTimeReflect;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
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
	private WorkUpdateService scheWorkUpdate;
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
				workInfo.getRecordInfo().getWorkTypeCode().v(),
				param.isAutoClearStampFlg(),
				0);
		//開始終了時刻の反映(事前)
		StartEndTimeRelectCheck startEndTimeData = new StartEndTimeRelectCheck(param.getEmployeeId(), param.getDateInfo(), param.getOvertimePara().getStartTime1(), 
				param.getOvertimePara().getEndTime1(), param.getOvertimePara().getStartTime2(), 
				param.getOvertimePara().getEndTime2(), 
				param.getOvertimePara().getWorkTimeCode(), param.getOvertimePara().getWorkTypeCode(), param.getOvertimePara().getOvertimeAtr());
		this.startEndTimeOutput(startEndTimeData, workInfo);
	}

	@Override
	public void clearAutomaticEmbossing(String employeeId, GeneralDate dateData, String worktypeCode,
			boolean isClearAuto, Integer timeData) {
		// INPUT．自動セット打刻をクリアフラグをチェックする
		if(!isClearAuto) {
			return;
		}
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
		boolean isStart = false;
		boolean isEnd = false;
		WorkStamp workStampStart = leavingStamp1.getAttendanceStamp().get().getStamp().get();
		//打刻自動セット区分を取得する
		if(!worktypeService.checkStampAutoSet(worktypeCode, AttendanceOfficeAtr.ATTENDANCE)
				&& (workStampStart.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
				|| workStampStart.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT)) {
			isStart = true;
		}
		WorkStamp workStampEnd = leavingStamp1.getLeaveStamp().get().getStamp().get();
		if(!worktypeService.checkStampAutoSet(worktypeCode, AttendanceOfficeAtr.OFFICEWORK)
				&& (workStampEnd.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
						|| workStampEnd.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT)) {
			isEnd = true;
		}
		TimeReflectPara timeReflectData = new TimeReflectPara(employeeId, dateData, timeData, timeData, 1, isStart, isEnd);
		scheWorkUpdate.updateRecordStartEndTimeReflect(timeReflectData);
	}

	@Override
	public void startEndTimeOutput(StartEndTimeRelectCheck param,
			WorkInfoOfDailyPerformance workInfo) {
		//反映する開始終了時刻を求める
		WorkTimeTypeOutput workInfor = new WorkTimeTypeOutput(workInfo.getRecordInfo().getWorkTimeCode() == null ? null : workInfo.getRecordInfo().getWorkTimeCode().v(), 
				workInfo.getRecordInfo().getWorkTypeCode() == null ? null : workInfo.getRecordInfo().getWorkTypeCode().v());
		ScheStartEndTimeReflectOutput findStartEndTime = scheTimereflect.findStartEndTime(param, workInfor);
		//ジャスト遅刻早退により時刻を編集する
		StartEndTimeOutput justLateEarly = this.justLateEarly(workInfor.getWorktimeCode(), findStartEndTime);
		//１回勤務反映区分(output)をチェックする
		if(findStartEndTime.isCountReflect1Atr()) {			
			//開始時刻を反映できるかチェックする
			boolean isStart = scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getBaseDate(), 1,
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(), true);
			//終了時刻を反映できるかチェックする
			boolean isEnd = scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getBaseDate(), 1, 
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(), false);
			TimeReflectPara timePara1 = new TimeReflectPara(param.getEmployeeId(), param.getBaseDate(), justLateEarly.getStart1(), justLateEarly.getEnd1(), 1, isStart, isEnd);
			scheWorkUpdate.updateRecordStartEndTimeReflect(timePara1);
		}
		//２回勤務反映区分(output)をチェックする
		if(findStartEndTime.isCountReflect2Atr()) {			
			//開始時刻2を反映できるかチェックする
			boolean isStart = scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getBaseDate(), 2, 
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(), true);
			//終了時刻2を反映できるかチェックする
			boolean isEnd = scheTimereflect.checkStartEndTimeReflect(param.getEmployeeId(), param.getBaseDate(), 2, 
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(),false);
			TimeReflectPara timePara2 = new TimeReflectPara(param.getEmployeeId(), param.getBaseDate(), justLateEarly.getStart2(), justLateEarly.getEnd2(), 2, isStart, isEnd);
			scheWorkUpdate.updateRecordStartEndTimeReflect(timePara2);
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
