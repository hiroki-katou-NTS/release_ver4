package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;


@Stateless
public class HolidayWorkReflectProcessImpl implements HolidayWorkReflectProcess{
	@Inject
	private WorkTimeIsFluidWork worktimeisFluid;
	@Inject
	private WorkUpdateService workUpdate;
	@Override
	public IntegrationOfDaily updateScheWorkTimeType(String employeeId, GeneralDate baseDate, String workTypeCode,
			String workTimeCode, boolean scheReflectFlg, boolean isPre,
			ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg,
			IntegrationOfDaily dailyData) {
		//ＩNPUT．勤務種類コードとＩNPUT．就業時間帯コードをチェックする
		if(workTimeCode.isEmpty()
				|| workTypeCode.isEmpty()) {
			return dailyData;
		}
		//予定勤種・就時を反映できるかチェックする
		if(!this.checkScheWorkTimeReflect(employeeId, baseDate, workTimeCode, scheReflectFlg, isPre, scheAndRecordSameChangeFlg)) {
			return dailyData;
		}
		//予定勤種・就時の反映
		ReflectParameter reflectInfo = new ReflectParameter(employeeId, 
				baseDate, 
				workTimeCode, 
				workTypeCode); 
		return workUpdate.updateWorkTimeTypeHoliwork(reflectInfo, true, dailyData);
	}

	@Override
	public boolean checkScheWorkTimeReflect(String employeeId, GeneralDate baseDate, String workTimeCode,
			boolean scheReflectFlg, boolean isPre, ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg) {
		//INPUT．予定反映区分をチェックする
		if((scheReflectFlg && isPre)
				|| scheAndRecordSameChangeFlg == ScheAndRecordSameChangeFlg.ALWAY) {
			return true;
		}
		//INPUT．予定と実績を同じに変更する区分をチェックする
		//INPUT．就業時間帯コードに値があるかチェックする
		if(scheAndRecordSameChangeFlg == ScheAndRecordSameChangeFlg.NOTAUTO
				|| workTimeCode.isEmpty()) {
			return false;
		}
		//流動勤務かどうかの判断処理
		return worktimeisFluid.checkWorkTimeIsFluidWork(workTimeCode);
		
	}

	@Override
	public IntegrationOfDaily reflectWorkTimeFrame(String employeeId, 
			GeneralDate baseDate, 
			Map<Integer, Integer> mapWorkTimeFrame, 
			IntegrationOfDaily dailyData) {
		Map<Integer, Integer> tmp = new HashMap<>();
		for(Map.Entry<Integer,Integer> entry : mapWorkTimeFrame.entrySet()){
			//INPUT．休出時間のループ中の番をチェックする
			//INPUT．残業時間のループ中の番を、残業時間(反映用)に追加する
			if(entry.getValue() >= 0) {
				tmp.put(entry.getKey(), entry.getValue());
			}
		}
		//事前休出時間の反映
		return workUpdate.updateWorkTimeFrame(employeeId, baseDate, tmp, true, dailyData);
		
	}

}
