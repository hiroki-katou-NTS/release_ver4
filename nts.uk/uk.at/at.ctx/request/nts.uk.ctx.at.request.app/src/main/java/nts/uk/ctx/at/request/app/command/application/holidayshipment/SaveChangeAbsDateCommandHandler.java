package nts.uk.ctx.at.request.app.command.application.holidayshipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.HolidayShipmentScreenAFinder;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.holidayshipment.ApplicationCombination;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.AppRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.EarchInterimRemainCheck;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCheckInputParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngCheckRegister;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class SaveChangeAbsDateCommandHandler
		extends CommandHandlerWithResult<SaveHolidayShipmentCommand, ProcessResult> {
	@Inject
	private SaveHolidayShipmentCommandHandler saveHanler;
	@Inject
	private CancelHolidayShipmentCommandHandler cancelHanler;
	@Inject
	private ApplicationApprovalService_New appImp;
	@Inject
	private AbsenceLeaveAppRepository absRepo;
	@Inject
	private ApplicationRepository_New appRepo;
	@Inject
	private NewAfterRegister_New newAfterReg;
	@Inject
	private OtherCommonAlgorithm ortherAl;
	@Inject
	private HdAppSetRepository repoHdAppSet;
	@Inject
	private InterimRemainDataMngCheckRegister checkRegister;
	@Inject
	private HolidayShipmentScreenAFinder afinder;
	@Inject
	private InterimRemainDataMngRegisterDateChange registerDateChange;

	@Override
	protected ProcessResult handle(CommandHandlerContext<SaveHolidayShipmentCommand> context) {
		SaveHolidayShipmentCommand command = context.getCommand();
		AbsenceLeaveAppCommand absCmd = command.getAbsCmd();
		String companyID = AppContexts.user().companyId();
		String sID =   command.getAppCmd().getEmployeeID();
		String oldAppID = absCmd.getAppID();
		// アルゴリズム「登録前エラーチェック（振休日変更）」を実行する
		String appReason = errorCheckBeforeReg(command, absCmd);
		
		//4.社員の当月の期間を算出する
		PeriodCurrentMonth cls =  this.ortherAl.employeePeriodCurrentMonthCalculate(companyID, sID, GeneralDate.today());
		
		// アルゴリズム「詳細画面申請データを取得する」を実行する
		Application_New oldApp =  getDetailApp(absCmd.getAppID());
		//実績の取得
		AchievementOutput achievement = afinder.getAchievement(companyID, sID, oldApp.getAppDate());
		
		//ドメインモデル「休暇申請設定」を取得する
		Optional<HdAppSet> hdAppSetOpt =  repoHdAppSet.getAll();
		
		boolean chkSubHoliday = false;
		boolean chkPause = false;
		boolean chkAnnual = false;
		boolean chkFundingAnnual = false;
		boolean chkSpecial = true;
		boolean chkPublicHoliday = false;
		boolean chkSuperBreak = true;
		String appName = "";
		if (hdAppSetOpt.isPresent()) {
			HdAppSet hdSet = hdAppSetOpt.get();
			chkPause = hdSet.getRegisInsuff().value == 1 ? true : false;// 休暇申請設定．振休残数不足登録できる
			if (hdSet.getFurikyuName() != null) {
				appName = hdSet.getFurikyuName().v();
			}
		}
		
		InterimRemainCheckInputParam inputParam = new InterimRemainCheckInputParam(companyID, sID,
				new DatePeriod(cls.getStartDate(), cls.getStartDate().addYears(1).addDays(-1)), false,
				command.getAbsCmd().getAppDate(),
				new DatePeriod(command.getAbsCmd().getAppDate(), command.getAbsCmd().getAppDate()), true,
				Collections.emptyList(), Collections.emptyList(),getAppData(command,sID,achievement,oldApp) , chkSubHoliday, chkPause, chkAnnual, chkFundingAnnual, chkSpecial,
				chkPublicHoliday, chkSuperBreak);
		
		//登録時の残数チェック
		EarchInterimRemainCheck check =  checkRegister.checkRegister(inputParam);
		
		if(check.isChkSubHoliday() ==true || check.isChkPause()==true || check.isChkAnnual() ==true || check.isChkFundingAnnual() ==true || check.isChkSpecial()==true){
			throw new BusinessException("Msg_1409", appName);
		}
		
		

		// アルゴリズム「振休振出申請の取消」を実行する
		cancelOldAbsApp(command, absCmd, oldAppID);
		// アルゴリズム「登録前共通処理（新規）」を実行する
		Application_New commonApp = createNewCommonApp(command, absCmd, appReason);
		command.getAbsCmd().setAppID(commonApp.getAppID());
		saveHanler.CmProcessBeforeReg(command, commonApp);
		// ドメイン「振休申請」を1件登録する
		createNewAbsApp(commonApp, command);
		//暫定データの登録
		this.registerDateChange.registerDateChange(companyID, sID,
				Arrays.asList(oldApp.getAppDate(), command.getAbsCmd().getAppDate()));
		// アルゴリズム「新規画面登録後の処理」を実行する
		return newAfterReg.processAfterRegister(commonApp);

	}
	
	private List<AppRemainCreateInfor> getAppData(SaveHolidayShipmentCommand command, String sID,
			AchievementOutput achievement, Application_New oldApp) {
		List<AppRemainCreateInfor> apps = new ArrayList<AppRemainCreateInfor>();
		// add oldapp
		apps.add(new AppRemainCreateInfor(sID, oldApp.getAppID(), GeneralDateTime.now(), oldApp.getAppDate(),
				EnumAdaptor.valueOf(command.getAppCmd().getPrePostAtr(),
						nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.PrePostAtr.class),
				nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType.COMPLEMENT_LEAVE_APPLICATION,
				Optional.ofNullable(achievement.getWorkType().getWorkTypeCode()),
				Optional.ofNullable(command.getAbsCmd().getWkTimeCD()), Optional.empty(), Optional.empty(),
				Optional.empty(), Optional.ofNullable(oldApp.getAppDate()), Optional.ofNullable(oldApp.getAppDate()),
				Collections.emptyList()));

		AbsenceLeaveAppCommand absCmd = command.getAbsCmd();
		String newAppID = IdentifierUtil.randomUniqueId();

		absCmd.setAppID(newAppID);
		// add newApp
		apps.add(new AppRemainCreateInfor(sID, newAppID, GeneralDateTime.now(), absCmd.getAppDate(),
				EnumAdaptor.valueOf(command.getAppCmd().getPrePostAtr(),
						nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.PrePostAtr.class),
				nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType.COMPLEMENT_LEAVE_APPLICATION,
				Optional.ofNullable(absCmd.getWkTypeCD()), Optional.ofNullable(absCmd.getWkTimeCD()), Optional.empty(),
				Optional.empty(), Optional.empty(), Optional.ofNullable(absCmd.getAppDate()),
				Optional.ofNullable(absCmd.getAppDate()), Collections.emptyList()));

		return apps;
	}

	private Application_New getDetailApp(String appID) {
		String companyID = AppContexts.user().companyId();
		Optional<Application_New> app = appRepo.findByID(companyID, appID);
		if (!app.isPresent()) {
			throw new BusinessException("Msg_198");
		}
		return app.get();
	}

	private Application_New createNewCommonApp(SaveHolidayShipmentCommand command, AbsenceLeaveAppCommand absCmd,
			String appReason) {
		String companyID = AppContexts.user().companyId();
		String employeeID = command.getAppCmd().getEmployeeID();
		ApplicationType appType = ApplicationType.COMPLEMENT_LEAVE_APPLICATION;
		Application_New commonApp = Application_New.firstCreate(companyID,
				EnumAdaptor.valueOf(command.getAppCmd().getPrePostAtr(), PrePostAtr.class), absCmd.getAppDate(),
				appType, employeeID, new AppReason(appReason));
		if (!AppContexts.user().employeeId().equals(employeeID)) {
			commonApp.setEnteredPersonID(AppContexts.user().employeeId());
		}
		appImp.insert(commonApp);
		return commonApp;
	}

	private void createNewAbsApp(Application_New commonApp, SaveHolidayShipmentCommand command) {

		AbsenceLeaveApp absApp = saveHanler.createNewAbsDomainFromCmd(command.getAbsCmd());

		absRepo.insert(absApp);
	}

	private void cancelOldAbsApp(SaveHolidayShipmentCommand command, AbsenceLeaveAppCommand absCmd, String oldAppID) {
		String companyID = AppContexts.user().companyId();
		HolidayShipmentCommand shipmentCmd = new HolidayShipmentCommand(oldAppID, null,
				command.getAppCmd().getAppVersion(), "", "", "");
		cancelHanler.cancelAppForPaidLeave(companyID, shipmentCmd);

	}

	private String errorCheckBeforeReg(SaveHolidayShipmentCommand command, AbsenceLeaveAppCommand absCmd) {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		ApplicationType appType = ApplicationType.COMPLEMENT_LEAVE_APPLICATION;
		// アルゴリズム「事前条件チェック」を実行する
		String appReason = saveHanler.preconditionCheck(command, companyID, appType, ApplicationCombination.Abs.value);
		// アルゴリズム「同日申請存在チェック」を実行する
		saveHanler.dateCheck(employeeID, null, absCmd.getAppDate(), command, command.getComType());
		return appReason;
	}

}
