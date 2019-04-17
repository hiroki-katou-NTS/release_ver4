package nts.uk.ctx.at.request.app.command.application.gobackdirectly;

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
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.service.GoBackDirectlyUpdateService;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateGoBackDirectlyCommandHandler extends CommandHandlerWithResult<UpdateApplicationGoBackDirectlyCommand, ProcessResult> {
	@Inject
	private GoBackDirectlyUpdateService goBackDirectlyUpdateService;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;

	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Override
	protected ProcessResult handle(CommandHandlerContext<UpdateApplicationGoBackDirectlyCommand> context) {
		String companyId = AppContexts.user().companyId();
		UpdateApplicationGoBackDirectlyCommand command = context.getCommand();
		
		// get new Application Item
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyId, 
				ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value).get();
		String appReason = Strings.EMPTY;	
		String typicalReason = Strings.EMPTY;
		String displayReason = Strings.EMPTY;
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)){
			typicalReason += command.appCommand.getAppReasonID();
		}
		if(appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if(Strings.isNotBlank(typicalReason)){
				displayReason += System.lineSeparator();
			}
			displayReason += command.appCommand.getApplicationReason();
		} else {
			if(Strings.isBlank(typicalReason)){
				displayReason = applicationRepository.findByID(companyId, command.getAppCommand().getApplicationID()).get().getAppReason().v();
			}
		}
		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
				.getApplicationSettingByComID(companyId);
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)
				|| appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
				if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
						&& Strings.isBlank(typicalReason+displayReason)) {
					throw new BusinessException("Msg_115");
				}
			}
		appReason = typicalReason + displayReason;
		
		Application_New updateApp = new Application_New(0L, companyId, command.goBackCommand.getAppID(),
				EnumAdaptor.valueOf(command.appCommand.getPrePostAtr(), PrePostAtr.class),
				command.appCommand.getInputDate(), command.appCommand.getEnteredPersonSID(),
				new AppReason(command.appCommand.getReversionReason()), command.appCommand.getApplicationDate(),
				new AppReason(appReason),
				EnumAdaptor.valueOf(command.appCommand.getApplicationType(), ApplicationType.class),
				command.appCommand.getApplicantSID(), Optional.of(command.appCommand.getStartDate()),
				Optional.of(command.appCommand.getEndDate()),
				ReflectionInformation_New.builder()
						.stateReflection(
								EnumAdaptor.valueOf(command.appCommand.getReflectPlanState(), ReflectedState_New.class))
						.stateReflectionReal(
								EnumAdaptor.valueOf(command.appCommand.getReflectPerState(), ReflectedState_New.class))
						.forcedReflection(EnumAdaptor.valueOf(command.appCommand.getReflectPlanEnforce(),
								DisabledSegment_New.class))
						.forcedReflectionReal(EnumAdaptor.valueOf(command.appCommand.getReflectPerEnforce(),
								DisabledSegment_New.class))
						.notReason(Optional.ofNullable(command.appCommand.getReflectPlanScheReason())
								.map(x -> EnumAdaptor.valueOf(x, ReasonNotReflect_New.class)))
						.notReasonReal(Optional.ofNullable(command.appCommand.getReflectPerScheReason())
								.map(x -> EnumAdaptor.valueOf(x, ReasonNotReflectDaily_New.class)))
						.dateTimeReflection(Optional.ofNullable(
								GeneralDateTime.legacyDateTime(command.appCommand.getReflectPlanTime().date())))
						.dateTimeReflectionReal(Optional.ofNullable(
								GeneralDateTime.legacyDateTime(command.appCommand.getReflectPerTime().date()))).build());
		
		// get new Go Back Item
		GoBackDirectly updateGoBack = new GoBackDirectly(companyId, 
				command.goBackCommand.getAppID(),
				command.goBackCommand.getWorkTypeCD(),
				command.goBackCommand.getSiftCD(), 
				command.goBackCommand.getWorkChangeAtr(), 
				command.goBackCommand.getGoWorkAtr1(), 
				command.goBackCommand.getBackHomeAtr1(),
				command.goBackCommand.getWorkTimeStart1(), 
				command.goBackCommand.getWorkTimeEnd1(), 
				command.goBackCommand.workLocationCD1,
				command.goBackCommand.getGoWorkAtr2(), 
				command.goBackCommand.getBackHomeAtr2(), 
				command.goBackCommand.getWorkTimeStart2(),
				command.goBackCommand.getWorkTimeEnd2(), 
				command.goBackCommand.workLocationCD2);
		// update
		
		return this.goBackDirectlyUpdateService.updateGoBackDirectly(updateGoBack, updateApp, command.goBackCommand.version);
	}
}
