package nts.uk.ctx.at.request.dom.applicationreflect.service;

/*import nts.arc.task.data.TaskDataSetter;*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.closurestatus.ClosureStatusManagementRequestImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExeStateOfCalAndSumImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionLogRequestImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.SetInforReflAprResultImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.TargetPersonImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.TargetPersonRequestImport;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AppReflectManagerFromRecordImpl implements AppReflectManagerFromRecord{
	@Inject
	private TargetPersonRequestImport targetPerson;
	@Inject
	private ExecutionLogRequestImport execuLog;
	@Inject
	private RequestSettingRepository requestSettingRepo;
	@Inject
	private ClosureStatusManagementRequestImport closureStatusImport;
	@Inject
	private ApplicationRepository_New applicationRepo;
	@Inject
	private AppReflectManager appRefMng;
	@Inject
	private GetClosureStartForEmployee getClosureStartForEmp;
	@Inject
	private InformationSettingOfAppForReflect appSetting;
	@Inject
	private ManagedParallelWithContext managedParallelWithContext;

	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public ProcessStateReflect applicationRellect(String workId, DatePeriod workDate, AsyncCommandHandlerContext asyncContext) {
		val dataSetter = asyncContext.getDataSetter();
		dataSetter.setData("reflectApprovalCount", 0);
		dataSetter.setData("reflectApprovalHasError", ErrorPresent.NO_ERROR.nameId );
		//ドメインモデル「申請承認設定」を取得する
		Optional<RequestSetting> optRequesSetting = requestSettingRepo.findByCompany(AppContexts.user().companyId());
		if(!optRequesSetting.isPresent()) {
			return ProcessStateReflect.SUCCESS;
		}
		//再実行かどうか判断する 
		Optional<SetInforReflAprResultImport> optRefAppResult = execuLog.optReflectResult(workId, 2);//2: 承認結果反映 
		//対象社員を取得
		List<TargetPersonImport> lstPerson = targetPerson.getTargetPerson(workId)
				.stream()
				.sorted(Comparator.comparing(TargetPersonImport::getEmployeeId))
				.collect(Collectors.toList());
		ExecutionTypeExImport aprResult = optRefAppResult.isPresent() ? optRefAppResult.get().getExecutionType() 
				: ExecutionTypeExImport.NORMAL_EXECUTION;
//		if(optRefAppResult.isPresent()) {
//			aprResult = optRefAppResult.get().getExecutionType();
//		}
		InformationSettingOfEachApp reflectSetting = appSetting.getSettingOfEachApp();
		AtomicInteger count = new AtomicInteger(0);
		List<ProcessStateReflect> status = Collections.synchronizedList(new ArrayList<>());
		this.managedParallelWithContext.forEach(lstPerson, x -> {
			if(status.stream().anyMatch(c -> c == ProcessStateReflect.INTERRUPTION)) {
				return;
			}
			count.incrementAndGet();
			//データ更新
			//状態確認
			Optional<ExeStateOfCalAndSumImport> optState = execuLog.executionStatus(workId);
			if(optState.isPresent() && optState.get() == ExeStateOfCalAndSumImport.START_INTERRUPTION) {
				asyncContext.finishedAsCancelled();	
				dataSetter.updateData("reflectApprovalStatus", ExecutionStatusReflect.STOPPING.nameId);
				status.add(ProcessStateReflect.INTERRUPTION);
			}
			//処理した社員の実行状況を「完了」にする
			execuLog.updateLogInfo(x.getEmployeeId(), workId, 2, 0);
			execuLog.updateLogInfo(workId, 2, 0);
			if(dataSetter != null) {
				dataSetter.updateData("reflectApprovalStatus", ExecutionStatusReflect.DONE.nameId);	
			}	
			
			dataSetter.updateData("reflectApprovalStatus", ExecutionStatusReflect.PROCESSING.nameId);
			dataSetter.updateData("reflectApprovalCount", count);
			//社員に対応する締め開始日を取得する
			Optional<GeneralDate> closure = getClosureStartForEmp.algorithm(x.getEmployeeId());
			if(closure.isPresent()) {
				GeneralDate startDateshime = closure.get();
				if(workDate.start().afterOrEquals(startDateshime)) {
					//社員の申請を反映 (Phản ánh nhân viên)
					if(!this.reflectAppOfEmployee(workId, x.getEmployeeId(), workDate, 
							optRequesSetting.get(), aprResult, reflectSetting)) {
						dataSetter.updateData("reflectApprovalStatus", ExecutionStatusReflect.STOPPING.nameId);
						status.add(ProcessStateReflect.INTERRUPTION);
					}
				}
				
			}
			
		});
		if(status.stream().anyMatch(c -> c == ProcessStateReflect.INTERRUPTION)) {
			return ProcessStateReflect.INTERRUPTION;
		}
		dataSetter.updateData("reflectApprovalCount", lstPerson.size());
		//処理した社員の実行状況を「完了」にする
		return ProcessStateReflect.SUCCESS;
	}
	@Override
	public boolean reflectAppOfEmployee(String workId, String sid, DatePeriod datePeriod,
			RequestSetting optRequesSetting, ExecutionTypeExImport refAppResult, InformationSettingOfEachApp reflectSetting) {
		
		
		//ドメインモデル「締め状態管理」を取得する
		Optional<DatePeriod> optClosureStatus = closureStatusImport.closureDatePeriod(sid);
		//「申請期間」を作成する
		//申請期間　←　パラメータ.期間のうちドメインモデル「締め状態管理.期間」に含まれている期間を削除した期間
		DatePeriod appDatePeriod = datePeriod;
		if(optClosureStatus.isPresent()) {
			DatePeriod closureDatePeriod = optClosureStatus.get();
			if(datePeriod.start().beforeOrEquals(closureDatePeriod.end())
					&& closureDatePeriod.end().before(datePeriod.end())) {
				appDatePeriod = new DatePeriod(closureDatePeriod.end().addDays(1), datePeriod.end());
			} else if (closureDatePeriod.end().beforeOrEquals(datePeriod.start())
					&& datePeriod.end().after(closureDatePeriod.end())) {
				GeneralDate sDate = datePeriod.start();
				if(closureDatePeriod.end().equals(datePeriod.start())) {
					sDate = datePeriod.start().addDays(1);
				}
				appDatePeriod = new DatePeriod(sDate, datePeriod.end());
			}	
		}		
		
		List<Application_New> lstApp = this.getApps(sid, appDatePeriod, refAppResult);
		if(lstApp.isEmpty()) {
			return true;
		}
		
		for (Application_New appData : lstApp) {			
			//ReflectResult reflectResult = 
					appRefMng.reflectEmployeeOfApp(appData, reflectSetting);
			
			/*if(reflectResult.isRecordResult() || reflectResult.isScheResult()) {
				
				
				//状態確認
				Optional<ExeStateOfCalAndSumImport> optState = execuLog.executionStatus(workId);
				//処理した社員の実行状況を「完了」にする
				execuLog.updateLogInfo(sid, workId, 2, 0);
				dataSetter.updateData("reflectApprovalStatus", ExecutionStatusReflect.DONE.nameId);
				if(optState.isPresent() && optState.get() == ExeStateOfCalAndSumImport.START_INTERRUPTION) {
					return false;
				}
				
			} else {
				if(!countError) {
					dataSetter.updateData("reflectApprovalHasError", ErrorPresent.HAS_ERROR.nameId);
					countError = true;
				}
			}*/
		}
		return true;
	}
	@Override
	public List<Application_New> getApps(String sid, DatePeriod datePeriod, ExecutionTypeExImport exeType) {
		List<Integer> lstApptype = new ArrayList<>();
		lstApptype.add(ApplicationType.ABSENCE_APPLICATION.value);
		lstApptype.add(ApplicationType.OVER_TIME_APPLICATION.value);
		lstApptype.add(ApplicationType.STAMP_APPLICATION.value);
		lstApptype.add(ApplicationType.BREAK_TIME_APPLICATION.value);
		lstApptype.add(ApplicationType.WORK_CHANGE_APPLICATION.value);
		lstApptype.add(ApplicationType.COMPLEMENT_LEAVE_APPLICATION.value);
		lstApptype.add(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value);
		List<Integer> lstRecordStatus = new ArrayList<>();
		List<Integer> lstScheStatus = new ArrayList<>();
		List<Application_New> lstApp = new ArrayList<>();
		// 実行種別を確認
		if(exeType == ExecutionTypeExImport.NORMAL_EXECUTION) {
			//反映待ちの申請を取得
			lstRecordStatus.add(ReflectedState_New.WAITREFLECTION.value);
			lstScheStatus.add(ReflectedState_New.WAITREFLECTION.value);			 
		} else {
			//反映済みも含めて申請を取得
			lstRecordStatus.add(ReflectedState_New.WAITREFLECTION.value);
			lstRecordStatus.add(ReflectedState_New.REFLECTED.value);
			lstScheStatus.add(ReflectedState_New.WAITREFLECTION.value);
			lstScheStatus.add(ReflectedState_New.REFLECTED.value);
		}
		lstApp = applicationRepo.getAppForReflect(sid, datePeriod, lstRecordStatus, lstScheStatus, lstApptype);
		//申請日でソートする		
		return this.sortData(lstApp);
	}
	private List<Application_New> sortData(List<Application_New> lstApp){
		//申請日、入力日、事前事後区分　ASC
		return lstApp.stream().sorted((a,b) ->{
			Integer rs = a.getAppDate().compareTo(b.getAppDate());
			if (rs == 0) {
				Integer sortInputDate = a.getInputDate().compareTo(b.getInputDate());
				if(sortInputDate == 0) {
					return a.getPrePostAtr().compareTo(b.getPrePostAtr());
				}
				return sortInputDate;
			}
			return rs;			
		}).collect(Collectors.toList());
	}
	
	@Override
	public ProcessStateReflect reflectAppOfEmployeeTotal(String workId, String sid, DatePeriod datePeriod) {
		InformationSettingOfEachApp reflectSetting = appSetting.getSettingOfEachApp();
		//ドメインモデル「申請承認設定」を取得する
		Optional<RequestSetting> optRequesSetting = requestSettingRepo.findByCompany(AppContexts.user().companyId());
		if(!optRequesSetting.isPresent()) {
			return ProcessStateReflect.SUCCESS;
		}
		//再実行かどうか判断する 
		Optional<SetInforReflAprResultImport> optRefAppResult = execuLog.optReflectResult(workId, 2);//2: 承認結果反映 
		//対象社員を取得
		//List<TargetPersonImport> lstPerson = 
				targetPerson.getTargetPerson(workId)
				.stream()
				.sorted(Comparator.comparing(TargetPersonImport::getEmployeeId))
				.collect(Collectors.toList());
		ExecutionTypeExImport aprResult = ExecutionTypeExImport.NORMAL_EXECUTION;
		if(optRefAppResult.isPresent()) {
			aprResult = optRefAppResult.get().getExecutionType();
		}
		if(!this.reflectAppOfEmployee(workId, sid, datePeriod, 
				optRequesSetting.get(), aprResult, reflectSetting)) {
			return ProcessStateReflect.INTERRUPTION;
		}
		
		return ProcessStateReflect.SUCCESS;
	}

}
