package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ScheTimeReflect;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;

@Stateless
public class StartEndTimeOffReflectImpl implements StartEndTimeOffReflect{

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
				param.getOvertimePara());
		//開始終了時刻の反映(事前)
		/*StartEndTimeRelectCheck startEndTimeData = new StartEndTimeRelectCheck(param.getEmployeeId(), param.getDateInfo(), param.getOvertimePara().getStartTime1(), 
				param.getOvertimePara().getEndTime1(), param.getOvertimePara().getStartTime2(), 
				param.getOvertimePara().getEndTime2(), 
				param.getOvertimePara().getWorkTimeCode(), param.getOvertimePara().getWorkTypeCode(), param.getOvertimePara().getOvertimeAtr());
		this.startEndTimeOutput(startEndTimeData, workInfo);*/
	}

	@Override
	public void clearAutomaticEmbossing(String employeeId, GeneralDate dateData, String worktypeCode,
			boolean isClearAuto, OvertimeAppParameter overInfor) {
		/*// INPUT．自動セット打刻をクリアフラグをチェックする
		if(!isClearAuto) {
			return;
		}
		Optional<TimeLeavingOfDailyPerformance> optTimeLeaving = timeLeavingOfDaily.findByKey(employeeId, dateData);
		if(!optTimeLeaving.isPresent()) {
			return;
		}
		List<TimeLeavingWork> leavingStamp = optTimeLeaving.get().getTimeLeavingWorks();
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
		if(!leavingStamp1.getAttendanceStamp().isPresent()
				|| !leavingStamp1.getAttendanceStamp().get().getStamp().isPresent()) {
			return;
		}
		WorkStamp workStampStart = leavingStamp1.getAttendanceStamp().get().getStamp().get();
		//打刻自動セット区分を取得する
		if(!worktypeService.checkStampAutoSet(worktypeCode, AttendanceOfficeAtr.ATTENDANCE)
				&& (workStampStart.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
				|| workStampStart.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT)) {
			isStart = true;
		}
		if(!leavingStamp1.getLeaveStamp().isPresent()
				|| !leavingStamp1.getLeaveStamp().get().getStamp().isPresent()) {
			return;
		}
		WorkStamp workStampEnd = leavingStamp1.getLeaveStamp().get().getStamp().get();
		if(!worktypeService.checkStampAutoSet(worktypeCode, AttendanceOfficeAtr.OFFICEWORK)
				&& (workStampEnd.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
						|| workStampEnd.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT)) {
			isEnd = true;
		}
		if(!isStart && !isEnd) {
			return;
		}
		TimeReflectPara timeReflectData = new TimeReflectPara(employeeId, dateData, overInfor.getStartTime1(), overInfor.getEndTime1(), 1, isStart, isEnd);
		scheWorkUpdate.updateRecordStartEndTimeReflect(timeReflectData);*/
	}

	@Override
	public void startEndTimeOutput(StartEndTimeRelectCheck param, IntegrationOfDaily dailyInfor) {
		WorkInfoOfDailyPerformance workInfo = dailyInfor.getWorkInformation();
		//反映する開始終了時刻を求める
		WorkTimeTypeOutput workInfor = new WorkTimeTypeOutput(workInfo.getRecordInfo().getWorkTimeCode() == null ? null : workInfo.getRecordInfo().getWorkTimeCode().v(), 
				workInfo.getRecordInfo().getWorkTypeCode() == null ? null : workInfo.getRecordInfo().getWorkTypeCode().v());
		ScheStartEndTimeReflectOutput findStartEndTime = scheTimereflect.findStartEndTime(param, workInfor);
		//ジャスト遅刻早退により時刻を編集する
		StartEndTimeOutput justLateEarly = this.justLateEarly(workInfor.getWorktimeCode(), findStartEndTime);
		//１回勤務反映区分(output)をチェックする
		if(findStartEndTime.isCountReflect1Atr()) {			
			//開始時刻を反映できるかチェックする
			boolean isStart = scheTimereflect.checkRecordStartEndTimereflect(param.getEmployeeId(), param.getBaseDate(), 1,
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(), true);
			//終了時刻を反映できるかチェックする
			boolean isEnd = scheTimereflect.checkRecordStartEndTimereflect(param.getEmployeeId(), param.getBaseDate(), 1, 
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(), false);
			TimeReflectPara timePara1 = new TimeReflectPara(param.getEmployeeId(), param.getBaseDate(), justLateEarly.getStart1(), justLateEarly.getEnd1(), 1, isStart, isEnd);
			scheWorkUpdate.updateRecordStartEndTimeReflect(timePara1, dailyInfor);
		}
		//２回勤務反映区分(output)をチェックする
		if(findStartEndTime.isCountReflect2Atr()) {			
			/*//開始時刻2を反映できるかチェックする
			boolean isStart = scheTimereflect.checkRecordStartEndTimereflect(param.getEmployeeId(), param.getBaseDate(), 2, 
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(), true);
			//終了時刻2を反映できるかチェックする
			boolean isEnd = scheTimereflect.checkRecordStartEndTimereflect(param.getEmployeeId(), param.getBaseDate(), 2, 
					workInfor.getWorkTypeCode(), param.getOverTimeAtr(),false);
			TimeReflectPara timePara2 = new TimeReflectPara(param.getEmployeeId(), param.getBaseDate(), justLateEarly.getStart2(), justLateEarly.getEnd2(), 2, isStart, isEnd);
			scheWorkUpdate.updateRecordStartEndTimeReflect(timePara2, attendanceLeave);*/
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
