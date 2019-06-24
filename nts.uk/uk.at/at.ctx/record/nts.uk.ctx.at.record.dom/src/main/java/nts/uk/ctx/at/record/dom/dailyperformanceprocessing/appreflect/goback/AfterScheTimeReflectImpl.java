package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;

@Stateless
public class AfterScheTimeReflectImpl implements AfterScheTimeReflect{
	@Inject
	private WorkTimeIsFluidWork isFluidWork;
	@Inject
	private ScheTimeReflect scheTimeReflect;
	@Inject
	private WorkUpdateService scheUpdateService;
	@Override
	public void reflectScheTime(GobackReflectParameter para, boolean timeTypeScheReflect,
			IntegrationOfDaily dailyInfor) {
		//予定時刻反映できるかチェックする
		if(!this.checkScheTimeCanReflect(para.getGobackData().getWorkTimeCode(), para.getScheAndRecordSameChangeFlg())) {
			return;
		}
		//(開始時刻)反映する時刻を求める
		TimeOfDayReflectOutput startTime = scheTimeReflect.getTimeOfDayReflect(timeTypeScheReflect, para.getGobackData().getStartTime1(), ApplyTimeAtr.START, para.getGobackData().getWorkTimeCode(), para.getScheTimeReflectAtr());
		TimeOfDayReflectOutput endTime = scheTimeReflect.getTimeOfDayReflect(timeTypeScheReflect, para.getGobackData().getEndTime1(), ApplyTimeAtr.END, para.getGobackData().getWorkTimeCode(), para.getScheTimeReflectAtr());
		TimeReflectPara timeData1 = new TimeReflectPara(para.getEmployeeId(), 
				para.getDateData(), 
				startTime.getTimeOfDay(), 
				endTime.getTimeOfDay(), 
				1,
				startTime.isReflectFlg(), 
				endTime.isReflectFlg());
		scheUpdateService.updateScheStartEndTime(timeData1, dailyInfor);
		//(開始時刻2)反映する時刻を求める,
		/*TimeOfDayReflectOutput startTime2 = scheTimeReflect.getTimeOfDayReflect(timeTypeScheReflect, 
				para.getGobackData().getStartTime2(), ApplyTimeAtr.START2, para.getGobackData().getWorkTimeCode(), para.getScheTimeReflectAtr());
		// (終了時刻2)反映する時刻を求める
		TimeOfDayReflectOutput endTime2 = scheTimeReflect.getTimeOfDayReflect(timeTypeScheReflect, para.getGobackData().getEndTime2(), ApplyTimeAtr.END2, para.getGobackData().getWorkTimeCode(), para.getScheTimeReflectAtr());
		TimeReflectPara timeData2 = new TimeReflectPara(para.getEmployeeId(), 
				para.getDateData(), 
				startTime2.getTimeOfDay(), 
				endTime2.getTimeOfDay(), 
				2, 
				startTime2.isReflectFlg(), 
				endTime2.isReflectFlg());
		scheUpdateService.updateScheStartEndTime(timeData2);		*/
	}

	@Override
	public boolean checkScheTimeCanReflect(String workTimeCode, ScheAndRecordSameChangeFlg scheAndRecordSameChange) {
		//INPUT．予定と実績を同じに変更する区分をチェックする
		if(scheAndRecordSameChange == ScheAndRecordSameChangeFlg.DO_NOT_CHANGE_AUTO) {
			return false;
		} else if (scheAndRecordSameChange == ScheAndRecordSameChangeFlg.ALWAYS_CHANGE_AUTO
				|| (scheAndRecordSameChange == ScheAndRecordSameChangeFlg.AUTO_CHANGE_ONLY_WORK
						&& isFluidWork.checkWorkTimeIsFluidWork(workTimeCode))){
			return true;
		} 
		return false;
	}

}
