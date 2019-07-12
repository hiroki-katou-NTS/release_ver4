package nts.uk.ctx.at.function.app.command.processexecution.approuteupdatedaily;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.app.command.processexecution.ListLeaderOrNotEmpOutput;
import nts.uk.ctx.at.function.app.command.processexecution.approuteupdatedaily.transfereeperson.TransfereePerson;
import nts.uk.ctx.at.function.dom.adapter.closure.FunClosureAdapter;
import nts.uk.ctx.at.function.dom.adapter.closure.PresentClosingPeriodFunImport;
import nts.uk.ctx.at.function.dom.adapter.employeemanage.EmployeeManageAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily.CreateperApprovalDailyAdapter;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecType;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecutionScopeItem;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.listempautoexec.ListEmpAutoExec;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDaily;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDailyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.ErrorMessageRC;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@Slf4j
public class AppRouteUpdateDailyDefault implements AppRouteUpdateDailyService {

	@Inject
	private ProcessExecutionLogRepository processExecutionLogRepo;
	
	@Inject
	private ClosureRepository closureRepository;
	
	@Inject
	private FunClosureAdapter funClosureAdapter;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	
	@Inject
	private TransfereePerson transfereePerson;
	
	@Inject
	private CreateperApprovalDailyAdapter createperApprovalDailyAdapter;
	
	@Inject
	private ListEmpAutoExec listEmpAutoExec;
	
	@Inject
	private AppDataInfoDailyRepository appDataInfoDailyRepo;
	
	@Inject
	private EmployeeManageAdapter employeeManageAdapter;
	
	public static int MAX_DELAY_PARALLEL = 0;
	
	@Override
	public boolean checkAppRouteUpdateDaily(String execId, ProcessExecution procExec, ProcessExecutionLog procExecLog) {
		String companyId = AppContexts.user().companyId();
		boolean checkError1552 = false;
		/**ドメインモデル「更新処理自動実行ログ」を更新する*/
		for(ExecutionTaskLog executionTaskLog :procExecLog.getTaskLogList() ) {
			if(executionTaskLog.getProcExecTask() == ProcessExecutionTask.APP_ROUTE_U_DAI) {
				executionTaskLog.setStatus(null);
				break;
			}
		}
		processExecutionLogRepo.update(procExecLog);
		/**承認ルート更新区分の判定*/
		//FALSEの場合
		if(procExec.getExecSetting().getAppRouteUpdateDaily().getAppRouteUpdateAtr() == NotUseAtr.NOT_USE) {
			for(ExecutionTaskLog executionTaskLog :procExecLog.getTaskLogList() ) {
				if(executionTaskLog.getProcExecTask() == ProcessExecutionTask.APP_ROUTE_U_DAI) {
					executionTaskLog.setStatus(Optional.of(EndStatus.NOT_IMPLEMENT));
					break;
				}
			}
			processExecutionLogRepo.update(procExecLog);
			return checkError1552;
		}
		log.info("更新処理自動実行_承認ルート更新（日次）_START_"+procExec.getExecItemCd()+"_"+GeneralDateTime.now());
		
		/**ドメインモデル「就業締め日」を取得する(lấy thông tin domain ル「就業締め日」)*/
		List<Closure> listClosure = closureRepository.findAllActive(procExec.getCompanyId(),UseClassification.UseClass_Use);
		
		log.info("承認ルート更新(日別) START PARALLEL (締めループ数:" + listClosure.size() + ")");
		long startTime = System.currentTimeMillis();
		
		List<CheckCreateperApprovalClosure> listCheckCreateApp = new ArrayList<>();
		//取得した就業締め日の数(so du lieu 就業締め日 lay duoc)　＝　回数
		for(Closure itemClosure :  listClosure) {
		//for(Closure closure : listClosure) {
			log.info("承認ルート更新(日別) 締め: " + itemClosure.getClosureId());
			/**締め開始日を取得する*/
			PresentClosingPeriodFunImport closureData =  funClosureAdapter.getClosureById(procExec.getCompanyId(), itemClosure.getClosureId().value).get();
			GeneralDate startDate = GeneralDate.today().addDays(-1);
			if(procExec.getProcessExecType() == ProcessExecType.RE_CREATE){
				startDate = closureData.getClosureStartDate();
			}
			
			//雇用コードを取得する(lấy 雇用コード)
			List<ClosureEmployment> listClosureEmployment =  closureEmploymentRepo.findByClosureId(procExec.getCompanyId(), itemClosure.getClosureId().value);
			List<String> listClosureEmploymentCode = listClosureEmployment.stream().map(c->c.getEmploymentCD()).collect(Collectors.toList());
			/**対象社員を取得する*/
			List<ProcessExecutionScopeItem> workplaceIdList = procExec.getExecScope()
					.getWorkplaceIdList();
			List<String> workplaceIds = new ArrayList<String>();
			workplaceIdList.forEach(x -> {
				workplaceIds.add(x.getWkpId());
			});
			List<String> listEmp = new ArrayList<>();
			try {
				listEmp = listEmpAutoExec.getListEmpAutoExec(companyId,
						new DatePeriod(startDate, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")),
						procExec.getExecScope().getExecScopeCls(), Optional.of(workplaceIds),
						Optional.of(listClosureEmploymentCode));
			} catch (Exception e) {
				appDataInfoDailyRepo.addAppDataInfoDaily(new AppDataInfoDaily("System", execId, new ErrorMessageRC(TextResource.localize("Msg_1552"))));
				checkError1552 = true;
				break;
			}
			System.out.println("対象者-承認ルート日次: " + listEmp);
			
			if(procExec.getProcessExecType() == ProcessExecType.NORMAL_EXECUTION) {
				//通常実行の場合
				/**「対象社員を取得する」で取得した社員IDを社員ID（List）とする*/
				
			}else {
				//再作成の場合
				if(!listEmp.isEmpty()) {
					/**異動者、勤務種別変更者、休職者・休業者のみの社員ID（List）を作成する*/	
					DatePeriod maxPeriodBetweenCalAndCreate = new DatePeriod(closureData.getClosureStartDate(), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
					ListLeaderOrNotEmpOutput listLeaderOrNotEmpOutput = transfereePerson.createProcessForChangePerOrWorktype(itemClosure.getClosureId().value, procExec.getCompanyId(),
							listEmp, 
							maxPeriodBetweenCalAndCreate, procExec);
					listEmp.addAll(listLeaderOrNotEmpOutput.getLeaderEmpIdList());
					listEmp.addAll(listLeaderOrNotEmpOutput.getNoLeaderEmpIdList());
				}
			}
			
			/** アルゴリズム「日別実績の承認ルート中間データの作成」を実行する */
			Integer createNewEmp = null;
			if(procExec.getExecSetting().getAppRouteUpdateDaily().getCreateNewEmp().isPresent())
				createNewEmp = procExec.getExecSetting().getAppRouteUpdateDaily().getCreateNewEmp().get().value;
			boolean check = createperApprovalDailyAdapter.createperApprovalDaily(procExec.getCompanyId(), procExecLog.getExecId(),
					listEmp.stream().distinct().collect(Collectors.toList()), procExec.getProcessExecType().value, createNewEmp, closureData.getClosureStartDate(),closureData.getClosureEndDate());
			listCheckCreateApp.add(new CheckCreateperApprovalClosure(itemClosure.getClosureId().value,check));
		
		
		};
		

		log.info("承認ルート更新(日別) END PARALLEL: " + ((System.currentTimeMillis() - startTime) / 1000) + "秒");
		log.info("更新処理自動実行_承認ルート更新（日次）_END_"+procExec.getExecItemCd()+"_"+GeneralDateTime.now());
		boolean checkError = false;
		/*終了状態で「エラーあり」が返ってきたか確認する*/
		for(CheckCreateperApprovalClosure checkCreateperApprovalClosure :listCheckCreateApp) {
			//エラーがあった場合
			if(checkCreateperApprovalClosure.isCheckCreateperApproval()) {
				checkError = true;
				break;
			}
		}
		for(ExecutionTaskLog executionTaskLog : procExecLog.getTaskLogList()) {
			//【条件 - dieu kien】 各処理の終了状態.更新処理　＝　承認ルート更新（月次）
			if(executionTaskLog.getProcExecTask() ==ProcessExecutionTask.APP_ROUTE_U_DAI) {
				//【更新内容 - noi dung update】
				if(checkError) {
					//各処理の終了状態　＝　[承認ルート更新（日次）、異常終了]
					executionTaskLog.setStatus(Optional.of(EndStatus.ABNORMAL_END));
				}else {
					//各処理の終了状態　＝　[承認ルート更新（日次）、正常終了]
					executionTaskLog.setStatus(Optional.of(EndStatus.SUCCESS));
				}
			}	
		}
		//ドメインモデル「更新処理自動実行ログ」を更新する( domain 「更新処理自動実行ログ」)
		processExecutionLogRepo.update(procExecLog);
		return checkError1552;
		
	}
		
		
		
}
