package nts.uk.ctx.at.request.app.command.application.holidayshipment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType_Old;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.PrePostAtr_Old;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.holidayshipment.ApplicationCombination;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveWorkingHour;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.WorkTime;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentWorkingHour;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.WorkTimeCode;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class UpdateHolidayShipmentCommandHandler extends CommandHandler<SaveHolidayShipmentCommand> {

	@Inject
	private SaveHolidayShipmentCommandHandler saveHanler;
	@Inject
	private DetailBeforeUpdate beforeRegisterRepo;
	@Inject
	private DetailAfterUpdate detailAfterUpdate;
	@Inject
	private ApplicationRepository_New appRepo;
	@Inject
	private AbsenceLeaveAppRepository absRepo;
	@Inject
	private RecruitmentAppRepository recRepo;
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	@Inject
	private WorkTypeRepository wkTypeRepo;

	@Override
	protected void handle(CommandHandlerContext<SaveHolidayShipmentCommand> context) {

		SaveHolidayShipmentCommand command = context.getCommand();
		String companyID = AppContexts.user().companyId();
		int comType = command.getComType();
		// アルゴリズム「振休振出申請の更新登録」を実行する
		updateApp(command, companyID, comType);

	}

	/**
	 * @param command
	 *            from client
	 * @return application after update
	 */
	private Application_New updateAbsDomain(SaveHolidayShipmentCommand command, String companyID, String appReason) {
		AbsenceLeaveAppCommand appCmd = command.getAbsCmd();

		Optional<Application_New> appLicationOpt = this.appRepo.findByID(companyID, appCmd.getAppID());
		if (appLicationOpt.isPresent()) {
			Application_New application = appLicationOpt.get();
			application.setAppReason(new AppReason(appReason));
			application.setVersion(command.getAppCmd().getAppVersion());
			this.appRepo.updateWithVersion(application);
			Optional<AbsenceLeaveApp> absAppOpt = this.absRepo.findByID(appCmd.getAppID());
			if (absAppOpt.isPresent()) {
				AbsenceLeaveApp absApp = absAppOpt.get();
				absApp.setWorkTimeCD(appCmd.getWkTimeCD());
				WkTimeCommand wkTime1 = appCmd.getWkTime1();
				absApp.setWorkTime1(new AbsenceLeaveWorkingHour(new WorkTime(wkTime1.getStartTime()),
						new WorkTime(wkTime1.getEndTime())));
//				WkTimeCommand wkTime2 = appCmd.getWkTime2();
//				absApp.setWorkTime2(new AbsenceLeaveWorkingHour(new WorkTime(wkTime2.getStartTime()),
//						new WorkTime(wkTime2.getEndTime())));
				absApp.setWorkTypeCD(new WorkTypeCode(appCmd.getWkTypeCD()));
				absApp.setChangeWorkHoursType(EnumAdaptor.valueOf(appCmd.getChangeWorkHoursType(), NotUseAtr.class));
				this.absRepo.update(absApp);

				return application;
			}

		}
		return null;

	}

	/**
	 * @param command
	 *            from client
	 * @return application after update
	 */
	private Application_New updateRecDomain(SaveHolidayShipmentCommand command, String companyID, String appReason) {
		RecruitmentAppCommand appCmd = command.getRecCmd();

		Optional<Application_New> absAppLicationOpt = this.appRepo.findByID(companyID, appCmd.getAppID());
		if (absAppLicationOpt.isPresent()) {
			Application_New application = absAppLicationOpt.get();
			application.setAppReason(new AppReason(appReason));
			application.setVersion(command.getAppCmd().getAppVersion());
			this.appRepo.updateWithVersion(application);
			Optional<RecruitmentApp> recAppOpt = this.recRepo.findByID(appCmd.getAppID());
			if (recAppOpt.isPresent()) {
				RecruitmentApp recApp = recAppOpt.get();
				recApp.setWorkTimeCD(new WorkTimeCode(appCmd.getWkTimeCD()));
				WkTimeCommand wkTime1 = appCmd.getWkTime1();
				recApp.setWorkTime1(new RecruitmentWorkingHour(new WorkTime(wkTime1.getStartTime()),
						EnumAdaptor.valueOf(wkTime1.getStartType(), NotUseAtr.class),
						new WorkTime(wkTime1.getEndTime()),
						EnumAdaptor.valueOf(wkTime1.getEndType(), NotUseAtr.class)));
//				WkTimeCommand wkTime2 = appCmd.getWkTime2();
//				recApp.setWorkTime2(new RecruitmentWorkingHour(new WorkTime(wkTime2.getStartTime()),
//						EnumAdaptor.valueOf(wkTime2.getStartType(), NotUseAtr.class),
//						new WorkTime(wkTime2.getEndTime()),
//						EnumAdaptor.valueOf(wkTime2.getEndType(), NotUseAtr.class)));
				recApp.setWorkTypeCD(new WorkTypeCode(appCmd.getWkTypeCD()));
				this.recRepo.update(recApp);

				return application;
			}

		}
		return null;

	}

	private void preRegisComonProcessing(String companyID, String employeeID, GeneralDate appDate, int rootAtr,
			ApplicationType_Old appType, int prePostAtr, String appID, Long appVer, String wkTypeCD, String wkTimeCD) {
		processBeforeRegOfDetailedScreen(companyID, employeeID, appDate, rootAtr, appType, prePostAtr, appID, appVer,
				wkTypeCD, wkTimeCD);

	}

	private void processBeforeRegOfDetailedScreen(String companyID, String employeeID, GeneralDate appDate, int rootAtr,
			ApplicationType_Old appType, int prePostAtr, String appID, Long appVer, String wkTypeCD, String wkTimeCD) {
		// error EA refactor 4
		/*beforeRegisterRepo.processBeforeDetailScreenRegistration(companyID, employeeID, appDate,
				EmploymentRootAtr.APPLICATION.value, appID, EnumAdaptor.valueOf(prePostAtr, PrePostAtr_Old.class),
				Long.valueOf(appVer), wkTypeCD, wkTimeCD);*/

	}

	public boolean isSaveRec(int comType) {
		if (comType == ApplicationCombination.RecAndAbs.value || comType == ApplicationCombination.Rec.value) {
			return true;
		}
		return false;
	}

	public boolean isSaveAbs(int comType) {
		if (comType == ApplicationCombination.RecAndAbs.value || comType == ApplicationCombination.Abs.value) {
			return true;
		}
		return false;
	}

	private void updateApp(SaveHolidayShipmentCommand command, String companyID, int comType) {
		// アルゴリズム「登録前エラーチェック（更新）」を実行する
		String appReason = errorCheckBeforeRegister(command, companyID, comType);
		//終日半日矛盾チェック
		//KAF011_Ver21
		checkDayConflict(command,comType);
		
		AbsenceLeaveAppCommand absCmd = command.getAbsCmd();
		RecruitmentAppCommand recCmd = command.getRecCmd();
		ApplicationType_Old appType = ApplicationType_Old.COMPLEMENT_LEAVE_APPLICATION;
		String employeeID = command.getAppCmd().getEmployeeID();
		if (isSaveRec(comType)) {
			// アルゴリズム「登録前共通処理（更新）」を実行する
			preRegisComonProcessing(companyID, employeeID, recCmd.getAppDate(), EmploymentRootAtr.APPLICATION.value,
					appType, command.getAppCmd().getPrePostAtr(), recCmd.getAppID(),
					command.getAppCmd().getAppVersion(),recCmd.getWkTypeCD(),recCmd.getWkTimeCD());

			// ドメイン「振出申請」を1件更新する
			Application_New recApp = updateRecDomain(command, companyID, appReason);
			// 暫定データの登録
			interimRemainDataMngRegisterDateChange.registerDateChange(companyID,
					recApp.getEmployeeID(),
					Arrays.asList(recCmd.getAppDate()));
			// アルゴリズム「詳細画面登録後の処理」を実行する
			if (recApp != null) {
				// error EA refactor 4
				/*this.detailAfterUpdate.processAfterDetailScreenRegistration(recApp);*/
			}
		}

		if (isSaveAbs(comType)) {
			// アルゴリズム「登録前共通処理（更新）」を実行する
			preRegisComonProcessing(companyID, employeeID, absCmd.getAppDate(), EmploymentRootAtr.APPLICATION.value,
					appType, command.getAppCmd().getPrePostAtr(), absCmd.getAppID(),
					command.getAppCmd().getAppVersion(), absCmd.getWkTypeCD(), absCmd.getWkTimeCD());
			// ドメイン「振休申請」を1件更新する
			Application_New absApp = updateAbsDomain(command, companyID, appReason);
			// 暫定データの登録
			interimRemainDataMngRegisterDateChange.registerDateChange(companyID,
					absApp.getEmployeeID(),
					Arrays.asList(absCmd.getAppDate()));
			// アルゴリズム「詳細画面登録後の処理」を実行する
			if (absApp != null) {
				// error EA refactor 4
				/*this.detailAfterUpdate.processAfterDetailScreenRegistration(absApp);*/
			}
		}

	}

	private String errorCheckBeforeRegister(SaveHolidayShipmentCommand command, String companyID, int comType) {
		ApplicationType_Old appType = ApplicationType_Old.COMPLEMENT_LEAVE_APPLICATION;
		// アルゴリズム「事前条件チェック」を実行する
		return saveHanler.preconditionCheck(command, companyID, appType, comType);

	}
	
	private void checkDayConflict(SaveHolidayShipmentCommand command, int comType) {
		if (saveHanler.isSaveBothApp(comType)) {
			// アルゴリズム「勤務種類別振休発生数の取得」を実行する takingout
			BigDecimal absDay = getByWorkType(command.getAbsCmd().getWkTypeCD(), WorkTypeClassification.Pause);

			// アルゴリズム「勤務種類別振休発生数の取得」を実行する holiday
			BigDecimal recDay = getByWorkType(command.getRecCmd().getWkTypeCD(), WorkTypeClassification.Shooting);

			boolean isBothDayNotZero = !(BigDecimal.valueOf(0).compareTo(absDay) == 0)
					&& !(BigDecimal.valueOf(0).compareTo(recDay) == 0);

			boolean isTwoDateNotSame = !(absDay.compareTo(recDay) == 0);

			if (isBothDayNotZero && isTwoDateNotSame) {

				throw new BusinessException("Msg_698");

			}
		}

	}
	
	private BigDecimal getByWorkType(String wkTypeCD, WorkTypeClassification wkTypeClass) {
		String companyID = AppContexts.user().companyId();
		Optional<WorkType> wkTypeOpt = wkTypeRepo.findByPK(companyID, wkTypeCD);
		BigDecimal result = new BigDecimal(0);
		if (wkTypeOpt.isPresent()) {
			WorkType wkType = wkTypeOpt.get();
			DailyWork dailyWk = wkType.getDailyWork();
			boolean isTypeUnitIsOneDay = dailyWk.getWorkTypeUnit().equals(WorkTypeUnit.OneDay);
			if (isTypeUnitIsOneDay) {

				if (dailyWk.getOneDay().equals(wkTypeClass)) {
					result = BigDecimal.valueOf(1);
				}

			}
			boolean isTypeUnitIsMorningAndAfterNoon = wkType.getDailyWork().getWorkTypeUnit()
					.equals(WorkTypeUnit.MonringAndAfternoon);
			if (isTypeUnitIsMorningAndAfterNoon) {
				if (dailyWk.getMorning().equals(wkTypeClass)) {
					result = result.add(BigDecimal.valueOf(0.5));
				} else {

				}
				if (dailyWk.getAfternoon().equals(wkTypeClass)) {
					result = result.add(BigDecimal.valueOf(0.5));
				} else {

				}

			}

		}
		return result;

	}

}
