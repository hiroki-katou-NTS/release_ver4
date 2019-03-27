package nts.uk.ctx.at.request.app.command.application.lateorleaveearly;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.service.FactoryLateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.service.LateOrLeaveEarlyService;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional

public class CreateLateOrLeaveEarlyCommandHandler
		extends CommandHandlerWithResult<CreateLateOrLeaveEarlyCommand, ProcessResult> {

	@Inject
	private LateOrLeaveEarlyService lateOrLeaveEarlyService;

	@Inject
	private FactoryLateOrLeaveEarly factoryLateOrLeaveEarly;

	@Inject
	private NewAfterRegister_New newAfterRegister;

	@Inject
	private RegisterAtApproveReflectionInfoService_New registerService;

	@Inject
	private NewBeforeRegister_New newBeforeRegister;

	@Inject
	private AppTypeDiscreteSettingRepository appTypeSetRepo;
	@Inject
	private ApplicationSettingRepository appSetRepo;

	@Override
	protected ProcessResult handle(CommandHandlerContext<CreateLateOrLeaveEarlyCommand> context) {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		CreateLateOrLeaveEarlyCommand command = context.getCommand();
		// アルゴリズム「遅刻早退申請理由登録内容生成」を実行する
		String appReason = genReason(command.getReasonTemp(), command.getAppReason(), companyID);
		Application_New application = Application_New.firstCreate(companyID,
				EnumAdaptor.valueOf(command.getPrePostAtr(), PrePostAtr.class), command.getApplicationDate(),
				ApplicationType.EARLY_LEAVE_CANCEL_APPLICATION, employeeID, new AppReason(appReason));
		LateOrLeaveEarly domainLateOrLeaveEarly = factoryLateOrLeaveEarly.buildLateOrLeaveEarly(application,
				command.getActualCancel(), command.getEarly1(), command.getEarlyTime1(), command.getLate1(),
				command.getLateTime1(), command.getEarly2(), command.getEarlyTime2(), command.getLate2(),
				command.getLateTime2());

		// 共通アルゴリズム「2-1.新規画面登録前の処理」を実行する
		newBeforeRegister.processBeforeRegister(domainLateOrLeaveEarly.getApplication(), 0, command.isCheckOver1Year());
		// 事前制約をチェックする
		// ドメインモデル「遅刻早退取消申請」の新規登録する
		lateOrLeaveEarlyService.createLateOrLeaveEarly(domainLateOrLeaveEarly);
		// 2-2.新規画面登録時承認反映情報の整理
		registerService.newScreenRegisterAtApproveInfoReflect(domainLateOrLeaveEarly.getApplication().getEmployeeID(),
				domainLateOrLeaveEarly.getApplication());
		// 共通アルゴリズム「2-3.新規画面登録後の処理」を実行する
		return newAfterRegister.processAfterRegister(domainLateOrLeaveEarly.getApplication());

	}

	public String genReason(String fixedReason, String reasonText, String companyID) {
		String appReason = "";
		Optional<AppTypeDiscreteSetting> appTypeSetOpt = this.appTypeSetRepo
				.getAppTypeDiscreteSettingByAppType(companyID, ApplicationType.EARLY_LEAVE_CANCEL_APPLICATION.value);
		if (!appTypeSetOpt.isPresent()) {
			throw new BusinessException("申請種類別設定 == null");
		}
		AppTypeDiscreteSetting appTypeSet = appTypeSetOpt.get();

		boolean isShowFixedHideText = appTypeSet.getDisplayReasonFlg().equals(AppDisplayAtr.NOTDISPLAY)
				&& appTypeSet.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY);

		if (isShowFixedHideText) {
			appReason = fixedReason;
		} else {
			if (!fixedReason.isEmpty() || !reasonText.isEmpty()) {
				appReason = !fixedReason.isEmpty() ? fixedReason + System.lineSeparator() + reasonText : reasonText;
			}
		}

		Optional<ApplicationSetting> appSetOp = appSetRepo.getApplicationSettingByComID(companyID);

		ApplicationSetting appSet = appSetOp.get();

		boolean isReasonBlankWhenRequired = appSet.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
				&& Strings.isBlank(appReason);
		boolean isAnyReasonControlDisplay = isComboBoxReasonDisplay(appTypeSet) || isReasonTextFieldDisplay(appTypeSet);
		if (isAnyReasonControlDisplay && isReasonBlankWhenRequired) {
			throw new BusinessException("Msg_115");
		}
		return appReason;
	}

	private boolean isReasonTextFieldDisplay(AppTypeDiscreteSetting appTypeSet) {
		return appTypeSet.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY);
	}

	private boolean isComboBoxReasonDisplay(AppTypeDiscreteSetting appTypeSet) {
		return appTypeSet.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY);
	}
}
