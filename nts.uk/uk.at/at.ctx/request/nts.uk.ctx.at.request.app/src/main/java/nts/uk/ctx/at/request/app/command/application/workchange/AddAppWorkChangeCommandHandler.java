package nts.uk.ctx.at.request.app.command.application.workchange;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.dom.application.workchange.IWorkChangeRegisterService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.ctx.at.request.app.command.application.common.CreateApplicationCommand;
import nts.uk.ctx.at.request.app.command.application.common.appapprovalphase.AppApprovalPhaseCmd;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectPerScheReason;
import nts.uk.ctx.at.request.dom.application.ReflectPlanPerEnforce;
import nts.uk.ctx.at.request.dom.application.ReflectPlanPerState;
import nts.uk.ctx.at.request.dom.application.ReflectPlanScheReason;
import nts.uk.ctx.at.request.dom.application.common.appapprovalphase.AppApprovalPhase;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;

@Stateless
@Transactional
public class AddAppWorkChangeCommandHandler extends CommandHandlerWithResult<AddAppWorkChangeCommand, List<String>> {

	@Inject
	private IWorkChangeRegisterService workChangeRegisterService;

	@Override
	protected List<String> handle(CommandHandlerContext<AddAppWorkChangeCommand> context) {
		AddAppWorkChangeCommand addCommand = context.getCommand();

		// Application command
		CreateApplicationCommand appCommand = addCommand.getAppCommand();
		// Work change command
		AppWorkChangeCommand workChangeCommand = addCommand.getWorkChangeCommand();
		// Phase command
		List<AppApprovalPhaseCmd> approvalPhaseCommand = addCommand.getAppApprovalPhaseCmds();

		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		// 入力者
		String persionId = AppContexts.user().personId();
		// 申請者
		String applicantSID = AppContexts.user().employeeId();

		// Phase list
		List<AppApprovalPhase> pharseList = ApprovalPhaseUtils.convertToDomain(approvalPhaseCommand, companyId, appID);

		// 申請
		Application app = new Application(companyId, appID,
				EnumAdaptor.valueOf(appCommand.getPrePostAtr(), PrePostAtr.class), GeneralDateTime.now(), persionId,
				new AppReason(appCommand.getReversionReason()), appCommand.getApplicationDate(),
				new AppReason(appCommand.getApplicationReason()), ApplicationType.WORK_CHANGE_APPLICATION, applicantSID,
				EnumAdaptor.valueOf(0, ReflectPlanScheReason.class), null,
				EnumAdaptor.valueOf(0, ReflectPlanPerState.class), EnumAdaptor.valueOf(0, ReflectPlanPerEnforce.class),
				EnumAdaptor.valueOf(0, ReflectPerScheReason.class), null,
				EnumAdaptor.valueOf(0, ReflectPlanPerState.class), EnumAdaptor.valueOf(0, ReflectPlanPerEnforce.class),
				appCommand.getApplicationDate(), appCommand.getApplicationDate(), pharseList);

		// 勤務変更申請
		AppWorkChange workChangeDomain = AppWorkChange.createFromJavaType(workChangeCommand.getCid(),
				workChangeCommand.getAppId(), workChangeCommand.getWorkTypeCd(), workChangeCommand.getWorkTimeCd(),
				workChangeCommand.getExcludeHolidayAtr(), workChangeCommand.getWorkChangeAtr(),
				workChangeCommand.getGoWorkAtr1(), workChangeCommand.getBackHomeAtr1(),
				workChangeCommand.getBreakTimeStart1(), workChangeCommand.getBreakTimeEnd1(),
				workChangeCommand.getWorkTimeStart1(), workChangeCommand.getWorkTimeEnd1(),
				workChangeCommand.getWorkTimeStart2(), workChangeCommand.getWorkTimeEnd2(),
				workChangeCommand.getGoWorkAtr2(), workChangeCommand.getBackHomeAtr2());

		return workChangeRegisterService.registerData(workChangeDomain, app);
	}
}
