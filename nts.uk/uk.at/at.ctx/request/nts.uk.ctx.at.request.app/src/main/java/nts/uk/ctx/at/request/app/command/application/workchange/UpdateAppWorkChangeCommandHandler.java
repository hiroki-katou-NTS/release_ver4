package nts.uk.ctx.at.request.app.command.application.workchange;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.app.command.application.common.CreateApplicationCommand;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.DisabledSegment_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflect_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.ReflectionInformation_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.InitMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailScreenInitModeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.OutputMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.User;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IWorkChangeUpdateService;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateAppWorkChangeCommandHandler extends CommandHandlerWithResult<AddAppWorkChangeCommand, ProcessResult> {
	//private static final String COLON_STRING = ":";
	@Inject
	private IWorkChangeUpdateService updateService;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	@Inject
	private ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private InitMode initMode;
	
	@Inject
	private ApplicationRepository_New applicationRepository;

	@Override
	protected ProcessResult handle(CommandHandlerContext<AddAppWorkChangeCommand> context) {
		AddAppWorkChangeCommand updateCommand = context.getCommand();
		// Command data
		CreateApplicationCommand appCommand = updateCommand.getApplication();
		AppWorkChangeCommand workChangeCommand = updateCommand.getWorkChange();
		
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = appCommand.getApplicationID();
		
		DetailScreenInitModeOutput output = initMode.getDetailScreenInitMode(EnumAdaptor.valueOf(context.getCommand().getUser(), User.class), context.getCommand().getReflectPerState());
		String appReason = applicationRepository.findByID(companyId, appID).get().getAppReason().v();
		if(output.getOutputMode()==OutputMode.EDITMODE){
			AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
					companyId, 
					ApplicationType.WORK_CHANGE_APPLICATION.value).get();
			String typicalReason = Strings.EMPTY;
			String displayReason = Strings.EMPTY;
			if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)){
				typicalReason += appCommand.getAppReasonID();
			}
			if(appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
				if(Strings.isNotBlank(typicalReason)){
					displayReason += System.lineSeparator();
				}
				displayReason += appCommand.getApplicationReason();
			}
			Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
					.getApplicationSettingByComID(companyId);
			ApplicationSetting applicationSetting = applicationSettingOp.get();
			if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)
				||appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
				if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
						&& Strings.isBlank(typicalReason+displayReason)) {
					throw new BusinessException("Msg_115");
				}
				appReason = typicalReason + displayReason;
			}
		}
		
		// 申請
		Application_New updateApp = new Application_New(
				appCommand.getVersion(), 
				companyId, 
				appID,
				EnumAdaptor.valueOf(appCommand.getPrePostAtr(), PrePostAtr.class), 
				GeneralDateTime.now(), 
				appCommand.getEnteredPersonSID(), 
				new AppReason(Strings.EMPTY), 
				appCommand.getStartDate(),
				new AppReason(appReason),
				ApplicationType.WORK_CHANGE_APPLICATION, 
				appCommand.getApplicantSID(),
				Optional.of(appCommand.getStartDate()),
				Optional.of(appCommand.getEndDate()), 
				ReflectionInformation_New.builder()
						.stateReflection(
								EnumAdaptor.valueOf(appCommand.getReflectPlanState(), ReflectedState_New.class))
						.stateReflectionReal(
								EnumAdaptor.valueOf(appCommand.getReflectPerState(), ReflectedState_New.class))
						.forcedReflection(
								EnumAdaptor.valueOf(appCommand.getReflectPlanEnforce(), DisabledSegment_New.class))
						.forcedReflectionReal(
								EnumAdaptor.valueOf(appCommand.getReflectPerEnforce(), DisabledSegment_New.class))
						.notReason(Optional.ofNullable(appCommand.getReflectPlanScheReason())
								.map(x -> EnumAdaptor.valueOf(x, ReasonNotReflect_New.class)))
						.notReasonReal(Optional.ofNullable(appCommand.getReflectPerScheReason())
								.map(x -> EnumAdaptor.valueOf(x, ReasonNotReflectDaily_New.class)))
						.dateTimeReflection(Optional
								.ofNullable(appCommand.getReflectPlanTime() == null ? null : GeneralDateTime.legacyDateTime(appCommand.getReflectPlanTime().date())))
						.dateTimeReflectionReal(Optional
								.ofNullable(appCommand.getReflectPerTime() == null ? null : GeneralDateTime.legacyDateTime(appCommand.getReflectPerTime().date())))
						.build());
		// 勤務変更申請
		AppWorkChange workChangeDomain = AppWorkChange.createFromJavaType(workChangeCommand.getCid(),
				workChangeCommand.getAppId(), workChangeCommand.getWorkTypeCd(), workChangeCommand.getWorkTimeCd(),
				workChangeCommand.getExcludeHolidayAtr(), workChangeCommand.getWorkChangeAtr(),
				workChangeCommand.getGoWorkAtr1(), workChangeCommand.getBackHomeAtr1(),
				workChangeCommand.getBreakTimeStart1(), workChangeCommand.getBreakTimeEnd1(),
				workChangeCommand.getWorkTimeStart1(), workChangeCommand.getWorkTimeEnd1(),
				workChangeCommand.getWorkTimeStart2(), workChangeCommand.getWorkTimeEnd2(),
				workChangeCommand.getGoWorkAtr2(), workChangeCommand.getBackHomeAtr2());
		//OptimisticLock
		workChangeDomain.setVersion(appCommand.getVersion());
		//updateApp.setVersion(workChangeCommand.getVersion());
		
		// アルゴリズム「勤務変更申請登録（更新）」を実行する
		return updateService.UpdateWorkChange(updateApp, workChangeDomain);
	}

}
