package nts.uk.ctx.at.request.app.command.application.applicationlist;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.AppReflectManagerFromRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.InformationSettingOfAppForReflect;
import nts.uk.ctx.at.request.dom.applicationreflect.service.InformationSettingOfEachApp;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class ReflectAfterApproveAsyncCmdHandler extends AsyncCommandHandler<List<String>>{

	@Inject
	private ApplicationRepository_New repoApp;
	
	@Inject
	private AppReflectManagerFromRecord appReflectManager;
	@Inject
	private InformationSettingOfAppForReflect appSetting;
	@Inject
	private ManagedParallelWithContext managedParallelWithContext;
	@Override
	protected void handle(CommandHandlerContext<List<String>> context) {
		List<String> lstID = context.getCommand();
		//get list application by list id
		List<Application_New> lstApplication = repoApp.findByListID(AppContexts.user().companyId(), lstID);		
		this.managedParallelWithContext.forEach(lstApplication, app -> {
			if((app.getPrePostAtr().equals(PrePostAtr.PREDICT)&&
					app.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION)
				|| app.getAppType().equals(ApplicationType.BREAK_TIME_APPLICATION))
				|| app.getAppType().equals(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION)
				|| app.getAppType().equals(ApplicationType.WORK_CHANGE_APPLICATION)
				|| app.getAppType().equals(ApplicationType.ABSENCE_APPLICATION)
				|| app.getAppType().equals(ApplicationType.COMPLEMENT_LEAVE_APPLICATION)){
				InformationSettingOfEachApp reflectSetting = appSetting.getSettingOfEachApp();
				GeneralDate startDate = app.getStartDate().isPresent() ? app.getStartDate().get() : app.getAppDate();
				GeneralDate endDate = app.getEndDate().isPresent() ? app.getEndDate().get() : app.getAppDate();
				appReflectManager.reflectAppOfAppDate("",
						app.getEmployeeID(),
						ExecutionTypeExImport.RERUN,
						reflectSetting,
						new DatePeriod(startDate, endDate));
			}
		});
	}
    @Override
    public boolean keepsTrack(){
        return false;
    }
}
