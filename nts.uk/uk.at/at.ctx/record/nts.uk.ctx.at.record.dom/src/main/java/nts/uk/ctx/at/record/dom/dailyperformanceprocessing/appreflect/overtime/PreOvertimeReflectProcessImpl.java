package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
@Stateless
public class PreOvertimeReflectProcessImpl implements PreOvertimeReflectProcess{
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private WorkTimeIsFluidWork workTimeService;
	@Inject
	private WorkUpdateService workUpdate;
	@Inject
	private ScheStartEndTimeReflect scheStartEndTimeReflect;
	
	@Override
	public WorkInfoOfDailyPerformance workTimeWorkTimeUpdate(OvertimeParameter para, WorkInfoOfDailyPerformance dailyInfo) {
		//ＩNPUT．勤務種類コードとＩNPUT．就業時間帯コードをチェックする		
		//INPUT．勤種反映フラグ(予定)をチェックする
		if(para.getOvertimePara().getWorkTimeCode() == null || para.getOvertimePara().getWorkTimeCode().isEmpty()
				|| para.getOvertimePara().getWorkTypeCode() == null || para.getOvertimePara().getWorkTypeCode().isEmpty()
				|| !para.isScheReflectFlg()) {
			return dailyInfo;
		}
				
		ReflectParameter reflectInfo = new ReflectParameter(para.getEmployeeId(), 
				para.getDateInfo(), 
				para.getOvertimePara().getWorkTimeCode(), 
				para.getOvertimePara().getWorkTypeCode(),
				false); 
		return workUpdate.updateWorkTimeType(reflectInfo, true, dailyInfo);	
	}
	
	@Override
	public AppReflectRecordWork changeFlg(OvertimeParameter para, WorkInfoOfDailyPerformance dailyInfo) {
		boolean ischeck = false;
		//ＩNPUT．勤務種類コードとＩNPUT．就業時間帯コードをチェックする
		//INPUT．勤種反映フラグ(実績)をチェックする
		if(para.getOvertimePara().getWorkTimeCode() == null || para.getOvertimePara().getWorkTimeCode().isEmpty()
				|| para.getOvertimePara().getWorkTypeCode() == null || para.getOvertimePara().getWorkTypeCode().isEmpty()
				|| !para.isActualReflectFlg()) {
			return new AppReflectRecordWork(ischeck, dailyInfo);
		}
		
		//反映前後勤就に変更があるかチェックする
		//取得した勤務種類コード ≠ INPUT．勤務種類コード OR
		//取得した就業時間帯コード ≠ INPUT．就業時間帯コード
		
		if(dailyInfo.getRecordInfo().getSiftCode() == null
				|| dailyInfo.getRecordInfo().getWorkTypeCode() == null
				|| !dailyInfo.getRecordInfo().getWorkTimeCode().v().equals(para.getOvertimePara().getWorkTimeCode())
				||!dailyInfo.getRecordInfo().getWorkTypeCode().v().equals(para.getOvertimePara().getWorkTypeCode())){
			ischeck = true;
		} 
		//勤種・就時の反映
		ReflectParameter reflectInfo = new ReflectParameter(para.getEmployeeId(), 
				para.getDateInfo(), 
				para.getOvertimePara().getWorkTimeCode(), 
				para.getOvertimePara().getWorkTypeCode(),
				false); 
		dailyInfo = workUpdate.updateWorkTimeType(reflectInfo, false, dailyInfo);
		return new AppReflectRecordWork(ischeck, dailyInfo);
		
	}
	@Override
	public WorkInfoOfDailyPerformance startAndEndTimeReflectSche(OvertimeParameter para, boolean changeFlg,
			WorkInfoOfDailyPerformance dailyData) {
		//設定による予定開始終了時刻を反映できるかチェックする
		if(!this.timeReflectCheck(para, changeFlg, dailyData)) {
			return dailyData;
		}
		//予定開始終了時刻の反映(事前事後共通部分)
		//WorkTimeTypeOutput timeTypeData = this.getScheWorkTimeType(para.getEmployeeId(), para.getDateInfo());
		
		WorkTimeTypeOutput dataOut = new WorkTimeTypeOutput(dailyData.getScheduleInfo().getWorkTimeCode() == null ? null : dailyData.getScheduleInfo().getWorkTimeCode().v(),
				dailyData.getScheduleInfo().getWorkTypeCode() == null ? null : dailyData.getScheduleInfo().getWorkTypeCode().v());
		return scheStartEndTimeReflect.reflectScheStartEndTime(para, dataOut, dailyData);
	}
	@Override
	public boolean timeReflectCheck(OvertimeParameter para, boolean changeFlg,
			WorkInfoOfDailyPerformance dailyData) {
		//INPUT．勤種反映フラグ(予定)をチェックする
		if(para.isScheReflectFlg()) {
			return true;
		}
		//実績に反映するかチェックする
		//INPUT．勤種反映フラグ(実績) == する AND
		//INPUT．予定出退勤反映フラグ == する AND
		//反映前後勤就の変更フラグ == true
		if(!para.isActualReflectFlg()
				|| !para.isScheTimeOutFlg()
				|| !changeFlg) {
			return false;
		}
		//INPUT．予定と実績を同じに変更する区分をチェックする
		if(para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.ALWAYS_CHANGE_AUTO) {
			return true;
		}
		//流動勤務かどうかの判断処理
		return workTimeService.checkWorkTimeIsFluidWork(para.getOvertimePara().getWorkTimeCode());
		
	}

	@Override
	public WorkTimeTypeOutput getScheWorkTimeType(String employeeId, GeneralDate dataData) {
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(employeeId, dataData);
		if (!optDailyPerfor.isPresent()) {
			return null;
		}
		WorkInfoOfDailyPerformance dailyPerfor = optDailyPerfor.get();
		WorkTimeTypeOutput dataOut = new WorkTimeTypeOutput(dailyPerfor.getScheduleInfo().getWorkTimeCode() == null ? null : dailyPerfor.getScheduleInfo().getWorkTimeCode().v(),
				dailyPerfor.getScheduleInfo().getWorkTypeCode() == null ? null : dailyPerfor.getScheduleInfo().getWorkTypeCode().v());
		return dataOut;
	}

	@Override
	public WorkTimeTypeOutput getRecordWorkTimeType(String employeeId, GeneralDate dataData) {
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(employeeId, dataData);
		if (!optDailyPerfor.isPresent()) {
			return null;
		}
		WorkInfoOfDailyPerformance dailyPerfor = optDailyPerfor.get();
		WorkTimeTypeOutput dataOut = new WorkTimeTypeOutput(dailyPerfor.getRecordInfo().getWorkTimeCode() == null ? null : dailyPerfor.getRecordInfo().getWorkTimeCode().v(),
				dailyPerfor.getRecordInfo().getWorkTypeCode() == null ? null : dailyPerfor.getRecordInfo().getWorkTypeCode().v());
		return dataOut;
	}

	@Override
	public AttendanceTimeOfDailyPerformance getReflectOfOvertime(OvertimeParameter para, AttendanceTimeOfDailyPerformance attendanceTimeData) {
		Map<Integer, Integer> tmp = new HashMap<>();
		for(Map.Entry<Integer,Integer> entry : para.getOvertimePara().getMapOvertimeFrame().entrySet()){
			//INPUT．残業時間のループ中の番をチェックする
			//INPUT．残業時間のループ中の番を、残業時間(反映用)に追加する
			if(entry.getValue() != null && entry.getValue() >= 0) {
				tmp.put(entry.getKey(), entry.getValue());
			}
		}
		
		//残業時間の反映
		return workUpdate.reflectOffOvertime(para.getEmployeeId(), para.getDateInfo(), tmp, true, attendanceTimeData);
	}

	@Override
	public AttendanceTimeOfDailyPerformance overTimeShiftNight(String employeeId, GeneralDate dateData, boolean timeReflectFlg,
			Integer overShiftNight, AttendanceTimeOfDailyPerformance attendanceTimeData) {
		// INPUT．残業時間反映フラグをチェックする
		//INPUT．外深夜時間をチェックする
		if(overShiftNight == null || overShiftNight < 0) {
			return attendanceTimeData;
		}
		//所定外深夜時間の反映
		return workUpdate.updateTimeShiftNight(employeeId, dateData, overShiftNight, true, attendanceTimeData);
	}

	@Override
	public AttendanceTimeOfDailyPerformance reflectOfFlexTime(String employeeId, GeneralDate dateDate, boolean timeReflectFlg, 
			Integer flexExessTime, AttendanceTimeOfDailyPerformance attendanceTimeData) {
		//INPUT．残業時間反映フラグをチェックする
		//INPUT．フレックス時間をチェックする
		if(flexExessTime == null || flexExessTime < 0) {
			return attendanceTimeData;
		}
		//フレックス時間を反映する
		//日別実績の残業時間
		return workUpdate.updateFlexTime(employeeId, dateDate, flexExessTime, true, attendanceTimeData);
	}

}
