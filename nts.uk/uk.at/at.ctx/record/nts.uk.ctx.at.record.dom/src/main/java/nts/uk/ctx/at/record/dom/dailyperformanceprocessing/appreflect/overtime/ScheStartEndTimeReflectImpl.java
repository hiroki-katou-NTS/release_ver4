package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.service.AttendanceOfficeAtr;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ScheStartEndTimeReflectImpl implements ScheStartEndTimeReflect {
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeRepo;
	@Inject
	private BasicScheduleService basicSche;
	@Inject
	private WorkUpdateService scheWork;
	@Inject
	private WorkTypeIsClosedService workTypeService;
	@Inject
	private EditStateOfDailyPerformanceRepository editDailyPerforRespository;
	@Inject
	private WorkInformationRepository workInforRepository;
	@Override
	public void reflectScheStartEndTime(OvertimeParameter para,
			WorkTimeTypeOutput timeTypeData, IntegrationOfDaily dailyPerfor) {
		//反映する開始終了時刻を求める
		StartEndTimeRelectCheck startEndTimeData = new StartEndTimeRelectCheck(para.getEmployeeId(), para.getDateInfo(), para.getOvertimePara().getStartTime1(), 
				para.getOvertimePara().getEndTime1(), para.getOvertimePara().getStartTime2(), 
				para.getOvertimePara().getEndTime2(), 
				para.getOvertimePara().getWorkTimeCode(), para.getOvertimePara().getWorkTypeCode(), para.getOvertimePara().getOvertimeAtr());
		ScheStartEndTimeReflectOutput findStartEndTime = this.findStartEndTime(startEndTimeData, timeTypeData);
		
		//申請する開始終了時刻に値があるかチェックする
		if(para.getOvertimePara().getStartTime1() != null
				|| para.getOvertimePara().getEndTime1() != null
				|| para.getOvertimePara().getStartTime2() != null
				|| para.getOvertimePara().getEndTime2() != null) {
			//１回勤務反映区分(output)をチェックする
			if(findStartEndTime.isCountReflect1Atr()) {
				//予定開始時刻の反映
				//予定終了時刻の反映
				//INPUT．残業区分をチェックする
				boolean isEndTime = true;
				boolean isStartTime = true;
				if(para.getOvertimePara().getOvertimeAtr() == OverTimeRecordAtr.REGULAROVERTIME) {
					isStartTime = false;
				}
				if(para.getOvertimePara().getOvertimeAtr() == OverTimeRecordAtr.PREOVERTIME) {
					isEndTime = false;
				}
				
				TimeReflectPara timeData1 = new TimeReflectPara(para.getEmployeeId(), 
						para.getDateInfo(), 
						findStartEndTime.getStartTime1(), 
						findStartEndTime.getEndTime1(), 
						1, isStartTime, isEndTime);
				if(isStartTime || isEndTime) {
					scheWork.updateScheStartEndTime(timeData1, dailyPerfor);	
				}				
			}
			//２回勤務反映区分(output)をチェックする
			if(findStartEndTime.isCountReflect2Atr()) {
				//予定開始時刻２の反映
				//予定終了時刻２の反映
				boolean isEndTime = true;
				boolean isStartTime = true;
				if(para.getOvertimePara().getOvertimeAtr() == OverTimeRecordAtr.REGULAROVERTIME) {
					isStartTime = false;
				}
				if(para.getOvertimePara().getOvertimeAtr() == OverTimeRecordAtr.PREOVERTIME) {
					isEndTime = false;
				}
				TimeReflectPara timeData2 = new TimeReflectPara(para.getEmployeeId(),
						para.getDateInfo(),
						findStartEndTime.getStartTime2(), 
						findStartEndTime.getEndTime2(), 
						2,
						isStartTime, 
						isEndTime);
				if(isStartTime || isEndTime) {
					scheWork.updateScheStartEndTime(timeData2, dailyPerfor);	
				}				
			}
		} else {
			//１回勤務反映区分(output)をチェックする
			if(findStartEndTime.isCountReflect1Atr()) {
				//予定開始時刻を反映できるかチェックする
				boolean isCheckStart = this.checkStartEndTimeReflect(para.getEmployeeId(), para.getDateInfo(), 1, 
						timeTypeData.getWorkTypeCode(), para.getOvertimePara().getOvertimeAtr(), true);
				boolean isCheckEnd = this.checkStartEndTimeReflect(para.getEmployeeId(), para.getDateInfo(), 1, 
						timeTypeData.getWorkTypeCode(), para.getOvertimePara().getOvertimeAtr(), false);
				if(isCheckStart || isCheckEnd) {
					TimeReflectPara timeData1 = new TimeReflectPara(para.getEmployeeId(), para.getDateInfo(), 
							findStartEndTime.getStartTime1(), findStartEndTime.getEndTime1(), 
							1, isCheckStart, isCheckEnd);
					scheWork.updateScheStartEndTime(timeData1, dailyPerfor);	
				}
			}
			//２回勤務反映区分(output)をチェックする
			if(findStartEndTime.isCountReflect2Atr()) {
				boolean isCheckStart = this.checkStartEndTimeReflect(para.getEmployeeId(), para.getDateInfo(), 2,
						timeTypeData.getWorkTypeCode(), para.getOvertimePara().getOvertimeAtr(), true);
				boolean isCheckEnd = this.checkStartEndTimeReflect(para.getEmployeeId(), para.getDateInfo(), 2, 
						timeTypeData.getWorkTypeCode(), para.getOvertimePara().getOvertimeAtr(), false);
				
				if(isCheckStart || isCheckEnd) {
					TimeReflectPara timeData1 = new TimeReflectPara(para.getEmployeeId(),
							para.getDateInfo(),
							findStartEndTime.getStartTime2(),
							findStartEndTime.getEndTime2(),
							2, isCheckStart, isCheckEnd);
					scheWork.updateScheStartEndTime(timeData1, dailyPerfor);	
				}
				
			}
		}
	}

	@Override
	public ScheStartEndTimeReflectOutput findStartEndTime(StartEndTimeRelectCheck para, WorkTimeTypeOutput timeTypeData) {
		ScheStartEndTimeReflectOutput findDataOut = new ScheStartEndTimeReflectOutput(null, null, true, null, null, true);
		//ドメインモデル「就業時間帯の設定」を取得する
		String companyId = AppContexts.user().companyId();
		Optional<PredetemineTimeSetting> optWorkTimeData = predetemineTimeRepo.findByWorkTimeCode(companyId, timeTypeData.getWorktimeCode());
		if(!optWorkTimeData.isPresent()) {
			findDataOut.setCountReflect2Atr(false);
			return findDataOut;
		} 
		PredetemineTimeSetting workTimeData = optWorkTimeData.get();		
		List<TimezoneUse> lstTimeZone2 = workTimeData.getPrescribedTimezoneSetting().getLstTimezone()
				.stream()
				.filter(x -> x.getWorkNo() == 2).collect(Collectors.toList());
		List<TimezoneUse> lstTimeZone1 = workTimeData.getPrescribedTimezoneSetting().getLstTimezone()
				.stream()
				.filter(x -> x.getWorkNo() == 1).collect(Collectors.toList());
		TimezoneUse timeZone2 = null;
		if(lstTimeZone2.isEmpty()) {
			findDataOut.setCountReflect2Atr(false);
		} else {
			timeZone2 = lstTimeZone2.get(0);	
		}
		TimezoneUse timeZone1 = null;
		if(lstTimeZone1.isEmpty()) {
			findDataOut.setCountReflect1Atr(false);
		} else {
			timeZone1 = lstTimeZone1.get(0);	
		}
		
		if(timeZone2 != null && timeZone2.getUseAtr() == UseSetting.NOT_USE) {
			findDataOut.setCountReflect2Atr(false);
		} else {
			findDataOut.setCountReflect2Atr(true);	
		}
		//1日半日出勤・1日休日系の判定
		WorkStyle workStyle =  basicSche.checkWorkDay(timeTypeData.getWorkTypeCode());
		PrescribedTimezoneSetting prescribSeting = workTimeData.getPrescribedTimezoneSetting();
		//申請の開始、終了時刻に値があるかチェックする
		if(para.getStartTime1() != null
				|| para.getEndTime1() != null
				|| para.getStartTime2() != null
				|| para.getEndTime2() != null) {
			//取得した２回勤務使用区分をチェックする
			if(!findDataOut.isCountReflect2Atr()) {
				findDataOut.setStartTime1(para.getStartTime1());
				findDataOut.setEndTime1(para.getEndTime1());
				return findDataOut;
			} else {
				//昼休憩は勤務時間帯か、勤務時間帯2にあるかチェックする
				
				//所定時間帯設定．午後開始時刻＜（２枠目）時間帯(使用区分付き)．開始
				if(timeZone2 != null 
						&& prescribSeting.getAfternoonStartTime().v() < timeZone2.getStart().v()) {//true：昼休憩は勤務時間帯にある
					//開始時刻=申請の開始時刻、終了時刻=申請の開始時刻
					findDataOut.setStartTime1(para.getStartTime1());
					findDataOut.setEndTime1(para.getEndTime1());
					//OUTPUT．出勤休日区分をチェックする
					if(workStyle == WorkStyle.MORNING_WORK) {
						findDataOut.setCountReflect2Atr(false);
					} else {
						//開始時刻2=申請の開始時刻2、終了時刻2=申請の開始時刻2
						findDataOut.setStartTime2(para.getStartTime2());
						findDataOut.setEndTime2(para.getEndTime2());
					}
					return findDataOut;
				} else { //false：昼休憩は勤務時間帯２にある
					//開始時刻2=申請の開始時刻2、終了時刻2=申請の開始時刻2
					findDataOut.setStartTime2(para.getStartTime2());
					findDataOut.setEndTime2(para.getEndTime2());
					//OUTPUT．出勤休日区分をチェックする
					if(workStyle == WorkStyle.AFTERNOON_WORK) {
						findDataOut.setCountReflect1Atr(false);
					}else {
						//開始時刻=申請の開始時刻、終了時刻=申請の開始時刻
						findDataOut.setStartTime1(para.getStartTime1());
						findDataOut.setEndTime1(para.getEndTime1());
					}
					return findDataOut;
				}
			}
		} else {
			//取得した２回勤務使用区分をチェックする
			if(!findDataOut.isCountReflect2Atr()) {
				//OUTPUT．出勤休日区分をチェックする
				if(workStyle == WorkStyle.AFTERNOON_WORK) {
					//開始時刻=所定時間帯設定．午後開始時刻
					findDataOut.setStartTime1(prescribSeting.getAfternoonStartTime().v());
					//終了時刻=（１枠目）時間帯(使用区分付き)．終了
					findDataOut.setEndTime1(timeZone1.getEnd().v());
				} else if (workStyle == WorkStyle.MORNING_WORK) {
					//開始時刻=（１枠目）時間帯(使用区分付き)．開始
					findDataOut.setStartTime1(timeZone1.getStart().v());
					//終了時刻=所定時間帯設定．午前終了時刻
					findDataOut.setEndTime1(prescribSeting.getMorningEndTime().v());
				} else if (workStyle == WorkStyle.ONE_DAY_WORK) {
					//開始時刻=（１枠目）時間帯(使用区分付き)．開始
					findDataOut.setStartTime1(timeZone1.getStart().v());
					//終了時刻=（１枠目）時間帯(使用区分付き)．終了
					findDataOut.setEndTime1(timeZone1.getEnd().v());
				}
				return findDataOut;
			} else {
				//昼休憩は勤務時間帯か、勤務時間帯2にあるかチェックする
				if(timeZone2 != null 
						&& prescribSeting.getAfternoonStartTime().v() < timeZone2.getStart().v()) {
					//OUTPUT．出勤休日区分をチェックする
					if (workStyle == WorkStyle.AFTERNOON_WORK) {
						//開始時刻=所定時間帯設定．午後開始時刻
						findDataOut.setStartTime1(prescribSeting.getAfternoonStartTime().v());
						//終了時刻=（１枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime1(timeZone1.getEnd().v());
						//開始時刻2=（２枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime2(timeZone2.getStart().v());
						//終了時刻2=（２枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime2(timeZone2.getEnd().v());
					} else if (workStyle == WorkStyle.MORNING_WORK) {
						//開始時刻=（１枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime1(timeZone1.getStart().v());
						//終了時刻=所定時間帯設定．午前終了時刻
						findDataOut.setEndTime1(prescribSeting.getMorningEndTime().v());
						//２回勤務反映区分=false
						findDataOut.setCountReflect2Atr(false);
					} else if (workStyle == WorkStyle.ONE_DAY_WORK) {
						//開始時刻=（１枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime1(timeZone1.getStart().v());
						//終了時刻=（１枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime1(timeZone1.getEnd().v());
						//開始時刻2=（２枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime2(timeZone2.getStart().v());
						//終了時刻2=（２枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime2(timeZone2.getEnd().v());
					}
					return findDataOut;
				} else {
					//OUTPUT．出勤休日区分をチェックする
					if (workStyle == WorkStyle.AFTERNOON_WORK) {
						//開始時刻2=所定時間帯設定．午後開始時刻
						findDataOut.setStartTime2(prescribSeting.getAfternoonStartTime().v());
						//終了時刻2=（２枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime2(timeZone2.getEnd().v());
						//１回勤務反映区分=false
						findDataOut.setCountReflect2Atr(false);
					} else if (workStyle == WorkStyle.MORNING_WORK) {
						//開始時刻=（１枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime1(timeZone1.getStart().v());
						//終了時刻=（１枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime1(timeZone1.getEnd().v());
						//開始時刻2=（２枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime2(timeZone2.getStart().v());
						//終了時刻2=所定時間帯設定．午前終了時刻
						findDataOut.setEndTime2(prescribSeting.getMorningEndTime().v());						
					} else if (workStyle == WorkStyle.ONE_DAY_WORK) {
						//開始時刻=（１枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime1(timeZone1.getStart().v());
						//終了時刻=（１枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime1(timeZone1.getEnd().v());
						//開始時刻2=（２枠目）時間帯(使用区分付き)．開始
						findDataOut.setStartTime2(timeZone2.getStart().v());
						//終了時刻2=（２枠目）時間帯(使用区分付き)．終了
						findDataOut.setEndTime2(timeZone2.getEnd().v());
					}
					return findDataOut;
				}
			}
		}		
	}

	@Override
	public boolean checkStartEndTimeReflect(String employeeId, GeneralDate datadata, Integer frameNo,
			String workTypeCode, OverTimeRecordAtr overTimeAtr, boolean isPre) {
		if((overTimeAtr == OverTimeRecordAtr.REGULAROVERTIME && isPre)
				|| overTimeAtr == OverTimeRecordAtr.PREOVERTIME && !isPre) {
			return false;
		}
		//打刻自動セット区分を取得する
		if(workTypeService.checkStampAutoSet(workTypeCode, AttendanceOfficeAtr.ATTENDANCE)) {
			//編集状態を取得する
			//勤怠項目ID: 予定項目ID=予定開始時刻(枠番)の項目ID 3??
			Integer attendentId;
			if(isPre) {
				attendentId = 3;
			} else {
				attendentId = 4;
			}
			Optional<EditStateOfDailyPerformance> optDailyData = editDailyPerforRespository.findByKeyId(employeeId, datadata, attendentId);
			if(!optDailyData.isPresent()) {
				return false;
			}
			EditStateOfDailyPerformance dailyData = optDailyData.get();
			//予定出勤時刻を取得する
			Optional<WorkInfoOfDailyPerformance> optWorkInfor = workInforRepository.find(employeeId, datadata);
			if(!optWorkInfor.isPresent()) {
				return false;
			}
			List<ScheduleTimeSheet> lstScheduleTimeSheets = optWorkInfor.get().getScheduleTimeSheets()
					.stream()
					.filter(x -> x.getWorkNo().v() == frameNo).collect(Collectors.toList());
			
			//取得した予定出勤時刻に値がない  OR 
			//（取得した編集状態が手修正（本人）AND 手修正（他人）ではない）
			if(lstScheduleTimeSheets.isEmpty()
					|| (dailyData.getEditStateSetting() != EditStateSetting.HAND_CORRECTION_MYSELF
					&& dailyData.getEditStateSetting() != EditStateSetting.HAND_CORRECTION_OTHER)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean checkRecordStartEndTimereflect(String employeeId, GeneralDate datadata, Integer frameNo,
			String workTypeCode, OverTimeRecordAtr overTimeAtr, boolean isPre) {
		if((overTimeAtr == OverTimeRecordAtr.REGULAROVERTIME && isPre)
				|| overTimeAtr == OverTimeRecordAtr.PREOVERTIME && !isPre) {
			return false;
		}
		//打刻自動セット区分を取得する
		/*if(workTypeService.checkStampAutoSet(workTypeCode, AttendanceOfficeAtr.ATTENDANCE)) {
			return absService.checkReflectRecordStartEndTime(employeeId, datadata, frameNo, isPre);
		}*/
		return true;
	}

}
