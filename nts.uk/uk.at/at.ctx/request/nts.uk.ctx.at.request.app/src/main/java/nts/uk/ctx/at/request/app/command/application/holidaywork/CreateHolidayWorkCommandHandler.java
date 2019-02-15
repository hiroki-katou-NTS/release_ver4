package nts.uk.ctx.at.request.app.command.application.holidaywork;

import java.util.Arrays;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.CancelHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.HolidayShipmentCommand;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.holidayshipment.brkoffsupchangemng.BrkOffSupChangeMng;
import nts.uk.ctx.at.request.dom.application.holidayshipment.brkoffsupchangemng.BrkOffSupChangeMngRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayService;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.IFactoryHolidayWork;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class CreateHolidayWorkCommandHandler extends CommandHandlerWithResult<CreateHolidayWorkCommand, ProcessResult> {
	@Inject
	private IFactoryHolidayWork factoryHolidayWork;
	@Inject
	private HolidayService holidayService;
	@Inject
	private NewAfterRegister_New newAfterRegister;
	@Inject
	private RegisterAtApproveReflectionInfoService_New registerService;
	@Inject
	private ApplicationRepository_New applicationRepository_New;
	@Inject
	private BrkOffSupChangeMngRepository brkOffSupChangeMngRepository;
	@Inject
	private CancelHolidayShipmentCommandHandler cancelHolidayShipmentCommandHandler;
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	@Override
	protected ProcessResult handle(CommandHandlerContext<CreateHolidayWorkCommand> context) {
		
		CreateHolidayWorkCommand command = context.getCommand();
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyId, 
				ApplicationType.BREAK_TIME_APPLICATION.value).get();
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
				Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
						command.getPrePostAtr(), appReason, appReason, command.getApplicantSID());

				Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
				Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
				Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
				Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
				int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
				int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
				int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
				int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

				Optional<AppOvertimeDetail> appOvertimeDetailOtp = command.getAppOvertimeDetail() == null ? Optional.empty()
						: Optional.ofNullable(command.getAppOvertimeDetail().toDomain(companyId, appID));
				AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
						command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
						workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
						 command.getOverTimeShiftNight(),
						CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID),
						appOvertimeDetailOtp);

		// ドメインモデル「残業申請」の登録処理を実行する(INSERT)
		holidayService.createHolidayWork(holidayWorkDomain, appRoot);
		if(command.getUiType() ==1){
			// 9.振休申請取り消し
			Optional<Application_New> optapplicationLeaveApp = this.applicationRepository_New.findByID(companyId, command.getLeaveAppID());
			if(optapplicationLeaveApp.isPresent()){
				HolidayShipmentCommand commandHoliday = new HolidayShipmentCommand(command.getLeaveAppID(), null, optapplicationLeaveApp.get().getVersion(), null);	
				this.cancelHolidayShipmentCommandHandler.handle(commandHoliday);
			}
			// 10.関連マスタ更新
			BrkOffSupChangeMng brkOffSupChangeMng = new BrkOffSupChangeMng(appID, command.getLeaveAppID());
			this.brkOffSupChangeMngRepository.insert(brkOffSupChangeMng);
		}
		// 2-2.新規画面登録時承認反映情報の整理
		registerService.newScreenRegisterAtApproveInfoReflect(appRoot.getEmployeeID(), appRoot);
		
		// 暫定データの登録
		interimRemainDataMngRegisterDateChange.registerDateChange(
				AppContexts.user().companyId(), 
				command.getApplicantSID(), 
				Arrays.asList(command.getApplicationDate()));

		// 2-3.新規画面登録後の処理を実行
		return newAfterRegister.processAfterRegister(appRoot);
	}

}
