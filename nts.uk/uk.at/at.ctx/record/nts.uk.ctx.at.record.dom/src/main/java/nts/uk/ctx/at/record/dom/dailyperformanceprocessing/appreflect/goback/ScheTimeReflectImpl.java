package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.shared.dom.worktime.common.LateEarlyAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ScheTimeReflectImpl implements ScheTimeReflect{

	@Inject
	private PredetemineTimeSettingRepository predetemineTimeRepo;
	@Inject
	private WorkUpdateService scheUpdateService;
	@Inject
	private WorkTimeIsFluidWork workTimeisFluidWork;
	@Inject
	private WorkTimeSettingRepository workTimeSetting;
	/*フレックス勤務設定*/
	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepository;
	/*固定勤務設定*/
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepository;
	/*流動勤務設定*/
	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepository;
	/*時差勤務設定*/
	@Inject
	private DiffTimeWorkSettingRepository diffTimeWorkSettingRepository;
	@Override
	public void reflectScheTime(GobackReflectParameter para, boolean timeTypeScheReflect,
			IntegrationOfDaily dailyInfor, boolean isPre) {
		//予定時刻反映できるかチェックする
		if(!this.checkScheReflect(para.getGobackData().getWorkTimeCode(), para.isScheReflectAtr(), para.getScheAndRecordSameChangeFlg(), isPre)) {
			return;
		}
		//(開始時刻)反映する時刻を求める
		TimeOfDayReflectOutput startTimeReflect = this.getTimeOfDayReflect(timeTypeScheReflect, 
				para.getGobackData().getStartTime1(), 
				ApplyTimeAtr.START, 
				para.getGobackData().getWorkTimeCode(), 
				para.getScheTimeReflectAtr());
		//(終了時刻)反映する時刻を求める
		TimeOfDayReflectOutput endTimeReflect = this.getTimeOfDayReflect(timeTypeScheReflect, 
				para.getGobackData().getEndTime1(), 
				ApplyTimeAtr.END, 
				para.getGobackData().getWorkTimeCode(), 
				para.getScheTimeReflectAtr());
		TimeReflectPara timeData1 = new TimeReflectPara(para.getEmployeeId(), para.getDateData(), startTimeReflect.getTimeOfDay(), endTimeReflect.getTimeOfDay(), 1, startTimeReflect.isReflectFlg(), endTimeReflect.isReflectFlg());
		scheUpdateService.updateScheStartEndTime(timeData1, dailyInfor);		
		/*//(開始時刻2)反映する時刻を求める
		TimeOfDayReflectOutput startTime2Reflect = this.getTimeOfDayReflect(timeTypeScheReflect, 
				para.getGobackData().getStartTime2(), 
				ApplyTimeAtr.START2, 
				para.getGobackData().getWorkTimeCode(), 
				para.getScheTimeReflectAtr());
		//(終了時刻2)反映する時刻を求める
		TimeOfDayReflectOutput endTime2Reflect = this.getTimeOfDayReflect(timeTypeScheReflect, 
				para.getGobackData().getEndTime2(), 
				ApplyTimeAtr.END2, 
				para.getGobackData().getWorkTimeCode(), 
				para.getScheTimeReflectAtr());
		TimeReflectPara timeData2 = new TimeReflectPara(para.getEmployeeId(),
				para.getDateData(), startTime2Reflect.getTimeOfDay(), 
				endTime2Reflect.getTimeOfDay(), 
				2, 
				startTime2Reflect.isReflectFlg(), 
				endTime2Reflect.isReflectFlg());
		scheUpdateService.updateScheStartEndTime(timeData2);*/		
	}
	@Override
	public TimeOfDayReflectOutput getTimeOfDayReflect(boolean timeTypeScheReflect, 
			Integer timeData,
			ApplyTimeAtr applyTimeAtr,
			String workTimeCode,
			ScheTimeReflectAtr scheTimeReflectAtr) {
		//反映するフラグ=false、反映する時刻=0（初期化）
		TimeOfDayReflectOutput reflectOutput = new TimeOfDayReflectOutput(false, 0);
		//INPUT．予定時刻反映区分をチェックする
		if(scheTimeReflectAtr == ScheTimeReflectAtr.APPTIME) {
			//INPUT．申請する時刻をチェックする
			if(timeData != null && timeData > 0) {
				reflectOutput.setReflectFlg(true);
				reflectOutput.setTimeOfDay(timeData);
				return reflectOutput;	
			} else {
				return reflectOutput;
			}
			
		} else {
			//INPUT．勤種・就時の反映できるフラグをチェックする
			if(timeTypeScheReflect) {
				//ドメインモデル「就業時間帯の設定」を取得する
				String companyId = AppContexts.user().companyId();
				Optional<PredetemineTimeSetting> optFindByCode = predetemineTimeRepo.findByWorkTimeCode(companyId, workTimeCode);
				if(!optFindByCode.isPresent()) {
					return reflectOutput;
				}
				PredetemineTimeSetting workTimeData = optFindByCode.get();
				TimezoneUse timeZone;
				if(applyTimeAtr == ApplyTimeAtr.START
						|| applyTimeAtr == ApplyTimeAtr.END) {
					List<TimezoneUse> lstTimeZone = workTimeData.getPrescribedTimezoneSetting().getLstTimezone()
					.stream()
					.filter(x -> x.getWorkNo() == 1).collect(Collectors.toList());
					if(lstTimeZone.isEmpty()) {
						return reflectOutput;
					}
					timeZone = lstTimeZone.get(0);
				} else {
					List<TimezoneUse> lstTimeZone = workTimeData.getPrescribedTimezoneSetting().getLstTimezone()
							.stream()
							.filter(x -> x.getWorkNo() == 2).collect(Collectors.toList());
							if(lstTimeZone.isEmpty()) {
								return reflectOutput;
							}
							timeZone = lstTimeZone.get(0);
				}
				
				if(timeZone.getUseAtr() == UseSetting.USE) {
					if(applyTimeAtr == ApplyTimeAtr.START) {
						reflectOutput.setTimeOfDay(timeZone.getStart().v());						
					} else {
						reflectOutput.setTimeOfDay(timeZone.getEnd().v());
					}
					reflectOutput.setReflectFlg(true);
					return reflectOutput;
				}
			}
		}
		
		return reflectOutput;
	}
	@Override
	public void reflectTime(GobackReflectParameter para, boolean workTypeTimeReflect, IntegrationOfDaily dailyInfor) {
		String tmpWorkTimeCode;
		//INPUT．勤種・就時の反映できるフラグをチェックする
		if(workTypeTimeReflect) {
			tmpWorkTimeCode = para.getGobackData().getWorkTimeCode();
		} else {
			//ドメインモデル「日別実績の勤務情報」を取得する
			
			tmpWorkTimeCode = dailyInfor.getWorkInformation().getRecordInfo().getWorkTimeCode() == null ? null 
					: dailyInfor.getWorkInformation().getRecordInfo().getWorkTimeCode().v();
		}
		//出勤時刻を反映できるかチェックする
		boolean isStart1 = this.checkAttendenceReflect(para, 1, true, dailyInfor.getAttendanceLeave());
		//ジャスト遅刻により時刻を編集する
		//開始時刻を反映する 
		Integer startTime1 = isStart1 ? this.justTimeLateLeave(tmpWorkTimeCode, para.getGobackData().getStartTime1(), 1, true) : null;
		//退勤時刻を反映できるか
		boolean isEnd1 = this.checkAttendenceReflect(para, 1, false, dailyInfor.getAttendanceLeave());
		//ジャスト早退により時刻を編集する
		//終了時刻の反映
		Integer endTime1 = isEnd1 ? this.justTimeLateLeave(tmpWorkTimeCode, para.getGobackData().getEndTime1(), 1, false) : null;		
		TimeReflectPara timePara1 = new TimeReflectPara(para.getEmployeeId(), para.getDateData(), startTime1, endTime1, 1, isStart1, isEnd1);
		scheUpdateService.updateRecordStartEndTimeReflect(timePara1, dailyInfor);		
		/*//出勤時刻２を反映できるか
		boolean startTime2 = this.checkAttendenceReflect(para, 2, true);
		//ジャスト遅刻により時刻を編集する
		Integer timeLate2 = startTime2 ? this.justTimeLateLeave(tmpWorkTimeCode, para.getGobackData().getStartTime2(), 2, true) : null;
		//退勤時刻２を反映できるか
		boolean endTime2 = this.checkAttendenceReflect(para, 2, false);
		//ジャスト早退により時刻を編集する
		Integer timeLeave2 = endTime2 ? this.justTimeLateLeave(tmpWorkTimeCode, para.getGobackData().getEndTime2(), 2, false) : null;
		TimeReflectPara timePara2 = new TimeReflectPara(para.getEmployeeId(), para.getDateData(), timeLate2, timeLeave2, 2, startTime2, endTime2);
		scheUpdateService.updateRecordStartEndTimeReflect(timePara2);*/
	}
	@Override
	public boolean checkAttendenceReflect(GobackReflectParameter para, Integer frameNo, boolean isPre,
			Optional<TimeLeavingOfDailyPerformance> optTimeLeave) {
		//INPUT．打刻優先区分をチェックする
		if(para.getPriorStampAtr() == PriorStampAtr.APP_TIME) {
			//INPUT．申請する時刻に値があるかチェックする
			if(isPre && frameNo == 1 && para.getGobackData().getStartTime1() != null && para.getGobackData().getStartTime1() > 0
					|| isPre && frameNo == 2 && para.getGobackData().getStartTime2() != null && para.getGobackData().getStartTime2() > 0
					|| !isPre && frameNo == 1 && para.getGobackData().getEndTime1() != null && para.getGobackData().getEndTime1() > 0
					|| !isPre && frameNo == 2 && para.getGobackData().getEndTime2() != null && para.getGobackData().getEndTime2() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			if(!optTimeLeave.isPresent()) {
				return true;
			}
			TimeLeavingOfDailyPerformance timeLeave = optTimeLeave.get();
			List<TimeLeavingWork> lstTimeLeaveWork = timeLeave.getTimeLeavingWorks().stream().filter(x-> x.getWorkNo().v() == frameNo)
					.collect(Collectors.toList());
			if(lstTimeLeaveWork.isEmpty()) {
				return true;
			}			
			//打刻元情報をチェックする
			TimeLeavingWork timeLeaveWork = lstTimeLeaveWork.get(0);
			Optional<TimeActualStamp> optAttendanceStamp;
			if(isPre) {
				//出勤打刻があるか
				optAttendanceStamp = timeLeaveWork.getAttendanceStamp();				
			} else {
				//退勤打刻があるか
				optAttendanceStamp = timeLeaveWork.getLeaveStamp();
			}

			if(!optAttendanceStamp.isPresent()) {
				return true;
			}
			TimeActualStamp attendanceStamp = optAttendanceStamp.get();
			Optional<WorkStamp> optActualStamp = attendanceStamp.getStamp();
			if(!optActualStamp.isPresent()) {
				return true;		
			} 
			/*WorkStamp actualStamp = optActualStamp.get();
			//ドメインモデル「勤怠打刻」．打刻元情報が「打刻自動セット(個人情報)、直行直帰」
			if(actualStamp.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
					|| actualStamp.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT) {
				return true;
			}*/
		}
		
		//return false;
		return true;
		
	}
	@Override
	public Integer justTimeLateLeave(String workTimeCode, Integer timeData, Integer frameNo, boolean isPre) {
		String companyId = AppContexts.user().companyId();
		Optional<WorkTimezoneCommonSet> optWorkTimeSetting = this.getWorkTimezoneCommonSet(companyId, workTimeCode);
		if(!optWorkTimeSetting.isPresent()) {
			return timeData;
		}		
		//時間丁度の打刻は遅刻・早退とするをチェックする		
		WorkTimezoneCommonSet worktimeSet = optWorkTimeSetting.get();
		WorkTimezoneLateEarlySet lateEarlySet = worktimeSet.getLateEarlySet();
		List<OtherEmTimezoneLateEarlySet> lstOtherClassSets = lateEarlySet.getOtherClassSets();
		if(lstOtherClassSets.isEmpty()) {
			return timeData;
		}
		OtherEmTimezoneLateEarlySet emTimezon = null;
		if(isPre) {
			List<OtherEmTimezoneLateEarlySet> temp = lstOtherClassSets.stream()
					.filter(x -> x.getLateEarlyAtr() == LateEarlyAtr.LATE)
					.collect(Collectors.toList());
			if(!temp.isEmpty()) {
				emTimezon = temp.get(0);
			}
		} else {
			List<OtherEmTimezoneLateEarlySet> temp = lstOtherClassSets.stream()
					.filter(x -> x.getLateEarlyAtr() == LateEarlyAtr.EARLY)
					.collect(Collectors.toList());
			if(!temp.isEmpty()) {
				emTimezon = temp.get(0);
			}
		}
		if(emTimezon != null && emTimezon.isStampExactlyTimeIsLateEarly()) {
			if(isPre) {
				return timeData - 1;				
			} else {
				return timeData + 1;
			}

		}
		return timeData;
	}
	@Override
	public boolean checkScheReflect(String worktimeCode, boolean scheReflectAtr, ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg, boolean isPre) {
		//INPUT．予定反映区分をチェックする
		//INPUT．予定と実績を同じに変更する区分をチェックする
		if((scheReflectAtr && isPre)
				|| scheAndRecordSameChangeFlg == ScheAndRecordSameChangeFlg.ALWAYS_CHANGE_AUTO) {
			return true;
		}
		//INPUT．予定と実績を同じに変更する区分が「流動勤務のみ自動変更する」
		if(scheAndRecordSameChangeFlg == ScheAndRecordSameChangeFlg.AUTO_CHANGE_ONLY_WORK) {
			//流動勤務かどうかの判断処理
			return workTimeisFluidWork.checkWorkTimeIsFluidWork(worktimeCode);
		}
		
		return false;
	}
	/**
	 * 就業時間帯の共通設定を取得する
	 * @return
	 */
	private Optional<WorkTimezoneCommonSet> getWorkTimezoneCommonSet(String companyId, String workTimeCd) {
		Optional<WorkTimeSetting> optWorktimeSetting = workTimeSetting.findByCode(companyId, workTimeCd);
		if(!optWorktimeSetting.isPresent()) {
			return Optional.empty();
		}
		/* 勤務種類の取得 */
		WorkTimeSetting workTime = optWorktimeSetting.get();
		if (workTime.getWorkTimeDivision().getWorkTimeDailyAtr().isFlex()) {
			val flexWorkSetOpt = flexWorkSettingRepository.find(companyId, workTimeCd);
			if(flexWorkSetOpt.isPresent()) {
				return Optional.of(flexWorkSetOpt.get().getCommonSetting());
			}
		}else{
		
			switch (workTime.getWorkTimeDivision().getWorkTimeMethodSet()) {
			case FIXED_WORK:
				/* 固定 */
				val fixedWorkSetting = fixedWorkSettingRepository.findByKey(companyId, workTimeCd);
				if(fixedWorkSetting.isPresent()) {
					return Optional.of(fixedWorkSetting.get().getCommonSetting());
				}
			break;
			case FLOW_WORK:
				/* 流動勤務 */
				val flowWorkSetOpt = flowWorkSettingRepository.find(companyId, workTimeCd);
				if(flowWorkSetOpt.isPresent()) {
					return Optional.of(flowWorkSetOpt.get().getCommonSetting());
				}
				break;
			case DIFFTIME_WORK:
				/* 時差勤務 */
				val diffWorkSetOpt = diffTimeWorkSettingRepository.find(companyId, workTimeCd);
				if(diffWorkSetOpt.isPresent()) {
					return Optional.of(diffWorkSetOpt.get().getCommonSet());
				}
				break;
			default:
				throw new RuntimeException(
						"unknown workTimeMethodSet" + workTime.getWorkTimeDivision().getWorkTimeMethodSet());
			}
		}
		return Optional.empty();
	}
}
