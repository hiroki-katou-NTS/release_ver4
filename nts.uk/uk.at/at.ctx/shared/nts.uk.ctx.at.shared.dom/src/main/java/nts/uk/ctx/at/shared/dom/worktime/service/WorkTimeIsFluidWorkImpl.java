package nts.uk.ctx.at.shared.dom.worktime.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class WorkTimeIsFluidWorkImpl implements WorkTimeIsFluidWork{
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;
	@Inject
	private WorkTypeRepository workTypeRespo;
	@Override
	public boolean checkWorkTimeIsFluidWork(String workTimeCode) {
		String companyId = AppContexts.user().companyId();
		WorkTimeForm workTimeForm = WorkTimeForm.FIXED;
		//就業時間帯の設定
		Optional<WorkTimeSetting> findByCode = workTimeSettingRepository.findByCode(companyId, workTimeCode);
		if(!findByCode.isPresent()) {
			workTimeForm = WorkTimeForm.FIXED;
			return false;
		}
		WorkTimeSetting workTimeData = findByCode.get();
		if(workTimeData.getWorkTimeDivision().getWorkTimeDailyAtr() == WorkTimeDailyAtr.FLEX_WORK) {
			workTimeForm = WorkTimeForm.FLEX;
		} else if (workTimeData.getWorkTimeDivision().getWorkTimeDailyAtr() == WorkTimeDailyAtr.REGULAR_WORK) {
			if(workTimeData.getWorkTimeDivision().getWorkTimeMethodSet() == WorkTimeMethodSet.FIXED_WORK) {
				workTimeForm = WorkTimeForm.FIXED;
			} else if (workTimeData.getWorkTimeDivision().getWorkTimeMethodSet() == WorkTimeMethodSet.DIFFTIME_WORK) {
				workTimeForm = WorkTimeForm.TIMEDIFFERENCE;
			} else {
				workTimeForm = WorkTimeForm.FLOW;
			}
		}
		if(workTimeForm == WorkTimeForm.FLOW) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public Integer getTimeByWorkTimeTypeCode(String workTimeCode, String workTypeCode) {
		String companyId = AppContexts.user().companyId();
		Optional<PredetemineTimeSetting> pred = this.predetemineTimeSettingRepository.findByWorkTimeCode(companyId, workTimeCode);
		Optional<WorkType> workTypeInfor = workTypeRespo.findByDeprecated(companyId, workTypeCode);
		if(pred.isPresent() 
				&& workTypeInfor.isPresent()) {
			PredetemineTimeSetting timeSetting = pred.get();
			WorkType workTypeData = workTypeInfor.get();
			if(workTypeData.getDailyWork().getWorkTypeUnit() == WorkTypeUnit.OneDay) {
				return timeSetting.getPredTime().getPredTime().getOneDay().v();
			} else if (workTypeData.getDailyWork().decisionNeedPredTime() == AttendanceHolidayAttr.MORNING) {
				return timeSetting.getPredTime().getPredTime().getMorning().v();
			} else if (workTypeData.getDailyWork().decisionNeedPredTime() == AttendanceHolidayAttr.AFTERNOON) {
				return timeSetting.getPredTime().getPredTime().getAfternoon().v();
			} else {
				return 0;
			}
		}
		return 0;
	}

}
