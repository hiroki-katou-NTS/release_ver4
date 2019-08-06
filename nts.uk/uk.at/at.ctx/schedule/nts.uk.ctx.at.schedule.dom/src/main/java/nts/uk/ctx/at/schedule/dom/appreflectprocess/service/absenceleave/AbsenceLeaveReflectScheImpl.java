package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.absenceleave;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AbsenceLeaveReflectScheImpl implements AbsenceLeaveReflectSche{
	@Inject
	private UpdateScheCommonAppRelect updateSche;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private BasicScheduleRepository basicScheRepo;
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeRepo;
	@Inject
	private WorkScheduleStateRepository workScheReposi;
	@Override
	public void absenceLeaveReflect(CommonReflectParamSche param) {
		BasicSchedule scheData = basicScheRepo.find(param.getEmployeeId(), param.getAppDate()).get();
		List<WorkScheduleState> lstState = workScheReposi.findByDateAndEmpId(param.getEmployeeId(), param.getAppDate());
		//勤種・就時の反映 
		updateSche.updateScheWorkType(scheData, 
				lstState, 
				param.getWorktypeCode());
		//開始予定・終了予定の置き換え
		this.absenceLeaveStartEndTimeReflect(param, scheData, lstState);
		basicScheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}

	@Override
	public void absenceLeaveStartEndTimeReflect(CommonReflectParamSche param, BasicSchedule scheData, List<WorkScheduleState> lstState) {
		//1日半日出勤・1日休日系の判定
		WorkStyle checkworkDay = basicService.checkWorkDay(param.getWorktypeCode());
		//日休日の場合
		if(checkworkDay == WorkStyle.ONE_DAY_REST) {
			//就業時間帯の必須チェック
			SetupType checkNeededOfWorkTimeSetting = basicService.checkNeededOfWorkTimeSetting(param.getWorktypeCode());
			//必須任意不要区分(output)が不要
			if(checkNeededOfWorkTimeSetting == SetupType.NOT_REQUIRED) {
				//就時の反映
				updateSche.updateScheWorkTime(scheData, 
						lstState, 
						null);//就業時間帯クリア
			}
			//勤務開始終了のクリア
			updateSche.updateStartTimeRflect(new TimeReflectScheDto(param.getEmployeeId(),
					param.getAppDate(), 0, 0, 1, true, true), scheData, lstState);
		} else if(checkworkDay == WorkStyle.AFTERNOON_WORK
				|| checkworkDay == WorkStyle.MORNING_WORK){ //出勤休日区分が午前出勤系又は、午後出勤系
			//就業時間帯が反映できるか
			WorkTimeIsReflect workTimeReflect = this.workTimeIsReflect(param.getEmployeeId(), 
					param.getAppDate(),
					param.getWorkTimeCode());
			if(workTimeReflect.isReflect()) {
				//就時の反映
				updateSche.updateScheWorkTime(scheData, 
						lstState, 
						workTimeReflect.getWorkTimeCode());
			}
			//開始終了時刻が反映できるか
			StartEndTimeIsReflect chkReflect = this.startEndTimeIsReflect(param.getEmployeeId(), 
					param.getAppDate(), 
					checkworkDay, 
					param.getStartTime(), 
					param.getEndTime(), 
					workTimeReflect.getWorkTimeCode());
			if(chkReflect.isChkReflect()) {
				//開始時刻の反映
				//終了時刻の反映
				updateSche.updateStartTimeRflect(new TimeReflectScheDto(param.getEmployeeId(),
						param.getAppDate(), chkReflect.getStartTime(), chkReflect.getEndTime(), 1, true, true), scheData, lstState);
			}
		}
			
	}

	@Override
	public WorkTimeIsReflect workTimeIsReflect(String employeeId, GeneralDate baseDate, String worktimeCode) {
		//反映できるフラグ=false、反映就業時間帯をクリア(初期化)
		WorkTimeIsReflect dataOutput = new WorkTimeIsReflect(false, null);
		//INPUT．就業時間帯をチェックする
		if(worktimeCode == null) {
			//ドメインモデル「勤務予定基本情報」を取得する
			Optional<BasicSchedule> optBasicSche = basicScheRepo.find(employeeId, baseDate);
			//ドメインモデル「勤務予定基本情報」．就業時間帯をチェックする
			if(optBasicSche.isPresent()
					|| optBasicSche.get().getWorkTimeCode() != "") {
				BasicSchedule basicSche = optBasicSche.get();
				//dataOutput.setReflect(true);
				dataOutput.setWorkTimeCode(basicSche.getWorkTimeCode());
				return dataOutput;
			}
			//ドメインモデル「個人労働条件」を取得する
			//ドメインモデル「個人労働条件」を取得する(lay dieu kien lao dong ca nhan(個人労働条件))
			Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeId, baseDate);
			if(!personalLablorCodition.isPresent()) {
				return dataOutput;
			}
			WorkingConditionItem workingConditionData = personalLablorCodition.get();
			if(workingConditionData.getWorkCategory().getWeekdayTime().getWorkTimeCode().isPresent()) {
				dataOutput.setReflect(true);	
			} else {
				return dataOutput; 
			}
			
			dataOutput.setWorkTimeCode(workingConditionData.getWorkCategory().getWeekdayTime().getWorkTimeCode().get().v());
		} else {
			dataOutput.setReflect(true);
			dataOutput.setWorkTimeCode(worktimeCode);
		}
		return dataOutput;
	}

	@Override
	public StartEndTimeIsReflect startEndTimeIsReflect(String employeeId, GeneralDate baseDate, WorkStyle workStype,
			Integer startTime, Integer endTime, String workTimeCode) {
		StartEndTimeIsReflect outData = new StartEndTimeIsReflect(false, 0, 0);
		String companyId = AppContexts.user().companyId();
		//INPUT．開始時刻1とINPUT．終了時刻1に値がある
		if(startTime != null && endTime != null) {
			outData.setChkReflect(true);
			outData.setStartTime(startTime);
			outData.setEndTime(endTime);
			return outData;
		}
		//ドメインモデル「就業時間帯の設定」を取得する
		Optional<PredetemineTimeSetting> optFindByCode = predetemineTimeRepo.findByWorkTimeCode(companyId, workTimeCode);
		if(!optFindByCode.isPresent()) {
			return outData;
		}
		PredetemineTimeSetting timeSetting = optFindByCode.get();
		PrescribedTimezoneSetting timeZoneSetting = timeSetting.getPrescribedTimezoneSetting();		
		List<TimezoneUse> lstTimezone = timeZoneSetting.getLstTimezone().stream()
				.filter(x -> x.getWorkNo() == 1)
				.collect(Collectors.toList());
		if(lstTimezone.isEmpty()) {
			return outData;
		}
		outData.setChkReflect(true);
		//INPUT．出勤休日区分をチェックする		
		if(workStype == WorkStyle.MORNING_WORK) {//INPUT．出勤休日区分が午前出勤系			
			//反映開始時刻=「所定時間帯設定」．時間帯．開始(勤務NO=1)
			outData.setStartTime(lstTimezone.get(0).getStart().v());
			//反映終了時刻=「所定時間帯設定」．午前終了時刻
			outData.setEndTime(timeZoneSetting.getMorningEndTime().v());
		} else if(workStype == WorkStyle.AFTERNOON_WORK){
			//反映開始時刻=「所定時間帯設定」．午後開始時刻(勤務NO=1)
			outData.setStartTime(timeZoneSetting.getAfternoonStartTime().v());
			//反映終了時刻=「所定時間帯設定」．時間帯．終了(勤務NO=1)
			outData.setEndTime(lstTimezone.get(0).getEnd().v());
		}
		return outData;
	}
	

}
