package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectBreakTimeOfDailyDomainService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AdTimeAndAnyItemAdUpService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateDailyRecordServiceCenter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CommonCompanySettingForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CommonProcessCheckServiceImpl implements CommonProcessCheckService{
	@Inject
	private WorkTimeIsFluidWork workTimeisFluidWork;
	@Inject
	private WorkUpdateService workTimeUpdate;
	@Inject
	private PreOvertimeReflectService preOvertime;
	@Inject
	private CalculateDailyRecordServiceCenter calService;
	@Inject
	private CommonCompanySettingForCalc commonComSetting;
	@Inject
	private AdTimeAndAnyItemAdUpService timeAndAnyItemUpService;
	@Inject
	private ReflectBreakTimeOfDailyDomainService breaktimeSevice;
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeRepo;
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDaily;
	@Override
	public boolean commonProcessCheck(CommonCheckParameter para) {
		ReflectedStateRecord state = ReflectedStateRecord.CANCELED;
		if(para.getExecutiontype() == ExecutionType.RETURN) {
			return true;
		}
		//実績反映状態
		if(para.getDegressAtr() == DegreeReflectionAtr.RECORD) {
			state = para.getStateReflectionReal();
		} else {
			state = para.getStateReflection();
		}
		if(state == ReflectedStateRecord.WAITREFLECTION) {
			return true;
		}
		return false;
	}
	
	@Override
	public WorkInfoOfDailyPerformance reflectScheWorkTimeWorkType(CommonReflectParameter commonPara, boolean isPre,
			WorkInfoOfDailyPerformance dailyInfor) {
		//予定勤種を反映できるかチェックする
		if(!this.checkReflectScheWorkTimeType(commonPara, isPre, dailyInfor)) {
			return dailyInfor;
		}
		//予定勤種の反映		
		ReflectParameter para = new ReflectParameter(commonPara.getEmployeeId(), commonPara.getBaseDate(), commonPara.getWorkTimeCode(), 
				commonPara.getWorkTypeCode(), false);
		return workTimeUpdate.updateWorkTimeType(para, true, dailyInfor);
	}

	@Override
	public boolean checkReflectScheWorkTimeType(CommonReflectParameter commonPara, boolean isPre, WorkInfoOfDailyPerformance dailyInfo) {
		//INPUT．予定反映区分をチェックする
		if((commonPara.isScheTimeReflectAtr() == true && isPre)
				|| commonPara.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.ALWAYS_CHANGE_AUTO) {
			return true;
		}
		//INPUT．予定と実績を同じに変更する区分をチェックする
		if(commonPara.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.AUTO_CHANGE_ONLY_WORK) {
			//ドメインモデル「日別実績の勤務情報」を取得する
			WorkInformation recordWorkInformation = dailyInfo.getRecordInfo();
			if(recordWorkInformation.getWorkTimeCode() == null) {
				return true;
			}
			//流動勤務かどうかの判断処理
			return workTimeisFluidWork.checkWorkTimeIsFluidWork(recordWorkInformation.getWorkTimeCode().v());
		}
		
		return false;
	}

	@Override
	public void calculateOfAppReflect(IntegrationOfDaily integrationOfDaily, String sid, GeneralDate ymd) {
		if(integrationOfDaily == null) {
			integrationOfDaily = preOvertime.calculateForAppReflect(sid, ymd);
		}
		//就業時間帯の休憩時間帯を日別実績に反映する
		integrationOfDaily = this.updateBreakTimeInfor(sid, ymd, integrationOfDaily);
		
		List<IntegrationOfDaily> lstCal = calService.calculateForSchedule(CalculateOption.asDefault(),
				Arrays.asList(integrationOfDaily) , 
				Optional.of(commonComSetting.getCompanySetting()));
		lstCal.stream().forEach(x -> {
			timeAndAnyItemUpService.addAndUpdate(x);	
		});
	}

	@Override
	public IntegrationOfDaily updateBreakTimeInfor(String sid, GeneralDate ymd, IntegrationOfDaily integrationOfDaily) {

		String companyId = AppContexts.user().companyId();
		//日別実績の休憩時間帯
		BreakTimeOfDailyPerformance breakTimeInfor = null; 
		if(integrationOfDaily.getAttendanceLeave().isPresent()) {
			breakTimeInfor = breaktimeSevice.reflectBreakTime(companyId, 
					sid, 
					ymd, 
					null,
					integrationOfDaily.getAttendanceLeave().get(), 
					integrationOfDaily.getWorkInformation());
		}
				
		List<EditStateOfDailyPerformance> lstEditState = integrationOfDaily.getEditState();
		List<BreakTimeOfDailyPerformance> lstBeforeBreakTimeInfor = integrationOfDaily.getBreakTime();
		List<BreakTimeOfDailyPerformance> beforeBreakTime = lstBeforeBreakTimeInfor.stream().filter(x -> x.getBreakType() == BreakType.REFER_WORK_TIME)
				.collect(Collectors.toList());
		
		BreakTimeOfDailyPerformance beforBTWork = new BreakTimeOfDailyPerformance(sid, BreakType.REFER_WORK_TIME, new ArrayList<>(), ymd);
		if(!beforeBreakTime.isEmpty()) {
			beforBTWork = beforeBreakTime.get(0);
			lstBeforeBreakTimeInfor.remove(beforBTWork);
		} 
		List<BreakTimeSheet> breakTimeSheetsTmp = beforBTWork.getBreakTimeSheets();
		List<BreakTimeSheet> lstBreak = new ArrayList<>();
		if(breakTimeInfor != null) {
			lstBreak = breakTimeInfor.getBreakTimeSheets();
		}
				
		List<BreakTimeSheet> lstBreakOutput = new ArrayList<>();			
		for (int i = 0 ; i < 10 ; i ++ ) {
			int num_1 = (i * 6) + 157;
			int num_2 = num_1 + 2;
			int count = i;
			List<EditStateOfDailyPerformance> lstEditCheck = lstEditState.stream()
					.filter(x -> x.getAttendanceItemId() == num_1 || x.getAttendanceItemId() == num_2)
					.collect(Collectors.toList());
			if (lstEditCheck.isEmpty()) {
				if(!lstBreak.isEmpty()) {
					List<BreakTimeSheet> lstBreakOut = lstBreak.stream().filter(y -> y.getBreakFrameNo().v() == (count + 1))
							.collect(Collectors.toList());
					if(!lstBreakOut.isEmpty()) {
						lstBreakOutput.addAll(lstBreakOut);	
					}
				}
			} else {
				if(!breakTimeSheetsTmp.isEmpty()) {
					List<BreakTimeSheet> lstBreakOut = breakTimeSheetsTmp.stream().filter(y -> y.getBreakFrameNo().v() == count + 1)
							.collect(Collectors.toList());
					if(!lstBreakOut.isEmpty()) {
						lstBreakOutput.addAll(lstBreakOut);	
					}
				}
			}
		}			
				
		beforBTWork.setBreakTimeSheets(lstBreakOutput);
		breakTimeRepo.update(beforBTWork);
		beforeBreakTime.add(beforBTWork);
		integrationOfDaily.setBreakTime(beforeBreakTime);
		return integrationOfDaily;
	}

}
