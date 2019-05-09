package nts.uk.ctx.at.request.app.command.application.common;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.common.dto.InputCommonData;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterDeny;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.shr.com.context.AppContexts;
@Stateless
@Transactional
public class UpdateApplicationDenyHandler extends CommandHandlerWithResult<InputCommonData, ProcessResult> {
	
	@Inject
	private DetailAfterDeny detailAfterDeny;
	
	@Inject
	private DetailBeforeUpdate beforeRegisterRepo;

	@Override
	protected ProcessResult handle(CommandHandlerContext<InputCommonData> context) {
		String memo = context.getCommand().getMemo();
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		ApplicationDto_New command = context.getCommand().getApplicationDto();
		
		//共通アルゴリズム「詳細画面否認前の処理」を実行する(thực hiện xử lý 「詳細画面否認前の処理)
		// 4.1
		beforeRegisterRepo.processBeforeDetailScreenRegistration(companyID, command.getApplicantSID(),
				GeneralDate.today(), 1, command.getApplicationID(),
				EnumAdaptor.valueOf(command.getPrePostAtr(), PrePostAtr.class), command.getVersion(), null, null);
		
		//9.2 
		return detailAfterDeny.doDeny(companyID, command.getApplicationID(), employeeID, memo);
	}

}
