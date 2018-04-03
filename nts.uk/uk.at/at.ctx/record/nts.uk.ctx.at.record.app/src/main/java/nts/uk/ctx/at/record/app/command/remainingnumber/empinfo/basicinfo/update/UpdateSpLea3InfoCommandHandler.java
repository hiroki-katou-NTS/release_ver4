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
public class UpdateSpLea3InfoCommandHandler extends CommandHandler<UpdateSpecialleave3informationCommand>
implements PeregUpdateCommandHandler<UpdateSpecialleave3informationCommand>{

	@Inject 
	private SpLeaInfoCommandHandler updateSpLeaInfoCommandHandler;
	
	@Override
	public String targetCategoryCd() {
		return "CS00027";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateSpecialleave3informationCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateSpecialleave3informationCommand> context) {
		
		val command = context.getCommand();
		
		String cid = AppContexts.user().companyId();
		SpecialLeaveBasicInfo domain = new SpecialLeaveBasicInfo(cid,command.getSID(),SpecialLeaveCode.CS00027.value, command.getUseAtr(), command.getAppSet(), command.getGrantDate(),
				command.getGrantDays(), command.getGrantTable());
		updateSpLeaInfoCommandHandler.updateHandler(domain);
	}

}
