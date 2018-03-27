package nts.uk.ctx.at.request.app.command.application.common;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.AppReflectAfterConfirm;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
@Transactional
public class UpdateApplicationSettingCommandHandler extends CommandHandler<ApplicationSettingCommand>{
	@Inject
	private ApplicationSettingRepository appRep;
	/**
	 * update application setting
	 * @author yennth
	 */
	@Override
	protected void handle(CommandHandlerContext<ApplicationSettingCommand> context) {
		String companyId = AppContexts.user().companyId();
		ApplicationSettingCommand data = context.getCommand();
		ApplicationSetting appli = ApplicationSetting.createFromJavaType(companyId, 
				data.getAppActLockFlg(), data.getAppEndWorkFlg(), 
				data.getAppActConfirmFlg(), data.getAppOvertimeNightFlg(), 
				data.getAppActMonthConfirmFlg(), data.getRequireAppReasonFlg(), 
				data.getDisplayPrePostFlg(), data.getDisplaySearchTimeFlg(), 
				data.getManualSendMailAtr(), data.getBaseDateFlg(), 
				data.getAdvanceExcessMessDispAtr(), data.getHwAdvanceDispAtr(), 
				data.getHwActualDispAtr(), data.getActualExcessMessDispAtr(), 
				data.getOtAdvanceDispAtr(), data.getOtActualDispAtr(), 
				data.getWarningDateDispAtr(), data.getAppReasonDispAtr(), 
				data.getAppContentChangeFlg(), data.getScheReflectFlg(), 
				data.getPriorityTimeReflectFlg(), data.getAttendentTimeReflectFlg());
		Optional<ApplicationSetting> appSet = appRep.getApplicationSettingByComID(companyId);
		
		AppReflectAfterConfirm ref = AppReflectAfterConfirm.toDomain(data.getScheduleConfirmedAtr(), data.getAchievementConfirmedAtr());
		if(appSet.isPresent()){
			appRep.updateSingle(appli, ref);
			return;
		}
		appRep.insert(appli, ref);
	}
}
