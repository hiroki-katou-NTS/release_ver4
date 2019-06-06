package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.export.clearapprovalconfirm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ConfirmDeleteParamImport;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhnx
 *
 */
@Stateless
public class ClearConfirmApprovalService {

	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepository;

	@Inject
	private IdentificationRepository identificationRepo;

	@Inject
	private ApprovalProcessingUseSettingRepository approvalUseSetRepo;

//	@Inject
//	private GetClosurePeriod getClosurePeriod;

	@Inject
	private ConfirmationMonthRepository confirmationMonthRepo;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;
	
	@Inject
	private ClosureService closureService;

	/**
	 * 確認、承認のクリア
	 */
	public void clearConfirmApproval(String employeeId, List<GeneralDate> lstDate) {
		clearConfirmApproval(employeeId, lstDate, Optional.empty());
	}
	
	/**
	 * 確認、承認のクリア
	 */
	public void clearConfirmApproval(String employeeId, List<GeneralDate> lstDate, Optional<ApprovalProcessingUseSetting> approvalSetOpt) {
		clearConfirmApproval(employeeId, lstDate, Optional.empty(), Optional.empty());
	}
	
	/**
	 * 確認、承認のクリア
	 */
	@SuppressWarnings("unchecked")
	public void clearConfirmApproval(String employeeId, List<GeneralDate> lstDate, Optional<ApprovalProcessingUseSetting> approvalSetOpt, Optional<IdentityProcessUseSet> iPUS) {
		String companyId = AppContexts.user().companyId();
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> indenUseSetOpt = iPUS.isPresent() ? iPUS : identityProcessUseSetRepository.findByKey(companyId);
		if (indenUseSetOpt.isPresent() && indenUseSetOpt.get().isUseConfirmByYourself()) {
			// ドメインモデル「日の本人確認」を削除する
			identificationRepo.removeByEmpListDate(employeeId, lstDate);
		}

		// ドメインモデル「承認確認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalSettingOpt = approvalSetOpt.isPresent() ? approvalSetOpt : approvalUseSetRepo.findByCompanyId(companyId);
		if (approvalSettingOpt.isPresent() && approvalSettingOpt.get().getUseDayApproverConfirm()) {
			// [No.601]日別の承認をクリアする
			lstDate.forEach(date -> {
				approvalStatusAdapter.deleteRootConfirmDay(employeeId, date);
			});
		}

		List<ClosurePeriod> lstClosureAll = new ArrayList<>();
		for (GeneralDate dateRefer : lstDate) {
			// 集計期間 slow response 
			Closure closure = closureService.getClosureDataByEmployee(employeeId, dateRefer);
			//指定した年月日時点の締め期間を取得する
			// Check exist and active
			if (closure == null || closure.getUseClassification()
					.equals(UseClassification.UseClass_NotUse)) {
				continue;
			}

			Optional<ClosurePeriod> cPeriod = closure.getClosurePeriodByYmd(dateRefer);
			if(!cPeriod.isPresent()) continue;
//			List<ClosurePeriod> closurePeriods = getClosurePeriod.get(companyId, employeeId, dateRefer,
//					Optional.empty(), Optional.empty(), Optional.empty());
			lstClosureAll.add(cPeriod.get());
		};

		lstClosureAll = lstClosureAll.stream().filter(
				distinctByKeys(ClosurePeriod::getClosureId, ClosurePeriod::getClosureDate, ClosurePeriod::getYearMonth))
				.collect(Collectors.toList());
		// 取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
		if (indenUseSetOpt.isPresent() && indenUseSetOpt.get().isUseIdentityOfMonth()) {
			// ドメインモデル「月の本人確認」を削除する(Delete domain 「月の本人確認」)
			lstClosureAll.forEach(cls -> {
				confirmationMonthRepo.delete(companyId, employeeId, cls.getClosureId().value,
						cls.getClosureDate().getClosureDay().v(), cls.getClosureDate().getLastDayOfMonth(),
						cls.getYearMonth().v());
			});
		}

		if (approvalSettingOpt.isPresent() && approvalSettingOpt.get().getUseMonthApproverConfirm()) {
			// [No.602]月別の承認をクリアする
			approvalStatusAdapter
					.deleteRootConfirmMonth(employeeId,
							lstClosureAll.stream()
									.map(cls -> new ConfirmDeleteParamImport(cls.getYearMonth(),
											cls.getClosureId().value, cls.getClosureDate()))
									.collect(Collectors.toList()));
		}

	}

	@SuppressWarnings("unchecked")
	private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> {
			final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());

			return seen.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}
}
