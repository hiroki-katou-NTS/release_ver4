package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootSituation;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
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

	@Inject
	private ApprovalStatusActualDay approvalStatusActualDay;

	public boolean checkTrackRecordApprovalDay(String companyId, String employeeId, List<CheckTarget> lstCheckTarget) {

		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalUseSetting = approvalProcessingUseSettingRepository
				.findByCompanyId(companyId);
		if (!approvalUseSetting.isPresent() || !approvalUseSetting.get().getUseMonthApproverConfirm())
			return false;

		// ドメインモデル「締め」を取得する
		Map<Integer, Closure> mapClosureId = closureRepo.findAll(companyId).stream()
				.collect(Collectors.toMap(x -> x.getClosureId().value, x -> x));

		//
		for (CheckTarget target : lstCheckTarget) {
			Closure closureTaget = mapClosureId.get(target.getClosureId());
			if (closureTaget == null)
				continue;

			// 指定した年月の期間をすべて取得する
			List<DatePeriod> lstClosureHist = closureTaget.getPeriodByYearMonth(target.getYearMonth());
			if (lstClosureHist.isEmpty())
				continue;
			DatePeriod checkPeriod = mergeDatePeriod(lstClosureHist);
			// 対応するImported「基準社員の承認対象者」を取得する
			ApprovalRootOfEmployeeImport approvalImport = approvalStatusAdapter
					.getApprovalRootOfEmloyeeNew(checkPeriod.start(), checkPeriod.end(), employeeId, companyId, 1);

			if (approvalImport == null)
				continue;
			for (ApprovalRootSituation approvalRoot : approvalImport.getApprovalRootSituations()) {

				// 集計期間
				List<ClosurePeriod> lstClosurePeriod = getClosurePeriod.get(companyId, approvalRoot.getTargetID(),
						GeneralDate.today(), Optional.of(target.getYearMonth()),
						Optional.of(ClosureId.valueOf(target.getClosureId())), Optional.empty());

				List<DatePeriod> lstPeriod = lstClosurePeriod.stream()
						.flatMap(x -> x.getAggrPeriods().stream().map(y -> y.getPeriod())).collect(Collectors.toList());
				// 社員の日の実績の承認状況を取得する
				List<ApprovalStatusActualResult> lstApprovalResult = approvalStatusActualDay
						.processApprovalStatusRequest(companyId, employeeId, approvalRoot.getTargetID(),
								mergeDatePeriod(lstPeriod), target.getClosureId());

				List<ApprovalStatusActualResult> lstResult = lstApprovalResult.stream()
						.filter(x -> x.getPermissionCheck() == ReleasedAtr.CAN_IMPLEMENT).collect(Collectors.toList());
				if (!lstResult.isEmpty())
					return true;
			}

		}
		return false;
	}

	private DatePeriod mergeDatePeriod(List<DatePeriod> lstDate) {
		lstDate = lstDate.stream().sorted((x, y) -> x.start().compareTo(y.start())).collect(Collectors.toList());
		return new DatePeriod(lstDate.get(0).start(), lstDate.get(lstDate.size() - 1).end());
	}
}
