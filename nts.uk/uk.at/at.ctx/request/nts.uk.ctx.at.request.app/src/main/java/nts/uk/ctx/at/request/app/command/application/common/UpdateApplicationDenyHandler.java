package nts.uk.ctx.at.request.app.command.application.common;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto;
import nts.uk.ctx.at.request.app.find.application.common.CheckApprover;
import nts.uk.ctx.at.request.app.find.application.common.dto.InputCommonData;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.AfterDenialProcess;
@Stateless
@Transactional
public class UpdateApplicationDenyHandler extends CommandHandlerWithResult<InputCommonData, String> {

	@Inject 
	private AfterDenialProcess afterDenialProcessRepo;
	
	@Inject
	private CheckApprover checkApprover;

	@Override
	protected String handle(CommandHandlerContext<InputCommonData> context) {
		String memo = context.getCommand().getMemo();
		ApplicationDto command = context.getCommand().getApplicationDto();
		checkApprover.checkApprover(command,memo);
		Application application =  ApplicationDto.toEntity(command);
		//共通アルゴリズム「詳細画面否認前の処理」を実行する(thực hiện xử lý 「詳細画面否認前の処理)
		//アルゴリズム「排他チェック」を実行する(thực hiện xủa lý 「排他チェック」)
		//Thuc hien tu dong khi thay doi version cua bang
		//9.2 
		return afterDenialProcessRepo.detailedScreenAfterDenialProcess(application, memo);
	}

}
