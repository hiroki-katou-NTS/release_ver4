package nts.uk.ctx.at.request.app.command.application.common;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.AppReflectManager;
import nts.uk.ctx.at.request.dom.applicationreflect.service.InformationSettingOfAppForReflect;
import nts.uk.ctx.at.request.dom.applicationreflect.service.InformationSettingOfEachApp;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class ReflectAplicationCommmandHandler extends CommandHandler<List<String>>{
	@Inject
	private ApplicationRepository_New repoApp;
	@Inject
	private InformationSettingOfAppForReflect appSetting;
	@Inject
	private AppReflectManager appReflectManager;
	@Override
	protected void handle(CommandHandlerContext<List<String>> context) {
		InformationSettingOfEachApp reflectSetting = appSetting.getSettingOfEachApp();
		List<String> lstAppID = context.getCommand();
		for (String appID : lstAppID) {
			//get list application by list id
			Optional<Application_New> app = repoApp.findByID(AppContexts.user().companyId(), appID);
			if(!app.isPresent()){
				throw new BusinessException("Khong tim thay applicaion" + context.getCommand());
			}	
			Application_New application = app.get();
			if((application.getPrePostAtr().equals(PrePostAtr.PREDICT)&&
						application.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION))
					|| application.getAppType().equals(ApplicationType.BREAK_TIME_APPLICATION)
					|| application.getAppType().equals(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION)
					|| application.getAppType().equals(ApplicationType.WORK_CHANGE_APPLICATION)
					|| application.getAppType().equals(ApplicationType.ABSENCE_APPLICATION)
					|| application.getAppType().equals(ApplicationType.COMPLEMENT_LEAVE_APPLICATION))
			{
				appReflectManager.reflectEmployeeOfApp(application, reflectSetting, ExecutionTypeExImport.NORMAL_EXECUTION, "");
			}
		}
	}
}


