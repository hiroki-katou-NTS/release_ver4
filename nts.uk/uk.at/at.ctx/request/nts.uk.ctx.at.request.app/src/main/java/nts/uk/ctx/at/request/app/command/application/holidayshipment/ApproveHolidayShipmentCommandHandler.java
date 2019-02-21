package nts.uk.ctx.at.request.app.command.application.holidayshipment;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterApproval_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ApproveProcessResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ApproveHolidayShipmentCommandHandler
		extends CommandHandlerWithResult<HolidayShipmentCommand, ApproveProcessResult> {

	@Inject
	private DetailBeforeUpdate detailBefUpdate;
	@Inject
	private DetailAfterApproval_New detailAfAppv;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;

	@Override
	protected ApproveProcessResult handle(CommandHandlerContext<HolidayShipmentCommand> context) {
		HolidayShipmentCommand command = context.getCommand();
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		Long version = command.getAppVersion();
		String memo = context.getCommand().getMemo();
		
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyID, 
				ApplicationType.COMPLEMENT_LEAVE_APPLICATION.value).get();
		String appReason = Strings.EMPTY;	
		String typicalReason = Strings.EMPTY;
		String displayReason = Strings.EMPTY;
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)){
			typicalReason += context.getCommand().getComboBoxReason();
		}
		if(appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if(Strings.isNotBlank(typicalReason)){
				displayReason += System.lineSeparator();
			}
			displayReason += context.getCommand().getTextAreaReason();
		}
		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository.getApplicationSettingByComID(companyID);
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)
				&&appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
				if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
						&& Strings.isBlank(typicalReason+displayReason)) {
					throw new BusinessException("Msg_115");
				}
			}
		appReason = typicalReason + displayReason;
		
		// アルゴリズム「振休振出申請の承認」を実行する
		ProcessResult processResult = approvalApplication(command, companyID, employeeID, version, memo, appReason);
		
		return new ApproveProcessResult(
				processResult.isProcessDone(), 
				processResult.isAutoSendMail(), 
				processResult.getAutoSuccessMail(), 
				processResult.getAutoFailMail(), 
				processResult.getAppID(), 
				processResult.getReflectAppId(), 
				appReason);
	}

	private ProcessResult approvalApplication(HolidayShipmentCommand command, String companyID, String employeeeID,
			Long version, String memo, String appReason) {
		boolean isApprovalRec = command.getRecAppID() != null;
		boolean isApprovalAbs = command.getAbsAppID() != null;
		ProcessResult result = null;
		if (isApprovalRec) {
			// アルゴリズム「承認処理」を実行する
			result = approvalProcessing(companyID, command.getRecAppID(), employeeeID, version, memo, appReason);
		}
		if (isApprovalAbs) {
			// アルゴリズム「承認処理」を実行する
			result = approvalProcessing(companyID, command.getAbsAppID(), employeeeID, version, memo, appReason);
		}

		return result;
	}

	private ProcessResult approvalProcessing(String companyID, String appID, String employeeID, Long version,
			String memo, String appReason) {
		// アルゴリズム「詳細画面登録前の処理」を実行する
		detailBefUpdate.processBeforeDetailScreenRegistration(companyID, employeeID, GeneralDate.today(), 1, appID,
				PrePostAtr.PREDICT, version);
		// アルゴリズム「申請個別のエラーチェック」を実行する không xử lý

		// xử lý đồng thời
		// アルゴリズム「申請個別の更新」を実行する
		// アルゴリズム「詳細画面承認後の処理」を実行する
		return detailAfAppv.doApproval(companyID, appID, employeeID, memo, appReason, true);

	}

}
