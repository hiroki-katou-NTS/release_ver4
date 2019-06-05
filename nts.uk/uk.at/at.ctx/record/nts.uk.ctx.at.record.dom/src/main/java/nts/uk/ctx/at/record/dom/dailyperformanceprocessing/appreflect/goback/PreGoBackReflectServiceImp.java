package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.eclipse.persistence.exceptions.OptimisticLockException;

import nts.gul.error.ThrowableAnalyzer;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.AppReflectRecordWork;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;

@Stateless
public class PreGoBackReflectServiceImp implements PreGoBackReflectService {
	@Inject
	private WorkTimeTypeScheReflect timeTypeSche;
	@Inject
	private ScheTimeReflect scheTimeReflect;
	@Inject
	private AfterWorkTimeTypeReflect afterWorkTimeType;
	@Inject
	private AfterScheTimeReflect afterScheTime;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public boolean gobackReflect(GobackReflectParameter para) {
		try {
			WorkInfoOfDailyPerformance dailyInfor = workRepository.find(para.getEmployeeId(), para.getDateData()).get();
			//予定勤種・就時の反映
			AppReflectRecordWork chkTimeTypeSche = timeTypeSche.reflectScheWorkTimeType(para, dailyInfor);
			//予定時刻の反映
			dailyInfor = scheTimeReflect.reflectScheTime(para, chkTimeTypeSche.isChkReflect(), dailyInfor);
			//勤種・就時の反映
			AppReflectRecordWork reflectWorkTypeTime = timeTypeSche.reflectRecordWorktimetype(para, dailyInfor);
			workRepository.updateByKeyFlush(reflectWorkTypeTime.getDailyInfo());
			//時刻の反映
			scheTimeReflect.reflectTime(para, reflectWorkTypeTime.isChkReflect());			
			commonService.calculateOfAppReflect(null, para.getEmployeeId(), para.getDateData(), false);
			return true;
		} catch(Exception ex) {
			boolean isError = new ThrowableAnalyzer(ex).findByClass(OptimisticLockException.class).isPresent();
			if(!isError) {
				throw ex;
			}
			commonService.createLogError(para.getEmployeeId(), para.getDateData(), para.getExcLogId());
			return false;
		}
	}

	@Override
	public boolean afterGobackReflect(GobackReflectParameter para) {
		try {
			WorkInfoOfDailyPerformance dailyInfor = workRepository.find(para.getEmployeeId(), para.getDateData()).get();
			//予定勤種・就時の反映
			AppReflectRecordWork chkTimeTypeChe = afterWorkTimeType.workTimeAndTypeScheReflect(para, dailyInfor);
			//予定時刻の反映
			dailyInfor = afterScheTime.reflectScheTime(para, chkTimeTypeChe.isChkReflect(), chkTimeTypeChe.getDailyInfo());
			//勤種・就時の反映
			AppReflectRecordWork reflectWorkTypeTime = timeTypeSche.reflectRecordWorktimetype(para, dailyInfor);
			workRepository.updateByKeyFlush(reflectWorkTypeTime.getDailyInfo());
			//時刻の反映
			scheTimeReflect.reflectTime(para, reflectWorkTypeTime.isChkReflect());
			commonService.calculateOfAppReflect(null, para.getEmployeeId(), para.getDateData(), false);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
