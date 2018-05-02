package nts.uk.ctx.at.request.app.command.application.holidayshipment;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.holidayshipment.ApplicationCombination;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class SaveChangeAbsDateCommandHandler extends CommandHandlerWithResult<SaveHolidayShipmentCommand, String> {
	@Inject
	private SaveHolidayShipmentCommandHandler saveHanler;
	@Inject
	private CancelHolidayShipmentCommandHandler cancelHanler;
	@Inject
	private ApplicationApprovalService_New appImp;
	@Inject
	private AbsenceLeaveAppRepository absRepo;

	String companyID, appReason, employeeID;
	ApplicationType appType = ApplicationType.COMPLEMENT_LEAVE_APPLICATION;
	AbsenceLeaveAppCommand absCmd;
	SaveHolidayShipmentCommand command;

	@Override
	protected String handle(CommandHandlerContext<SaveHolidayShipmentCommand> context) {
		command = context.getCommand();
		absCmd = command.getAbsCmd();
		employeeID = AppContexts.user().employeeId();
		companyID = AppContexts.user().companyId();
		// アルゴリズム「登録前エラーチェック（振休日変更）」を実行する
		errorCheckBeforeReg();

		// アルゴリズム「振休振出申請の取消」を実行する
		cancelOldAbsApp();
		// アルゴリズム「登録前共通処理（新規）」を実行する
		Application_New commonApp = createNewCommonApp();

		saveHanler.CmProcessBeforeReg(command, commonApp);
		// ドメイン「振休申請」を1件登録する
		createNewAbsApp(commonApp);

		return commonApp.getAppID();

	}

	private Application_New createNewCommonApp() {
		Application_New commonApp = Application_New.firstCreate(companyID,
				EnumAdaptor.valueOf(command.getAppCmd().getPrePostAtr(), PrePostAtr.class), absCmd.getAppDate(),
				appType, employeeID, new AppReason(appReason));
		appImp.insert(commonApp);
		return commonApp;
	}

	private void createNewAbsApp(Application_New commonApp) {

		AbsenceLeaveApp absApp = saveHanler.createNewAbsDomainFromCmd(commonApp.getAppID(), command.getAbsCmd());

		absRepo.insert(absApp);
	}

	private void cancelOldAbsApp() {
		HolidayShipmentCommand shipmentCmd = new HolidayShipmentCommand(absCmd.getAppID(), null,
				command.getAppCmd().getAppVersion(), "");
		cancelHanler.cancelAppForPaidLeave(companyID, shipmentCmd);

	}

	private void errorCheckBeforeReg() {
		employeeID = AppContexts.user().employeeId();
		// アルゴリズム「事前条件チェック」を実行する
		appReason = saveHanler.preconditionCheck(command, companyID, appType, ApplicationCombination.Abs.value);
		// アルゴリズム「同日申請存在チェック」を実行する
		saveHanler.dateCheck(command);

	}

}
