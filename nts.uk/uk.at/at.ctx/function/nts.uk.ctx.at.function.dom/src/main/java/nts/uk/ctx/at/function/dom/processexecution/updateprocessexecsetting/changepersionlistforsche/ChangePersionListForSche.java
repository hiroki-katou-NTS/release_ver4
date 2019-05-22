package nts.uk.ctx.at.function.dom.processexecution.updateprocessexecsetting.changepersionlistforsche;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.AffCompanyHistImport;
import nts.uk.ctx.at.function.dom.adapter.AffWorkplaceHistoryImport;
import nts.uk.ctx.at.function.dom.adapter.EmployeeHistWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.WorkplaceWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.dailyperformanceformat.businesstype.BusinessTypeEmpOfHistAdapter;
import nts.uk.ctx.at.function.dom.adapter.dailyperformanceformat.businesstype.BusinessTypeOfEmpHistImport;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetClassification;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 異動者・勤務種別変更者リスト作成処理（スケジュール用） ( old :対象社員を絞り込み)
 * @author tutk
 *
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class ChangePersionListForSche {
	@Inject
	private ClosureRepository closureRepo;
	
	@Inject
	private WorkplaceWorkRecordAdapter workplaceWorkRecordAdapter;
	
	@Inject
	private BusinessTypeEmpOfHistAdapter businessTypeEmpOfHistAdapter;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private EmployeeHistWorkRecordAdapter employeeHistWorkRecordAdapter;
	
	public DatePeriod filterEmployeeList(ProcessExecution procExec, List<String> employeeIdList,
			List<String> reEmployeeList, List<String> newEmployeeList, List<String> temporaryEmployeeList) {

		String companyId = AppContexts.user().companyId();
		/** 作成対象の判定 */
		if (procExec.getExecSetting().getPerSchedule().getTarget()
				.getCreationTarget().value == TargetClassification.ALL.value) {
			return null;
		} else {
			Set<String> listSetReEmployeeList = new HashSet<>();
			// ドメインモデル「就業締め日」を取得する
			List<Closure> closureList = this.closureRepo.findAllActive(companyId, UseClassification.UseClass_Use);
			DatePeriod closurePeriod = this.findClosureMinMaxPeriod(companyId, closureList);

			DatePeriod newClosurePeriod = new DatePeriod(closurePeriod.start(), GeneralDate.ymd(9999, 12, 31));

			TargetSetting setting = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting();
			// 異動者を再作成するか判定
			if (setting.isRecreateTransfer()) {
				// Imported(勤務実績)「所属職場履歴」を取得する : 異動者の絞り込み
//				List<WorkPlaceHistImport> wkpImportList = this.workplaceAdapter.getWplByListSidAndPeriod(employeeIdList,
//						newClosurePeriod);
//				
				List<AffWorkplaceHistoryImport> list = workplaceWorkRecordAdapter
						.getWorkplaceBySidsAndBaseDate(employeeIdList, closurePeriod.start());
				list.forEach(emp -> {
					emp.getHistoryItems().forEach(x -> {
						if (x.start().afterOrEquals(closurePeriod.start())) {
							listSetReEmployeeList.add(emp.getSid());
							return;
						}
					});
				});
			}

			// 勤務種別変更者を再作成するか判定
			if (setting.isRecreateWorkType()) {
				employeeIdList.forEach(x -> {
					// ドメインモデル「社員の勤務種別の履歴」を取得する
					Optional<BusinessTypeOfEmpHistImport> optional = this.businessTypeEmpOfHistAdapter
							.findByEmployeeDesc(AppContexts.user().companyId(), x);
					if (optional.isPresent()) {
						for (DateHistoryItem history : optional.get().getHistory()) {
							// 「全締めの期間.開始日年月日」以降に「社員の勤務種別の履歴.履歴.期間.開始日」が存在する
							if (history.start().afterOrEquals(closurePeriod.start())) {
								// 取得したImported（勤務実績）「所属職場履歴」.社員IDを異動者とする
								listSetReEmployeeList.add(optional.get().getEmployeeId());
								break;
							}
						}
					}
				});
			}
			// 休職者・休業者を再作成するか判定
			// TODO:chua lam
			// 新入社員を作成するか判定
			if (setting.isCreateEmployee()) {
				// request list 211
				// Imported「所属会社履歴（社員別）」を取得する
				List<AffCompanyHistImport> employeeHistList = this.employeeHistWorkRecordAdapter
						.getWplByListSidAndPeriod(employeeIdList, newClosurePeriod);
				// 取得したドメインモデル「所属開始履歴（社員別）」.社員IDを新入社員とする
				employeeHistList.forEach(x -> newEmployeeList.add(x.getEmployeeId()));

				// 社員ID（新入社員以外）（List）
				for (String empID : employeeIdList) {
					boolean checkNotExist = true;
					for (String newEmpID : newEmployeeList) {
						if (empID.equals(newEmpID)) {
							checkNotExist = false;
							break;
						}
					}
					if (checkNotExist) {
						temporaryEmployeeList.add(empID);
					}
				}

			}
			// 社員ID（異動者、勤務種別変更者、休職者・休業者）（List）から重複している社員IDを1つになるよう削除する
			List<String> temp = new ArrayList<String>(listSetReEmployeeList);
			reEmployeeList.addAll(temp);
			return closurePeriod;
		}

	}
	
	private DatePeriod findClosureMinMaxPeriod(String companyId, List<Closure> closureList) {
		GeneralDate startYearMonth = null;
		GeneralDate endYearMonth = null;
		for (Closure closure : closureList) {
			DatePeriod datePeriod = this.closureService.getClosurePeriod(closure.getClosureId().value,
					closure.getClosureMonth().getProcessingYm());

			if (startYearMonth == null || datePeriod.start().before(startYearMonth)) {
				startYearMonth = datePeriod.start();
			}

			if (endYearMonth == null || datePeriod.end().after(endYearMonth)) {
				endYearMonth = datePeriod.end();
			}
		}

		return new DatePeriod(startYearMonth, endYearMonth);
	}
}
