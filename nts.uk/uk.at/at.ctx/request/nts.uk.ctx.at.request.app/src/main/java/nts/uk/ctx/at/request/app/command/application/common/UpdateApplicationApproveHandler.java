package nts.uk.ctx.at.request.app.command.application.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.app.find.application.ApplicationDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.InputApproveData;
import nts.uk.ctx.at.request.app.find.setting.company.request.applicationsetting.apptypesetting.DisplayReasonDto;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType_Old;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.InitMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterApproval_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.OutputMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.User;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ApproveProcessResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.DisplayReasonRepository;
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
	
	@Inject
	private DisplayReasonRepository displayRep;
	
	@Inject
	private InitMode initMode;
	
	@Inject
	private ApplicationRepository_New applicationRepository;

	@Override
	protected ApproveProcessResult handle(CommandHandlerContext<InputApproveData> context) {
		String companyID = AppContexts.user().companyId();
		String memo = context.getCommand().getMemo();
		String employeeID = AppContexts.user().employeeId();
		ApplicationDto command = context.getCommand().getApplicationDto();
		
        //アルゴリズム「排他チェック」を実行する (thực hiện xử lý 「check version」)
        beforeRegisterRepo.exclusiveCheck(companyID, command.getAppID(), command.getVersion());
        String appReason = Strings.EMPTY;
        boolean isUpdateReason = false;
        boolean isMobileCall =  context.getCommand().getMobileCall() == null ? false : context.getCommand().getMobileCall().booleanValue();
        if(!isMobileCall) {
			Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
					.getApplicationSettingByComID(companyID);
			ApplicationSetting applicationSetting = applicationSettingOp.get();
	        //14-3.詳細画面の初期モード
			OutputMode outputMode = initMode.getDetailScreenInitMode(EnumAdaptor.valueOf(context.getCommand().getUser(), User.class), context.getCommand().getReflectPerState());
			appReason = applicationRepository.findByID(companyID, command.getAppID()).get().getAppReason().v();
			if(outputMode==OutputMode.EDITMODE){
				boolean displayFixedReason = false;
				boolean displayAppReason = false;
				Integer appType = command.getAppType();
				if(appType==ApplicationType_Old.ABSENCE_APPLICATION.value){
					List<DisplayReasonDto> displayReasonDtoLst = 
							displayRep.findDisplayReason(companyID).stream().map(x -> DisplayReasonDto.fromDomain(x)).collect(Collectors.toList());
					DisplayReasonDto displayReasonSet = displayReasonDtoLst.stream().filter(x -> x.getTypeOfLeaveApp() == context.getCommand().getHolidayAppType())
							.findAny().orElse(null);
					displayFixedReason = displayReasonSet.getDisplayFixedReason() == 1 ? true : false;
					displayAppReason = displayReasonSet.getDisplayAppReason() == 1 ? true : false;
				} else {
					AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
							companyID, 
							appType).get();
					displayFixedReason = appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY);
					displayAppReason = appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY);
				}
				String typicalReason = Strings.EMPTY;
				String displayReason = Strings.EMPTY;
				if(displayFixedReason){
					typicalReason += context.getCommand().getComboBoxReason();
				}
				if(displayAppReason){
					if(Strings.isNotBlank(typicalReason)){
						displayReason += System.lineSeparator();
					}
					displayReason += context.getCommand().getTextAreaReason();
				} else {
					if(Strings.isBlank(typicalReason)){
						displayReason = applicationRepository.findByID(companyID, command.getAppID()).get().getAppReason().v();
					}
				}
				
				if(displayFixedReason||displayAppReason){
					if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
							&& Strings.isBlank(typicalReason+displayReason)) {
						throw new BusinessException("Msg_115");
					}
					appReason = typicalReason + displayReason;
					isUpdateReason = true;
				}
			}
        }
		
        // refactor 4 error
		//8-2.詳細画面承認後の処理
		/*ProcessResult processResult = detailAfterApproval_New.doApproval(companyID, command.getAppID(), employeeID, memo, appReason, isUpdateReason);
		
		return new ApproveProcessResult(
				processResult.isProcessDone(), 
				processResult.isAutoSendMail(), 
				processResult.getAutoSuccessMail(), 
				processResult.getAutoFailMail(), 
				processResult.getAppID(), 
				processResult.getReflectAppId(), 
				appReason);*/
        return null;
	}

}
