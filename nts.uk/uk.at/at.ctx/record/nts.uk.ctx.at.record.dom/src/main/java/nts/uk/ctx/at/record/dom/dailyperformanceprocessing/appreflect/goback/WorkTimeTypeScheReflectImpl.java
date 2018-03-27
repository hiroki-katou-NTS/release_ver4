package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ScheWorkUpdateService;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;

@Stateless
public class WorkTimeTypeScheReflectImpl implements WorkTimeTypeScheReflect {
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private WorkTypeIsClosedService workTypeService;
	@Inject
	private WorkTimeIsFluidWork workTimeService;
	@Inject
	private ScheWorkUpdateService workUpdate;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public boolean workTimeAndTypeScheReflect(GobackReflectParameter para) {
		if(!this.checkReflectWorkTimeType(para)) {
			return false;
		}
		//予定勤種・就時の反映
		ReflectParameter reflectInfo = new ReflectParameter(para.getEmployeeId(), para.getDateData(), 
				para.getGobackData().getWorkTimeCode(), 
				para.getGobackData().getWorkTypeCode()); 
		workUpdate.updateWorkTimeType(reflectInfo, true);
		return true;
	}

	@Override
	public boolean checkReflectWorkTimeType(GobackReflectParameter para) {
		//INPUT．勤務を変更するをチェックする
		if(para.getGobackData().getChangeAppGobackAtr() == ChangeAppGobackAtr.NOTCHANGE) {
			return false;
		}
		//INPUT．予定反映区分をチェックする
		if(para.isScheReflectAtr()) {
			return this.checkScheAndRecordSamseChange(para.getEmployeeId(), para.getDateData(), para.isOutResReflectAtr());			
		} else {
			//INPUT．予定と実績を同じに変更する区分をチェックする
			if(para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.ALWAY) {
				return this.checkScheAndRecordSamseChange(para.getEmployeeId(), para.getDateData(), para.isOutResReflectAtr());
			} else if(para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.FLUIDWORK
					&& workTimeService.checkWorkTimeIsFluidWork(para.getGobackData().getWorkTimeCode())){
				//流動勤務かどうかの判断処理
				return this.checkScheAndRecordSamseChange(para.getEmployeeId(), para.getDateData(), para.isOutResReflectAtr());
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean checkScheAndRecordSamseChange(String employeeId, GeneralDate dateData, boolean isOutResReflectAtr) {
		//INPUT．振出・休出時反映する区分をチェックする
		if(isOutResReflectAtr) {
			return true;
		}
		//ドメインモデル「日別実績の勤務情報」を取得する
		Optional<WorkInfoOfDailyPerformance> optDailyData = workRepository.find(employeeId, dateData);
		if(!optDailyData.isPresent()) {
			return false;
		} 
		WorkInfoOfDailyPerformance dailyData = optDailyData.get();
		//勤務種類が休出振出かの判断
		if(workTypeService.checkWorkTypeIsClosed(dailyData.getScheduleWorkInformation().getWorkTypeCode().v())) {
			return false;
		} else {
			return true;
		}
	}
}
