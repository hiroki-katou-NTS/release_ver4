package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ScheWorkUpdateService;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;

@Stateless
public class AfterWorkTimeTypeReflectImpl implements AfterWorkTimeTypeReflect{
	@Inject
	private WorkTimeTypeScheReflect tyScheReflect;
	@Inject
	private WorkTimeIsFluidWork isFluidWork;
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
		//INPUT．予定と実績を同じに変更する区分をチェックする
		if(para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.NOTAUTO) {
			return false;
		} else if (para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.ALWAY
				|| (para.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.FLUIDWORK
						&& isFluidWork.checkWorkTimeIsFluidWork(para.getGobackData().getWorkTimeCode()))){
			return tyScheReflect.checkScheAndRecordSamseChange(para.getEmployeeId(), para.getDateData(), para.isOutResReflectAtr());
		} 
		return false;
	}

}
