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
public class AddSpLea18InfoCommandHandler extends CommandHandlerWithResult<AddSpecialleave18informationCommand, PeregAddCommandResult>
implements PeregAddCommandHandler<AddSpecialleave18informationCommand>{

	@Inject 
	private SpLeaInfoCommandHandler addSpLeaInfoCommandHandler;
	
	@Override
	public String targetCategoryCd() {
		return "CS00056";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialleave18informationCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddSpecialleave18informationCommand> context) {
		
		val command = context.getCommand();
		
		String cid = AppContexts.user().companyId();
		
		SpecialLeaveBasicInfo domain = new SpecialLeaveBasicInfo(cid, command.getSID(), SpecialLeaveCode.CS00056.value, command.getUseAtr().intValue(), command.getAppSet().intValue(), command.getGrantDate(),
				command.getGrantDays() != null ? command.getGrantDays().intValue() : null, command.getGrantTable());
		return new PeregAddCommandResult(addSpLeaInfoCommandHandler.addHandler(domain));
	}

}
