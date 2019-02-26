package nts.uk.ctx.at.request.app.command.application.common;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.common.dto.InputApproveData;
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
@Transactional
public class UpdateApplicationApproveHandler extends CommandHandlerWithResult<InputApproveData, ApproveProcessResult> {

	// 4-1.詳細画面登録前の処理
	@Inject
	private DetailBeforeUpdate beforeRegisterRepo;
	
	@Inject
	private DetailAfterApproval_New detailAfterApproval_New;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;

	@Override
	protected ApproveProcessResult handle(CommandHandlerContext<InputApproveData> context) {
		String companyID = AppContexts.user().companyId();
		String memo = context.getCommand().getMemo();
		String employeeID = AppContexts.user().employeeId();
		ApplicationDto_New command = context.getCommand().getApplicationDto();
		
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyID, 
				command.getApplicationType()).get();
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
		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
				.getApplicationSettingByComID(companyID);
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)
				&&appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
				if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
						&& Strings.isBlank(typicalReason+displayReason)) {
					throw new BusinessException("Msg_115");
				}
			}
		appReason = typicalReason + displayReason;
		
		// 4-1.詳細画面登録前の処理 lan nay deu bi hoan lai
		beforeRegisterRepo.processBeforeDetailScreenRegistration(companyID, command.getApplicantSID(),
				GeneralDate.today(), 1,command.getApplicationID(), EnumAdaptor.valueOf(command.getPrePostAtr(), PrePostAtr.class), command.getVersion());
		
		//8-2.詳細画面承認後の処理
		ProcessResult processResult = detailAfterApproval_New.doApproval(companyID, command.getApplicationID(), employeeID, memo, appReason, true);
		
		return new ApproveProcessResult(
				processResult.isProcessDone(), 
				processResult.isAutoSendMail(), 
				processResult.getAutoSuccessMail(), 
				processResult.getAutoFailMail(), 
				processResult.getAppID(), 
				processResult.getReflectAppId(), 
				appReason);
	}

}
