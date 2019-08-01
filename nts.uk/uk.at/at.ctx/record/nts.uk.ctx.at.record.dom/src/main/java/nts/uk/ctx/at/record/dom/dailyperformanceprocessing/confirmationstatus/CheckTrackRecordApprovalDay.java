package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval.ApprovalStatusActualDayChange;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx 
 * [No593]承認すべき日の実績があるかチェックする
 * 
 */
@Stateless
public class CheckTrackRecordApprovalDay {

	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepository;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private ClosureRepository closureRepo;

	@Inject
	private GetClosurePeriod getClosurePeriod;

//	@Inject
//	private ApprovalStatusActualDay approvalStatusActualDay;
	
	@Inject
	private ApprovalStatusActualDayChange approvalActualDayChange;

	public boolean checkTrackRecordApprovalDay(String companyId, String employeeId, Integer closureId, YearMonth yearMonth) {

		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalUseSetting = approvalProcessingUseSettingRepository
				.findByCompanyId(companyId);
		if (!approvalUseSetting.isPresent() || !approvalUseSetting.get().getUseMonthApproverConfirm())
			return false;

		// ドメインモデル「締め」を取得する
		Map<Integer, Closure> mapClosureId = closureRepo.findAll(companyId).stream()
				.collect(Collectors.toMap(x -> x.getClosureId().value, x -> x));

		//
		//for (CheckTarget target : lstCheckTarget) {
			Closure closureTaget = mapClosureId.get(closureId);
			if (closureTaget == null)
				return false;

			// 指定した年月の期間をすべて取得する
			List<DatePeriod> lstClosureHist = closureTaget.getPeriodByYearMonth(yearMonth);
			if (lstClosureHist.isEmpty())
				return false;
			DatePeriod checkPeriod = mergeDatePeriod(lstClosureHist);
			// 対応するImported「基準社員の承認対象者」を取得する
//			ApprovalRootOfEmployeeImport approvalImport = approvalStatusAdapter
//					.getApprovalRootOfEmloyeeNew(checkPeriod.start(), checkPeriod.end(), employeeId, companyId, 1);
//			if (approvalImport == null)
//				continue;
			//[No.610]基準社員から指定期間の対象者を取得する
			List<String> listEmp =  approvalStatusAdapter.findEmpRequest610(employeeId, checkPeriod, 1).stream().distinct().collect(Collectors.toList());
			for (String approvalRoot : listEmp) {

				// 集計期間
				List<ClosurePeriod> lstClosurePeriod = getClosurePeriod
						.get(companyId, approvalRoot, checkPeriod.end(), Optional.of(yearMonth),
								Optional.of(ClosureId.valueOf(closureId)), Optional.empty())
						.stream().filter(c -> c.getClosureId().value == closureId && c.getYearMonth().equals(yearMonth))
						.collect(Collectors.toList());

				if (lstClosurePeriod.isEmpty())
					continue;
				List<DatePeriod> lstPeriod = new ArrayList<>();

				lstPeriod.addAll(
						lstClosurePeriod.stream().flatMap(x -> x.getAggrPeriods().stream().map(y -> y.getPeriod()))
								.collect(Collectors.toList()));
				
				if(lstPeriod.isEmpty()) {
					continue;
				}
				// 社員の日の実績の承認状況を取得する
//				List<ApprovalStatusActualResult> lstApprovalResult = approvalStatusActualDay
//						.processApprovalStatusRequest(companyId, employeeId, approvalRoot,
//								mergeDatePeriod(lstPeriod), closureId);
			List<ApprovalStatusActualResult> lstApprovalResult = approvalActualDayChange.processApprovalStatus(
					companyId, approvalRoot, Arrays.asList(employeeId), Optional.empty(), Optional.of(yearMonth),
					ModeData.APPROVAL.value);
			List<ApprovalStatusActualResult> lstResult = lstApprovalResult.stream()
						.filter(x -> x.isStatus() ? false : x.getPermissionCheck() == ReleasedAtr.CAN_IMPLEMENT).collect(Collectors.toList());
				if (!lstResult.isEmpty())
					return true;
			}

//		}
		return false;
	}

	private DatePeriod mergeDatePeriod(List<DatePeriod> lstDate) {
		List<GeneralDate> lstStartEnd = lstDate.stream().flatMap(x -> Arrays.asList(x.start(), x.end()).stream()).collect(Collectors.toList());
		lstStartEnd = lstStartEnd.stream().sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
		return new DatePeriod(lstStartEnd.get(0), lstStartEnd.get(lstStartEnd.size() - 1));
	}
}
