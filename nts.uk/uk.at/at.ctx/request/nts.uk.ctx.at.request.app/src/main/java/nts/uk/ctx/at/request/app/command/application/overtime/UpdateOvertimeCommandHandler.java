package nts.uk.ctx.at.request.app.command.application.overtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateOvertimeCommandHandler extends CommandHandlerWithResult<UpdateOvertimeCommand, ProcessResult>{

	@Inject
	private OvertimeRepository overtimeRepository;
	
	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;
	
	@Inject
	private DetailAfterUpdate detailAfterUpdate;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;

	@Override
	protected ProcessResult handle(CommandHandlerContext<UpdateOvertimeCommand> context) {
		String companyID = AppContexts.user().companyId();
		UpdateOvertimeCommand command = context.getCommand();
		Optional<AppOverTime> opAppOverTime = overtimeRepository.getFullAppOvertime(companyID, command.getAppID());
		if(!opAppOverTime.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de update");
		}
		
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyID, 
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
				.getApplicationSettingByComID(companyID);
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)
			||appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
					&& Strings.isBlank(typicalReason+displayReason)) {
				throw new BusinessException("Msg_115");
			}
		}
		appReason = typicalReason + displayReason;
		
		AppOverTime appOverTime = opAppOverTime.get();
		List<OverTimeInput> overTimeInputs = new ArrayList<>();
		overTimeInputs.addAll(command.getRestTime().stream().filter(x -> x.getStartTime()!=null||x.getEndTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		overTimeInputs.addAll(command.getOvertimeHours().stream().filter(x -> x.getApplicationTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		overTimeInputs.addAll(command.getBreakTimes().stream().filter(x -> x.getApplicationTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		overTimeInputs.addAll(command.getBonusTimes().stream().filter(x -> x.getApplicationTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		Optional<AppOvertimeDetail> appOvertimeDetailOtp = command.getAppOvertimeDetail() == null ? Optional.empty()
				: Optional.ofNullable(command.getAppOvertimeDetail().toDomain(companyID, appOverTime.getAppID()));
		String divergenceReason = command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator());
		String applicationReason = appReason;
		appOverTime.setDivergenceReason(divergenceReason);
		appOverTime.setFlexExessTime(command.getFlexExessTime());
		appOverTime.setOverTimeAtr(EnumAdaptor.valueOf(command.getOvertimeAtr(), OverTimeAtr.class));
		appOverTime.setOverTimeInput(overTimeInputs);
		appOverTime.setAppOvertimeDetail(appOvertimeDetailOtp);
		appOverTime.setOverTimeShiftNight(command.getOverTimeShiftNight());
		appOverTime.setSiftCode(command.getSiftTypeCode() == null ? null : new WorkTimeCode(command.getSiftTypeCode()));
		appOverTime.setWorkClockFrom1(command.getWorkClockFrom1());
		appOverTime.setWorkClockFrom2(command.getWorkClockFrom2());
		appOverTime.setWorkClockTo1(command.getWorkClockTo1());
		appOverTime.setWorkClockTo2(command.getWorkClockTo2());
		appOverTime.setWorkTypeCode(command.getWorkTypeCode() == null? null : new WorkTypeCode(command.getWorkTypeCode()));
		appOverTime.getApplication().setAppReason(new AppReason(applicationReason));
		appOverTime.setVersion(appOverTime.getVersion());
		appOverTime.getApplication().setVersion(command.getVersion());
		
		detailBeforeUpdate.processBeforeDetailScreenRegistration(
				companyID, 
				appOverTime.getApplication().getEmployeeID(), 
				appOverTime.getApplication().getAppDate(), 
				1, 
				appOverTime.getAppID(), 
				appOverTime.getApplication().getPrePostAtr(), command.getVersion());
		overtimeRepository.update(appOverTime);
		applicationRepository.updateWithVersion(appOverTime.getApplication());
		// 暫定データの登録
		interimRemainDataMngRegisterDateChange.registerDateChange(
				companyID, 
				command.getApplicantSID(), 
				Arrays.asList(command.getApplicationDate()));
		return detailAfterUpdate.processAfterDetailScreenRegistration(appOverTime.getApplication());
	}

}
