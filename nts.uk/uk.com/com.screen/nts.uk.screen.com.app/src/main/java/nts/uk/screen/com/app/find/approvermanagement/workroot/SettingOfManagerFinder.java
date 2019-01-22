package nts.uk.screen.com.app.find.approvermanagement.workroot;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Value;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;
import nts.uk.ctx.sys.auth.pub.role.RoleExport;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.HistoryCmm053Command;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ManagerSettingDto;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.PersonImport;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSetting;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class SettingOfManagerFinder {
	/** The work time hist repo. */
	@Inject
	private ClosureRepository closureRepo;

	/** The Closure service. */
	@Inject
	private ClosureService closureService;

	@Inject
	private PersonApprovalRootRepository personAppRootRepo;

	@Inject
	private ApprovalPhaseRepository appPhaseRepo;

	@Inject
	private EmployeeAdapter employeeAdapter;
	
	@Inject
	private ApprovalSettingRepository appSetRepo;
	
	@Inject
	private RoleExportRepo roleExportRepo;

	/**
	 * Get 上司の設定
	 * 
	 * @param employeeId
	 * @return
	 */
	public ManagerSettingDto getPsAppRootBySettingOfManager(String employeeId) {
		String companyId             = AppContexts.user().companyId();
		String loginId               = AppContexts.user().employeeId();
		GeneralDate startDate        = null;
		GeneralDate endDate          = null;
		GeneralDate startDateCommon  = null;
		GeneralDate endDateCommon    = null;
		GeneralDate startDateMonthly = null;
		GeneralDate endDateMonthly   = null;
		boolean isNewMode            = false;
		String departmentCode        = null;
		String departmentApproverId  = null;
		String departmentName        = null;
		String dailyApprovalCode     = null;
		String dailyApproverId       = null;
		String dailyApprovalName     = null;
		boolean hasAuthority         = true;
		boolean existCommonAppPhase  = false;
		boolean existMonthlyAppPhase = false;
		GeneralDate closingStartDate = null;
		SettingInfo commonSettingInfo  = null;
		SettingInfo monthlySettingInfo = null;
		boolean displayDailyApprover = false;

		// ドメインモデル「個人別就業承認ルート」を取得する
		Optional<PersonApprovalRoot> commonPsApp = this.personAppRootRepo.getNewestCommonPsAppRoot(companyId, employeeId);
		Optional<PersonApprovalRoot> monthlyPsApp = this.personAppRootRepo.getNewestMonthlyPsAppRoot(companyId, employeeId);

		if (commonPsApp.isPresent()) {
			commonSettingInfo = this.getSettingInfo(companyId, commonPsApp.get());
			startDateCommon     = commonSettingInfo.getStartDate();
			endDateCommon       = commonSettingInfo.getEndDate();
			startDate           = commonSettingInfo.getStartDate();
			endDate             = commonSettingInfo.getEndDate();
			dailyApprovalCode   = commonSettingInfo.getApprovalCode();
			dailyApproverId     = commonSettingInfo.getApproverId();
			dailyApprovalName   = commonSettingInfo.getApprovalName();
			existCommonAppPhase = commonSettingInfo.isExistAppPhase();
		}

		if (monthlyPsApp.isPresent()) {
			monthlySettingInfo = this.getSettingInfo(companyId, monthlyPsApp.get());
			startDateMonthly     = monthlySettingInfo.getStartDate();
			endDateMonthly       = monthlySettingInfo.getEndDate();
			startDate            = monthlySettingInfo.getStartDate();
			endDate              = monthlySettingInfo.getEndDate();
			departmentCode       = monthlySettingInfo.getApprovalCode();
			departmentApproverId = monthlySettingInfo.getApproverId();
			departmentName       = monthlySettingInfo.getApprovalName();
			existMonthlyAppPhase = monthlySettingInfo.isExistAppPhase();
		}

		if (commonPsApp.isPresent() && monthlyPsApp.isPresent()) {
			GeneralDate maxDate = GeneralDate.fromString(ConstantUtils.MAX_DATE, ConstantUtils.FORMAT_DATE_YYYYMMDD);

			// バラバラ履歴があります。
			if (startDateCommon.after(startDateMonthly)) {
				startDate = startDateCommon;
				endDate   = endDateCommon;
			}
			// 両方は同じ履歴があります。
			else {
				startDate = startDateMonthly;
				endDate   = endDateMonthly;
			}

			if (maxDate.equals(endDateCommon) && maxDate.equals(endDateMonthly)) {
				dailyApprovalCode    = commonSettingInfo.getApprovalCode();
				dailyApproverId      = commonSettingInfo.getApproverId();
				dailyApprovalName    = commonSettingInfo.getApprovalName();
				departmentCode       = monthlySettingInfo.getApprovalCode();
				departmentApproverId = monthlySettingInfo.getApproverId();
				departmentName       = monthlySettingInfo.getApprovalName();
			} else {
				if (startDateCommon.after(startDateMonthly)) {
					departmentCode       = null;
					departmentApproverId = null;
					departmentName       = null;
				} else {
					dailyApprovalCode = null;
					dailyApproverId   = null;
					dailyApprovalName = null;
				}
			}
		}
		// 両方がない。
		else if (!commonPsApp.isPresent() && !monthlyPsApp.isPresent()) {
			isNewMode = true;
		}

		// 履歴がありますが承認フェーズ1がありません。
		if (!existMonthlyAppPhase && !existCommonAppPhase) {
			isNewMode = true;
		}

		// ドメインモデル「締め」を取得する
		Optional<GeneralDate> closureStartDate = this.getClosureStartDate(companyId, 1);
		if(closureStartDate.isPresent()){
			closingStartDate = closureStartDate.get();
		}

		// 履歴の開始日がないの場合基準日は締めの開始日です。
		GeneralDate baseDate = isNewMode ? closingStartDate : startDate;

		// ログイン者の承認権限を取得する
		hasAuthority         = this.employeeAdapter.canApprovalOnBaseDate(companyId, loginId, baseDate);
		
		// ロールを取得する
		Optional<RoleExport> opRoleExport = roleExportRepo.findByRoleId(AppContexts.user().roles().forAttendance());
		if(opRoleExport.isPresent()){
			displayDailyApprover = true;
		} else {
			if(opRoleExport.get().getEmployeeReferenceRange()==3){
				displayDailyApprover = false;
			} else {
				displayDailyApprover = true;
			}
		}

		return new ManagerSettingDto(startDate, endDate, isNewMode, departmentCode, departmentApproverId,
				departmentName, dailyApprovalCode, dailyApproverId, dailyApprovalName, hasAuthority, closingStartDate, displayDailyApprover);
	}

	/**
	 * 処理年月と締め期間を取得する
	 * 
	 * @param cId
	 * @param closureId
	 * @return
	 */
	private Optional<GeneralDate> getClosureStartDate(String companyId, int closureId) {
		Optional<Closure> optClosure = closureRepo.findById(companyId, closureId);

		// Check exist and active
		if (!optClosure.isPresent()
				|| optClosure.get().getUseClassification().equals(UseClassification.UseClass_NotUse)) {
			return Optional.empty();
		}

		Closure closure = optClosure.get();

		// Get Processing Ym 処理年月
		YearMonth processingYm = closure.getClosureMonth().getProcessingYm();

		DatePeriod closurePeriod = closureService.getClosurePeriod(closureId, processingYm);
		
		return Optional.of(closurePeriod.start());
	}
	
	/**
	 * Get setting information
	 * @param psAppRoot
	 * @return
	 */
	private SettingInfo getSettingInfo(String companyId, PersonApprovalRoot psAppRoot){
		GeneralDate startDate = psAppRoot.getEmploymentAppHistoryItems().get(0).getDatePeriod().start();
		GeneralDate endDate   = psAppRoot.getEmploymentAppHistoryItems().get(0).getDatePeriod().end();
		boolean existAppPhase = false;
		String approvalCode = null;
		String approverId   = null;
		String approvalName = null;
		// ドメインモデル「承認フェーズ」を取得する
		Optional<ApprovalPhase> commonApprovalPhase = this.appPhaseRepo.getApprovalFirstPhase(companyId, psAppRoot.getBranchId());
		if (commonApprovalPhase.isPresent()) {
			commonApprovalPhase.get().getApprovers().sort((p1, p2) -> p1.getOrderNumber() - p2.getOrderNumber());
			// 「承認者」を取得する
			Approver firstApprover = commonApprovalPhase.get().getApprovers().get(0);
			// 履歴があります、承認フェーズ1がありますが承認者１が個人じゃなくて職位です。（履歴を表示しますが社員コードを表示しません）
			if (firstApprover.getApprovalAtr().value == 0) {
				PersonImport person = this.employeeAdapter.getEmployeeInformation(firstApprover.getEmployeeId());
				approvalCode = person.getEmployeeCode();
				approverId   = person.getSID();
				approvalName = person.getEmployeeName();
			}
			existAppPhase = true;
		}
		return new SettingInfo(startDate, endDate, approvalCode, approverId, approvalName, existAppPhase);
	}

	public void checkApprovalSetting(HistoryCmm053Command command) {
		// ドメインモデル「承認設定」．本人による承認をチェックする
		String companyId = AppContexts.user().companyId();
		Optional<ApprovalSetting> appSetOpt = this.appSetRepo.getApprovalByComId(companyId);
		if (appSetOpt.isPresent()) {
			appSetOpt.ifPresent(appSet -> {
				if (appSet.getPrinFlg().equals(PrincipalApprovalFlg.NOT_PRINCIPAL)) {
					// ドメインモデル「承認設定」．本人による承認がfalse(domain 「承認設定」．本人による承認 = false)
					String Sid = command.getEmployeeId();
					String selectedID = command.getDepartmentApproverId();
					if (Sid.equals(selectedID)) {
						// 選択している社員ID（A1_6） ＝＝ 入力している社員ID（A2_7）
						throw new BusinessException("Msg_1487");
					}

				}

			});
		} else {
			throw new BusinessException("Not Found ApprovalSetting(承認設定) domain");
		}

	}
}


@Value
class SettingInfo {
	private GeneralDate startDate;
	private GeneralDate endDate;
	private String approvalCode;
	private String approverId;
	private String approvalName;
	private boolean existAppPhase;
}
