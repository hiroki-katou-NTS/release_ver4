package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonCalculateOfAppReflectParam;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.AppReflectRecordWork;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;

@Stateless
public class PreGoBackReflectServiceImp implements PreGoBackReflectService {
	@Inject
	private WorkTimeTypeScheReflect timeTypeSche;
	@Inject
	private ScheTimeReflect scheTimeReflect;
	@Inject
	private CommonProcessCheckService commonService;
	@Inject
	private PreOvertimeReflectService preOTService;
	@Override
	public void gobackReflect(GobackReflectParameter para, boolean isPre) {
		IntegrationOfDaily dailyInfor = preOTService.calculateForAppReflect(para.getEmployeeId(), para.getDateData());
		//予定勤種・就時の反映
		AppReflectRecordWork chkTimeTypeSche = timeTypeSche.reflectScheWorkTimeType(para, dailyInfor);
		//予定時刻の反映
		scheTimeReflect.reflectScheTime(para, chkTimeTypeSche.isChkReflect(), dailyInfor, isPre);
		//勤種・就時の反映
		AppReflectRecordWork reflectWorkTypeTime = timeTypeSche.reflectRecordWorktimetype(para, dailyInfor);
		//workRepository.updateByKeyFlush(reflectWorkTypeTime.getDailyInfo());
		//時刻の反映
		scheTimeReflect.reflectTime(para, reflectWorkTypeTime.isChkReflect(), dailyInfor);
		CommonCalculateOfAppReflectParam calcParam = new CommonCalculateOfAppReflectParam(dailyInfor,
				para.getEmployeeId(), para.getDateData(),
				ApplicationType.GO_RETURN_DIRECTLY_APPLICATION,
				para.getGobackData().getWorkTypeCode(),
				para.getGobackData().getWorkTimeCode() == null ? Optional.empty() : Optional.of(para.getGobackData().getWorkTimeCode()),
				para.getGobackData().getStartTime1() == null ? Optional.empty() : Optional.of(para.getGobackData().getStartTime1()),
				para.getGobackData().getEndTime1() == null ? Optional.empty() : Optional.of(para.getGobackData().getEndTime1()));
		commonService.calculateOfAppReflect(calcParam);
	}
}
