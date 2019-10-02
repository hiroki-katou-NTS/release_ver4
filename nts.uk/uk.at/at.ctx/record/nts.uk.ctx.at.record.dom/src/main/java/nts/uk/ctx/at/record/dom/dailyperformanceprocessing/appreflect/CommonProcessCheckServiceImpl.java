package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectBreakTimeOfDailyDomainService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AdTimeAndAnyItemAdUpService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateDailyRecordServiceCenter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.service.event.breaktime.BreakTimeOfDailyService;
import nts.uk.ctx.at.record.dom.service.event.common.CorrectEventConts;
import nts.uk.ctx.at.record.dom.service.event.overtime.OvertimeOfDailyService;
import nts.uk.ctx.at.record.dom.service.event.schetimeleave.ScheTimeLeavingOfDailyService;
import nts.uk.ctx.at.record.dom.service.event.timeleave.TimeLeavingOfDailyService;
import nts.uk.ctx.at.record.dom.service.event.worktime.WorkTimeOfDailyService;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class CommonProcessCheckServiceImpl implements CommonProcessCheckService{
	@Inject
	private WorkTimeIsFluidWork workTimeisFluidWork;
	@Inject
	private WorkUpdateService workTimeUpdate;
	@Inject
	private CalculateDailyRecordServiceCenter calService;
	@Inject
	private AdTimeAndAnyItemAdUpService timeAndAnyItemUpService;
	@Inject
	private ReflectBreakTimeOfDailyDomainService breaktimeSevice;
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeRepo;
	@Inject
	private TimeLeavingOfDailyService timeLeavingService;
	@Inject
	private BreakTimeOfDailyService breakTimeDailyService;
	@Inject
	private WorkTypeRepository worktypeRepo;
	@Inject
	private OvertimeOfDailyService overTimeService;
	@Inject
	private EmployeeDailyPerErrorRepository employeeError;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private ErrMessageInfoRepository errMessInfo;
	@Inject
	private ScheTimeLeavingOfDailyService scheTimeService;
	@Inject
	private EditStateOfDailyPerformanceRepository dailyReposiroty;
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDaily;
	@Inject
	private WorkTimeOfDailyService workTimeDailyService;
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
	public void reflectScheWorkTimeWorkType(CommonReflectParameter commonPara, boolean isPre,
			IntegrationOfDaily dailyInfor) {
		//予定勤種の反映		
		ReflectParameter para = new ReflectParameter(commonPara.getEmployeeId(), commonPara.getAppDate(), commonPara.getWorkTimeCode(), 
				commonPara.getWorkTypeCode(), false);
		workTimeUpdate.updateWorkTimeType(para, true, dailyInfor);
	}

	@Override
	public boolean checkReflectScheWorkTimeType(CommonReflectParameter commonPara, boolean isPre, String workTimeCode) {
		if(!isPre) {
			return false;
		}
		//INPUT．予定反映区分をチェックする
		if((commonPara.isScheTimeReflectAtr() == true && isPre)
				|| commonPara.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.ALWAYS_CHANGE_AUTO) {
			return true;
		}
		//INPUT．予定と実績を同じに変更する区分をチェックする
		if(commonPara.getScheAndRecordSameChangeFlg() == ScheAndRecordSameChangeFlg.AUTO_CHANGE_ONLY_WORK) {
			//流動勤務かどうかの判断処理
			return workTimeisFluidWork.checkWorkTimeIsFluidWork(workTimeCode);
		}
		
		return false;
	}

	@Override
	public void calculateOfAppReflect(CommonCalculateOfAppReflectParam commonPara) {
		String companyId = AppContexts.user().companyId();
		//就業時間帯の休憩時間帯を日別実績に反映する
		this.updateBreakTimeInfor(commonPara.getSid(),
				commonPara.getYmd(),
				commonPara.getIntegrationOfDaily(), companyId);
		Optional<WorkType> workTypeInfor = Optional.empty();
		if(commonPara.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTypeCode() != null) {
			workTypeInfor = worktypeRepo.findByPK(companyId, 
					commonPara.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTypeCode().v());
			//就業時間帯の補正処理
			workTimeDailyService.correct(companyId, commonPara.getIntegrationOfDaily());
			//2019.02.26　渡邉から
			//残業申請の場合は、自動打刻セットの処理を呼ばない（大塚リリースの時はこの条件で実装する（製品版では、実績の勤務種類、就業時間帯を変更した場合に自動打刻セットを実行するように修正する事（渡邉）
			if(commonPara.getAppType() != ApplicationType.OVER_TIME_APPLICATION) {
				List<EditStateOfDailyPerformance> lstTime = commonPara.getIntegrationOfDaily().getEditState().stream()
						.filter(x -> CorrectEventConts.LEAVE_ITEMS.contains(x.getAttendanceItemId()) 
								|| CorrectEventConts.ATTENDANCE_ITEMS.contains(x.getAttendanceItemId()))
						.collect(Collectors.toList());
				if(!(commonPara.getAppType() == ApplicationType.WORK_CHANGE_APPLICATION && !commonPara.isPreRequest())) {
					//予定出退勤時刻を反映する
					scheTimeService.correct(companyId,
							commonPara.getWorkTypeCode(),
							commonPara.getWorkTimeCode(),
							commonPara.getStartTime(),
							commonPara.getEndTime(),
							commonPara.getIntegrationOfDaily());	
				}				
				if(commonPara.getAppType() != ApplicationType.BREAK_TIME_APPLICATION ||
						(commonPara.getAppType() == ApplicationType.BREAK_TIME_APPLICATION && lstTime.isEmpty())) {
					//出退勤時刻を補正する
					timeLeavingService.correct(companyId, commonPara.getIntegrationOfDaily(), Optional.empty(), workTypeInfor, false).getData();	
				}				
				// 申請された時間を補正する
				overTimeService.correct(commonPara.getIntegrationOfDaily(), workTypeInfor, true);
				//Neu khong phai don xin di lam vao ngay nghi va don xin di lam vao ngay nghi ko tich chon phan anh gio nghi
				if(workTypeInfor.isPresent() && (!workTypeInfor.get().getDailyWork().isHolidayWork()
						|| (workTypeInfor.get().getDailyWork().isHolidayWork() && !this.isReflectBreakTime(commonPara.getIntegrationOfDaily().getEditState())))) {
					//休憩時間帯を補正する	
					breakTimeDailyService.correct(companyId, commonPara.getIntegrationOfDaily(), workTypeInfor, false).getData();
				}
			}
		}
		
		List<IntegrationOfDaily> lstCal = calService.calculateForSchedule(CalculateOption.asDefault(),
				Arrays.asList(commonPara.getIntegrationOfDaily()) , 
				Optional.empty());
		IntegrationOfDaily x = lstCal.get(0);
		if(commonPara.getIntegrationOfDaily().getBreakTime().isEmpty()) {
			breakTimeRepo.delete(commonPara.getSid(), commonPara.getYmd());
		} else {
			breakTimeRepo.update(commonPara.getIntegrationOfDaily().getBreakTime());	
		}
		commonPara.getIntegrationOfDaily().getAttendanceLeave().ifPresent(a -> timeLeavingOfDaily.updateFlush(a));
		workRepository.updateByKeyFlush(x.getWorkInformation());
		timeAndAnyItemUpService.addAndUpdate(lstCal);
		dailyReposiroty.addAndUpdate(commonPara.getIntegrationOfDaily().getEditState());
		Map<String, List<GeneralDate>> param = new HashMap<>();
		param.put(commonPara.getSid(), Arrays.asList(commonPara.getYmd()));		
		employeeError.removeNotOTK(param);
		if(!x.getEmployeeError().isEmpty()) {
			employeeError.update(x.getEmployeeError());	
		}
	}

	@Override
	public IntegrationOfDaily updateBreakTimeInfor(String sid, GeneralDate ymd, IntegrationOfDaily integrationOfDaily, String companyId) {
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
		if(integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTypeCode() != null) {
			Optional<WorkType> workTypeInfor = worktypeRepo.findByPK(companyId, integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTypeCode().v());
			//休日出勤申請しか反映してない
			if(this.isReflectBreakTime(lstEditState)
					&& workTypeInfor.isPresent() && workTypeInfor.get().getDailyWork().isHolidayWork()) {
				if(lstBeforeBreakTimeInfor.size() == 1 && breakTimeInfor != null
						&& lstBeforeBreakTimeInfor.get(0).getBreakType() != breakTimeInfor.getBreakType()) {
					integrationOfDaily.getBreakTime().add(breakTimeInfor);
					//breakTimeRepo.updateV2(integrationOfDaily.getBreakTime());
				}
				return integrationOfDaily;
			}
		}
		
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
			if(!lstEditCheck.isEmpty() && lstEditCheck.size() == 2
					&& lstEditCheck.get(0).getEditStateSetting() == EditStateSetting.REFLECT_APPLICATION) {
				lstEditCheck.clear();
				integrationOfDaily.getBreakTime().stream().forEach(x -> {
					x.getBreakTimeSheets().remove(count);
				});
			}
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
		lstBeforeBreakTimeInfor.add(beforBTWork);
		if(!beforeBreakTime.isEmpty()) {
			integrationOfDaily.setBreakTime(lstBeforeBreakTimeInfor);
		} else {
			integrationOfDaily.setBreakTime(integrationOfDaily.getBreakTime().stream().map(x -> {
				if(x.getBreakType() == BreakType.REFER_WORK_TIME) {
					return new BreakTimeOfDailyPerformance(x.getEmployeeId(), x.getBreakType(), lstBreakOutput, x.getYmd());
				}
				return x;
			}).collect(Collectors.toList()));
		}
		return integrationOfDaily;
	}

	private boolean isReflectBreakTime(List<EditStateOfDailyPerformance> lstEditState) {
		List<EditStateOfDailyPerformance> lstEditReflect = lstEditState.stream()
				.filter(x -> (workTimeUpdate.lstBreakStartTime().contains(x.getAttendanceItemId()) 
							|| workTimeUpdate.lstBreakEndTime().contains(x.getAttendanceItemId())                            
							|| workTimeUpdate.lstScheBreakStartTime().contains(x.getAttendanceItemId())
                            || workTimeUpdate.lstScheBreakEndTime().contains(x.getAttendanceItemId()))
						&& x.getEditStateSetting() == EditStateSetting.REFLECT_APPLICATION)
				.collect(Collectors.toList());
		return lstEditReflect.isEmpty() ? false : true;
	}
	@Override
	public void createLogError(String sid, GeneralDate ymd, String excLogId) {
		ErrMessageInfo errMes = new ErrMessageInfo(sid, 
				excLogId,
				new ErrMessageResource("024"),
				EnumAdaptor.valueOf(2, ExecutionContent.class),
				ymd,
				new ErrMessageContent(TextResource.localize("Msg_1541")));
		this.errMessInfo.add(errMes);
	}
}
