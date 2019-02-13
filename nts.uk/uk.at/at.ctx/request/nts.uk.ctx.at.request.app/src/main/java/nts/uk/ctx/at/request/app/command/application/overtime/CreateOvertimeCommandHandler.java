package nts.uk.ctx.at.request.app.command.application.overtime;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.service.IFactoryOvertime;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class CreateOvertimeCommandHandler extends CommandHandlerWithResult<CreateOvertimeCommand, ProcessResult> {

	@Inject
	private IFactoryOvertime factoryOvertime;

	@Inject
	private OvertimeService overTimeService;

	@Inject
	private NewAfterRegister_New newAfterRegister;

	@Inject
	private RegisterAtApproveReflectionInfoService_New registerService;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;

	@Override
	protected ProcessResult handle(CommandHandlerContext<CreateOvertimeCommand> context) {

		CreateOvertimeCommand command = context.getCommand();
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyId, 
				ApplicationType.OVER_TIME_APPLICATION.value).get();
		String appReason = Strings.EMPTY;	
		String typicalReason = Strings.EMPTY;
		String displayReason = Strings.EMPTY;
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)){
			typicalReason += command.getAppReasonID();
		}
		if(appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if(Strings.isNotBlank(typicalReason)){
				displayReason += System.lineSeparator();
			}
			displayReason += command.getApplicationReason();
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
		}
		appReason = typicalReason + displayReason;

		// Create Application
		Application_New appRoot = factoryOvertime.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), appReason,
				appReason,command.getApplicantSID());

		Integer workClockFrom1 = command.getWorkClockFrom1() == null ? null : command.getWorkClockFrom1().intValue();
		Integer workClockTo1 = command.getWorkClockTo1() == null ? null : command.getWorkClockTo1().intValue();
		Integer workClockFrom2 = command.getWorkClockFrom2() == null ? null : command.getWorkClockFrom2().intValue();
		Integer workClockTo2 = command.getWorkClockTo2() == null ? null : command.getWorkClockTo2().intValue();

		Optional<AppOvertimeDetail> appOvertimeDetailOtp = command.getAppOvertimeDetail() == null ? Optional.empty()
				: Optional.ofNullable(command.getAppOvertimeDetail().toDomain(companyId, appID));
		AppOverTime overTimeDomain = factoryOvertime.buildAppOverTime(companyId, appID, command.getOvertimeAtr(),
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockFrom1, workClockTo1, workClockFrom2,
				workClockTo2, command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				command.getFlexExessTime(), command.getOverTimeShiftNight(),
				CheckBeforeRegisterOvertime.getOverTimeInput(command, companyId, appID), 
				appOvertimeDetailOtp);

		// ドメインモデル「残業申請」の登録処理を実行する(INSERT)
		overTimeService.CreateOvertime(overTimeDomain, appRoot);

		// 2-2.新規画面登録時承認反映情報の整理
		registerService.newScreenRegisterAtApproveInfoReflect(appRoot.getEmployeeID(), appRoot);

		// 2-3.新規画面登録後の処理を実行
		return newAfterRegister.processAfterRegister(appRoot);

	}
}
