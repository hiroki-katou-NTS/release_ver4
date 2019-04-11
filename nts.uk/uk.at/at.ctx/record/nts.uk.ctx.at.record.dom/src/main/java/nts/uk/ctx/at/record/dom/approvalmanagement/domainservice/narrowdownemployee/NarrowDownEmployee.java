package nts.uk.ctx.at.record.dom.approvalmanagement.domainservice.narrowdownemployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
/**
 * 画面に表示する社員に絞り込む (KTG030 use)
 * @author tutk
 *
 */
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.RegularSortingTypeImport;
import nts.uk.ctx.at.record.dom.adapter.employee.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.SortingConditionOrderImport;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootSituation;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.OneMonthApprovalStatusDto;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.AppEmpStatusImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.ApprovalStatusImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.RouteSituationImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class NarrowDownEmployee {

	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepository;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;

	@Inject
	private RegulationInfoEmployeeAdapter regulationInfoEmployeeAdapter;

	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;


	@Inject
	private EmploymentHistAdapter employmentHistAdapter;

	public Optional<AppEmpStatusImport> narrowDownEmployee(ApprovalRootOfEmployeeImport approvalRootOfEmployeeImport,
			Integer currentClosure, YearMonth yearMonth) {
		DatePeriod datePeriod = DatePeriod.daysFirstToLastIn(yearMonth);
		List<Identification> listIdentification = new ArrayList<>();
		OneMonthApprovalStatusDto oneMonthApprovalStatusDto = new OneMonthApprovalStatusDto();
		// 対応するドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalProcUseSet = approvalProcessingUseSettingRepository
				.findByCompanyId(AppContexts.user().companyId());
		ApprovalRootOfEmployeeImport approvalRootOfEmployeeImportTemp = new ApprovalRootOfEmployeeImport();
		// 取得したドメインモデル「承認処理の利用設定．日の承認者確認を利用する」をチェックする
		boolean useDayApprovalComfirmCheck = false;
		if (approvalProcUseSet.isPresent()) {
			useDayApprovalComfirmCheck = approvalProcUseSet.get().getUseDayApproverConfirm();
		}

		if (useDayApprovalComfirmCheck) {

			oneMonthApprovalStatusDto.setStartDate(datePeriod.start());
			oneMonthApprovalStatusDto.setEndDate(datePeriod.end());

			List<ApprovalRootSituation> approvalRootSituations = new ArrayList<>();
			List<String> lstEmploymentCd = new ArrayList<>();
			// fix bug 91363
			List<String> lstEmployees = new ArrayList<>();

			List<ApprovalRootSituation> listApp = approvalRootOfEmployeeImport.getApprovalRootSituations();
			Set<String> listAppId = approvalRootOfEmployeeImport.getApprovalRootSituations().stream()
					.map(c -> c.getTargetID()).collect(Collectors.toSet());
			// 対象期間に在職しているかチェックする
			// 社員ID（List）と指定期間から所属会社履歴項目を取得 【Request：No211】
			List<AffCompanyHistImport> listAffCompanyHistImport = this.syCompanyRecordAdapter
					.getAffCompanyHistByEmployee(new ArrayList<>(listAppId), datePeriod);
			if (listAffCompanyHistImport.isEmpty() || listAffCompanyHistImport.stream()
					.flatMap(x -> x.getLstAffComHistItem().stream()).collect(Collectors.toList()).isEmpty()) {
				oneMonthApprovalStatusDto.setMessageID("Msg_875");
				return Optional.empty();
			}
			// 日の本人確認を取得する
			listIdentification.addAll(this.getIdentification(new ArrayList<>(listAppId), datePeriod));
			// 取得した「所属会社履歴項目」に当てはまらない対象者、対象日の「ルート状況」を取り除く
			for (String approvalId : listAppId) {
				// loop find approvalID
				for (AffCompanyHistImport affCompanyHistImport : listAffCompanyHistImport) {

					if (approvalId.equals(affCompanyHistImport.getEmployeeId())) {
						List<GeneralDate> listDate = new ArrayList<>();
						// loop list period
						List<AffComHistItemImport> listAffComHistItemImport = affCompanyHistImport
								.getLstAffComHistItem();
						for (AffComHistItemImport affComHistItem : listAffComHistItemImport) {
							GeneralDate startDate = affComHistItem.getDatePeriod().start();
							GeneralDate endDate = affComHistItem.getDatePeriod().end();
							if (startDate.after(datePeriod.end()) || endDate.before(datePeriod.start())) {
								break;
							}
							GeneralDate dateS = datePeriod.start();
							GeneralDate dateE = datePeriod.end();
							if (startDate.afterOrEquals(datePeriod.start())) {
								dateS = startDate;
							}
							if (endDate.beforeOrEquals(datePeriod.end())) {
								dateE = endDate;
							}

							for (GeneralDate date = dateS; date.beforeOrEquals(dateE);) {
								listDate.add(date);
								date = date.addDays(1);
							}
						}
						// get item by date and emp id
						for (ApprovalRootSituation approval : listApp) {
							for (GeneralDate date : listDate) {
								if (approval.getTargetID().equals(approvalId) && approval.getAppDate().equals(date)) {
									approvalRootSituations.add(approval);
								}
							}
						}
						break;
					}
				}
			}

			approvalRootOfEmployeeImport.setApprovalRootSituations(approvalRootSituations);

			// クエリ「社員を並び替える(任意)」を実行する (Sort employee)
			String companyId = AppContexts.user().companyId();
			List<String> employeeList = approvalRootOfEmployeeImport.getApprovalRootSituations().stream().map(item -> {
				return item.getTargetID();
			}).collect(Collectors.toList());
			// list order conditions
			lstEmployees = this.regulationInfoEmployeeAdapter.sortEmployees(companyId, employeeList,
					this.createListConditions(), this.convertFromDateToDateTime(datePeriod.end()));

			List<ClosureEmployment> lstClosureEmployment = closureEmploymentRepository
					.findByClosureId(AppContexts.user().companyId(), currentClosure);
			lstEmploymentCd.addAll(lstClosureEmployment.stream().map(closureEmployment -> {
				return closureEmployment.getEmploymentCD();
			}).collect(Collectors.toList()));

			// 対象期間に対象の締めに紐付いた雇用に属しているかチェックする
			List<EmploymentHistImport> lstEmpHist = employmentHistAdapter.findBySidDatePeriod(lstEmployees, datePeriod)
					.stream().filter(x -> lstEmploymentCd.contains(x.getEmploymentCode())).collect(Collectors.toList());
			approvalRootOfEmployeeImportTemp = new ApprovalRootOfEmployeeImport(
					approvalRootOfEmployeeImport.getEmployeeStandard(), approvalRootSituations);
			List<ApprovalRootSituation> approvalRootSituationsTemp = new ArrayList<>();
			approvalRootOfEmployeeImport.getApprovalRootSituations().stream().forEach(x -> {
				val lstFilter = lstEmpHist.stream().filter(y -> {
					return y.getEmployeeId().equals(x.getTargetID())
							&& x.getAppDate().afterOrEquals(y.getPeriod().start())
							&& x.getAppDate().beforeOrEquals(y.getPeriod().end());
				}).collect(Collectors.toList());
				if (!lstFilter.isEmpty()) {
					approvalRootSituationsTemp.add(x);
				}
			});
			approvalRootOfEmployeeImportTemp.setApprovalRootSituations(approvalRootSituationsTemp);

		} else {
			return Optional.empty();
		}

		return Optional.of(convertToApprovalRoot(approvalRootOfEmployeeImportTemp));
	}

	@Inject
	private IdentificationRepository identificationRepo;

	/** 日の本人確認を取得する */
	public List<Identification> getIdentification(List<String> employeeId, DatePeriod datePeriod) {
		List<Identification> listIdentificationRepo = identificationRepo.findByListEmployeeID(employeeId,
				datePeriod.start(), datePeriod.end());
		return listIdentificationRepo;
	}

	// 並び替え条件 =<職場(inlevel)、1>、<分類コード(ASC)、2>、<職位(序列)、3>、<社員コード(ASC)、4>
	private List<SortingConditionOrderImport> createListConditions() {
		List<SortingConditionOrderImport> lstCondition = new ArrayList<>();
		lstCondition.add(new SortingConditionOrderImport(1, RegularSortingTypeImport.WORKPLACE));
		lstCondition.add(new SortingConditionOrderImport(2, RegularSortingTypeImport.CLASSIFICATION));
		lstCondition.add(new SortingConditionOrderImport(3, RegularSortingTypeImport.POSITION));
		// fix bug 101289
		// lstCondition.add(new
		// SortingConditionOrderImport(4,RegularSortingTypeImport.EMPLOYMENT));
		return lstCondition;
	}

	// convert from Date to DateTime
	private GeneralDateTime convertFromDateToDateTime(GeneralDate date) {
		return GeneralDateTime.ymdhms(date.year(), date.month(), date.day(), 0, 0, 0);
	}

	private AppEmpStatusImport convertToApprovalRoot(ApprovalRootOfEmployeeImport dto) {
		return new AppEmpStatusImport(dto.getEmployeeStandard(), dto.getApprovalRootSituations().stream()
				.map(c -> convertToApprovalRoot(c)).collect(Collectors.toList()));
	}

	private RouteSituationImport convertToApprovalRoot(ApprovalRootSituation dto) {
		return new RouteSituationImport(dto.getAppDate(), dto.getTargetID(), dto.getApprovalAtr().value,
				dto.getApprovalStatus() == null ? Optional.empty()
						: Optional.of(new ApprovalStatusImport(dto.getApprovalStatus().getReleaseDivision().value,
								dto.getApprovalStatus().getApprovalActionByEmpl().value)));
	}
}
