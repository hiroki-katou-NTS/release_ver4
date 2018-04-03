package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.update;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.SpLeaInfoCommandHandler;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.SpecialLeaveCode;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;

@Stateless
public class UpdateSpLea17InfoCommandHandler extends CommandHandler<UpdateSpecialleave17informationCommand>
implements PeregUpdateCommandHandler<UpdateSpecialleave17informationCommand>{

	@Inject 
	private SpLeaInfoCommandHandler updateSpLeaInfoCommandHandler;
	
	@Override
	public String targetCategoryCd() {
		return "CS00055";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateSpecialleave17informationCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateSpecialleave17informationCommand> context) {
		
		val command = context.getCommand();
		
		String cid = AppContexts.user().companyId();
		SpecialLeaveBasicInfo domain = new SpecialLeaveBasicInfo(cid,command.getSID(),SpecialLeaveCode.CS00055.value, command.getUseAtr(), command.getAppSet(), command.getGrantDate(),
				command.getGrantDays(), command.getGrantTable());
		updateSpLeaInfoCommandHandler.updateHandler(domain);
	}

}
