package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.add;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.SpLeaInfoCommandHandler;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.SpecialLeaveCode;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
public class AddSpLea3InfoCommandHandler extends CommandHandlerWithResult<AddSpecialleave3informationCommand, PeregAddCommandResult>
implements PeregAddCommandHandler<AddSpecialleave3informationCommand>{

	@Inject 
	private SpLeaInfoCommandHandler addSpLeaInfoCommandHandler;
	
	@Override
	public String targetCategoryCd() {
		return "CS00027";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialleave3informationCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddSpecialleave3informationCommand> context) {
		
		val command = context.getCommand();
		
		String cid = AppContexts.user().companyId();
		
		SpecialLeaveBasicInfo domain = new SpecialLeaveBasicInfo(cid, command.getSID(), SpecialLeaveCode.CS00027.value, command.getUseAtr(), command.getAppSet(), command.getGrantDate(),
				command.getGrantDays(), command.getGrantTable());
		return new PeregAddCommandResult(addSpLeaInfoCommandHandler.addHandler(domain));
	}

}
