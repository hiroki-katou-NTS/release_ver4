package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.ApplyTimeAtr;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.UpdateScheCommonAppRelect;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class TimeOfDayReflectGoBackScheImpl implements TimeOfDayReflectGoBackSche {
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeRepo;
	@Inject
	private UpdateScheCommonAppRelect updateSche;
	@Override
	public void stampReflectGobackSche(GobackReflectParam reflectPara, BasicSchedule scheData, List<WorkScheduleState> lstState) {
		//(開始時刻)反映する時刻を求める
		reflectPara.setApplyTimeAtr(ApplyTimeAtr.START);
		TimeOfDayReflectFindDto startTimeReflectFind = this.timeReflectFind(reflectPara);
		//(終了時刻)反映する時刻を求める
		reflectPara.setApplyTimeAtr(ApplyTimeAtr.END);
		TimeOfDayReflectFindDto endTimeReflectFind = this.timeReflectFind(reflectPara);
		TimeReflectScheDto timeData = new TimeReflectScheDto(reflectPara.getEmployeeId(),
				reflectPara.getDatePara(),
				startTimeReflectFind.getTimeOfDay(),
				endTimeReflectFind.getTimeOfDay(), 
				1,
				startTimeReflectFind.isReflectFlg(),
				endTimeReflectFind.isReflectFlg());
		updateSche.updateStartTimeRflect(timeData, scheData, lstState);		
		//TODO (開始時刻2, 終了時刻2)反映する時刻を求める
	}

	@Override
	public TimeOfDayReflectFindDto timeReflectFind(GobackReflectParam reflectPara) {
		String companyId = AppContexts.user().companyId();
		TimeOfDayReflectFindDto timeFind = new TimeOfDayReflectFindDto(false, 0);
		//INPUT．申請する時刻をチェックする
		if(reflectPara.getAppInfor().getWorkTimeStart1() != null && reflectPara.getAppInfor().getWorkTimeStart1() >= 0  &&  reflectPara.getApplyTimeAtr() == ApplyTimeAtr.START
				|| reflectPara.getAppInfor().getWorkTimeEnd1() != null && reflectPara.getAppInfor().getWorkTimeEnd1() >= 0 && reflectPara.getApplyTimeAtr() == ApplyTimeAtr.END
				|| reflectPara.getAppInfor().getWorkTimeStart2() != null && reflectPara.getAppInfor().getWorkTimeStart2() >= 0 && reflectPara.getApplyTimeAtr() == ApplyTimeAtr.START2
				|| reflectPara.getAppInfor().getWorkTimeEnd2() != null && reflectPara.getAppInfor().getWorkTimeEnd2() >= 0 && reflectPara.getApplyTimeAtr() == ApplyTimeAtr.END2) {
			timeFind.setReflectFlg(true);
			if(reflectPara.getApplyTimeAtr() == ApplyTimeAtr.START) {
				timeFind.setTimeOfDay(reflectPara.getAppInfor().getWorkTimeStart1());	
			} else if(reflectPara.getApplyTimeAtr() == ApplyTimeAtr.END) {
				timeFind.setTimeOfDay(reflectPara.getAppInfor().getWorkTimeEnd1());
			} else if (reflectPara.getApplyTimeAtr() == ApplyTimeAtr.START2) {
				timeFind.setTimeOfDay(reflectPara.getAppInfor().getWorkTimeStart2());
			} else {
				timeFind.setTimeOfDay(reflectPara.getAppInfor().getWorkTimeEnd2());
			}
			
			return timeFind;
		} else {
			//INPUT．勤種・就時の反映できるフラグをチェックする
			if(!reflectPara.isOutsetBreakReflectAtr()) {
				return timeFind;
			} else {
				//ドメインモデル「就業時間帯の設定」を取得する
				Optional<PredetemineTimeSetting> optFindByCode = predetemineTimeRepo.findByWorkTimeCode(companyId, reflectPara.getAppInfor().getWorkTime());
				if(!optFindByCode.isPresent()) {
					return timeFind;
				}
				PredetemineTimeSetting workTimeData = optFindByCode.get();
				TimezoneUse timeZone = workTimeData.getPrescribedTimezoneSetting().getLstTimezone()
						.stream()
						.filter(x -> x.getWorkNo() == 1).collect(Collectors.toList()).get(0);
				//ドメインモデル「時間帯(使用区分付き)」．使用区分をチェックする
				//「所定時間設定」．使用区分をチェックする
				//TODO lam sau
				timeFind.setReflectFlg(true);
				if(reflectPara.getApplyTimeAtr() == ApplyTimeAtr.START) {
					timeFind.setTimeOfDay(timeZone.getStart().v());
				} else if (reflectPara.getApplyTimeAtr() == ApplyTimeAtr.END) {
					timeFind.setTimeOfDay(timeZone.getEnd().v());
				} 
				return timeFind;
			}
		}
	}

}
