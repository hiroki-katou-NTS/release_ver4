package nts.uk.ctx.at.function.app.command.processexecution.approuteupdatedaily;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.task.parallel.ManagedParallelWithContext.ControlOption;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.app.command.processexecution.ListLeaderOrNotEmpOutput;
import nts.uk.ctx.at.function.app.command.processexecution.approuteupdatedaily.transfereeperson.TransfereePerson;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapterImport;
import nts.uk.ctx.at.function.dom.adapter.closure.FunClosureAdapter;
import nts.uk.ctx.at.function.dom.adapter.closure.PresentClosingPeriodFunImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily.CreateperApprovalDailyAdapter;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionScopeClassification;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecType;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecutionScopeItem;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
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
	private RegulationInfoEmployeeAdapter regulationInfoEmployeeAdapter;
	
	@Inject
	private TransfereePerson transfereePerson;
	
	@Inject
	private CreateperApprovalDailyAdapter createperApprovalDailyAdapter;
	
	@Inject
	private ManagedParallelWithContext managedParallelWithContext;
	
	public static int MAX_DELAY_PARALLEL = 0;
	
	@Override
	public void checkAppRouteUpdateDaily(String execId, ProcessExecution procExec, ProcessExecutionLog procExecLog) {
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
			return;
		}
		
		
		/**ドメインモデル「就業締め日」を取得する(lấy thông tin domain ル「就業締め日」)*/
		List<Closure> listClosure = closureRepository.findAllActive(procExec.getCompanyId(),UseClassification.UseClass_Use);
		
		List<CheckCreateperApprovalClosure> listCheckCreateApp = new ArrayList<>();
		//取得した就業締め日の数(so du lieu 就業締め日 lay duoc)　＝　回数
		this.managedParallelWithContext.forEach(
				ControlOption.custom().millisRandomDelay(MAX_DELAY_PARALLEL),
				listClosure,
				itemClosure -> {
		//for(Closure closure : listClosure) {
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
			RegulationInfoEmployeeAdapterImport regulationInfoEmployeeAdapterImport = new RegulationInfoEmployeeAdapterImport();
			if (procExec.getExecScope().getExecScopeCls() == ExecutionScopeClassification.WORKPLACE) {
				// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】
				// 基準日 → システム日付
				regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
				// 検索参照範囲 → 参照範囲を考慮しない
				regulationInfoEmployeeAdapterImport.setReferenceRange(3);
				// 雇用で絞り込む → True
				regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
				// 雇用コード一覧 → 取得した雇用コード（List）
				regulationInfoEmployeeAdapterImport.setEmploymentCodes(listClosureEmploymentCode);
				// 部門で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
				// 部門ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
				// 職場で絞り込む → TRUE
				regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);

				List<ProcessExecutionScopeItem> workplaceIdList = procExec.getExecScope()
						.getWorkplaceIdList();
				List<String> workplaceIds = new ArrayList<String>();
				workplaceIdList.forEach(x -> {
					workplaceIds.add(x.getWkpId());
				});
				// 職場ID一覧 → 職場ID（List）←ドメインモデル「更新処理自動実行」．実行範囲．職場実行範囲
				regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workplaceIds);
				// 分類で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
				// 分類コード一覧 → なし
				regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
				// 職位で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
				// 職位ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
				// 在職・休職・休業のチェック期間　　→　※補足参照～9999/12/31
				regulationInfoEmployeeAdapterImport.setPeriodStart(startDate);
				regulationInfoEmployeeAdapterImport.setPeriodEnd(GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
				// 在職者を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
				// 休職者を含める → TRUE(EA: 101326)
				regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(true);
				// 休業者を含める → TRUE(EA: 101326)
				regulationInfoEmployeeAdapterImport.setIncludeOccupancy(true);
				// 出向に来ている社員を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
				// 出向に行っている社員を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
				// 退職者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
				// 退職日のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setRetireStart(startDate);
				regulationInfoEmployeeAdapterImport.setRetireEnd(GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
				// 並び順NO → 1
				regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
				// 氏名の種類 → ビジネスネーム日本語
				regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");
				
				regulationInfoEmployeeAdapterImport.setSystemType(2);
				//勤務種別で絞り込む　→　FALSE
				regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
				//勤務種別コード一覧　→　空
				regulationInfoEmployeeAdapterImport.setWorktypeCodes(new ArrayList<String>());
				
				//就業締めで絞り込む　→　FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClosure(false);

			} else {
				// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】
				// 基準日 → システム日付
				regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
				// 検索参照範囲 → 参照範囲を考慮しない
				regulationInfoEmployeeAdapterImport.setReferenceRange(3);
				// 雇用で絞り込む → TRUE
				regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
				// 雇用コード一覧 → 取得した雇用コード（List）
				regulationInfoEmployeeAdapterImport.setEmploymentCodes(listClosureEmploymentCode);
				// 部門で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
				// 部門ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
				// 職場で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false);
				// 職場ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null);
				// 分類で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
				// 分類コード一覧 → なし
				regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
				// 職位で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
				// 職位ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
				// 在職・休職・休業のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setPeriodStart(startDate);
				regulationInfoEmployeeAdapterImport.setPeriodEnd(GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
				// 在職者を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
				// 休職者を含める → TRUE(EA: 101326)
				regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(true);
				// 休業者を含める → TRUE(EA: 101326)
				regulationInfoEmployeeAdapterImport.setIncludeOccupancy(true);
				// 出向に来ている社員を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
				// 出向に行っている社員を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
				// 退職者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
				// 退職日のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setRetireStart(startDate);
				regulationInfoEmployeeAdapterImport.setRetireEnd(GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
				// 並び順NO → 1
				regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
				// 氏名の種類 → ビジネスネーム日本語
				regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");
				
				regulationInfoEmployeeAdapterImport.setSystemType(2);
				//勤務種別で絞り込む　→　FALSE
				regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
				//勤務種別コード一覧　→　空
				regulationInfoEmployeeAdapterImport.setWorktypeCodes(new ArrayList<String>());
				
				//就業締めで絞り込む　→　FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClosure(false);
			}
			
			// <<Public>> 就業条件で社員を検索して並び替える
			List<RegulationInfoEmployeeAdapterDto> lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
					.find(regulationInfoEmployeeAdapterImport);
			
			
			
			/** パラメータ.更新処理自動実行.実行種別　をチェックする */
			List<String> listEmployeeID = new ArrayList<>();
			if(procExec.getProcessExecType() == ProcessExecType.NORMAL_EXECUTION) {
				//通常実行の場合
				/**「対象社員を取得する」で取得した社員IDを社員ID（List）とする*/
				listEmployeeID = lstRegulationInfoEmployee.stream().map(c->c.getEmployeeId()).collect(Collectors.toList());
			}else {
				//再作成の場合
				List<String> listEmp = lstRegulationInfoEmployee.stream().map(c->c.getEmployeeId()).collect(Collectors.toList());
				if(!listEmp.isEmpty()) {
					/**異動者、勤務種別変更者、休職者・休業者のみの社員ID（List）を作成する*/	
					DatePeriod maxPeriodBetweenCalAndCreate = new DatePeriod(closureData.getClosureStartDate(), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
					ListLeaderOrNotEmpOutput listLeaderOrNotEmpOutput = transfereePerson.createProcessForChangePerOrWorktype(itemClosure.getClosureId().value, procExec.getCompanyId(),
							listEmp, 
							maxPeriodBetweenCalAndCreate, procExec);
					listEmployeeID.addAll(listLeaderOrNotEmpOutput.getLeaderEmpIdList());
					listEmployeeID.addAll(listLeaderOrNotEmpOutput.getNoLeaderEmpIdList());
				}
			}
			
			/** アルゴリズム「日別実績の承認ルート中間データの作成」を実行する */
			Integer createNewEmp = null;
			if(procExec.getExecSetting().getAppRouteUpdateDaily().getCreateNewEmp().isPresent())
				createNewEmp = procExec.getExecSetting().getAppRouteUpdateDaily().getCreateNewEmp().get().value;
			boolean check = createperApprovalDailyAdapter.createperApprovalDaily(procExec.getCompanyId(), procExecLog.getExecId(),
					listEmployeeID, procExec.getProcessExecType().value, createNewEmp, closureData.getClosureStartDate(),closureData.getClosureEndDate());
			listCheckCreateApp.add(new CheckCreateperApprovalClosure(itemClosure.getClosureId().value,check));
		
		
				});
		
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
		
	}
		
		
		
}
