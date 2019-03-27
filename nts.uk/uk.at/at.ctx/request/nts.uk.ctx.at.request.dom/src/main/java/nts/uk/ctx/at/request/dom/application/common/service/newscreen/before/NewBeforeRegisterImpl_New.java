package nts.uk.ctx.at.request.dom.application.common.service.newscreen.before;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.PesionInforImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.actuallock.ActualLockAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.actuallock.ActualLockImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.workfixed.WorkFixedAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.workrecord.identificationstatus.IdentificationAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.daycalendar.ObtainDeadlineDateAdapter;
//import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalStatusForEmployeeImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApproveRootStatusForEmpImPort;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.applimitset.AppLimitSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.ReceptionRestrictionSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadline;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadlineRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.DeadlineCriteria;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AllowAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.CheckMethod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class NewBeforeRegisterImpl_New implements NewBeforeRegister_New {
	
	private static final String DATE_FORMAT = "yyyy/MM/dd";
	
	@Inject
	private EmployeeRequestAdapter employeeAdaptor;
	
	@Inject
	private ApplicationDeadlineRepository appDeadlineRepository;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithmService;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
//	@Inject
//	private ApprovalRootAdapter approvalRootService;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private WorkplaceAdapter workplaceAdapter;
	
	@Inject
	private ObtainDeadlineDateAdapter obtainDeadlineDateAdapter;
	@Inject
	private RequestSettingRepository requestSettingRepository;
	@Inject
	private ActualLockAdapter actualLockAdapter;
	@Inject
	private WorkFixedAdapter workFixedAdater;
	
	@Inject
	private IdentificationAdapter identificationAdapter;
	
	public void processBeforeRegister(Application_New application, int overTimeAtr, boolean checkOver1Year){
		// アルゴリズム「未入社前チェック」を実施する
		retirementCheckBeforeJoinCompany(application.getCompanyID(), application.getEmployeeID(), application.getAppDate());
		
		// アルゴリズム「社員の当月の期間を算出する」を実行する
		PeriodCurrentMonth periodCurrentMonth = otherCommonAlgorithmService.employeePeriodCurrentMonthCalculate(application.getCompanyID(), application.getEmployeeID(), application.getAppDate());
		
		GeneralDate startDate = application.getAppDate();
		GeneralDate endDate = application.getAppDate();
		if (application.getStartDate().isPresent() && application.getEndDate().isPresent()) {
			startDate = application.getStartDate().get();
			endDate = application.getEndDate().get();
			
			// 登録する期間のチェック
			//((TimeSpan)(申請する終了日 - 申請する開始日)).Days > 31がtrue
			if((ChronoUnit.DAYS.between(startDate.localDate(), endDate.localDate()) + 1)  > 31){
				throw new BusinessException("Msg_277");
			}
			// 登録可能期間のチェック(１年以内)
			//EA修正履歴 No.3210
			//hoatt 2019.03.22
			if(periodCurrentMonth.getStartDate().addYears(1).beforeOrEquals(endDate) && checkOver1Year) {
				//締め期間．開始年月日.AddYears(1) <= 申請する終了日がtrue
				//確認メッセージ（Msg_1518）を表示する
				throw new BusinessException("Msg_1518", periodCurrentMonth.getStartDate().addYears(1).toString(DATE_FORMAT));
			}
			
			// 過去月のチェック
			if(startDate.before(periodCurrentMonth.getStartDate())) {
				throw new BusinessException("Msg_236");			
			}
		}		
		
		// キャッシュから承認ルートを取得する(Lấy comfirm root từ cache)	
		ApprovalRootContentImport_New approvalRootContentImport = approvalRootStateAdapter.getApprovalRootContent(
				application.getCompanyID(), 
				application.getEmployeeID(), 
				application.getAppType().value, 
				application.getAppDate(), 
				application.getAppID(),
				true);
		switch (approvalRootContentImport.getErrorFlag()) {
		case NO_CONFIRM_PERSON:
			throw new BusinessException("Msg_238");
		case APPROVER_UP_10:
			throw new BusinessException("Msg_237");
		case NO_APPROVER:
			throw new BusinessException("Msg_324");
		default:
			break;
		}
		
		// アルゴリズム「申請の締め切り期限をチェック」を実施する
		SEmpHistImport empHistImport = employeeAdaptor.getEmpHist(application.getCompanyID(), application.getEmployeeID(), application.getAppDate());
		if(empHistImport==null || empHistImport.getEmploymentCode()==null){
			throw new BusinessException("Msg_426");
		}
		String employmentCD = empHistImport.getEmploymentCode();
		Optional<ClosureEmployment> closureEmployment = closureEmploymentRepository.findByEmploymentCD(application.getCompanyID(), employmentCD);
		if(!closureEmployment.isPresent()){
			throw new RuntimeException("Not found ClosureEmployment in table KCLMT_CLOSURE_EMPLOYMENT, employment =" + employmentCD);
		}
		deadlineApplicationCheck(application.getCompanyID(), closureEmployment.get().getClosureId(), application.getEmployeeID(),
				periodCurrentMonth.getStartDate(), periodCurrentMonth.getEndDate(), startDate, endDate);
		
		// アルゴリズム「申請の受付制限をチェック」を実施する
		applicationAcceptanceRestrictionsCheck(application.getCompanyID(), application.getAppType(), application.getPrePostAtr(), startDate, endDate,overTimeAtr);
		// 申請する開始日～申請する終了日までループする
		for(GeneralDate loopDate = startDate; loopDate.beforeOrEquals(endDate); loopDate = loopDate.addDays(1)){
			if(loopDate.equals(GeneralDate.today()) && application.getPrePostAtr().equals(PrePostAtr.PREDICT) && application.isAppOverTime()){
				confirmCheckOvertime(application.getCompanyID(), application.getEmployeeID(), loopDate);
			}else{
				// アルゴリズム「確定チェック」を実施する
				confirmationCheck(application.getCompanyID(), application.getEmployeeID(), loopDate);
			}
		}
	}
	
	// moi nguoi chi co the o mot cty vao mot thoi diem
	// check xem nguoi xin con trong cty k
	public void retirementCheckBeforeJoinCompany(String companyID, String employeeID, GeneralDate date){
		// Imported(就業)「社員」を取得する
		PesionInforImport pesionInforImport = employeeAdaptor.getEmployeeInfor(employeeID);
		// 入社前をチェックする
		// データが１件以上取得できた 且つ 申請対象日 < Imported(就業)「社員」．入社年月日
		if(pesionInforImport.getEntryDate() != null && date.before(pesionInforImport.getEntryDate())) {
			throw new BusinessException("Msg_235");
		}
		
		// 退職後をチェックする
		// データが１件以上取得できた 且つ 申請対象日 > Imported(就業)「社員」．退職年月日
		if(pesionInforImport.getRetiredDate() != null && date.after(pesionInforImport.getRetiredDate())) {
			throw new BusinessException("Msg_391");
		}
	}
	
	public void deadlineApplicationCheck(String companyID, Integer closureID, String employeeID, 
			GeneralDate deadlineStartDate, GeneralDate deadlineEndDate, GeneralDate appStartDate, GeneralDate appEndDate){
		/*ログイン者のパスワードレベルが０の場合、チェックしない
		ロールが決まったら、要追加*/
		// if(passwordLevel!=0) return;
		
		// ドメインモデル「申請締切設定」．利用区分をチェックする(check利用区分)
		Optional<ApplicationDeadline> appDeadlineOp = appDeadlineRepository.getDeadlineByClosureId(companyID, closureID);
		if(!appDeadlineOp.isPresent()) {
			throw new RuntimeException("Not found ApplicationDeadline in table KRQST_APP_DEADLINE, closureID =" + closureID);
		}
		ApplicationDeadline appDeadline = appDeadlineOp.get();
		
		GeneralDate systemDate = GeneralDate.today();
		// ドメインモデル「申請締切設定」．利用区分をチェックする(check利用区分)
		if(appDeadline.getUserAtr().equals(UseAtr.NOTUSE)) { 
			return; 
		};
		
		// 申請する開始日(input)から申請する終了日(input)までループする
		for(int i = 0; appStartDate.compareTo(appEndDate) + i <= 0; i++){
			GeneralDate loopDate = appStartDate.addDays(i);
			if(loopDate.after(deadlineEndDate)){
				continue;
			}
			GeneralDate deadline = null;
			// ドメインモデル「申請締切設定」．締切基準をチェックする
			if(appDeadline.getDeadlineCriteria().equals(DeadlineCriteria.WORKING_DAY)) {
				// アルゴリズム「社員所属職場履歴を取得」を実行する
				WkpHistImport wkpHistImport = workplaceAdapter.findWkpBySid(employeeID, systemDate);
				// アルゴリズム「締切日を取得する」を実行する
				deadline = obtainDeadlineDateAdapter.obtainDeadlineDate(
						deadlineEndDate, 
						appDeadline.getDeadline().v(), 
						wkpHistImport.getWorkplaceId(), 
						companyID);
			} else {
				deadline = deadlineEndDate.addDays(appDeadline.getDeadline().v());
			}
			// システム日付と申請締め切り日を比較する
			if(systemDate.after(deadline)) {
				throw new BusinessException("Msg_327", deadline.toString(DATE_FORMAT)); 
			}
		}	
	}
	
	public void applicationAcceptanceRestrictionsCheck(String companyID, ApplicationType appType, PrePostAtr postAtr, GeneralDate startDate, GeneralDate endDate,int overTimeAtr){
		/*ログイン者のパスワードレベルが０の場合、チェックしない
		ロールが決まったら、要追加*/
		// if(passwordLevel!=0) return;
		GeneralDateTime systemDateTime = GeneralDateTime.now();
		GeneralDate systemDate = GeneralDate.today();
		
		// キャッシュから取得
		Optional<AppTypeDiscreteSetting> appTypeDiscreteSettingOp = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(companyID, appType.value);
		if(!appTypeDiscreteSettingOp.isPresent()) {
			throw new RuntimeException("Not found AppTypeDiscreteSetting in table KRQST_APP_TYPE_DISCRETE, appType =" +  appType);
		}
		Optional<RequestSetting> requestSetting = this.requestSettingRepository.findByCompany(companyID);
		List<ReceptionRestrictionSetting> receptionRestrictionSetting = new ArrayList<>();
		if(requestSetting.isPresent()){
			receptionRestrictionSetting = requestSetting.get().getApplicationSetting().getListReceptionRestrictionSetting().stream().filter(x -> x.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION)).collect(Collectors.toList());
		}
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingOp.get();
		
		// 事前事後区分(input)をチェックする
		if(postAtr.equals(PrePostAtr.POSTERIOR)){
			// ドメインモデル「事後の受付制限」．未来日許可しないをチェックする
			if (!appTypeDiscreteSetting.getRetrictPostAllowFutureFlg().equals(AllowAtr.ALLOW)) {
				return;
			}
			// 未来日の事後申請かチェックする
			if (startDate.after(systemDate) || endDate.after(systemDate)) {
				throw new BusinessException("Msg_328");
			} 
		} else {
			// ドメインモデル「事前の受付制限」．利用区分をチェックする
			if(appTypeDiscreteSetting.getRetrictPreUseFlg().equals(UseAtr.NOTUSE)){
				return;
			}
			// 申請する開始日(input)から申請する終了日(input)までループする
			for(int i = 0; startDate.compareTo(endDate) + i <= 0; i++){
				GeneralDate loopDay = startDate.addDays(i);
				// ドメインモデル「事前の受付制限」．チェック方法をチェックする
				if(appTypeDiscreteSetting.getRetrictPreMethodFlg().equals(CheckMethod.DAYCHECK)){
					// ループする日と受付制限日と比較する
					GeneralDate limitDay = systemDate.addDays(appTypeDiscreteSetting.getRetrictPreDay().value);
					if(loopDay.before(limitDay)) {
						throw new BusinessException("Msg_327", loopDay.toString(DATE_FORMAT));
					}
				} else {
					if(appType.equals(ApplicationType.OVER_TIME_APPLICATION)){
						// ループする日とシステム日付を比較する
						if(loopDay.before(systemDate)){
							throw new BusinessException("Msg_327", loopDay.toString(DATE_FORMAT));
						} else if(loopDay.equals(systemDate)){
							Integer systemTime = systemDateTime.hours() * 60 + systemDateTime.minutes();
							int resultCompare = 0;
							if(overTimeAtr == 0 && receptionRestrictionSetting.get(0).getBeforehandRestriction().getPreOtTime() != null){
								resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getPreOtTime().v());
							}else if(overTimeAtr == 1 && receptionRestrictionSetting.get(0).getBeforehandRestriction().getNormalOtTime() !=  null){
								resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getNormalOtTime().v());
							}else if(overTimeAtr == 2 && receptionRestrictionSetting.get(0).getBeforehandRestriction().getTimeBeforehandRestriction() !=  null){
								resultCompare = systemTime.compareTo(receptionRestrictionSetting.get(0).getBeforehandRestriction().getTimeBeforehandRestriction().v());
							}
							// システム日時と受付制限日時と比較する
							if(resultCompare == 1) {
								throw new BusinessException("Msg_327", loopDay.toString(DATE_FORMAT));
							}
						}
					}
				}
			}
		
		}
	}
	
	public void confirmationCheck(String companyID, String employeeID, GeneralDate appDate){
		// アルゴリズム「申請の締め切り期限をチェック」を実施する
		SEmpHistImport empHistImport = employeeAdaptor.getEmpHist(companyID,
				employeeID, appDate);
		if (empHistImport == null || empHistImport.getEmploymentCode() == null) {
			throw new BusinessException("Msg_426");
		}
		String employmentCD = empHistImport.getEmploymentCode();
		Optional<ClosureEmployment> closureEmployment = closureEmploymentRepository
				.findByEmploymentCD(companyID, employmentCD);
		if (!closureEmployment.isPresent()) {
			throw new RuntimeException(
					"Not found ClosureEmployment in table KCLMT_CLOSURE_EMPLOYMENT, employment =" + employmentCD);
		}
		
		Optional<ActualLockImport> actualLockImport = this.actualLockAdapter.findByID(companyID,
				closureEmployment.get().getClosureId());
		
		Optional<RequestSetting> requestSetting = this.requestSettingRepository.findByCompany(companyID);
		if (!requestSetting.isPresent()) {
			return;
		}
		ApplicationSetting applicationSetting = requestSetting.get().getApplicationSetting();
		AppLimitSetting appLimitSetting = applicationSetting.getAppLimitSetting();
		// ドメインモデル「申請制限設定」．日別実績が確認済なら申請できないをチェックする(check domain 「申請制限設定」．日別実績が確認済なら申請できない)
		if (!appLimitSetting.getCanAppAchievementConfirm()) {
			List<ApproveRootStatusForEmpImPort> approveRootStatus = Collections.emptyList();
			try {
				approveRootStatus = this.approvalRootStateAdapter.getApprovalByEmplAndDate(appDate, appDate, employeeID, companyID, 1);
			} catch (Exception e) {
				approveRootStatus = Collections.emptyList();
			}
			if(CollectionUtil.isEmpty(approveRootStatus)){
				return;
			}
			boolean isConfirm = false;
			for(ApproveRootStatusForEmpImPort approve : approveRootStatus){
				if(approve.getApprovalStatus().equals(ApprovalStatusForEmployeeImport.APPROVED)){
					isConfirm = true;
					break;
				}
			}
			//「Imported(申請承認)「実績確定状態」．日別実績が確認済をチェックする(check 「Imported(申請承認)「実績確定状態」．日別実績が確認済)
			if(isConfirm){
				throw new BusinessException("Msg_448");
			}
			List<GeneralDate> identificationDateLst = identificationAdapter.getProcessingYMD(companyID, employeeID, new DatePeriod(appDate, appDate));
			if(!CollectionUtil.isEmpty(identificationDateLst)){
				throw new BusinessException("Msg_448");
			}
		}
		confirmCheck(appLimitSetting,actualLockImport,appDate,companyID,employeeID,closureEmployment);
	}
	public void confirmCheckOvertime(String companyID, String employeeID, GeneralDate appDate){
		// アルゴリズム「申請の締め切り期限をチェック」を実施する
				SEmpHistImport empHistImport = employeeAdaptor.getEmpHist(companyID,
						employeeID, appDate);
				if (empHistImport == null || empHistImport.getEmploymentCode() == null) {
					throw new BusinessException("Msg_426");
				}
				String employmentCD = empHistImport.getEmploymentCode();
				Optional<ClosureEmployment> closureEmployment = closureEmploymentRepository
						.findByEmploymentCD(companyID, employmentCD);
				if (!closureEmployment.isPresent()) {
					throw new RuntimeException(
							"Not found ClosureEmployment in table KCLMT_CLOSURE_EMPLOYMENT, employment =" + employmentCD);
				}
				Optional<ActualLockImport> actualLockImport = this.actualLockAdapter.findByID(companyID,
						closureEmployment.get().getClosureId());
				
				
				Optional<RequestSetting> requestSetting = this.requestSettingRepository.findByCompany(companyID);
				if (!requestSetting.isPresent()) {
					return;
				}
				ApplicationSetting applicationSetting = requestSetting.get().getApplicationSetting();
				AppLimitSetting appLimitSetting = applicationSetting.getAppLimitSetting();
				confirmCheck(appLimitSetting,actualLockImport,appDate,companyID,employeeID,closureEmployment);
	}
	private void confirmCheck(AppLimitSetting appLimitSetting, Optional<ActualLockImport> actualLockImport,
			GeneralDate appDate, String companyID, String employeeID,Optional<ClosureEmployment> closureEmployment) {
		// ドメインモデル「申請制限設定」．月別実績が確認済なら申請できないをチェックする(check domain
		// 「申請制限設定」．月別実績が確認済なら申請できない)
		if (!appLimitSetting.getCanAppAchievementMonthConfirm()) {
			// 「Imported(申請承認)「実績確定状態」．月別実績が確認済をチェックする(check
			// 「Imported(申請承認)「実績確定状態」．月別実績が確認済)
			List<ApproveRootStatusForEmpImPort> approveRootStatus = Collections.emptyList();
			try {
				approveRootStatus = this.approvalRootStateAdapter.getApprovalByEmplAndDate(appDate, appDate, employeeID, companyID, 2);
			} catch (Exception e) {
				approveRootStatus = Collections.emptyList();
			}
			if(CollectionUtil.isEmpty(approveRootStatus)){
				return;
			}
			boolean isConfirm = false;
			for(ApproveRootStatusForEmpImPort approve : approveRootStatus){
				if(approve.getApprovalStatus().equals(ApprovalStatusForEmployeeImport.APPROVED)){
					isConfirm = true;
					break;
				}
			}
			if (isConfirm) {
				throw new BusinessException("Msg_449");
			}
		}
		// ドメインモデル「申請制限設定」．就業確定済の場合申請できないをチェックする(check domain
		// 「申請制限設定」．就業確定済の場合申請できない)
		if (!appLimitSetting.getCanAppFinishWork()) {
			GeneralDate systemDate = GeneralDate.today();
			WkpHistImport wkpHistImport = workplaceAdapter.findWkpBySid(employeeID, systemDate);
			if(wkpHistImport == null){
				throw new RuntimeException("Not found workplaceID");
			}
			// 「Imported(申請承認)「実績確定状態」．所属職場の就業確定区分をチェックする(check
			// 「Imported(申請承認)「実績確定状態」．所属職場の就業確定区分)
			if (this.workFixedAdater.getEmploymentFixedStatus(companyID, appDate, wkpHistImport.getWorkplaceId(),
					closureEmployment.get().getClosureId())) {
				throw new BusinessException("Msg_450");
			}
		}
		if (!actualLockImport.isPresent()) {
			return;
		}
		// ドメインモデル「申請制限設定」．実績修正がロック状態なら申請できないをチェックする(check domain
		// 「申請制限設定」．実績修正がロック状態なら申請できない)
		if (!appLimitSetting.getCanAppAchievementLock()) {
			// 4.社員の当月の期間を算出する
			PeriodCurrentMonth periodCurrentMonth = this.otherCommonAlgorithmService
					.employeePeriodCurrentMonthCalculate(companyID, employeeID, appDate);
			if (appDate.afterOrEquals(periodCurrentMonth.getStartDate())
					&& appDate.beforeOrEquals(periodCurrentMonth.getEndDate())) {
				if (actualLockImport.get().getDailyLockState() == 1
						|| actualLockImport.get().getMonthlyLockState() == 1) {
					throw new BusinessException("Msg_451");
				}
			}
		}
	}
}
