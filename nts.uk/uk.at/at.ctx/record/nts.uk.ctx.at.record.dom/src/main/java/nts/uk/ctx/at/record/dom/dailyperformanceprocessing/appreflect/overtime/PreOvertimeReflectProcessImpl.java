package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ScheWorkUpdateService;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
@Stateless
public class PreOvertimeReflectProcessImpl implements PreOvertimeReflectProcess{
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private WorkTimeIsFluidWork workTimeService;
	@Inject
	private ScheWorkUpdateService workUpdate;
	@Inject
	private ScheStartEndTimeReflect scheStartEndTimeReflect;
	
	@Override
	public void workTimeWorkTimeUpdate(OvertimeParameter para) {
		//ＩNPUT．勤務種類コードとＩNPUT．就業時間帯コードをチェックする		
		//INPUT．勤種反映フラグ(予定)をチェックする
		if(para.getOvertimePara().getWorkTimeCode().isEmpty()
				|| para.getOvertimePara().getWorkTypeCode().isEmpty()
				|| !para.isScheReflectFlg()) {
			return;
		}
				
		ReflectParameter reflectInfo = new ReflectParameter(para.getEmployeeId(), 
				para.getDateInfo(), 
				para.getOvertimePara().getWorkTimeCode(), 
				para.getOvertimePara().getWorkTypeCode()); 
		workUpdate.updateWorkTimeType(reflectInfo, true);	
	}
	
	@Override
	public boolean changeFlg(OvertimeParameter para) {
		//ＩNPUT．勤務種類コードとＩNPUT．就業時間帯コードをチェックする
		//INPUT．勤種反映フラグ(実績)をチェックする
		if(para.getOvertimePara().getWorkTimeCode().isEmpty()
				|| para.getOvertimePara().getWorkTypeCode().isEmpty()
				|| !para.isActualReflectFlg()) {
			return false;
		}
				
		//勤種・就時の反映
		ReflectParameter reflectInfo = new ReflectParameter(para.getEmployeeId(), 
				para.getDateInfo(), 
				para.getOvertimePara().getWorkTimeCode(), 
				para.getOvertimePara().getWorkTypeCode()); 
		workUpdate.updateWorkTimeType(reflectInfo, false);
		//ドメインモデル「日別実績の勤務情報」を取得する
		WorkInfoOfDailyPerformance dailyPerfor = workRepository.find(para.getEmployeeId(), para.getDateInfo()).get();
		//反映前後勤就に変更があるかチェックする
		//取得した勤務種類コード ≠ INPUT．勤務種類コード OR
		//取得した就業時間帯コード ≠ INPUT．就業時間帯コード
		if(!dailyPerfor.getRecordWorkInformation().getWorkTimeCode().v().equals(para.getOvertimePara().getWorkTimeCode())
				||!dailyPerfor.getRecordWorkInformation().getWorkTypeCode().v().equals(para.getOvertimePara().getWorkTypeCode())){
			 return true;
		}
		
		return false;
		
	}
	@Override
	public void startAndEndTimeReflectSche(OvertimeParameter para, boolean changeFlg,
			WorkInfoOfDailyPerformance dailyData) {
		//設定による予定開始終了時刻を反映できるかチェックする
		if(!this.timeReflectCheck(para, changeFlg, dailyData)) {
			return;
		}
		//予定開始終了時刻の反映(事前事後共通部分)
		//WorkTimeTypeOutput timeTypeData = this.getScheWorkTimeType(para.getEmployeeId(), para.getDateInfo());
		
		WorkTimeTypeOutput dataOut = new WorkTimeTypeOutput(dailyData.getScheduleWorkInformation().getWorkTimeCode() == null ? null : dailyData.getScheduleWorkInformation().getWorkTimeCode().v(),
				dailyData.getScheduleWorkInformation().getWorkTypeCode() == null ? null : dailyData.getScheduleWorkInformation().getWorkTypeCode().v());
		scheStartEndTimeReflect.reflectScheStartEndTime(para, dataOut);
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
		if(para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.ALWAY) {
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
		WorkTimeTypeOutput dataOut = new WorkTimeTypeOutput(dailyPerfor.getScheduleWorkInformation().getWorkTimeCode() == null ? null : dailyPerfor.getScheduleWorkInformation().getWorkTimeCode().v(),
				dailyPerfor.getScheduleWorkInformation().getWorkTypeCode() == null ? null : dailyPerfor.getScheduleWorkInformation().getWorkTypeCode().v());
		return dataOut;
	}

	@Override
	public WorkTimeTypeOutput getRecordWorkTimeType(String employeeId, GeneralDate dataData) {
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(employeeId, dataData);
		if (!optDailyPerfor.isPresent()) {
			return null;
		}
		WorkInfoOfDailyPerformance dailyPerfor = optDailyPerfor.get();
		WorkTimeTypeOutput dataOut = new WorkTimeTypeOutput(dailyPerfor.getRecordWorkInformation().getWorkTimeCode().v(),
				dailyPerfor.getRecordWorkInformation().getWorkTypeCode().v());
		return dataOut;
	}

	@Override
	public void getReflectOfOvertime(OvertimeParameter para) {
		//INPUT．残業時間反映フラグをチェックする
		if(!para.isTimeReflectFlg()) {
			return;
		}
		for(Map.Entry<Integer,Integer> entry : para.getOvertimePara().getMapOvertimeFrame().entrySet()){
			//INPUT．残業時間のループ中の番をチェックする
			//INPUT．残業時間のループ中の番を、残業時間(反映用)に追加する
			if(entry.getValue() <= 0) {
				para.getOvertimePara().getMapOvertimeFrame().remove(entry.getKey());
			}			
		}
		
		//残業時間の反映
		workUpdate.reflectOffOvertime(para.getEmployeeId(), para.getDateInfo(), para.getOvertimePara().getMapOvertimeFrame());
	}

	@Override
	public void overTimeShiftNight(String employeeId, GeneralDate dateData, boolean timeReflectFlg,
			Integer overShiftNight) {
		// INPUT．残業時間反映フラグをチェックする
		//INPUT．外深夜時間をチェックする
		if(!timeReflectFlg || overShiftNight <= 0) {
			return;
		}
		//所定外深夜時間の反映
		workUpdate.updateTimeShiftNight(employeeId, dateData, overShiftNight);
	}

	@Override
	public void reflectOfFlexTime(String employeeId, GeneralDate dateDate, boolean timeReflectFlg, Integer flexExessTime) {
		//INPUT．残業時間反映フラグをチェックする
		//INPUT．フレックス時間をチェックする
		if(!timeReflectFlg
				|| flexExessTime <= 0) {
			return;
		}
		//フレックス時間を反映する
		//日別実績の残業時間
		workUpdate.updateFlexTime(employeeId, dateDate, flexExessTime);
	}

}
